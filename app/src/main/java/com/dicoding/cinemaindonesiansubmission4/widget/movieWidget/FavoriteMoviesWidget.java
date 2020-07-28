package com.dicoding.cinemaindonesiansubmission4.widget.movieWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.dicoding.cinemaindonesiansubmission4.MainActivity;
import com.dicoding.cinemaindonesiansubmission4.R;
import com.dicoding.cinemaindonesiansubmission4.widget.widgetAdapter.WidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteMoviesWidget extends AppWidgetProvider {
    public static final String UPDATE_WIDGET_FAVORITE = "com.dicoding.picodiploma.UPDATE_WIDGET_FAVORITE";
    public static final String GET_DATA_FAVMVWIDGET = "com.dicoding.picodiploma.GET_DATA_FAVMVWIDGET";
    public static final String MOVE_ACTIVITY_MV = "com.dicoding.picodiploma.MOVE_ACTIVITY_MV";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(GET_DATA_FAVMVWIDGET);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_favorite_banner);
        views.setRemoteAdapter(R.id.stackview, intent);
        views.setEmptyView(R.id.stackview, R.id.empty_view);
        views.setTextViewText(R.id.banner_text, context.getString(R.string.widget_title_favoriteMv));
        views.setTextViewText(R.id.empty_view, context.getString(R.string.empty_widget_favoriteMv));


        Intent moveDetail = new Intent(context, FavoriteMoviesWidget.class);
        moveDetail.setAction(MOVE_ACTIVITY_MV);
        moveDetail.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                moveDetail, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stackview, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(UPDATE_WIDGET_FAVORITE)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName componentName = new ComponentName(context, FavoriteMoviesWidget.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stackview);
            } else if (intent.getAction().equals(MOVE_ACTIVITY_MV)) {
                Intent moveDetail = new Intent(context, MainActivity.class);
                context.startActivity(moveDetail);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

