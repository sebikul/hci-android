package edu.itba.hci.define.activities.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.itba.hci.define.DefineApplication;
import edu.itba.hci.define.activities.HomeFragment;
import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.CategoryFragment;
import edu.itba.hci.define.activities.HelpActivity;
import edu.itba.hci.define.activities.LoginActivity;
import edu.itba.hci.define.activities.PurchasesActivity;
import edu.itba.hci.define.activities.SettingsActivity;
import edu.itba.hci.define.api.ApiManager;

public class NavBasicActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle toggleDrawer;
    private NavigationView nvDrawer;

    protected DefineApplication context;

    static private final String LOG_TAG = "NavBasicActivity";

    static private int LOGIN_REQUEST = 1;

    private View guestView;
    private View sessionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_nav);

        context = (DefineApplication) getApplicationContext();

        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggleDrawer = setupDrawerToggle();
        mDrawer.setDrawerListener(toggleDrawer);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        View header;

        sessionView = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        sessionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LOG_TAG, "Abriendo informacion de la cuenta del usuario.");
            }
        });

        guestView = LayoutInflater.from(this).inflate(R.layout.nav_header_guest, null);
        guestView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(NavBasicActivity.this, LoginActivity.class);
                startActivityForResult(loginIntent, LOGIN_REQUEST);
            }
        });

        if (context.isLoggedIn()) {
            Log.v(LOG_TAG, "Agregando header con sesion activa al navdrawer");
            header = sessionView;
        } else {
            Log.v(LOG_TAG, "Agregando header con sesion de invitado al navdrawer");
            header = guestView;

        }

        nvDrawer.addHeaderView(header);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        if (savedInstanceState == null) {
            selectDrawerItem(nvDrawer.getMenu().getItem(0));
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggleDrawer.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggleDrawer.onConfigurationChanged(newConfig);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        // Uncomment to inflate menu items to Action Bar
//        inflater.inflate(R.menu.menu_foo, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggleDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;
        Intent activity = null;

        switch (menuItem.getItemId()) {
            case R.id.item_home:
                fragment = new HomeFragment();
                break;

            //Seccion con sesion activa
            case R.id.item_purchases:
                if (!this.getClass().equals(PurchasesActivity.class)) {
                    activity = new Intent(this, PurchasesActivity.class);
                }
                break;
            //END Seccion con sesion activa

            case R.id.item_category_1:
                fragment = new CategoryFragment();
                break;
            case R.id.item_category_2:
                fragment = new CategoryFragment();
                break;
            case R.id.item_category_3:
                fragment = new CategoryFragment();
                break;
            case R.id.item_category_4:
                fragment = new CategoryFragment();
                break;
            case R.id.item_category_5:
                fragment = new CategoryFragment();
                break;
            case R.id.item_settings:
                if (!this.getClass().equals(SettingsActivity.class)) {
                    activity = new Intent(this, SettingsActivity.class);
                }
                break;
            case R.id.item_help:
                if (!this.getClass().equals(HelpActivity.class)) {
                    activity = new Intent(this, HelpActivity.class);
                }
                break;
            default:
                fragment = new CategoryFragment();
        }

        // Insert the fragment by replacing any existing fragment
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, fragment);
            //transaction.addToBackStack(null);
            transaction.commit();

            // Highlight the selected item, update the title, and close the drawer
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
        }

        if (activity != null) {
            startActivity(activity);
        }

        mDrawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(nvDrawer))
            mDrawer.closeDrawer(nvDrawer);
        else
            super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == LOGIN_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Log.v(LOG_TAG, "Reemplazando el header con el de la sesion.");

                if (context.isLoggedIn()) {

                    nvDrawer.removeHeaderView(guestView);

                    nvDrawer.addHeaderView(sessionView);

                    MenuItem purchases = nvDrawer.getMenu().findItem(R.id.item_purchases);
                    purchases.setVisible(true);
                }


                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }

}