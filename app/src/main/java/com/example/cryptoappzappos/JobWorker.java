package com.example.cryptoappzappos;

import android.app.Notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;


import com.example.cryptoappzappos.Model.PriceCheck;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.cryptoappzappos.App.CHANNEL_ID;

/**
 * This is the worker class for the WorkManager. This contains tasks to be executed when the job runs every hour.
 */

public class JobWorker extends Worker {
    NotificationManagerCompat notificationManagerCompat;

    private DatabaseReference databaseReference;
    private double setThresholdVal;

    public JobWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // Create the NotificaitonManagerCompat
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());


        // Get value of threshold from the db and set it
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Threshold");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setThreshold(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });





        //Hit api and  check the value
        Retrofit retrofit = new Retrofit.Builder().baseUrl(App.getContext().getResources().getString(R.string.data_url))
                .addConverterFactory(GsonConverterFactory.create()).build();

        APICall apicall = retrofit.create(APICall.class);
        Call<PriceCheck> call = apicall.getPriceAlert();


        call.enqueue(new Callback<PriceCheck>() {
            @Override
            public void onResponse(Call<PriceCheck> call, Response<PriceCheck> response) {
                if (!response.isSuccessful()) {
                    //textView.setText("Code: "+response.code());
                    return;
                }

                PriceCheck priceCheck = response.body();

                double currentPrice=(double)priceCheck.getLast();

            // Check the current value of bitcoin with the set threshold price.
                if(currentPrice<setThresholdVal){
                    sendNotification();
                }
            }

            @Override
            public void onFailure(Call<PriceCheck> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
        return Result.success();
    }


    // Method to send the notification
    public  void  sendNotification(){


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "app";
//            String description = "app desc";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(description, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);


        Resources res= getApplicationContext().getResources();
        // Building notification to display in tray
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(res.getString(R.string.notification_title))
                .setContentIntent(intent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(res.getString(R.string.notification_content)));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        Notification notification=builder.build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;



        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        notificationManager.notify(1001, notification);

    }

    public void setThreshold(DataSnapshot snapshot){
        for (DataSnapshot ds:snapshot.getChildren()){
             setThresholdVal= Double.parseDouble(ds.getValue().toString());
        }
    }
}
