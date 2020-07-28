package com.dicoding.cinemaindonesiansubmission4.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.dicoding.cinemaindonesiansubmission4.BuildConfig;
import com.dicoding.cinemaindonesiansubmission4.MainActivity;
import com.dicoding.cinemaindonesiansubmission4.R;
import com.dicoding.cinemaindonesiansubmission4.entity.Database;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class Reminder extends BroadcastReceiver {

    private static final String DAILY_REMINDER = "DailyReminder";
    private static final String RELEASE_REMINDER = "ReleaseReminder";
    private static final String CHANNEL_ID = "Channel_1";
    private static final String CHANNEL_NAME = "Daily Reminder Channel";

    public static final int ID_DAILY_REMIND = 100;
    public static final int ID_RELEASE_REMIND = 300;

    private String TAG = Reminder.class.getSimpleName();
    private final ArrayList<Database> listDatabaseItems = new ArrayList<>();
    private String API_KEY = BuildConfig.TMDB_API_KEY;
    private AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case DAILY_REMINDER:
                    showNotification(
                            context,
                            context.getString(R.string.app_name),
                            context.getString(R.string.app_message),
                            ID_DAILY_REMIND
                    );
                    Log.d(TAG, "onReceive Daily Reminder");
                    break;
                case RELEASE_REMINDER:
                    getReminderRelease(context);
                    break;
            }
        }
    }

    public void setDailyRemind(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar reminder = Calendar.getInstance();
        reminder.set(Calendar.HOUR_OF_DAY, 7);
        reminder.set(Calendar.MINUTE, 0);
        reminder.set(Calendar.SECOND, 0);

        Calendar timeNow = Calendar.getInstance();

        Intent intent = new Intent(context, Reminder.class);
        intent.setAction(DAILY_REMINDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMIND, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (timeNow.before(reminder)) {
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP, reminder.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent
                );
            }
        } else {
            reminder.add(Calendar.DATE, 1);
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP, reminder.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent
                );
            }
        }

        Log.d(TAG, "setDailyReminder: ");
    }

    public void setRelaseReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar reminder = Calendar.getInstance();
        reminder.set(Calendar.HOUR_OF_DAY, 8);
        reminder.set(Calendar.MINUTE, 0);
        reminder.set(Calendar.SECOND, 0);

        Calendar timeNow = Calendar.getInstance();

        Intent intent = new Intent(context, Reminder.class);
        intent.setAction(RELEASE_REMINDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ID_RELEASE_REMIND, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (timeNow.before(reminder)) {
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP, reminder.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent
                );
            }
        } else {
            reminder.add(Calendar.DATE, 1);
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP, reminder.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent
                );
            }
        }
        Log.d(TAG, "setRelaseReminder: ");
    }

    private void getReminderRelease(final Context context) {
        String dateNow = getCurrentTime();

        String url = "http://api.themoviedb.org/3/discover/movie?api_key=" +
                API_KEY + "&primary_release_date.gte=" + dateNow + "&primary_release_date.gte=" + dateNow;

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
                        database.setTitle(movies.getString("original_title"));
                        database.setDescription(movies.getString("overview"));
//                        String urlImg = movies.getString("poster_path");
//                        database.setImage(urlImg);
                        listDatabaseItems.add(database);
                    }

                    int id = ID_RELEASE_REMIND;
                    if (listDatabaseItems.size() != 0) {
                        for (int i = 0; i < listDatabaseItems.size(); i++) {
                            String title = listDatabaseItems.get(i).getTitle();
                            String message = listDatabaseItems.get(i).getDescription();
//                String images = listDatabaseItems.get(i).getImage();

                            showNotification(context, title, message, id++);
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });

        Log.d(TAG, "getReminderRelease: ");
    }

    public void cancelReminder(Context context, int id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, Reminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Log.d(TAG, "cancelReminder: ");
    }

    private void showNotification(Context context, String title, String message, int id) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(id, notification);
        }
    }

    private String getCurrentTime() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}