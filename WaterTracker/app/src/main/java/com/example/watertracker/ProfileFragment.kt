package com.example.watertracker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class ProfileFragment : Fragment() {

    private lateinit var tvDailyTargetValue: TextView
    private lateinit var tvCupSizeValue: TextView
    private lateinit var tvWeightValue: TextView
    private lateinit var tvReminderFrequencyValue: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvDailyTargetValue = view.findViewById(R.id.tvDailyTargetValue)
        tvCupSizeValue = view.findViewById(R.id.tvCupSizeValue)
        tvWeightValue = view.findViewById(R.id.tvWeightValue)
        tvReminderFrequencyValue = view.findViewById(R.id.tvReminderFrequencyValue)

        val cardDailyTarget = view.findViewById<MaterialCardView>(R.id.cardDailyTarget)
        val cardCupSize = view.findViewById<MaterialCardView>(R.id.cardCupSize)
        val cardWeight = view.findViewById<MaterialCardView>(R.id.cardWeight)
        val btnOpenWeb = view.findViewById<View>(R.id.btnOpenWeb)
        val btnExit = view.findViewById<View>(R.id.btnExit)

        loadProfileData()

        cardDailyTarget.setOnClickListener {
            showEditDialog("Daily Target (ml)", "daily_target", 2000) {
                loadProfileData()
            }
        }

        cardCupSize.setOnClickListener {
            showEditDialog("Cup Size (ml)", "cup_size", 250) {
                loadProfileData()
            }
        }

        cardWeight.setOnClickListener {
            showEditDialog("Weight (kg)", "user_weight", 70) {
                loadProfileData()
            }
        }

        btnOpenWeb.setOnClickListener {
            val url = "https://eclass.ukdw.ac.id/e-class/id/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        btnExit.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Exit Application")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes") { _, _ ->
                    requireActivity().finishAffinity()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun loadProfileData() {
        val waterPrefs = requireActivity().getSharedPreferences("water_prefs", Context.MODE_PRIVATE)
        
        val target = waterPrefs.getInt("daily_target", 2000)
        tvDailyTargetValue.text = "$target ml"

        val cupSize = waterPrefs.getInt("cup_size", 250)
        tvCupSizeValue.text = "$cupSize ml"

        val profilePrefs = requireActivity().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        val weight = profilePrefs.getInt("user_weight", 0)
        tvWeightValue.text = if (weight > 0) "$weight kg" else "-- kg"

        val reminderPrefs = requireActivity().getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        val alarms = reminderPrefs.getStringSet("scheduled_alarms", emptySet()) ?: emptySet()
        tvReminderFrequencyValue.text = "${alarms.size} times"
    }

    private fun showEditDialog(title: String, prefKey: String, defaultValue: Int, onUpdate: () -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit $title")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        
        val currentVal = if (prefKey == "daily_target" || prefKey == "cup_size") {
            requireActivity().getSharedPreferences("water_prefs", Context.MODE_PRIVATE).getInt(prefKey, defaultValue)
        } else {
            requireActivity().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE).getInt(prefKey, defaultValue)
        }
        input.setText(currentVal.toString())
        
        builder.setView(input)

        builder.setPositiveButton("Save") { _, _ ->
            val newValue = input.text.toString().toIntOrNull()
            if (newValue != null && newValue > 0) {
                if (prefKey == "daily_target" || prefKey == "cup_size") {
                    requireActivity().getSharedPreferences("water_prefs", Context.MODE_PRIVATE)
                        .edit().putInt(prefKey, newValue).apply()
                } else {
                    requireActivity().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
                        .edit().putInt(prefKey, newValue).apply()
                }
                onUpdate()
            } else {
                Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}
