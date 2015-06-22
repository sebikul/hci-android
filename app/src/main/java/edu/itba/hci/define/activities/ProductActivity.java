package edu.itba.hci.define.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.Arrays;
import java.util.Comparator;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.ToolbarActivity;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.Attribute;
import edu.itba.hci.define.models.Product;


public class ProductActivity extends ToolbarActivity implements ObservableScrollViewCallbacks {

    static private final String LOG_TAG = "ProductActivity";

    private int productId;

    // Fields
    private View mImageView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;

    private TextView mName;
    private TextView mBrand;

    private TextView mColors;
    private TextView mSizes;
    private TextView mDetails;

    private SliderLayout sliderLayout;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        productId = getIntent().getIntExtra("productId", -1);

        if (productId == -1) {
            //fixme
            //todo
            productId = 1;
        }

        Log.v(LOG_TAG, "ID de producto: " + productId);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        sliderLayout = (SliderLayout) findViewById(R.id.imagen);
        mImageView = findViewById(R.id.imagen);
        mToolbarView = findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(
                ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(
                R.dimen.parallax_image_height);

        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        sliderLayout.stopAutoCycle();
        sliderLayout.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

        int baseColor = getResources().getColor(R.color.primary);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0.2f, baseColor));

        mName = (TextView) findViewById(R.id.product_name);
        mBrand = (TextView) findViewById(R.id.product_brand);
        mColors = (TextView) findViewById(R.id.product_colors);
        mSizes = (TextView) findViewById(R.id.product_sizes);
        mDetails = (TextView) findViewById(R.id.product_details);
        getProduct();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    private void getProduct() {
        ApiManager.getProductById(productId, new Callback<Product>() {
            @Override
            public void onSuccess(Product response) {


                // TODO: El setTitle no esta funcionando
                title=response.getName();

                ProductActivity.this.setTitle((title.length() > 15 ? title.substring(0, 15) + "..." : title));

                mName.setText(response.getName());
                //mBrand.setText(response.getBrand());
                for (Attribute attribute : response.getAttributes()) {

                    if (attribute.getName().equals("Marca")) {
                        mBrand.setText(attribute.getValues()[0]);
                    }

                    if (attribute.getName().equals("Color")) {
                        String[] colors = attribute.getValues();
                        Arrays.sort(colors);
                        String color = "";

                        for (int i = 0; i < colors.length; i++) {
                            color += colors[i] + ", ";
                        }

                        mColors.setText(color.substring(0, color.length() - 2));
                    }

                    if(attribute.getName().contains("Material")){
                        mDetails.setText(attribute.getValues()[0]);
                    }

                    if (attribute.getName().contains("Talle")) {

                        String[] talles = attribute.getValues();

                        Arrays.sort(talles, new Comparator<String>() {
                            @Override
                            public int compare(String lhs, String rhs) {
                                return val(lhs) - val(rhs);
                            }
                        });
                        String talle = "";

                        for (int i = 0; i < talles.length; i++) {
                            talle += talles[i] + ", ";
                        }

                        mSizes.setText(talle.substring(0, talle.length() - 2));
                    }

                }


                for (String imageUrl : response.getImageUrl()) {
                    DefaultSliderView sliderView = new DefaultSliderView(ProductActivity.this);
                    sliderView.image(imageUrl);
                    sliderLayout.addSlider(sliderView);
                }

            }

            @Override
            public void onError(ApiError error) {

            }

            @Override
            public void onErrorConnection() {

            }
        });
    }

    private int val(String str) {
        switch (str){
            case "XXL":
                return -1;
            case "XL":
                return -2;
            case "L":
                return -3;
            case "M":
                return -4;
            case "S":
                return -5;
            case "XS":
                return -6;
            case "XXS":
                return -7;
            default:
                return Integer.parseInt(str);
        }
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(Math.max(alpha, 0.2f), baseColor));
        mImageView.setTranslationY(scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

//        ActionBar ab = getSupportActionBar();
//        if (scrollState == ScrollState.UP) {
//            if (ab.isShowing()) {
//                ab.hide();
//            }
//        } else if (scrollState == ScrollState.DOWN) {
//            if (!ab.isShowing()) {
//                ab.show();
//            }
//        }

    }
}
