package com.dicoding.cinemaindonesiansubmission4.widget.widgetAdapter;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListFavoriteWidgetServiceFactory(this.getApplicationContext(), intent);
    }
}
