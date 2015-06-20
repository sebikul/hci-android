package edu.itba.hci.define.activities;


import android.os.Bundle;
import android.util.Log;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.ToolbarActivity;

public class PurchaseActivity extends ToolbarActivity {

    static private final String LOG_TAG = "PurchaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orderId = getIntent().getIntExtra("orderid", -1);

        Log.v(LOG_TAG, "ID de orden: " + orderId);

        setContentView(R.layout.activity_purchase);
    }

}
