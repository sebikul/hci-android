package edu.itba.hci.define.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.NavBasicActivity;
import edu.itba.hci.define.activities.base.TabbedFragment;
import edu.itba.hci.define.models.Category;

public class CategoryFragment extends TabbedFragment {

    public static final int GIRL = 1;
    public static final int BOY = 2;
    public static final int ADULT = 4;
    public static final int KID = 8;
    public static final int BABY = 16;
    public static final int BOTH = 0;

    private int age, gender;

    @Override
    protected void setupViewPager() {

        Bundle args = getArguments();

        age = args.getInt("age");
        gender = args.getInt("gender");

        setHasOptionsMenu(true);

        Log.v("CategotyFragment", "ColocandoTabs " + getOurCategory(age, gender));

        Fragment fragment;

        //fixme
        fragment = new SubcategoriesFragment();
        fragment.setArguments(args);
        adapter.addFrag(fragment, getString(R.string.title_subcategories));

        //fixme
        fragment = new SaleFragment();
        fragment.setArguments(args);
        adapter.addFrag(fragment, getString(R.string.title_sales));

        //fixme
        fragment = new SaleFragment();
        fragment.setArguments(args);
        adapter.addFrag(fragment, getString(R.string.title_new));

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v("CategotyFragment", "Cargando categoria " + getOurCategory(age, gender));
        inflater.inflate(R.menu.options_menu, menu);
        NavBasicActivity activity = ((NavBasicActivity) getActivity());
        activity.setTitle(getOurCategory(age, gender));

        activity.setToggleDrawer(true);
        setNavigationDrawerOptionChecked(activity, age, gender);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setNavigationDrawerOptionChecked(NavBasicActivity activity, int age, int gender) {
        switch (age + gender) {
            case GIRL + ADULT:
                ((NavigationView) activity.findViewById(R.id.nvView)).getMenu().findItem(R.id.item_category_1).setChecked(true);
                return;
            case BOY + ADULT:
                ((NavigationView) activity.findViewById(R.id.nvView)).getMenu().findItem(R.id.item_category_2).setChecked(true);
                return;
            case GIRL + KID:
                ((NavigationView) activity.findViewById(R.id.nvView)).getMenu().findItem(R.id.item_category_3).setChecked(true);
                return;
            case BOY + KID:
                ((NavigationView) activity.findViewById(R.id.nvView)).getMenu().findItem(R.id.item_category_4).setChecked(true);
                return;
            case BABY + BOTH:
                ((NavigationView) activity.findViewById(R.id.nvView)).getMenu().findItem(R.id.item_category_5).setChecked(true);
                return;
            default:
                throw new IllegalStateException("Category name not found");
        }
    }


    private String getOurCategory(int age, int gender) {
        switch (age + gender) {
            case GIRL + ADULT:
                return getResources().getString(R.string.category_woman);
            case BOY + ADULT:
                return getResources().getString(R.string.category_men);
            case GIRL + KID:
                return getResources().getString(R.string.category_girls);
            case BOY + KID:
                return getResources().getString(R.string.category_boys);
            case BABY + BOTH:
                return getResources().getString(R.string.category_babies);
            default:
                throw new IllegalStateException("Category name not found");
        }
    }
}
