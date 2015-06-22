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
import android.widget.TextView;

import edu.itba.hci.define.DefineApplication;
import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.AccountActivity;
import edu.itba.hci.define.activities.CategoryFragment;
import edu.itba.hci.define.activities.HelpActivity;
import edu.itba.hci.define.activities.HomeFragment;
import edu.itba.hci.define.activities.LoginActivity;
import edu.itba.hci.define.activities.PurchaseActivity;
import edu.itba.hci.define.activities.PurchasesFragment;
import edu.itba.hci.define.activities.SettingsActivity;

public class NavBasicActivity extends AppCompatActivity {

    public static final int INTENT_NONE = 0;
    public static final int INTENT_PURCHASES = 1;

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

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setToggleDrawer(true);

        sessionView = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        sessionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LOG_TAG, "Abriendo informacion de la cuenta del usuario.");
                Intent intent = new Intent(NavBasicActivity.this, AccountActivity.class);
                startActivity(intent);

                /*replaceContentWithFragment(new AccountFragment(), "account");
                mDrawer.closeDrawers();*/

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

        View header;
        if (context.isLoggedIn()) {
            Log.v(LOG_TAG, "Agregando header con sesion activa al navdrawer");
            MenuItem purchases = nvDrawer.getMenu().findItem(R.id.item_purchases);
            purchases.setVisible(true);


            ((TextView) sessionView.findViewById(R.id.header_email)).setText(context.getSession().getEmail());
            ((TextView) sessionView.findViewById(R.id.header_name)).setText(context.getSession().getFirstName() + " " + context.getSession().getLastName());

            header = sessionView;
        } else {
            Log.v(LOG_TAG, "Agregando header con sesion de invitado al navdrawer");
            header = guestView;

        }

        nvDrawer.addHeaderView(header);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        if (getIntent().getIntExtra("action", INTENT_NONE) == INTENT_PURCHASES) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, new PurchasesFragment(), "purchases");
            transaction.commit();
        } else if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, new HomeFragment(), "home");
            transaction.commit();
        }
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

                if (!this.getClass().equals(PurchaseActivity.class)) {
                    activity = new Intent(this, PurchaseActivity.class);
                    startActivity(activity);
                }

                //replaceContentWithFragment(new PurchasesFragment(), "purchases", menuItem);
                break;
            //END Seccion con sesion activa

            case R.id.item_category_1:
                replaceContentWithArgs(CategoryFragment.class, null, menuItem, CategoryFragment.GIRL, CategoryFragment.ADULT);
                break;

            case R.id.item_category_2:
                replaceContentWithArgs(CategoryFragment.class, null, menuItem, CategoryFragment.BOY, CategoryFragment.ADULT);
                break;

            case R.id.item_category_3:
                replaceContentWithArgs(CategoryFragment.class, null, menuItem, CategoryFragment.GIRL, CategoryFragment.KID);
                break;

            case R.id.item_category_4:
                replaceContentWithArgs(CategoryFragment.class, null, menuItem, CategoryFragment.BOY, CategoryFragment.KID);
                break;

            case R.id.item_category_5:
                replaceContentWithArgs(CategoryFragment.class, null, menuItem, CategoryFragment.BOTH, CategoryFragment.BABY);
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
        if (mDrawer.isDrawerOpen(nvDrawer)) {
            mDrawer.closeDrawer(nvDrawer);
        } else {
            super.onBackPressed();
        }
    }

    public void unselectAll() {

        Log.v(LOG_TAG, "Limpiando seleccion del navdrawer");

        for (int i = 0; i < nvDrawer.getMenu().size(); i++) {

            nvDrawer.getMenu().getItem(i).setChecked(false);
        }
    }

    protected void replaceContentWithFragment(Fragment fragment, String tag, MenuItem trigger) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment != null && currentFragment.isVisible()) {
            Log.v(LOG_TAG, "Este fragmento ya esta encajado");
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(tag);
        transaction.replace(R.id.content, fragment, tag);

        transaction.commit();

//        if(trigger != null) {
//            trigger.setChecked(true);
//        }
    }

    protected void replaceContentWithFragment(Fragment fragment, String tag) {
        replaceContentWithFragment(fragment, tag, null);
    }

    private void replaceContentWithArgs(Class<CategoryFragment> fragmentClass, String tag, MenuItem trigger, int gender, int age) {

        try {
            CategoryFragment fragment = fragmentClass.newInstance();

            Bundle args = new Bundle();
            args.putInt("age", age);
            args.putInt("gender", gender);
            fragment.setArguments(args);

            replaceContentWithFragment(fragment, tag, trigger);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.v(LOG_TAG, "Reemplazando el header con el de la sesion.");

                if (context.isLoggedIn()) {
                    nvDrawer.removeHeaderView(guestView);


                    ((TextView) sessionView.findViewById(R.id.header_email)).setText(context.getSession().getEmail());
                    ((TextView) sessionView.findViewById(R.id.header_name)).setText(context.getSession().getFirstName() + " " + context.getSession().getLastName());


                    nvDrawer.addHeaderView(sessionView);

                    MenuItem purchases = nvDrawer.getMenu().findItem(R.id.item_purchases);
                    purchases.setVisible(true);
                }
            }
        }
    }

    public void setToggleDrawer(boolean bool) {
        if (bool) {
            toggleDrawer = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);

            toggleDrawer.syncState();
            mDrawer.setDrawerListener(toggleDrawer);
        } else {
            toggleDrawer.setDrawerIndicatorEnabled(false);
            toggleDrawer.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            toggleDrawer.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }


}