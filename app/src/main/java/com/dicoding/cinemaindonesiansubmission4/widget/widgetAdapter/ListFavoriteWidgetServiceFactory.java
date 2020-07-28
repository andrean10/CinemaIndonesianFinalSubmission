package com.dicoding.cinemaindonesiansubmission4.widget.widgetAdapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.dicoding.cinemaindonesiansubmission4.BuildConfig;
import com.dicoding.cinemaindonesiansubmission4.R;
import com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract;
import com.dicoding.cinemaindonesiansubmission4.entity.Database;
import com.dicoding.cinemaindonesiansubmission4.mapping.MappingHelper;
import com.dicoding.cinemaindonesiansubmission4.widget.movieWidget.FavoriteMoviesWidget;

import java.util.ArrayList;

import static com.dicoding.cinemaindonesiansubmission4.widget.tvshowWidget.FavoriteTvShowWidget.GET_DATA_FAVTVWIDGET;

public class ListFavoriteWidgetServiceFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Database> mData = new ArrayList<>();
    private final String URL_IMAGE = BuildConfig.TMDB_URL_IMG;
    private final Context context;
    private Intent mIntent;

    ListFavoriteWidgetServiceFactory(Context context, Intent intent) {
        this.context = context;
        this.mIntent = intent;
    }

    @Override
    public void onCreate() {
        // required
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();

        if (mIntent.getAction() != null) {
            if (mIntent.getAction().equals(FavoriteMoviesWidget.GET_DATA_FAVMVWIDGET)) {
                Cursor cursor = context.getContentResolver().query(
                        DatabaseContract.CONTENT_URI_MOVIES,
                        null,
                        null,
                        null,
                        null
                );

                if (cursor != null) {
                    mData = MappingHelper.mapCursorToArrayList(cursor);
                    cursor.close();
                }
            } else if (mIntent.getAction().equals(GET_DATA_FAVTVWIDGET)) {
                Cursor cursor = context.getContentResolver().query(
                        DatabaseContract.CONTENT_URI_TVSHOW,
                        null,
                        null,
                        null,
                        null
                );

                if (cursor != null) {
                    mData = MappingHelper.mapCursorToArrayList(cursor);
                    cursor.close();
                }
            }
        }

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        // required
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        try {
            Bitmap bitmap = Glide.with(context)
                    .asBitmap()
                    .load(URL_IMAGE + mData.get(position).getImage())
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            remoteViews.setImageViewBitmap(R.id.widget_image, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent fillIntent = new Intent();
        remoteViews.setOnClickFillInIntent(R.id.widget_image, fillIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
