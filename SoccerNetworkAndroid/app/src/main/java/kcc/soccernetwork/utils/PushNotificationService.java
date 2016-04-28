package kcc.soccernetwork.utils;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Administrator on 4/16/2016.
 */
public class PushNotificationService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.i("SoccerNetwork", from);
        String message = data.getString("message");
        Log.i("SoccerNetwork", message);
        NotificationMaker notificationMaker = new NotificationMaker(getApplicationContext());
        notificationMaker.notify("Soccer Network", message);
    }
}