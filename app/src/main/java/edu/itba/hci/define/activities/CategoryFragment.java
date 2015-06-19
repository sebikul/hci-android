package edu.itba.hci.define.activities;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.TabbedFragment;

public class CategoryFragment extends TabbedFragment {

    @Override
    protected void setupViewPager() {
        adapter.addFrag(new PurchaseFragment(), getString(R.string.title_subcategories));
        adapter.addFrag(new PurchaseFragment(), getString(R.string.title_sales));
        adapter.addFrag(new PurchaseFragment(), getString(R.string.title_new));


    }
}
