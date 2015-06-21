package edu.itba.hci.define.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.itba.hci.define.R;

/**
 * TODO: document your custom view class.
 */
public class ProductView extends LinearLayout {

    private String src;
    private String name;
    private String brand;
    private String price;
    private int textSize;


    public ProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.product_item, this);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProductView,
                0, 0);

        try {
            src = a.getString(R.styleable.ProductView_src);
            name = a.getString(R.styleable.ProductView_name);
            brand = a.getString(R.styleable.ProductView_brand);
            price = a.getString(R.styleable.ProductView_price);
            textSize = a.getInteger(R.styleable.ProductView_textSize, 12);
            updateView();

        } finally {
            a.recycle();
        }
    }



    private void updateView() {
        ((TextView)findViewById(R.id.title_product)).setText(name);
        ((TextView)findViewById(R.id.title_brand)).setText(brand);
        ((TextView)findViewById(R.id.title_price)).setText(price);
        ((ImageView)findViewById(R.id.photo_img)).setImageURI(Uri.parse(src));
    }

    public String getSrc() {
        return src;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getPrice() {
        return price;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setSrc(String src) {
        this.src = src;
        updateView();
    }

    public void setName(String name) {
        this.name = name;
        updateView();
    }

    public void setBrand(String brand) {
        this.brand = brand;
        updateView();
    }

    public void setPrice(String price) {
        this.price = price;
        updateView();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        updateView();
    }
}



