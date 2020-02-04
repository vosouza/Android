package com.example.miwokapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class PhraseFragment extends Fragment {
    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    public PhraseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.word_activity, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<word> phrase = new ArrayList<word>();
        phrase.add(new word("Where are you going?", "minto wuksus",word.noId, R.raw.phrase_where_are_you_going));
        phrase.add(new word("What is your name?", "tinnә oyaase'nә",word.noId, R.raw.phrase_what_is_your_name));
        phrase.add(new word("My name is...", "oyaaset...",word.noId,  R.raw.phrase_my_name_is));
        phrase.add(new word("How are you feeling?", "michәksәs?",word.noId,  R.raw.phrase_how_are_you_feeling));
        phrase.add(new word("I’m feeling good.", "kuchi achit",word.noId,  R.raw.phrase_im_feeling_good));
        phrase.add(new word("Are you coming?", "әәnәs'aa?",word.noId,  R.raw.phrase_are_you_coming));
        phrase.add(new word("Yes, I’m coming.", "hәә’ әәnәm",word.noId,  R.raw.phrase_yes_im_coming));
        phrase.add(new word("I’m coming.", "әәnәm",word.noId,  R.raw.phrase_im_coming));
        phrase.add(new word("Let’s go.", "yoowutis",word.noId,  R.raw.phrase_lets_go));
        phrase.add(new word("Come here.", "әnni'nem",word.noId,  R.raw.phrase_come_here));

        WordAdapter itens = new WordAdapter( getActivity(), phrase, R.color.category_phrases);

        //Capta o lugar on a lista será mostrada
        ListView l = (ListView) rootView.findViewById(R.id.list);
        //Passa o adptador que tem metodos especificos para facilitar a criação da lista
        l.setAdapter(itens);
        //Metodo usado para o click nos itens da lista
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();

                //Capta o objeto da lista criada anteriormente
                word palavra = phrase.get(position);

                //Requisição de permissão para tocar o som
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                //Se estiver tudo ok entao toca o som
                if(palavra.hasSound() || (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)){
                    mMediaPlayer = MediaPlayer.create( getActivity(), palavra.getSoundId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    //metodo para liberar o som que estava armazenado para que possa ser colocado outro
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }
}
