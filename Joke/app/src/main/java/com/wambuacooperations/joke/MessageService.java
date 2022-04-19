package com.wambuacooperations.joke;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;


public class MessageService extends IntentService {

    private NotificationManager mNotificationManager;
    public static final String EXTRA_MESSAGE ="MESSAGE";
    public static final int NOTIFICATION_ID =1;
    public static final String CHANNEL_ID ="1";



    public MessageService() {
        super("MessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        synchronized (this){
            try {
                wait(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        final String text =intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);

    }
    public void showText(final String text){
        Log.v("Delayed message service", "What is the secret of comedy?: "+text);



        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
//
//        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
//        bigText.bigText(verseurl);
//        bigText.setBigContentTitle("Today's Bible Verse");
//        bigText.setSummaryText("Text in detail");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.funny);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText(text);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
//        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "1";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Joke notification",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

}
