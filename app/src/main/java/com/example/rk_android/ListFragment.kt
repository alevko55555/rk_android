package com.example.rk_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rk_android.databinding.FragmentHostBinding
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

import kotlin.concurrent.thread

class ListFragment : Fragment() {

    private var url = "https://min-api.cryptocompare.com/documentation?key=Historical&cat=dataHistoday&api_key=600bf09ff8d82df6383db691c3873eb7fa6ea91651f8a33bfbd90dd2ddafcf4a"
    private var fragmentFirstBinding: FragmentHostBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var APP_PREFERENCES = "mysettings"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainView: View = inflater.inflate(R.layout.fragment_host, container, false)
        mainView.findViewById<Button>(R.id.open_link).setOnClickListener {
            run {
                openWebPage(url)
            }
        }
        return mainView

    }

    fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        var checkCryptoCurrency = sharedPref.getString("cryptoCurrency", "")
        if (checkCryptoCurrency != null) {
            if (checkCryptoCurrency.isEmpty()) {
                checkCryptoCurrency = "BTC"
                editor.putString("cryptoCurrency", "BTC")
                editor.apply()
            }
        }


        fragmentFirstBinding = FragmentHostBinding.bind(view)

        val recyclerView: RecyclerView = fragmentFirstBinding!!.myRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        recyclerView.adapter = ListAdapter(generalFakeValues())
    }

    @Volatile
    var values = mutableListOf<ListAdapter.Elem>()

    @SuppressLint("CheckResult")
    private fun generalFakeValues(): List<ListAdapter.Elem> {
        values = mutableListOf<ListAdapter.Elem>()
        val sharedPref = requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        var strUrl: String = "https://min-api.cryptocompare.com/data/v2/histoday?"
        var currency: String? = sharedPreferences.getString("currencyList", "USD")
        var days: String? = sharedPreferences.getString("days", "10")
        strUrl += "fsym=" + sharedPref.getString("cryptoCurrency", "BTC") +
                "&tsym=" + currency + "&limit=" + days

        val url =
            URL(strUrl)
        var s: StringBuilder = java.lang.StringBuilder("")
        val job = thread(start = true) {
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"  // optional default is GET

                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        s.append(line)
                    }
                }
            }
        }
        job.join()
        val jsonObject = JSONObject(s.toString())
        val jsonArray = jsonObject.getJSONObject("Data").getJSONArray("Data")
        val format = SimpleDateFormat("dd.MM.yyyy")
        for (jsonIndex in 0 until jsonArray.length()) {

            var close: String = jsonArray.getJSONObject(jsonIndex).getString("close")
            var date = Date(jsonArray.getJSONObject(jsonIndex).getLong("time") * 1000)
            val high: String = jsonArray.getJSONObject(jsonIndex).getString("high")
            val low: String = jsonArray.getJSONObject(jsonIndex).getString("low")
            val open: String = jsonArray.getJSONObject(jsonIndex).getString("open")
            val elem = currency?.let {
                ListAdapter.Elem(
                    format.format(date),
                    close,
                    it,
                    high,
                    low,
                    open
                )
            }
            if (elem != null) {
                values.add(elem)
            }
        }
        return values
    }
}