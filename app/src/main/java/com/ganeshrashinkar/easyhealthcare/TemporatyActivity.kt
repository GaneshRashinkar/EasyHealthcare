package com.ganeshrashinkar.easyhealthcare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ganeshrashinkar.easyhealthcare.databinding.ActivityTemporatyBinding

class TemporatyActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityTemporatyBinding
    val CHANNEL_ID="888"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        mBinding= ActivityTemporatyBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.btnNotification.setOnClickListener {
            var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Appointment Request")
                .setContentText("Patient Want To Book An Appointment")
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("A Patient named Albert Einstine having symptums of hypertension, Click here to confirm appointment."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(55, builder.build())
            }
        }

    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Patient Notification"
            val descriptionText = "just show notification about appointment"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}