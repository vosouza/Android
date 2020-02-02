package com.example.miwokapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

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

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<word> numbers = new ArrayList<word>();
        numbers.add(new word("One","lutti",R.drawable.number_one,R.raw.number_one));
        numbers.add(new word("two","otiiko",R.drawable.number_two,R.raw.number_two));
        numbers.add(new word("three","tolookosu",R.drawable.number_three,R.raw.number_three));
        numbers.add(new word("four","oyyisa",R.drawable.number_four,R.raw.number_four));
        numbers.add(new word("five","massokka",R.drawable.number_five,R.raw.number_five));
        numbers.add(new word("six","temmokka",R.drawable.number_six,R.raw.number_six));
        numbers.add(new word("seven","kenekaku",R.drawable.number_seven,R.raw.number_seven));
        numbers.add(new word("eigth","kawinta",R.drawable.number_eight,R.raw.number_eight));
        numbers.add(new word("nine","wo’e",R.drawable.number_nine,R.raw.number_nine));
        numbers.add(new word("ten","na’aacha",R.drawable.number_ten,R.raw.number_ten));

        WordAdaoter itens = new WordAdaoter(this, numbers, R.color.category_numbers);

        ListView l = (ListView) findViewById(R.id.list);
        l.setAdapter(itens);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                word palavra = numbers.get(position);

                int result = am.requestAudioFocus(mOnAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(palavra.hasSound() || (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)){

                    mp = MediaPlayer.create(NumbersActivity.this,palavra.getSoundId());
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
