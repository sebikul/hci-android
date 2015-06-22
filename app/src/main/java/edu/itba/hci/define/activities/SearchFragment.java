package edu.itba.hci.define.activities;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.NavBasicActivity;
import edu.itba.hci.define.adapters.ProductListAdapter;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.ApiProductFilter;
import edu.itba.hci.define.models.ProductList;

/**
 * Created by Diego on 22/06/2015.
 */
public class SearchFragment extends Fragment {
    private String query;
    private ListView listView;
    private AsyncTask request;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.category_product_list);
        listView.setClickable(true);
        setHasOptionsMenu(true);
        Bundle args = getArguments();

        query = args.getString("query");

        request= ApiManager.getProductsByName(query, 1, 500, new ApiProductFilter[]{},  new Callback<ProductList>() {
            @Override
            public void onSuccess(final ProductList response) {
                ProductListAdapter adapter = new ProductListAdapter(getActivity(), R.layout.product_item, response.getProducts());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), ProductActivity.class);
                        intent.putExtra("productId", response.getProducts().get(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(ApiError error) {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_img), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorConnection() {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_conection), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        request.cancel(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v("SearchFragment", "Cargando menu del search");

        NavBasicActivity activity = ((NavBasicActivity) getActivity());
        activity.setTitle(getResources().getString(R.string.title_search) + " \""  + query +"\"");
        activity.setToggleDrawer(false);
        ((NavigationView) activity.findViewById(R.id.nvView)).getMenu().findItem(R.id.item_purchases).setChecked(true);
        super.onCreateOptionsMenu(menu, inflater);

    }
}
