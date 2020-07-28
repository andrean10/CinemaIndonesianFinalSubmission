package com.dicoding.cinemaindonesiansubmission4.view.detail;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dicoding.cinemaindonesiansubmission4.BuildConfig;
import com.dicoding.cinemaindonesiansubmission4.R;
import com.dicoding.cinemaindonesiansubmission4.entity.Database;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.BaseColumns._ID;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CONTENT_URI_MOVIES;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.DATE;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.DESCRIPTION;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.IMAGES;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.POPULARITY;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.RATING;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.TITLE;
import static java.util.Objects.requireNonNull;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String URL_IMAGE = BuildConfig.TMDB_URL_IMG;
    public static final String EXTRA_NAME = "extra_name";

    private ImageView imgPhoto;
    private TextView tvRelease, tvPopularity, tvRate, tvDescription;
    private RatingBar rbVoteAverage;
    private ProgressBar progressBar, progressBarImg;
    private Database databaseIntent;
    private Toolbar mToolbar;
    private LikeButton likeButton;
    private Uri uriWithId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        progressBar = findViewById(R.id.progressBar);
        progressBarImg = findViewById(R.id.progressBarImage);
        imgPhoto = findViewById(R.id.expandedImage);
        tvPopularity = findViewById(R.id.tv_popularity);
        tvRelease = findViewById(R.id.tv_release);
        rbVoteAverage = findViewById(R.id.rb_voteaverage);
        tvRate = findViewById(R.id.tv_rating);
        tvDescription = findViewById(R.id.tv_description);
        likeButton = findViewById(R.id.heart_button);
        showLoading(true);

        databaseIntent = getIntent().getParcelableExtra(EXTRA_NAME);

        assert databaseIntent != null;
        prepare(databaseIntent);
        showLoading(false);
    }

    // prepare data
    void prepare(final Database databaseIntent) {
        Glide.with(DetailMovieActivity.this)
                .load(URL_IMAGE + databaseIntent.getImage())
                .listener(showLoadingImage(progressBarImg))
                .into(imgPhoto);

        tvRelease.setText(convertToDatePattern(databaseIntent.getDate()));
        tvPopularity.setText(databaseIntent.getPopularity());
        float ratingStars = databaseIntent.getRating() / 2;
        rbVoteAverage.setRating(ratingStars);
        String rating = String.valueOf(databaseIntent.getRating());
        tvRate.setText(rating);
        tvDescription.setText(databaseIntent.getDescription());

        setDetailTitle(databaseIntent.getTitle());
        checkStateFavorite(databaseIntent);

        // listener button favorite
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ContentValues values = new ContentValues();
                values.put(_ID, databaseIntent.getId());
                values.put(TITLE, databaseIntent.getTitle());
                values.put(DATE, databaseIntent.getDate());
                values.put(RATING, databaseIntent.getRating());
                values.put(POPULARITY, databaseIntent.getPopularity());
                values.put(DESCRIPTION, databaseIntent.getDescription());
                values.put(IMAGES, databaseIntent.getImage());

                getContentResolver().insert(CONTENT_URI_MOVIES, values);
                Toast.makeText(DetailMovieActivity.this, getResources().getString(R.string.succes_add_itemMv), Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(DetailMovieActivity.this, FavoriteMoviesWidget.class);
//                intent.setAction(UPDATE_WIDGET_FAVORITE);
//                sendBroadcast(intent);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                getContentResolver().delete(uriWithId, null, null);
                Toast.makeText(DetailMovieActivity.this, getResources().getString(R.string.succes_remove_itemMv), Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(DetailMovieActivity.this, FavoriteMoviesWidget.class);
//                intent.setAction(UPDATE_WIDGET_FAVORITE);
//                sendBroadcast(intent);
            }
        });
    }

    // mengatur button state favorite
    private void checkStateFavorite(Database database) {
        uriWithId = Uri.parse(CONTENT_URI_MOVIES + "/" + databaseIntent.getId());
        Cursor cursor = getContentResolver().query(
                uriWithId,
                null,
                null,
                null,
                null
        );
        assert cursor != null;
        if (cursor.getCount() > 0) {
            likeButton.setLiked(true);
        }
        cursor.close();
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private String convertToDatePattern(String release) {
        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat toString = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date date;
        String setDate = "";
        try {
            date = toDate.parse(release);
            assert date != null;
            setDate = toString.format(date);
            return setDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return setDate;
    }

    private RequestListener<Drawable> showLoadingImage(final ProgressBar progressBar) {
        return new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.INVISIBLE);
                return false;
            }
        };
    }

    private void setDetailTitle(String dataTitle) {
        if (mToolbar != null) {
            requireNonNull(getSupportActionBar()).setTitle(dataTitle);
        }
        requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}