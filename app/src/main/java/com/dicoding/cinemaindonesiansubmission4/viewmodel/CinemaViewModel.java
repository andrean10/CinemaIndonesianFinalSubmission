package com.dicoding.cinemaindonesiansubmission4.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.cinemaindonesiansubmission4.BuildConfig;
import com.dicoding.cinemaindonesiansubmission4.entity.Database;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class CinemaViewModel extends ViewModel {

    private final ArrayList<Database> listDatabaseItems = new ArrayList<>();
    private String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<Database>> listCinema = new MutableLiveData<>();
    private AsyncHttpClient client = new AsyncHttpClient();

    public void moviesList(final Context context) {
        listDatabaseItems.clear();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=en_USyear=2020";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movies = results.getJSONObject(i);
                        Database database = new Database();
                        database.setId(movies.getInt("id"));
                        database.setTitle(movies.getString("original_title"));
                        database.setDate(movies.getString("release_date"));
                        String popularity = String.valueOf(movies.getDouble("popularity"));
                        database.setPopularity(popularity);
                        float rating = (float) movies.getDouble("vote_average");
                        database.setRating(rating);
                        database.setDescription(movies.getString("overview"));
                        String urlImg = movies.getString("poster_path");
                        database.setImage(urlImg);

                        listDatabaseItems.add(database);
                    }
                    listCinema.postValue(listDatabaseItems);
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));

                String errorMessage;
                switch (statusCode) {
                    case 400:
                        errorMessage = statusCode + "Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + "Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + "Not Found";
                        break;
                    case 500:
                        errorMessage = statusCode + "Server Error";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                listCinema.postValue(null);
            }
        });
    }

    public void tvshowList(final Context context) {
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=en_USyear=2020";
        listDatabaseItems.clear();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject tvShow = results.getJSONObject(i);
                        Database database = new Database();

                        database.setId(tvShow.getInt("id"));
                        database.setTitle(tvShow.getString("name"));
                        database.setDate(tvShow.getString("first_air_date"));
                        String popularity = String.valueOf(tvShow.getDouble("popularity"));
                        database.setPopularity(popularity);
                        float rating = (float) tvShow.getDouble("vote_average");
                        database.setRating(rating);
                        database.setDescription(tvShow.getString("overview"));
                        String urlImg = tvShow.getString("poster_path");
                        database.setImage(urlImg);

                        listDatabaseItems.add(database);
                    }
                    listCinema.postValue(listDatabaseItems);
                } catch (Exception e) {
                    Log.d("Exception ", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure: ", Objects.requireNonNull(error.getMessage()));

                String errorMessage;
                switch (statusCode) {
                    case 400:
                        errorMessage = statusCode + "Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + "Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + "Not Found";
                        break;
                    case 500:
                        errorMessage = statusCode + "Server Error";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                listCinema.postValue(null);
            }
        });
    }

    public void searchMoviesList(final Context context, String inputan) {
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + inputan;
        listDatabaseItems.clear();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movies = results.getJSONObject(i);
                        Database database = new Database();

                        database.setId(movies.getInt("id"));
                        database.setTitle(movies.getString("original_title"));
                        database.setDate(movies.getString("release_date"));
                        String popularity = String.valueOf(movies.getDouble("popularity"));
                        database.setPopularity(popularity);
                        float rating = (float) movies.getDouble("vote_average");
                        database.setRating(rating);
                        database.setDescription(movies.getString("overview"));
                        String urlImg = movies.getString("poster_path");
                        database.setImage(urlImg);

                        listDatabaseItems.add(database);
                    }
                    listCinema.postValue(listDatabaseItems);
                } catch (Exception e) {
                    Log.d("Exception ", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));

                String errorMessage;
                switch (statusCode) {
                    case 400:
                        errorMessage = statusCode + "Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + "Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + "Not Found";
                        break;
                    case 500:
                        errorMessage = statusCode + "Server Error";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();

                listCinema.postValue(listDatabaseItems);
            }
        });
    }

    public void searchTvShowList(final Context context, String inputan) {
        String url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=en-US&query=" + inputan;
        listDatabaseItems.clear();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject tvShow = results.getJSONObject(i);
                        Database database = new Database();

                        database.setId(tvShow.getInt("id"));
                        database.setTitle(tvShow.getString("name"));
                        database.setDate(tvShow.getString("first_air_date"));
                        String popularity = String.valueOf(tvShow.getDouble("popularity"));
                        database.setPopularity(popularity);
                        float rating = (float) tvShow.getDouble("vote_average");
                        database.setRating(rating);
                        database.setDescription(tvShow.getString("overview"));
                        String urlImg = tvShow.getString("poster_path");
                        database.setImage(urlImg);

                        listDatabaseItems.add(database);
                    }
                    listCinema.postValue(listDatabaseItems);
                } catch (Exception e) {
                    Log.d("Exception ", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure: ", Objects.requireNonNull(error.getMessage()));

                String errorMessage;
                switch (statusCode) {
                    case 400:
                        errorMessage = statusCode + "Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + "Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + "Not Found";
                        break;
                    case 500:
                        errorMessage = statusCode + "Server Error";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();

                listCinema.postValue(listDatabaseItems);
            }
        });
    }

    public MutableLiveData<ArrayList<Database>> getListDatabase() {
        return listCinema;
    }
}