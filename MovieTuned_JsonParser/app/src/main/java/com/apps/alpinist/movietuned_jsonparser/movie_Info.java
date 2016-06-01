package com.apps.alpinist.movietuned_jsonparser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import BusinessObjects.Movie;

public class movie_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__info);

        Intent i = this.getIntent();
        Movie movie = (Movie)i.getExtras().getSerializable("movie");

        ((TextView)findViewById(R.id.movietitle)).setText(movie.getTitle());
        ((TextView)findViewById(R.id.poster_path)).setText(movie.getPosterPath());
        ((TextView)findViewById(R.id.overview)).setText(movie.getOverview());
        ((TextView)findViewById(R.id.language)).setText(movie.getLanguage());
        ((TextView)findViewById(R.id.rating)).setText(movie.getRating());
        ((TextView)findViewById(R.id.runtime)).setText(movie.getRuntime());
        ((TextView)findViewById(R.id.trailer_path)).setText(movie.getTrailer());
        ((TextView)findViewById(R.id.director)).setText(movie.getDirectors().get(0));
        ((TextView)findViewById(R.id.writer)).setText(movie.getWriters().get(0));
        ((TextView)findViewById(R.id.cast)).setText(movie.getCast().get(0));
    }
}
