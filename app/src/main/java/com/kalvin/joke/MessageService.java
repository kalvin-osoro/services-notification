package com.kalvin.joke;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MessageService extends IntentService {

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        handler= new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    public static final String EXTRA_MESSAGE= "MESSAGE";
    //CREATE A HANDLER SO THAT YOU CAN POST ON THE MAIN THREAD
    //To create a handler on the main thread we need to create a handler
    //object in a method that runs on the main thread
    private Handler handler;

    public static final int NOTIFICATION_ID=1;


    public MessageService() {
        super("MessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this){
            //synchronized method is Java code which allows us ti
            //lock a particular block of code from access by other threads
            try{
                //wait for 10 seconds
                wait(3000);
            }catch (InterruptedException error){
                error.printStackTrace();
            }
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        //call showText method
        showText(text);
    }

    private void showText(final String text) {
        Log.v("DelayedMessageServioce","What is the secret of comedy?: "+text);

        //post the Toast code to the main thread using the handler post method
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(this, MainActivity.class);

        //use a TaskBackBuilder to make the back button play nicely and create the pending intent
        TaskStackBuilder stackBuilder= TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        //Get the pending intent
        PendingIntent pendingIntent= stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        //Build the notification
        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "notify_001");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_joke_round);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText(text);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
//        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            mBuilder.setAutoCancel(true);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

//        Intent ii = new Intent(mContext.getApplicationContext(), RootActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, ii, 0);

//        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
//        bigText.bigText(verseurl);
//        bigText.setBigContentTitle("Today's Bible Verse");
//        bigText.setSummaryText("Text in detail");

    //        Notification notification= new Notification.Builder(this)
//                //this displays a small notification  icon-in this case the mipmap called ic_joke_round
//        .setSmallIcon(R.drawable.ic_joke_round_background)
//                //set the title as your application name
//        .setContentTitle(getString(R.string.app_name))
//                //set the content text
//        .setContentText(text)
//                //make the notification disappear when clicked
//        .setAutoCancel(true)
//                //give it a maximum priority to allow peeking
//        .setPriority(Notification.PRIORITY_MAX)
//                //set it to vibrate
//        .setDefaults(Notification.DEFAULT_VIBRATE)
//                //open main activity on clicking the notification
//        .setContentIntent(pendingIntent)
//                .build();
//        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        //Issue the notification
//        notificationManager.notify(NOTIFICATION_ID,notification);
}