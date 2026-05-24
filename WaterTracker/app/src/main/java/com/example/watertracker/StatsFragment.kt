package com.example.watertracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StatsFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var rvTiers: RecyclerView
    private lateinit var switchPublic: SwitchCompat
    private lateinit var adapter: HydrationTierAdapter
    private var tierList: List<TierUser> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        databaseHelper = DatabaseHelper(requireContext())

        // Cek data di DB supaya tidak insert berkali-kali jika belum ada tabel yg terisi
        if (databaseHelper.getComplianceData(true).isEmpty()) {
            databaseHelper.insertDummyTiers()
        }

        rvTiers = view.findViewById(R.id.rvTiers)
        switchPublic = view.findViewById(R.id.switchPublic)

        rvTiers.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi data berdasarkan switch saat ini
        loadTiersData(switchPublic.isChecked)

        // Set listener pada switch
        switchPublic.setOnCheckedChangeListener { _, isChecked ->
            loadTiersData(isChecked)
        }

        // Task 4 (Modul 7): FAB click opens AlertDialog to add a new TierUser
        val fabAddUser = view.findViewById<FloatingActionButton>(R.id.fabAddUser)
        fabAddUser.setOnClickListener {
            showAddUserDialog()
        }

        return view
    }

    private fun loadTiersData(isUserVisible: Boolean) {
        tierList = databaseHelper.getComplianceData(isUserVisible)
        adapter = HydrationTierAdapter(tierList)
        rvTiers.adapter = adapter
    }

    // Task 4 (Modul 7): AlertDialog with form to insert a new TierUser
    private fun showAddUserDialog() {
        // Build dialog layout programmatically
        val dialogLayout = LinearLayoutContainerView(requireContext())

        val etName = EditText(requireContext()).apply {
            hint = "Name"
            setText("71230994") // NIM as placeholder/default per task spec
        }
        val etDays = EditText(requireContext()).apply {
            hint = "Days Compliant (0-7)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        dialogLayout.addView(etName)
        dialogLayout.addView(etDays)

        AlertDialog.Builder(requireContext())
            .setTitle("Add New User")
            .setView(dialogLayout)
            .setPositiveButton("Add") { dialog, _ ->
                val name = etName.text.toString().trim()
                val daysText = etDays.text.toString().trim()

                if (name.isEmpty() || daysText.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val days = daysText.toIntOrNull()
                if (days == null || days < 0 || days > 7) {
                    Toast.makeText(requireContext(), "Days must be between 0 and 7", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Insert into SQLite via DatabaseHelper
                databaseHelper.insertTierUser(
                    name = name,
                    days = days,
                    uri = null,
                    isMe = false
                )

                // Refresh RecyclerView immediately
                loadTiersData(switchPublic.isChecked)
                adapter.notifyDataSetChanged()

                Toast.makeText(requireContext(), "User \"$name\" added!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // Simple LinearLayout container for the dialog form
    private inner class LinearLayoutContainerView(context: android.content.Context) :
        android.widget.LinearLayout(context) {
        init {
            orientation = VERTICAL
            val padding = (16 * resources.displayMetrics.density).toInt()
            setPadding(padding, padding, padding, padding)
        }
    }
}
