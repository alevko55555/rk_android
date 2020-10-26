package com.example.rk_android

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*

class SettingsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener{
        private var mSharedPreferences: SharedPreferences? = null
        private var defaultDays = "10"
        private var defaultCurrency = "Рубль"

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_fragment, rootKey)
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            mSharedPreferences!!.registerOnSharedPreferenceChangeListener(this)
            val notificationPreference: EditTextPreference? = findPreference("days")
            if (notificationPreference != null) {
                defaultDays = notificationPreference.text
            }
            if (notificationPreference != null) {
                notificationPreference.summary = defaultDays
            }
            notificationPreference!!.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener{preference, newValue ->
                    var days: Int? = newValue.toString().toIntOrNull()
                    if (days != null && days > 0) {
                        preference.summary = newValue.toString()
                        defaultDays = newValue.toString()
                        true
                    } else {
                        preference.summary = defaultDays
                            false
                    }
                }
            val countryPreference: ListPreference? = findPreference("currencyList")
            if (countryPreference != null) {
                defaultCurrency = "USD"
            }
            countryPreference?.summary = defaultCurrency
            countryPreference!!.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener{preference, newValue ->
                    defaultCurrency = newValue.toString()
                    preference.summary = newValue.toString()
                    true
                }
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            s: String
        ) {
            val preference = findPreference<Preference>(s)
            if (preference != null) {
                if (preference is EditTextPreference) {
                    val string = mSharedPreferences!!.getString(s, "Empty")
                    preference.setSummary(string)
                }
                if (preference is ListPreference) {
                    val listPreference = preference
                    val string = mSharedPreferences!!.getString(preference.getKey(), "")
                    val index = listPreference.findIndexOfValue(string)
                    if (index >= 0) {
                        preference.setSummary(listPreference.entries[index])
                    }
                }
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            mSharedPreferences!!.unregisterOnSharedPreferenceChangeListener(this)
        }

    }
}