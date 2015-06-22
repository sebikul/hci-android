package edu.itba.hci.define.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.CategoryProductFragment;
import edu.itba.hci.define.activities.SubcategoryProductFragment;
import edu.itba.hci.define.activities.base.NavBasicActivity;
import edu.itba.hci.define.models.CategoryInterface;

/**
 * Created by Diego on 22/06/2015.
 */
public class CategoryAdapter extends ArrayAdapter<CategoryInterface> {

    private final ImageLoader imageLoader;
    private final Bundle bundle;
    private List<CategoryInterface> categoryList;
    private Context context;
    private Activity activity;

    public CategoryAdapter(Context context, int resource, List<CategoryInterface> objects, Bundle bundle, Activity activity) {
        super(context, resource, objects);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.categoryList = objects;
        this.bundle=bundle;
        this.context=context;
        this.activity=activity;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final CategoryInterface category=categoryList.get(position);
        View singleView;
        if(category.isCategory()) {
            singleView = inflater.inflate(R.layout.category_item, parent, false);
            singleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("CategoryAdapter", "Clickie en una categoria");
                    Fragment fragment = new CategoryProductFragment();
                    Bundle intent=new Bundle();
                    intent.putInt("categoryId", category.getId());
                    intent.putString("categoryName", category.getName());
                    intent.putInt("gender", bundle.getInt("gender"));
                    intent.putInt("age", bundle.getInt("age"));
                    fragment.setArguments(intent);
                    ((NavBasicActivity)activity).replaceContentWithFragment(fragment, null, null);

                }
            });
        }else{
            singleView = inflater.inflate(R.layout.subcategory_item, parent, false);
            singleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("CategoryAdapter", "Clickie en una subcategoria");
                    Fragment fragment = new SubcategoryProductFragment();
                    Bundle intent=new Bundle();
                    intent.putInt("categoryId", category.getId());
                    intent.putString("categoryName", category.getName());
                    intent.putInt("gender", bundle.getInt("gender"));
                    intent.putInt("age", bundle.getInt("age"));
                    fragment.setArguments(intent);
                    ((NavBasicActivity)activity).replaceContentWithFragment(fragment, null, null);
                }
            });
        }
        TextView text = (TextView) singleView.findViewById(R.id.text);
        text.setText(category.getName());
        return singleView;
    }
}
