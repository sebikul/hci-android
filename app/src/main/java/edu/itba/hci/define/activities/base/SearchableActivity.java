package edu.itba.hci.define.activities.base;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.SearchFragment;

public class SearchableActivity extends NavBasicActivity implements SearchView.OnQueryTextListener {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_item_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        Log.v("SearchableActivity", "Realizando llamado a la busqueda");
        if(query==""){
            return false;
        }
        Fragment fragment = new SearchFragment();
        Bundle b = new Bundle();
        b.putString("query", query);
        fragment.setArguments(b);
        replaceContentWithFragment(fragment, null);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
