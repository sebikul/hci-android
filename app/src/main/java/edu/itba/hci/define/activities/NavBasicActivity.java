package edu.itba.hci.define.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.old.CategoryFragment;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Uncomment to inflate menu items to Action Bar
        inflater.inflate(R.menu.menu_foo, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
            case R.id.home_item:
                if(!this.getClass().equals(MainActivity.class)) {
                    activity = new Intent(this, MainActivity.class);
                }
                break;
            case R.id.purchases_item:
                Intent purchases = new Intent(this, PurchasesActivityNav.class);
                startActivity(purchases);
                break;
            case R.id.category_item1:
                fragment = new CategoryFragment();
                break;
            case R.id.category_item2:
                fragment = new CategoryFragment();
                break;
            case R.id.category_item3:
                fragment = new CategoryFragment();
                break;
            case R.id.category_item4:
                fragment = new CategoryFragment();
                break;
            case R.id.category_item5:
                fragment = new CategoryFragment();
                break;
            case R.id.settings_item:
                if(!this.getClass().equals(SettingsActivity.class)) {
                    activity = new Intent(this, SettingsActivity.class);
                }

                break;
            default:
                fragment = new CategoryFragment();
        }

        // Insert the fragment by replacing any existing fragment
        if(fragment != null) {
//            if(this.getClass().equals(CategoryActivity.class)) {
//                //FragmentManager fragmentManager = getSupportFragmentManager();
//                //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
//                // Highlight the selected item, update the title, and close the drawer
//                menuItem.setChecked(true);
//                setTitle(menuItem.getTitle());
//            } else {
//                activity = new Intent(this, CategoryActivity.class);
//            }
        }

        if(activity != null) {
            startActivity(activity);
        }

        mDrawer.closeDrawers(); //TODO: AFUERA O ADENTRO?
    }
}