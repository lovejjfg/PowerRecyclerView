package com.lovejjfg.swiperefreshrecycleview;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by Joe on 2016/11/21.
 * Email lovejjfg@gmail.com
 */

public class NotificationUtil {

    static int number = 1;

    /**
     * 发送一个点击跳转到MainActivity的消息
     */
    public static void sendSimplestNotificationWithAction(Context context) {

        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //获取PendingIntent
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建 Notification.Builder 对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker("啦啦啦啦啦啦啦啦啦啦啦啦阿拉啦啦啦啦啦啦啦啦啦",)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle("我是tittle")
                .setContentText("我是内容")
                .setNumber(++number)
                .setPriority(2)
                .setContentIntent(mainPendingIntent);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        String[] events = new String[]{"CCCCCCCCCCCCCCCCC","DDDDDDDDDDDDDDDDDDDDD","EEEEEEEEEEEEEEEEEEEE","FFFFFFFFFFFFFFFFFFFF","HHHHHHHHHHHHHHHHHHH",};
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Event tracker details:");
// Moves events into the expanded layout
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(context.getResources(), R.mipmap.about_bg));
        bigPictureStyle.setSummaryText("啦啦啦啦啦啦啦啦啦DDDDDDDDDDDDDDDDDDDDD啦啦啦啦啦啦啦");
// Moves the expanded layout object into the notification object.
//        builder.setStyle(bigPictureStyle);

        //发送通知
        mNotifyManager.notify(3, builder.build());
    }
}
