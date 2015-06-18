package edu.itba.hci.define.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.ToolbarActivity;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ToolbarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = new LoginFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();


    }


}

