package edu.itba.hci.define.activities.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.CategoryFragment;
import edu.itba.hci.define.activities.HelpActivity;
import edu.itba.hci.define.activities.MainActivity;
import edu.itba.hci.define.activities.PurchasesActivityNav;
import edu.itba.hci.define.activities.SettingsActivity;

public class NavBasicActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle toggleDrawer;
    private NavigationView nvDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_nav);

        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggleDrawer = setupDrawerToggle();
        mDrawer.setDrawerListener(toggleDrawer);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open,  R.string.drawer_close);
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
        if(toggleDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;
        Intent activity = null;

        switch(menuItem.getItemId()) {
            case R.id.item_home:
                if(!this.getClass().equals(MainActivity.class)) {
                    activity = new Intent(this, MainActivity.class);
                }
                break;
            case R.id.item_purchases:
                Intent purchases = new Intent(this, PurchasesActivityNav.class);
                startActivity(purchases);
                break;
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
                if(!this.getClass().equals(SettingsActivity.class)) {
                    activity = new Intent(this, SettingsActivity.class);
                }
                break;
            case R.id.item_help:
                if(!this.getClass().equals(HelpActivity.class)) {
                    activity = new Intent(this, HelpActivity.class);
                }
                break;
            default:
                fragment = new CategoryFragment();
        }

        // Insert the fragment by replacing any existing fragment
        if(fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();

            // Highlight the selected item, update the title, and close the drawer
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
        }

        if(activity != null) {
            startActivity(activity);
        }

        mDrawer.closeDrawers();
    }
}