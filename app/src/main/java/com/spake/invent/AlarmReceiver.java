package com.spake.invent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.spake.invent.database.AppDatabase;
import com.spake.invent.database.ItemDAO;
import com.spake.invent.database.entity.Item;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    Context context;
    ItemDAO itemDao;
    ExecutorService executorService;

    public static final String ACTION_ALARM_RECEIVER = "NOTIFICATION_CHECKER_ACTION";

    public static final String NOTIFICATION_CHANNEL_PRIMARY = "notification_channel_primary";
    public static final int NOTIFICATION_ID_PRIMARY = 1100;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        itemDao = AppDatabase.getInstance(context.getApplicationContext()).itemDao();
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final long DAY_IN_MS = 1000 * 60 * 60 * 24;

               fireNotifications(itemDao.findBetweenExpire(System.currentTimeMillis(), System.currentTimeMillis()+3*DAY_IN_MS));
            }
        });


        Log.i("Alarm", "Alarm has been launched");
    }

    void fireNotifications(List<Item> items){
        for(Item i:items){
            Log.i("Item", "Runned "+i.getExpireAt().toString());
            showNotification("Przypomnienie o kończącej dacie trwałości", "Data trwałości "+i.getName()+" kończy się za niedługo!");
        }
    }

    void showNotification(String title, String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(context, ShowItemInfo.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
