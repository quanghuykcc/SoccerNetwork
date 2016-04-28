package kcc.soccernetwork.fragments;

/**
 * Created by Ravi on 29/07/15.
 */
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import kcc.soccernetwork.R;


public class FragmentHomeSoccer extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_home, container, false);

        TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabLayout);


        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.newss).setText(getString(R.string.match)));
//        TextView tvNews = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tabs,null);
//        tvNews.setCompoundDrawablesWithIntrinsicBounds(R.drawable.newss, 0, 0, 0);
//        tvNews.setText("News");
//        tabLayout.addTab(tabLayout.newTab().setCustomView(tvNews));
//
//        TextView tvField = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tabs,null);
//        tvField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.field, 0, 0, 0);
//        tabLayout.addTab(tabLayout.newTab().setCustomView(tvField));
//
//        TextView tvMaps = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tabs,null);
//        tvMaps.setCompoundDrawablesWithIntrinsicBounds(R.drawable.maps, 0, 0, 0);
//        tabLayout.addTab(tabLayout.newTab().setCustomView(tvMaps));

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.field).setText(getString(R.string.field)));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.maps).setText(getString(R.string.map)));


        final ViewPager viewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);

        viewPager.setAdapter(new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return inflatedView;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    FragmentNews tab1 = new FragmentNews();
                    return tab1;
                case 1:
                    FragmentFields tab2 = new FragmentFields();
                    return tab2;
                case 2:
                    FragmentMaps tab3 = new FragmentMaps();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
