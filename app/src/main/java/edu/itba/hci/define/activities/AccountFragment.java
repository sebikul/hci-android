package edu.itba.hci.define.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.itba.hci.define.R;

public class AccountFragment extends Fragment{

    static private final String LOG_TAG = "AccountFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.account_fragment, container, false);

        Log.v(LOG_TAG, "Fragmento de informacion de cuenta creado");

        return view;
    }

}
