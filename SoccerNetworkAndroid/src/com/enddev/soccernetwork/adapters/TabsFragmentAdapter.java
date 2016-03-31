package com.enddev.soccernetwork.adapters;

import com.enddev.soccernetwork.fragments.AboutsFragment;
import com.enddev.soccernetwork.fragments.MapsFragment;
import com.enddev.soccernetwork.fragments.NewsFragment;
import com.enddev.soccernetwork.fragments.SearchFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsFragmentAdapter extends FragmentPagerAdapter{

	public TabsFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch(arg0){
		case 0:
			return new NewsFragment();
		case 1:
			return new SearchFragment();
		case 2:
			return new MapsFragment();
		case 3:
			return new AboutsFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}
	
}
