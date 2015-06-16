package edu.itba.hci.define.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.itba.hci.define.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Intent settings = new Intent(this, BasicActivity.class);
        startActivity(settings);

        //INTENTO DE LISTA////


        ////INTENTO DE LISTA////
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent settings = new Intent(this, BasicActivity.class);
                startActivity(settings);
                return true;
            case R.id.search_button:
                AlertDialog alertDialog = createNeutralDialog("Search", "Function not available");
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AlertDialog createNeutralDialog(String title, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(true)
        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return adb.create();
    }
}
