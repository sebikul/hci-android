package edu.itba.hci.define.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SearchActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.v("SearchActivity", "Iniciando actividad "+getIntent().getStringExtra("query"));

        //handleIntent(getIntent());
    }
}
