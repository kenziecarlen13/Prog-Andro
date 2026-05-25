package com.example.watertracker

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.Calendar

class ReminderFragment : Fragment() {

    private lateinit var timePicker: TimePicker
    private lateinit var btnSetReminder: Button
    private lateinit var tvScheduledAlarms: TextView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            scheduleAlarm()
        } else {
            Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminder, container, false)
        timePicker = view.findViewById(R.id.timePicker)
        btnSetReminder = view.findViewById(R.id.btnSetReminder)
        tvScheduledAlarms = view.findViewById(R.id.tvScheduledAlarms)

        btnSetReminder.setOnClickListener {
            checkPermissionsAndSchedule()
        }

        displayAlarms()

        return view
    }

    private fun checkPermissionsAndSchedule() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
                return
            }
        }

        scheduleAlarm()
    }

    private fun scheduleAlarm() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) timePicker.hour else timePicker.currentHour
        val minute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) timePicker.minute else timePicker.currentMinute
        
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            hour * 100 + minute,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            
            saveAlarmTime(String.format("%02d:%02d", hour, minute))
            displayAlarms()
            
            Toast.makeText(requireContext(), "Reminder set for ${calendar.time}", Toast.LENGTH_LONG).show()
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Exact alarm permission required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAlarmTime(time: String) {
        val sharedPref = requireActivity().getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        val alarms = sharedPref.getStringSet("scheduled_alarms", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        alarms.add(time)
        sharedPref.edit().putStringSet("scheduled_alarms", alarms).apply()
    }

    private fun displayAlarms() {
        val sharedPref = requireActivity().getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        val alarms = sharedPref.getStringSet("scheduled_alarms", emptySet())?.toList()?.sorted() ?: emptyList()
        tvScheduledAlarms.text = if (alarms.isEmpty()) "No reminders set" else alarms.joinToString("\n")
    }
}
