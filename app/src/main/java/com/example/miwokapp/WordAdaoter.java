package com.example.miwokapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class WordAdaoter extends ArrayAdapter<word> {

    /*
        Classe necessario para criar um lista responsiva que se recicla
     */

    public int listColor; //cor de fundo do texto
    public word palavra; // Objeto da lista

    public WordAdaoter(Activity context, List<word> palavra, int color){
        super(context,0, palavra);
        listColor = color;
    }

    //Metodo que é chamado para cada item da lista com as regras de como inflar na tela
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Pega o item que vai ser inflado
        View listItemView = convertView;
        if(listItemView == null) {
            //Como a lista se recicla, se o listItemView for null que dizer que ele é novo e precisa ser inflado
            //se ele nao for null é porque ja foi inflado e so é necessario mudar o conteudo
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //Pega o container dos textos para mudar a cor
        View txtPlace = listItemView.findViewById(R.id.txtholder);
        int c = ContextCompat.getColor(getContext(),listColor);
        txtPlace.setBackgroundColor(c);

        //resgata o objeto word que foi adicionado na lista
        palavra = getItem(position);

        //pega todos os view que serão mudados
        TextView miwok = listItemView.findViewById(R.id.miwok_word);
        TextView english = listItemView.findViewById(R.id.english_word);
        ImageView img = listItemView.findViewById(R.id.imageView);

        //se o objeto word tiver uma imagem setada então ela é passada para a tela
        //se não tiver uma imagem setada então retira o ImageView da tela
        if(palavra.hasImage()){
            img.setImageResource(palavra.getImageId());
            img.setVisibility(View.VISIBLE);
        }else{
            img.setVisibility(View.GONE);
        }

        //Coloca os textos nos seus respectivos lugares
        miwok.setText(palavra.getMiwokWord());
        english.setText(palavra.getEnglishWord());

        //retorna o item que vai ser inflado ja com as informações corretas
        return listItemView;
    }
}
