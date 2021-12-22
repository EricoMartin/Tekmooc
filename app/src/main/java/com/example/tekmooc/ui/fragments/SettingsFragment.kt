package com.example.tekmooc.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.tekmooc.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}