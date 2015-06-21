package edu.itba.hci.define.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.itba.hci.define.R;

public class SaleFragment extends Fragment {

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

        //request= ApiManager.getAllProducts()
        return view;
    }
}
