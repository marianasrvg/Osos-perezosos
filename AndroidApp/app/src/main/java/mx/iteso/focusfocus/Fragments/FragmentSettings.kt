package mx.iteso.focusfocus.Fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import mx.iteso.focusfocus.R

/**
 * Created by luisacfl on 07/05/18.
 */
class FragmentSettings : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}