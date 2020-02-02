package com.example.miwokapp;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class WordAdaoter extends ArrayAdapter<word> {

    public int listColor;
    public word palavra;

    public WordAdaoter(Activity context, List<word> palavra, int color){
        super(context,0, palavra);
        listColor = color;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        View txtPlace = listItemView.findViewById(R.id.txtholder);
        int c = ContextCompat.getColor(getContext(),listColor);
        txtPlace.setBackgroundColor(c);

        palavra = getItem(position);

        TextView miwok = listItemView.findViewById(R.id.miwok_word);
        TextView english = listItemView.findViewById(R.id.english_word);
        ImageView img = listItemView.findViewById(R.id.imageView);

        if(palavra.hasImage()){
            img.setImageResource(palavra.getImageId());
            img.setVisibility(View.VISIBLE);
        }else{
            img.setVisibility(View.GONE);
        }

        miwok.setText(palavra.getMiwokWord());
        english.setText(palavra.getEnglishWord());

        return listItemView;
    }
}
