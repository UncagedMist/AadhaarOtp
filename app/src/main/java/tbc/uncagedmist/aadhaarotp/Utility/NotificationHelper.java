package tbc.uncagedmist.aadhaarotp.Utility;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import tbc.uncagedmist.aadhaarotp.R;

public class NotificationHelper extends ContextWrapper {

    public static final String CHANNEL_ID = "channel";
    public static final String CHANNEL_NAME = "Aadhaar Card";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    public NotificationManager getManager()   {
        if (manager == null)    {
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);

        channel.setDescription("Just to test");
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getNotification(String title, String message, Uri soundUri, PendingIntent pendingIntent, boolean isClicked)    {
        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notif)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(isClicked);
    }
}