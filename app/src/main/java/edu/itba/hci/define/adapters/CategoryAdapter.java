package edu.itba.hci.define.adapters;

import android.content.Context;
import android.content.Intent;
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
import edu.itba.hci.define.activities.CategoryActivity;
import edu.itba.hci.define.activities.SubcategoryActivity;
import edu.itba.hci.define.models.CategoryInterface;

/**
 * Created by Diego on 22/06/2015.
 */
public class CategoryAdapter extends ArrayAdapter<CategoryInterface> {

    private final ImageLoader imageLoader;
    private List<CategoryInterface> categoryList;

    public CategoryAdapter(Context context, int resource, List<CategoryInterface> objects) {
        super(context, resource, objects);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.categoryList = objects;
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

                    Intent intent = new Intent(v.getContext(), CategoryActivity.class);
                    intent.putExtra("categoryId", category.getId());
                    //TODO: No se como sacar el gender y el age
                }
            });
        }else{
            singleView = inflater.inflate(R.layout.subcategory_item, parent, false);
            singleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("CategoryAdapter", "Clickie en una subcategoria");
                    Intent intent = new Intent(v.getContext(), SubcategoryActivity.class);
                    intent.putExtra("categoryId", category.getId());
                }
            });
        }
        TextView text = (TextView) singleView.findViewById(R.id.text);
        text.setText(category.getName());
        return singleView;
    }
}
