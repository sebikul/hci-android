package edu.itba.hci.define.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.NavBasicActivity;

public class PurchasesActivityNav extends NavBasicActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);



        listView = (ListView) findViewById(R.id.listView);

        Map<String, String> values1 = new HashMap<>();
        values1.put("nombre", "Item 1");
        values1.put("descripcion", "Descripcion Item 1");

        Map<String, String> values2 = new HashMap<>();
        values2.put("nombre", "Item 2");
        values2.put("descripcion", "Descripcion Item 2");

        Map<String, String> values3 = new HashMap<>();
        values3.put("nombre", "Item 3");
        values3.put("descripcion", "Descripcion Item 3");

        Map<String, String> values4 = new HashMap<>();
        values4.put("nombre", "Item 4");
        values4.put("descripcion", "Descripcion Item 4");

        List<Map<String, String>> values = Arrays.asList(values1, values2, values3, values4, values4, values3, values2, values1);

        PurchaseListAdapter adapter =
                new PurchaseListAdapter(PurchasesActivityNav.this, R.layout.purchase_list_item, values);
        listView.setClickable(true);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_purchases, menu);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
