package com.example.miwokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhraseActivity extends AppCompatActivity {

    private MediaPlayer mp;

    private MediaPlayer.OnCompletionListener endAudio = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    private AudioManager am;
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
                mp.pause();
                mp.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mp.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_activity);

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


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

        WordAdaoter itens = new WordAdaoter(this, phrase, R.color.category_phrases);

        ListView l = (ListView) findViewById(R.id.list);
        l.setAdapter(itens);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word palavra = phrase.get(position);
                int result = am.requestAudioFocus(mOnAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(palavra.hasSound()  || (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)){
                    releaseMediaPlayer();
                    mp = MediaPlayer.create(PhraseActivity.this,palavra.getSoundId());
                    mp.start();
                    mp.setOnCompletionListener(endAudio);
                }
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mp != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mp.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mp = null;
            am.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }
}
