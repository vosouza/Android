package com.example.miwokapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    //Classe Estatica MediaPlayer usada para tocar video ou musica
    //Essa classe necissita ser reutizada para tocar as diferentes musicas pois
    //caso seja instanciada muitas vezes pode causar bugs no sistema
    private MediaPlayer mp;

    //metodo usado quando a musica termina
    private MediaPlayer.OnCompletionListener endAudio = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    /*
        A classe AudioManager é um serviço oferecido pelo sistema para gerenciar sons
        de diversos apps ao mesmo tempo. Para isso é necessario pedir ao sistema para
        usar as caixas de som e implementar o OnAudioFocusChangeListener para que o app
        decida o que fazer quando o sistema empresta a caixa de som para outro app
     */
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

    //Metodo principal que lida com a interface e funcionaalidades
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Passado para o sistema qual layout deve ser carregado
        //nesse caso é usado um layout comum para todas as outras classes em que
        //serão mostradas as listas
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_activity);

        //Instruçoes para mostrar a seta de voltar na parte superiror do app
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Requisicao do sistema de audio
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        /*
            Lista de objetos com as informações que serão mostradas na tela
         */
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

        /*
            Adaptador onde passa-se o layout que sera usado nos itens da lista e
            a lista de objetos com as informações para preencher o layout
         */
        WordAdaoter itens = new WordAdaoter(this, numbers, R.color.category_numbers);

        //Capta o lugar on a lista será mostrada
        ListView l = (ListView) findViewById(R.id.list);
        //Passa o adptador que tem metodos especificos para facilitar a criação da lista
        l.setAdapter(itens);
        //Metodo usado para o click nos itens da lista
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();

                //Capta o objeto da lista criada anteriormente
                word palavra = numbers.get(position);

                //Requisição de permissão para tocar o som
                int result = am.requestAudioFocus(mOnAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                //Se estiver tudo ok entao toca o som
                if(palavra.hasSound() || (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)){
                    mp = MediaPlayer.create(NumbersActivity.this,palavra.getSoundId());
                    mp.start();
                    mp.setOnCompletionListener(endAudio);
                }
            }
        });

    }

    //Metodo acionado quando a tela do app sai de foco
    @Override
    protected void onStop(){
        super.onStop();
        releaseMediaPlayer();
    }

    //metodo para liberar o som que estava armazenado para que possa ser colocado outro
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
