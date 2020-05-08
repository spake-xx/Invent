package com.spake.invent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    /**
     * Fire notifications
     * @param items List of Items to fire notification about
     */
    void fireNotifications(List<Item> items){
        for(Item i:items){
            showNotification("Przypomnienie o kończącej dacie trwałości", "Data trwałości "+i.getName()+" kończy się za niedługo!", i);
        }
    }

    /**
     * Shows notification to user
     * @param title notification title
     * @param message notification text
     * @param item Item object
     */
    void showNotification(String title, String message, Item item) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("item_notifications",
                    "Expire notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications about item expire date.");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "item_notifications")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(context, ShowItemInfo.class);
        Bundle b = new Bundle();
        b.putInt("item_id", item.getId());
        intent.putExtras(b);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(item.getId(), mBuilder.build());
    }
}
