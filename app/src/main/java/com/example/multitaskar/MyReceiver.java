package com.example.multitaskar;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.multitaskar.utility.AndroidUtitlity;
import com.google.firebase.firestore.DocumentReference;

public class MyReceiver extends BroadcastReceiver {
    String Schedule_Status = "0";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyReceiver", "Received broadcast");
        String documentId = intent.getStringExtra("docId");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/); // Set a timeout for the wake lock

        try {
            updateScheduleStatus(context, documentId);
            sendNotification(context, title, content);
        } catch (Exception e) {
            Log.e("Update", "Failed to update schedule status", e);
        } finally {
            wakeLock.release();
        }
    }

    private void sendNotification(Context context, String title, String content) {
        Intent i = new Intent(context, Reminder_MainActivity.class); // Change this to the activity you want to open
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), i, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Alarm_Manager")
                .setSmallIcon(R.drawable.reminder_img) // Replace with your actual icon
                .setContentTitle("Reminder: " + title)
                .setContentText(content)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.w("MyReceiver", "Notification permission not granted");
            return;
        }

        notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
    }

    void updateScheduleStatus(Context context, String documentId) {
        DocumentReference documentReference = AndroidUtitlity.getCollectionReferenceReminder().document(documentId);
        documentReference.update("schedule_status", Schedule_Status)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("Update", "Status Updated");
                    } else {
                        Log.e("Update", "Failed to update status", task.getException());
                    }
                });
    }
}
