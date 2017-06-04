package one.marshangeriksen.loloaldrin.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ReminderBroadcastReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 1111;
    public static final String ACTION = "one.marshangeriksen.loloaldrin.myapplication.ReminderBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ReminderIntentService.class);
        context.startService(i);
    }
}
