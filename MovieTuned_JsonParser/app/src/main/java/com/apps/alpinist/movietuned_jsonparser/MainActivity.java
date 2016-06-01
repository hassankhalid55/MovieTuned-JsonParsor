package com.apps.alpinist.movietuned_jsonparser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

import BusinessObjects.Movie;
import BusinessObjects.MovieListItem;
import Model.HTTPURLConnectionHelper;
import Model.MyAdapter;

public class MainActivity extends AppCompatActivity {
    String itemClickedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetList gL = new GetList();
        gL.execute("popular");
//        while(true){
//            if (gL.getStatus() == AsyncTask.Status.FINISHED) {
//                new ParseMovie().execute(itemClickedId);
//                break;
//            }
//        }


    }

    private class GetList extends AsyncTask<String,Integer,ArrayList>{
        ListView lv;
        String URL = "";
        String criteria;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Data, Please Wait...");

            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<MovieListItem> doInBackground(String... criteria) {
            HTTPURLConnectionHelper httpurlConnectionHelper = new HTTPURLConnectionHelper();
            ArrayList<MovieListItem> moviesList = new ArrayList<MovieListItem>();
            if (criteria[0].equals("popular")){
                URL = "http://api.themoviedb.org/3/movie/popular?api_key=c40c1b5db40769edb93a7fc73ce0afe9";
                String JsonStr = httpurlConnectionHelper.makeServiceCall(URL);
                if (JsonStr != null){
                    //PARSE THE DATA
                    try{
                        JSONObject obj = new JSONObject(JsonStr);
                        JSONArray results = obj.getJSONArray(getString(R.string.RESULT_TAG));
                        for (int c=0;c<results.length();c++){
                            JSONObject m = results.getJSONObject(c);

                            MovieListItem mLI = new MovieListItem();
                            mLI.setTitle(m.getString(getString(R.string.TITLE_TAG)));
                            mLI.setCover_path(m.getString(getString(R.string.POSTER_TAG)));
                            mLI.setId(m.getString(getString(R.string.ID_TAG)));

                            moviesList.add(mLI);
                        }

                        return moviesList;

                    }catch(Exception e){
                        Log.e("Json exception: ",e.getLocalizedMessage());
                    }
                }
                else
                    Log.e("Error", "Couldnt get data");
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList arrayList) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (arrayList != null){
                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), R.layout.list_view_item, arrayList);
                lv = (ListView) findViewById(R.id.lv1);
                lv.setAdapter(myAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        itemClickedId = ((MovieListItem)arrayList.get(position)).getId();
                        new ParseMovie().execute(itemClickedId);
                    }
                });
            }
            else
                Toast.makeText(getApplicationContext(), "No Data Recieved From Server", Toast.LENGTH_LONG).show();
        }
    }

    private class ParseMovie extends AsyncTask<String,Integer,Movie>{
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Data, Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Movie doInBackground(String... params) {
            if (params[0] != null){
                Movie movie = new Movie();
                String movieInfoUrl = "http://api.themoviedb.org/3/movie/" + params[0] + "?api_key=c40c1b5db40769edb93a7fc73ce0afe9";
                String movieCreditsURL = "http://api.themoviedb.org/3/movie/" + params[0] + "/credits?api_key=c40c1b5db40769edb93a7fc73ce0afe9";
                String movieTrailerURL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos?api_key=c40c1b5db40769edb93a7fc73ce0afe9";
                HTTPURLConnectionHelper httpurlConnectionHelper = new HTTPURLConnectionHelper();
                String jsonInfoStr = httpurlConnectionHelper.makeServiceCall(movieInfoUrl);
                String jsonCreditsStr = httpurlConnectionHelper.makeServiceCall(movieCreditsURL);
                String jsonTrailerStr = httpurlConnectionHelper.makeServiceCall(movieTrailerURL);

                if(jsonInfoStr != null){
                    try {
                        JSONObject mainObj = new JSONObject(jsonInfoStr);
                        JSONArray genresArray = mainObj.getJSONArray(getString(R.string.GENRES_LIST_TAG));
                        ArrayList<String> genres = new ArrayList<String>();

                        for(int c=0;c<genresArray.length();c++){
                            JSONObject genre = genresArray.getJSONObject(c);
                            genres.add(genre.getString(getString(R.string.GENRE_NAME_TAG)));
                        }
                        movie.setGenres(genres);
                        movie.setId(mainObj.getString(getString(R.string.ID_TAG)));
                        movie.setLanguage(mainObj.getString(getString(R.string.LANGUAGE_TAG)));
                        movie.setTitle(mainObj.getString(getString(R.string.TITLE_TAG)));
                        movie.setOverview(mainObj.getString(getString(R.string.OVERVIEW_TAG)));
                        movie.setPopularity(mainObj.getString(getString(R.string.POPULARITY_TAG)));
                        movie.setPosterPath(mainObj.getString(getString(R.string.POSTER_TAG)));
                        movie.setReleaseDate(Date.valueOf(mainObj.getString(getString(R.string.RELEASE_DATE_TAG))));
                        movie.setRuntime(mainObj.getString(getString(R.string.RUNTIME_TAG)));
                        movie.setStatus(mainObj.getString(getString(R.string.STATUS_TAG)));
                        movie.setTagline(mainObj.getString(getString(R.string.TAGLINE_TAG)));
                        movie.setTitle(mainObj.getString(getString(R.string.TITLE_TAG)));
                        movie.setRating(mainObj.getString(getString(R.string.AVERAGE_RATING_TAG)));

                        //get trailer
                        JSONObject trailersObj = new JSONObject(jsonTrailerStr);
                        JSONArray trailerArray = trailersObj.getJSONArray("results");
                        for (int c=0; c<trailerArray.length(); c++){
                            String name = trailerArray.getJSONObject(c).getString(getString(R.string.NAME_TAG));
                            String site = trailerArray.getJSONObject(c).getString(getString(R.string.SITE_TAG));
                            String trailer = trailerArray.getJSONObject(c).getString(getString(R.string.TRAILER_KEY_TAG));

                            if(site.equals("YouTube") && name.contains("Official Trailer")) {
                                movie.setTrailer(trailer);
                                break;
                            }
                        }

                        //get directors, writers & cast
                        JSONObject creditsMainObj = new JSONObject(jsonCreditsStr);
                        JSONArray castArray = creditsMainObj.getJSONArray(getString(R.string.CAST_TAG));
                        ArrayList<String> cast = new ArrayList<String>();
                        if (castArray.length() > 5){
                            for (int c=0; c<5; c++){
                                cast.add(castArray.getJSONObject(c).getString(getString(R.string.NAME_TAG)));
                            }
                            movie.setCast(cast);
                        }
                        else{
                            for (int c=0; c<5; c++){
                                cast.add(castArray.getJSONObject(c).getString(getString(R.string.NAME_TAG)));
                            }
                            movie.setCast(cast);
                        }

                        //Directors & Writers
                        JSONArray crewArray = creditsMainObj.getJSONArray(getString(R.string.CREW_TAG));
                        ArrayList<String> directors = new ArrayList<String>();
                        ArrayList<String> writers = new ArrayList<String>();
                        for (int c=0; c<crewArray.length(); c++){
                            JSONObject crewObj = crewArray.getJSONObject(c);
                            if (crewObj.getString(getString(R.string.DEPARTMENT_TAG)).equals("Directing")){
                                directors.add(crewObj.getString(getString(R.string.NAME_TAG)));
                            }
                            else if (crewObj.getString(getString(R.string.DEPARTMENT_TAG)).equals("Writing")){
                                writers.add(crewObj.getString(getString(R.string.NAME_TAG)));
                            }
                        }

                        movie.setDirectors(directors);
                        movie.setWriters(writers);
                        return movie;
                    }catch (Exception e){
                        //Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Could not get Data", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (movie != null){
                Intent i = new Intent(getApplicationContext(), movie_Info.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie", movie);
                i.putExtras(bundle);

                startActivity(i);
            }
            else
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
        }


    }
}