/*
 * Copyright (c) 2016.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovejjfg.swiperefreshrecycleview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Joe on 2016/11/21.
 * Email lovejjfg@gmail.com
 */

class NotificationUtil {

    static int NUMBER = 1;

    /**
     * 发送一个点击跳转到MainActivity的消息
     */
    public static void sendSimplestNotificationWithAction(Context context) {

        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //获取PendingIntent
        Intent mainIntent = new Intent(context, NormalActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建 Notification.Builder 对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"test")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle("我是tittle")
                .setContentText("我是内容")
                .setNumber(++NUMBER)
                .setPriority(2)
                .setContentIntent(mainPendingIntent)
                .setFullScreenIntent(mainPendingIntent, true);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        String[] events = new String[]{"CCCCCCCCCCCCCCCCC", "DDDDDDDDDDDDDDDDDDDDD", "EEEEEEEEEEEEEEEEEEEE", "FFFFFFFFFFFFFFFFFFFF", "HHHHHHHHHHHHHHHHHHH"};
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Event tracker details:");
// Moves events into the expanded layout
        for (String event : events) {
            inboxStyle.addLine(event);
        }

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(context.getResources(), R.mipmap.about_bg));
        bigPictureStyle.setSummaryText("啦啦啦啦啦啦啦啦啦DDDDDDDDDDDDDDDDDDDDD啦啦啦啦啦啦啦");
// Moves the expanded layout object into the notification object.
//        builder.setStyle(bigPictureStyle);

        //发送通知
        Notification build = builder.build();

        mNotifyManager.notify(3, build);
    }
}
