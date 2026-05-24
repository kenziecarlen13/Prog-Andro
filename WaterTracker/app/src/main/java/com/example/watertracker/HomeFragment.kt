package com.example.watertracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
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

        progressBar.visibility = View.VISIBLE

        apiService.getRandomQuotes().enqueue(object : Callback<List<Quote>> {

            override fun onResponse(call: Call<List<Quote>>, response: Response<List<Quote>>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    // Task 8 (Modul 12): Log successful API response
                    Log.d("DEBUG_API", "Response Berhasil, jumlah data API: ${response.body()?.size ?: 0}")

                    val quote = response.body()!![0]
                    tvQuote.text    = "\"${quote.text}\""
                    tvAuthor.text   = "- ${quote.author}"
                    tvQuote.visibility  = View.VISIBLE
                    tvAuthor.visibility = View.VISIBLE
                } else {
                    // Task 8 (Modul 12): Log failed response with status code
                    Log.e("DEBUG_API", "Response gagal: ${response.code()}")
                    Toast.makeText(context, "Failed to load quote.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Quote>>, t: Throwable) {
                progressBar.visibility = View.GONE
                // Task 8 (Modul 12): Log network/connection failure
                Log.e("DEBUG_API", "Network error/Gagal: ${t.message}")
                Toast.makeText(context, "Check your internet connection.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
