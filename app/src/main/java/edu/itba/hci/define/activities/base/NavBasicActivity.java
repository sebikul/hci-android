package edu.itba.hci.define.activities.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import edu.itba.hci.define.DefineApplication;
import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.AccountFragment;
import edu.itba.hci.define.activities.CategoryFragment;
import edu.itba.hci.define.activities.HelpActivity;
import edu.itba.hci.define.activities.HomeFragment;
import edu.itba.hci.define.activities.LoginActivity;
import edu.itba.hci.define.activities.PurchasesFragment;
import edu.itba.hci.define.activities.SettingsActivity;

public class NavBasicActivity extends AppCompatActivity {

    static public final int INTENT_NONE = 0;
    static public final int INTENT_PURCHASES = 1;

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
                replaceContentWithFragment(new AccountFragment(), "account");
                mDrawer.closeDrawers();
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
            MenuItem purchases = nvDrawer.getMenu().findItem(R.id.item_purchases);
            purchases.setVisible(true);

            header = sessionView;
        } else {
            Log.v(LOG_TAG, "Agregando header con sesion de invitado al navdrawer");
            header = guestView;

        }

        nvDrawer.addHeaderView(header);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        if (getIntent().getIntExtra("action", INTENT_NONE) == INTENT_PURCHASES) {
            replaceContentWithFragment(new AccountFragment(), "account");
        }

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggleDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {

        Intent activity;

        switch (menuItem.getItemId()) {
            case R.id.item_home:
                replaceContentWithFragment(new HomeFragment(), "home", menuItem);
                break;

            //Seccion con sesion activa
            case R.id.item_purchases:
                toggleDrawer.setDrawerIndicatorEnabled(false);
                toggleDrawer.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                toggleDrawer.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onBackPressed();

                        Log.v(LOG_TAG, "33333_ -----------------------------");
                    }
                });
                replaceContentWithFragment(new PurchasesFragment(), "purchases", menuItem);
                break;
            //END Seccion con sesion activa

            case R.id.item_category_1:
                replaceContentWithFragment(new CategoryFragment(), null, menuItem);
                break;

            case R.id.item_category_2:
                replaceContentWithFragment(new CategoryFragment(), null, menuItem);
                break;

            case R.id.item_category_3:
                replaceContentWithFragment(new CategoryFragment(), null, menuItem);
                break;

            case R.id.item_category_4:
                replaceContentWithFragment(new CategoryFragment(), null, menuItem);
                break;

            case R.id.item_category_5:
                replaceContentWithFragment(new CategoryFragment(), null, menuItem);
                break;

            case R.id.item_settings:
                if (!this.getClass().equals(SettingsActivity.class)) {
                    activity = new Intent(this, SettingsActivity.class);
                    startActivity(activity);
                }
                break;

            case R.id.item_help:
                if (!this.getClass().equals(HelpActivity.class)) {
                    activity = new Intent(this, HelpActivity.class);
                    startActivity(activity);
                }
                break;

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

    private void replaceContentWithFragment(Fragment fragment, String tag, MenuItem trigger) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment != null && currentFragment.isVisible()) {
            Log.v(LOG_TAG, "Este fragmento ya esta encajado wacho");
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(tag);
        transaction.replace(R.id.content, fragment, tag);

        transaction.commit();

        if (trigger != null) {
            trigger.setChecked(true);
            setTitle(trigger.getTitle());

        }

    }

    private void replaceContentWithFragment(Fragment fragment, String tag) {

        replaceContentWithFragment(fragment, tag, null);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {

                Log.v(LOG_TAG, "Reemplazando el header con el de la sesion.");

                if (context.isLoggedIn()) {

                    nvDrawer.removeHeaderView(guestView);
                    nvDrawer.addHeaderView(sessionView);

                    MenuItem purchases = nvDrawer.getMenu().findItem(R.id.item_purchases);
                    purchases.setVisible(true);
                }


            }
        }
    }

}