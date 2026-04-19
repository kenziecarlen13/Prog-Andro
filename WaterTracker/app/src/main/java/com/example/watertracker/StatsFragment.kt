package com.example.watertracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        return view
    }

    private fun loadTiersData(isUserVisible: Boolean) {
        tierList = databaseHelper.getComplianceData(isUserVisible)
        adapter = HydrationTierAdapter(tierList)
        rvTiers.adapter = adapter
    }
}
