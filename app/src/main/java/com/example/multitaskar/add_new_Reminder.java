package com.example.multitaskar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.multitaskar.Model.ReminderModel;
import com.example.multitaskar.utility.AndroidUtitlity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class add_new_Reminder extends AppCompatActivity {
    EditText editTextTitle,editTextDate,editTextTime,editTextDetails;
    ImageView backBtn;
    FloatingActionButton floatingActionButton;
    Calendar calendar;
    String strDate,strTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_reminder);
        editTextTitle=findViewById(R.id.title_edittext);
        editTextDate=findViewById(R.id.Date_editText);
        editTextTime=findViewById(R.id.Time_editText);
        editTextDetails=findViewById(R.id.Details_Edittext);
        backBtn=findViewById(R.id.back_btn);
        floatingActionButton=findViewById(R.id.SaveReminder_btn);

        // Date and time picker
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        backBtn.setOnClickListener(v -> onBackPressed());
        editTextDate.setOnClickListener(v -> {
            com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view1, int year1, int monthOfYear, int dayOfMonth) {
                    calendar.set(year1, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    strDate = sdf.format(calendar.getTime());
                    editTextDate.setText(strDate);
                }
            }, year, month, day);
            datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");
        });

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        editTextTime.setOnClickListener(v -> {
                    com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view12, int hourOfDay, int minute1, int second) {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute1);

                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            strTime = sdf.format(calendar.getTime());
                            editTextTime.setText(strTime);
                        }
                    }, hour, minute, true);
                    timePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
                });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EmptyEdittext())
                {
                    SaveReminder();
                }
            }
        });
        CreateNotificationChannal();
    }

    boolean EmptyEdittext()
    {
        if(editTextTitle.getText().toString().isEmpty() || editTextTitle.getText().toString()==null)
        {
            editTextTitle.setError("set title");
            return false;
        }
        if(editTextDate.getText().toString().isEmpty() || editTextDate.getText().toString()==null)
        {
            editTextDate.setError("Date cannot be empty");
            return false;
        }
        if(editTextTime.getText().toString().isEmpty() || editTextTime.getText().toString()==null)
        {
            editTextTime.setError("Time cannot be empty");
            return false;
        }
        return true;
    }

    void SaveReminder()
    {
        Calendar now = Calendar.getInstance();
        if (calendar.before(now)) {
            AndroidUtitlity.showtoast(getApplicationContext(), "Cannot set a reminder for the past");
            return;
        }
        ReminderModel reminderModel=new ReminderModel();
        reminderModel.setSchedule_title(editTextTitle.getText().toString());
        reminderModel.setSchedule_Date(editTextDate.getText().toString());
        reminderModel.setSchedule_Time(editTextTime.getText().toString());
        reminderModel.setSchedule_Content(editTextDetails.getText().toString());
        reminderModel.setSchedule_status("1");
        reminderModel.setTimestamp(Timestamp.now());
        SavetoFirebase(reminderModel);
    }
    void SavetoFirebase(ReminderModel reminderModel)
    {
        DocumentReference documentReference;
        documentReference= AndroidUtitlity.getCollectionReferenceReminder().document();
        documentReference.set(reminderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    AndroidUtitlity.showtoast(getApplicationContext(),"Scheduled Sucessfully");
                    setAlarm(documentReference.getId(), reminderModel.getSchedule_title(),reminderModel.getSchedule_Content());
                    finish();
                }
                else {
                    AndroidUtitlity.showtoast(getApplicationContext(),"Failed ");
                }
            }
        });
    }
    private void CreateNotificationChannal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder Notification";
            String description = "Channel for Reminder Alarms";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Alarm_Manager", name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.i("alaram","Manager_01");
        }
        else {
            Log.i("alaram","Manager_lower_then_8");
        }
    }


    private void setAlarm(String documentId, String title,String content) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long alarmTime = calendar.getTimeInMillis();

        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        intent.putExtra("docId", documentId);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        Log.i("Time", "aaaa" + alarmTime);
        PendingIntent pi;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pi = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(),
                    intent, PendingIntent.FLAG_ONE_SHOT  | PendingIntent.FLAG_IMMUTABLE);
        }
        else {
            pi = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(),
                    intent, PendingIntent.FLAG_ONE_SHOT  | PendingIntent.FLAG_IMMUTABLE);
        }
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pi);
    }
}