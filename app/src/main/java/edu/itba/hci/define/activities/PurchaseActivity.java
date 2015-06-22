package edu.itba.hci.define.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.NavBasicActivity;

public class PurchaseActivity extends NavBasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = new PurchasesFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();


    }

}
