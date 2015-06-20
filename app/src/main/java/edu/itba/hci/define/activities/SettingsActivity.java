package edu.itba.hci.define.activities;

import android.os.Bundle;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.ToolbarActivity;

public class SettingsActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();

    }

}
