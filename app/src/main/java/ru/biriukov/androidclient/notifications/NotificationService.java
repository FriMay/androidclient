package ru.biriukov.androidclient.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.apollographqlandroid.NotificationForStudentMutation;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import ru.biriukov.androidclient.R;

public class NotificationService extends IntentService {

    private static String CHANNEL_ID = "ru.biriukov.androidclient.notifiactions.ANDROID";
    private static String CHANNEL_NAME = "Notification service channel.";
    private static Integer id = 1002;
    final String LOG_TAG = "myLogs";
    private String BASE_URL = "http://192.168.1.102:8080/graphql";
    private ApolloClient apolloClient = ApolloClient
            .builder()
            .serverUrl(BASE_URL)
            .build();

    public NotificationService() {
        super("Notification service.");
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Приложение работает в фоне.")
                        .setContentText("Ожидает новых уведомлений.")
                        .setCategory(Notification.CATEGORY_ALARM)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        startForeground(id++, builder.build());

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        createNotificationChannel(CHANNEL_ID, CHANNEL_NAME);

        int userId = intent.getIntExtra("userId", -1);
        int groupId = intent.getIntExtra("groupId", -1);

        for (; ; ) {

            apolloClient.mutate(NotificationForStudentMutation.builder().groupId(groupId).studentId(userId).build())
                    .enqueue(new ApolloCall.Callback<NotificationForStudentMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<NotificationForStudentMutation.Data> response) {

                            for (NotificationForStudentMutation.NotificationsForStudent notificationsForStudent : response.data().notificationsForStudent()) {
                                NotificationCompat.Builder builder =
                                        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setContentText(notificationsForStudent.message())
                                                .setCategory(Notification.CATEGORY_ALARM)
                                                .setPriority(NotificationCompat.PRIORITY_MAX);

                                NotificationManager notificationManager =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


                                notificationManager.notify(id++, builder.build());

                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                        }
                    });

            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException s) {
                s.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    private String createNotificationChannel(String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

}
