package com.swipeacademy.multiplicationtableswipe;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

/**
 * Created by tonyn on 7/6/2017.
 */

public class WidgetIntentService extends IntentService {

    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));

        String result24 = PrefUtility.getRecent24(this);
        String result48 = PrefUtility.getRecent48(this);
        String result72 = PrefUtility.getRecent72(this);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);

            views.setTextViewText(R.id.widget_recent_24_result, result24);
            views.setTextViewText(R.id.widget_recent_48_result, result48);
            views.setTextViewText(R.id.widget_recent_72_result, result72);

            Intent launchIntent = new Intent(this, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
