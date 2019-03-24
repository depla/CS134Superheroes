package edu.miracosta.cs134;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity for choose what quiz to take
 *
 * @author Dennis La
 * @version
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * Displays the settings fragment to the screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
        new SettingsActivityFragment()).commit();
    }

    /**
     * Creates a fragment using preferences.xml
     */
    public static class SettingsActivityFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource

            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
