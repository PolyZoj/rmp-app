package com.ifmo.rmp.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ifmo.rmp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import android.content.pm.ServiceInfo

class StepCounterService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var initialSteps: Float = 0f
    private lateinit var sharedPreferences: android.content.SharedPreferences
    private var lastResetDay: Int = -1

    companion object {
        private val _steps = MutableStateFlow(0)
        val steps = _steps.asStateFlow()

        const val CHANNEL_ID = "StepCounterChannel"
        const val NOTIFICATION_ID = 1
        const val PREFS_NAME = "StepPrefs"
        const val KEY_RESET_DAY = "reset_day"
    }

    override fun onCreate() {
        super.onCreate()
        try {
            sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            createNotificationChannel()
            val notification = createNotification()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }

            lastResetDay = sharedPreferences.getInt(KEY_RESET_DAY, -1)
            checkDailyReset()
        } catch (e: Exception) {
            Log.e("StepCounterService", "Failed to initialize service: ${e.message}", e)
            _steps.value = -1
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (stepCounterSensor != null) {
            try {
                sensorManager.registerListener(
                    this,
                    stepCounterSensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            } catch (e: Exception) {
                Log.e("StepCounterService", "Failed to register sensor listener: ${e.message}", e)
                _steps.value = -1
                stopSelf()
            }
        } else {
            Log.w("StepCounterService", "Step counter sensor unavailable")
            _steps.value = -1
            stopSelf()
        }
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                try {
                    checkDailyReset()
                    if (initialSteps == 0f) {
                        initialSteps = it.values[0]
                    }
                    val currentSteps = (it.values[0] - initialSteps).toInt()
                    _steps.value = currentSteps
                    Log.d("StepCounterService", "Steps updated: $currentSteps")
                } catch (e: Exception) {
                    Log.e("StepCounterService", "Error processing sensor event: ${e.message}", e)
                }
            }
        }
    }

    private fun checkDailyReset() {
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        if (lastResetDay != currentDay) {
            initialSteps = 0f
            _steps.value = 0
            sharedPreferences.edit()
                .putInt(KEY_RESET_DAY, currentDay)
                .apply()
            lastResetDay = currentDay
            Log.d("StepCounterService", "Steps reset for day: $currentDay")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        try {
            sensorManager.unregisterListener(this)
        } catch (e: Exception) {
            Log.e("StepCounterService", "Error unregistering sensor listener: ${e.message}", e)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Step Counter Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return try {
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Step Counter")
                .setContentText("Counting your steps...")
                .setSmallIcon(R.drawable.e_workout)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
        } catch (e: Exception) {
            Log.e("StepCounterService", "Failed to create notification: ${e.message}", e)
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Step Counter")
                .setContentText("Counting your steps...")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
        }
    }
}