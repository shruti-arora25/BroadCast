package com.example.alarmmanager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.getSystemService
import com.example.alarmmanager.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class MainActivity : AppCompatActivity() {

    private lateinit var bind:ActivityMainBinding
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar:Calendar
    private lateinit var alarmMan:AlarmManager
    private lateinit var pendingI:PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind=ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)


        createNtfnChanel()

        bind.btn1.setOnClickListener {
            showTimePicker()

        }

        bind.btn2.setOnClickListener {
            setAlarm()

        }
        bind.btn3.setOnClickListener {
            cancelAlarm()

        }
    }

    private fun cancelAlarm() {
        alarmMan=getSystemService(ALARM_SERVICE) as AlarmManager
        val intent=Intent(this,AlarmReceiver::class.java)

        pendingI= PendingIntent.getBroadcast(this,0,intent,0)
        alarmMan.cancel(pendingI)

        Toast.makeText(this,"Cancel Alarm",Toast.LENGTH_SHORT).show()


    }


    private fun setAlarm() {
        if (calendar == null) {
            Toast.makeText(this, "Please Set Time", Toast.LENGTH_SHORT).show()
            return
        }


            alarmMan = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReceiver::class.java)

            pendingI = PendingIntent.getBroadcast(this, 0, intent, 0)
            alarmMan.setRepeating(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, pendingI

            )

            Toast.makeText(this, "Alarm Set Successfully", Toast.LENGTH_SHORT).show()

        }


    private fun showTimePicker() {

        picker=MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Notification Time")
            .build()

        picker.show(supportFragmentManager,"Chanel")

        picker.addOnPositiveButtonClickListener {
            if (picker.hour > 12) {
                bind.time.text = String.format("%02d", picker.hour - 12) + ":" +
                        String.format("%02d", picker.minute) + " PM"


            } else {
                String.format("%02d", picker.hour) + ":" +
                        String.format("%02d", picker.minute) + " AM"


            }


            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND]=0
            calendar[Calendar.MILLISECOND]=0
        }

    }


    private fun createNtfnChanel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            val name:CharSequence="Main Chanel"
            val desc="Chanel for alarm Manager"
            val importance=NotificationManager.IMPORTANCE_HIGH
            val chanel=NotificationChannel("Chanel",name,importance)
            chanel.description=desc
            val ntfcnM=getSystemService(NotificationManager::class.java)

            ntfcnM.createNotificationChannel(chanel)


        }


    }


}