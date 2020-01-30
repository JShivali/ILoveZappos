package com.example.cryptoappzappos;


import android.app.Application;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

/**
 * This class encapsulates the entire application and creates the  notification channel and WorkManager request for the alerts feature
 */


public class App extends Application {
    public static final String CHANNEL_ID = "PRICE ALERT";
    private PeriodicWorkRequest periodicWork;

    private static Context mContext;

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        // Create Notifications
        createNotificationChannels();

        // Set WorkManager on install of app
        periodicWork = new PeriodicWorkRequest.Builder(JobWorker.class, 1, TimeUnit.HOURS).addTag(getString(R.string.unique_work_name))
                .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork(getString(R.string.unique_work_name), ExistingPeriodicWorkPolicy.REPLACE, periodicWork);

    }

    private void createNotificationChannels() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel priceAlertChannel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.notify_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            priceAlertChannel.setDescription(getString(R.string.notify_channel_desc));
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(priceAlertChannel);

        }
    }

}
