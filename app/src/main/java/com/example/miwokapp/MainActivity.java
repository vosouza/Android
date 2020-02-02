package com.example.miwokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
       Metodos que estão atrelados aos Textviews do layout
       activity_main no atributo OnClick
     */

    public void numberList(View view){

        // Intent serve para chamar outras classes filhas da classe activity
        Intent i = new Intent(this, NumbersActivity.class);
        startActivity(i);
    }

    public void phraseList(View view){
        Intent i = new Intent(this, PhraseActivity.class);
        startActivity(i);
    }

    public void familyList(View view){
        Intent i = new Intent(this, FamilyActivity.class);
        startActivity(i);
    }
    public void colorsList(View view){
        Intent i = new Intent(this, ColorsActivity.class);
        startActivity(i);
    }
}
