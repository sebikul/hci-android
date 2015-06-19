package edu.itba.hci.define.activities;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

import edu.itba.hci.define.DefineApplication;
import edu.itba.hci.define.R;
import edu.itba.hci.define.models.User;

public class AccountFragment extends Fragment {

    static private final String LOG_TAG = "AccountFragment";
    private TextView nameView;
    private TextView lastNameView;
    private TextView sexView;
    private TextView identView;
    private TextView emailView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.account_fragment, container, false);

        nameView = (TextView) view.findViewById(R.id.account_name);
        lastNameView = (TextView) view.findViewById(R.id.account_lastname);
        sexView = (TextView) view.findViewById(R.id.account_sex);
        identView = (TextView) view.findViewById(R.id.account_ident);
        emailView = (TextView) view.findViewById(R.id.account_email);

        Log.v(LOG_TAG, "Fragmento de informacion de cuenta creado");

        final DefineApplication context = (DefineApplication) this.getActivity().getApplicationContext();

        User user = context.getSession();

        if (user == null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    context.updateUserData();

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    updateUserData(context.getSession());
                }
            }.execute();
        } else {
            updateUserData(user);
        }




        return view;
    }

    private void updateUserData(User user) {

        nameView.setText(user.getFirstName());
        lastNameView.setText(user.getLastName());
        Resources resources = getActivity().getResources();
        sexView.setText(user.getGender() == 'M' ? resources.getString(R.string.sex_male) : resources.getString(R.string.sex_female));
        identView.setText(user.getIdentityCard());
        emailView.setText(user.getEmail());

    }

}