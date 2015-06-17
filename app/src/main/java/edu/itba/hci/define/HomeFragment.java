package edu.itba.hci.define;

import edu.itba.hci.define.activities.PurchaseFragment;
import edu.itba.hci.define.activities.base.TabbedFragment;

public class HomeFragment extends TabbedFragment {

    @Override
    protected void setupViewPager() {
        adapter.addFrag(new PurchaseFragment(), getString(R.string.title_sales));
        adapter.addFrag(new PurchaseFragment(), getString(R.string.title_new));
    }
}
