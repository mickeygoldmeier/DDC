package com.ddc.Model;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.ddc.MainActivity;
import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.ParcelRepository;
import com.ddc.R;

import java.util.List;

import static com.ddc.Model.Parcel.Parcel_Status.CollectionOffered;
import static com.ddc.Model.Parcel.Parcel_Status.Registered;

public class ServiceUpdate extends LifecycleService {

    ParcelRepository repository;
    LiveData<List<Parcel>> allParcel;
    List<Parcel> parcels;
    private static int ID = 1;
    private static int lastShownNotificationId = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        repository = new ParcelRepository(getApplication());
        allParcel = repository.getAllParcels();
        parcels = allParcel.getValue();

        allParcel.observe(this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                detectChanges();
            }
        });
    }

    private void detectChanges() {
        if (allParcel.getValue() != null) {
            for (Parcel parcel : allParcel.getValue())
                if (!parcel.isNotified()) {
                    //generateNotification(parcel);
                    createAndShowForegroundNotification(this, ++ID, parcel);
                    parcel.setNotified(true);
                    repository.update(parcel);
                }
        }
    }

    private void generateNotification(Parcel parcel) {
        String massege = "";
        String tilte = "";

        switch (parcel.getParcelStatus()) {
            case Registered:
                massege = "יש לך חבילה חדשה שמחכה לך במחסן, שמספרה " + parcel.getParcelID() + "\nברגע שאחד מהחברים שלך יציע לאסוף אותה תקבל הודעה מתאימה";
                tilte = "חבילה חדשה!";
                break;
            case CollectionOffered:
                massege = "מישהו הציע לאסוף עבורך את החבילה שמספרה " + parcel.getParcelID() + "!\n"
                        + "כנס לאפליקציה על מנת לאשר לו את ההובלה";
                tilte = "איזה מזל שיש חברים";
                break;
            case Delivered:
                massege = "החבילה שלך (חבילה מספר " + parcel.getParcelID() + ") נמצאת כעת בדרכה אליך.\n" +
                        "אתה מוזמן ליצור קשר עם החבר שמוביל אותה במספר " + parcel.getSelectedDeliver();
                tilte = "חבילה בדרך אליך!";
                break;
            default:
                return;
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelId);
            notificationChannel.setVibrationPattern(new long[]{0, 500, 500, 300});
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), channelId)
                    .setContentTitle(tilte)
                    .setContentText(massege)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(""))
                    .setSmallIcon(R.mipmap.app_icon)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .build();
            notificationManager.notify(ID++, notification);
        } else {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.app_icon)
                            .setContentTitle(tilte)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(""))
                            .setVibrate(new long[]{0, 500, 500, 300})
                            .setLights(Color.RED, 500, 500)
                            .setContentText(massege);

            Intent targetIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(ID++, builder.build());
        }
    }

    private void createAndShowForegroundNotification(Service yourService, int notificationId, Parcel parcel) {
        String massege = "";
        String tilte = "";

        switch (parcel.getParcelStatus()) {
            case Registered:
                massege = "יש לך חבילה חדשה שמחכה לך במחסן, שמספרה " + parcel.getParcelID() + "\nברגע שאחד מהחברים שלך יציע לאסוף אותה תקבל הודעה מתאימה";
                tilte = "חבילה חדשה!";
                break;
            case CollectionOffered:
                massege = "מישהו הציע לאסוף עבורך את החבילה שמספרה " + parcel.getParcelID() + "!\n"
                        + "כנס לאפליקציה על מנת לאשר לו את ההובלה";
                tilte = "איזה מזל שיש חברים";
                break;
            case Delivered:
                massege = "החבילה שלך (חבילה מספר " + parcel.getParcelID() + ") נמצאת כעת בדרכה אליך.\n" +
                        "אתה מוזמן ליצור קשר עם החבר שמוביל אותה במספר " + parcel.getSelectedDeliver();
                tilte = "חבילה בדרך אליך!";
                break;
            default:
                return;
        }


        final NotificationCompat.Builder builder = getNotificationBuilder(yourService,
                "com.example.your_app.notification.CHANNEL_ID_FOREGROUND", // Channel id
                NotificationManagerCompat.IMPORTANCE_DEFAULT); //Low importance prevent visual appearance for this notification channel on top
        builder.setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(tilte)
                .setContentTitle(tilte)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(""))
                .setContentText(massege);

        NotificationManager manager = (NotificationManager)getSystemService(Service.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        manager.notify(ID++, notification);

        //yourService.startForeground(notificationId, notification);

        if (notificationId != lastShownNotificationId) {
            // Cancel previous notification
            final NotificationManager nm = (NotificationManager) yourService.getSystemService(Activity.NOTIFICATION_SERVICE);
            nm.cancel(lastShownNotificationId);
        }
        lastShownNotificationId = notificationId;
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context context, String channelId, int importance) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prepareChannel(context, channelId, importance);
            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        return builder;
    }

    @TargetApi(26)
    private static void prepareChannel(Context context, String id, int importance) {
        final String appName = context.getString(R.string.app_name);
        String description = context.getString(R.string.gcm_defaultSenderId);
        final NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);

        if(nm != null) {
            NotificationChannel nChannel = nm.getNotificationChannel(id);

            if (nChannel == null) {
                nChannel = new NotificationChannel(id, appName, importance);
                nChannel.setDescription(description);
                nm.createNotificationChannel(nChannel);
            }
        }
    }
}
