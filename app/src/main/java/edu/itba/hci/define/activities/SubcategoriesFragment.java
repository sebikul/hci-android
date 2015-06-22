package edu.itba.hci.define.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import edu.itba.hci.define.R;
import edu.itba.hci.define.adapters.CategoryAdapter;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.ApiProductFilter;
import edu.itba.hci.define.models.Category;
import edu.itba.hci.define.models.CategoryInterface;
import edu.itba.hci.define.models.CategoryList;
import edu.itba.hci.define.models.Subcategory;
import edu.itba.hci.define.models.SubcategoryList;

public class SubcategoriesFragment extends Fragment {

    private ListView listView;
    private int age;
    private int gender;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subcategories_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.category_list);
        listView.setClickable(true);
        Bundle args = getArguments();

        age = args.getInt("age");
        gender = args.getInt("gender");
        final ApiProductFilter[] filters;
        if(CategoryFragment.convertToString(gender)!=null){
            filters = new ApiProductFilter[2];
            filters[1]= new ApiProductFilter(1, CategoryFragment.convertToString(gender));
        }
        else {
            filters = new ApiProductFilter[1];
        }
        filters[0]=new ApiProductFilter(2, CategoryFragment.convertToString(age));
        ApiManager.getAllCategories(filters, new Callback<CategoryList>() {
            @Override
            public void onSuccess(final CategoryList categoryResponse) {
                final List<CategoryInterface> list = new LinkedList<CategoryInterface>();
                final int[] iteration = {0};
                for (final Category category : categoryResponse.getCategories()) {
                    ApiManager.getAllSubcategories(category.getId(), filters, new Callback<SubcategoryList>() {
                        @Override
                        public void onSuccess(SubcategoryList subcategoryResponse) {
                            Log.e("Subcategory", iteration[0]+"");
                            list.add(category);
                            for(Subcategory subcategory : subcategoryResponse.getSubcategories()){
                                list.add(subcategory);
                            }
                            if((++iteration[0])==categoryResponse.getCategories().size()){
                                CategoryAdapter adapter = new CategoryAdapter(getActivity(), 0, list);
                                listView.setAdapter(adapter);
                            }
                        }
                        @Override  public void onError(ApiError error) {}
                        @Override public void onErrorConnection() {}
                    });
                }
                CategoryAdapter adapter = new CategoryAdapter(getActivity(), 0, list);
                listView.setAdapter(adapter);
            }

            @Override
            public void onError(ApiError error) {
            }

            @Override
            public void onErrorConnection() {
            }
        });

        return view;
    }
}
