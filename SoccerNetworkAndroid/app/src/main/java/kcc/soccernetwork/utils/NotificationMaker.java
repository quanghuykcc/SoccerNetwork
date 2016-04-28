package kcc.soccernetwork.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import kcc.soccernetwork.R;

/**
 * Created by Administrator on 4/16/2016.
 */
public class NotificationMaker {
    private Context context;
    public NotificationMaker(Context context) {
        this.context = context;
    }

    public void notify(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_app_notify)
                        .setContentTitle(title)
                        .setContentText(message);
        notificationManager.notify(1234, builder.build());
    }
}
