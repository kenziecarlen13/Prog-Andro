package com.example.watertracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var apiService: ApiService

    private lateinit var progressBar: ProgressBar
    private lateinit var tvQuote: TextView
    private lateinit var tvAuthor: TextView
    
    private lateinit var progressCircleIndicator: CircularProgressIndicator
    private lateinit var textProgress: TextView
    private lateinit var fabAddWater: ExtendedFloatingActionButton

    private var currentIntake = 0
    private var dailyGoal = 2000 

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar)
        tvQuote     = view.findViewById(R.id.tvQuote)
        tvAuthor    = view.findViewById(R.id.tvAuthor)
        
        progressCircleIndicator = view.findViewById(R.id.progressCircleIndicator)
        textProgress = view.findViewById(R.id.textProgress)
        fabAddWater = view.findViewById(R.id.fabAddWater)

        // Load persisted data
        val sharedPref = requireActivity().getSharedPreferences("water_prefs", Context.MODE_PRIVATE)
        currentIntake = sharedPref.getInt("current_intake", 0)
        dailyGoal = sharedPref.getInt("daily_target", 2000) 
        
        updateUI()

        fabAddWater.setOnClickListener {
            // Ambil cup_size terbaru dari SharedPreferences
            val cupSize = sharedPref.getInt("cup_size", 250)
            currentIntake += cupSize
            
            with(sharedPref.edit()) {
                putInt("current_intake", currentIntake)
                apply()
            }
            updateUI()
            Toast.makeText(context, "Added $cupSize ml of water!", Toast.LENGTH_SHORT).show()
        }

        fetchQuote()
    }

    private fun updateUI() {
        progressCircleIndicator.max = dailyGoal
        progressCircleIndicator.setProgress(currentIntake, true)
        textProgress.text = "${currentIntake}ml\n/ ${dailyGoal}ml"
    }

    private fun fetchQuote() {
        progressBar.visibility = View.VISIBLE
        apiService.getRandomQuotes().enqueue(object : Callback<List<Quote>> {
            override fun onResponse(call: Call<List<Quote>>, response: Response<List<Quote>>) {
                progressBar.visibility = View.GONE
                val quotes = response.body()
                if (response.isSuccessful && !quotes.isNullOrEmpty()) {
                    val quote = quotes.random()
                    tvQuote.text    = "\"${quote.text}\""
                    tvAuthor.text   = "- ${quote.author}"
                    tvQuote.visibility  = View.VISIBLE
                    tvAuthor.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Quote>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("DEBUG_API", "Network error: ${t.message}")
            }
        })
    }
}
