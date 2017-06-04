package one.marshangeriksen.loloaldrin.myapplication;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;
import one.marshangeriksen.loloaldrin.myapplication.screenSpecificWord.WordActivity;

import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID;

public class ReminderIntentService extends IntentService {
    public ReminderIntentService() {
        super("ReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        BaseDatabaseModel baseDatabaseModel = new BaseDatabaseModel(this);
        Word word = baseDatabaseModel.getRandomFavoriteWord();
        Log.e("time", Utils.sdfTime.format(System.currentTimeMillis()));
        if (word != null) {

            Intent resultIntent = new Intent(this, WordActivity.class);
            resultIntent.putExtra(KEY_ID, word.getId());
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_favorite);
            contentView.setTextViewText(R.id.tvContent, "\"" + word.getVocabulary() + "\" "
                    + getString(R.string.notification_content));
            android.support.v4.app.NotificationCompat.Builder mBuilder
                    = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.icon_star)
                    .setContent(contentView)
                    .setContentIntent(resultPendingIntent)
                    .setDefaults(DEFAULT_ALL)
                    .setAutoCancel(true);

            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(1111, mBuilder.build());
        }
    }
}
