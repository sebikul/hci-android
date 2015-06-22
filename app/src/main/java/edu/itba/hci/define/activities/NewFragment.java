package edu.itba.hci.define.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import edu.itba.hci.define.R;
import edu.itba.hci.define.adapters.ProductListAdapter;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.ApiProductFilter;
import edu.itba.hci.define.models.ProductList;

/**
 * Created by Diego on 21/06/2015.
 */
public class NewFragment extends Fragment {

    private int age, gender;
    private ListView listView;
    private AsyncTask request;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.category_product_list);
        listView.setClickable(true);

        Bundle args = getArguments();

        age = args.getInt("age");
        gender = args.getInt("gender");
        ApiProductFilter[] filters = new ApiProductFilter[3];
        filters[0]=new ApiProductFilter(6, "Nuevo");
        filters[1]=new ApiProductFilter(2, CategoryFragment.convertToString(age));
        if(CategoryFragment.convertToString(gender)!=null){
            filters[2]= new ApiProductFilter(1, CategoryFragment.convertToString(gender));
        }

        request= ApiManager.getAllProducts(1, 500, filters, new Callback<ProductList>() {
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

}
