package com.enddev.soccernetwork;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.enddev.soccernetwork.adapters.TabsFragmentAdapter;

	public class MainActivity extends FragmentActivity implements TabListener {
		private ViewPager viewPager;
		private TabsFragmentAdapter tabPagerAdapter;
		private ActionBar actionBar;
		private String[] tabNames = { "First", "Second", "Third" };

		@SuppressWarnings("deprecation")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			viewPager = (ViewPager) findViewById(R.id.pager);
			tabPagerAdapter = new TabsFragmentAdapter(getSupportFragmentManager());
			viewPager.setAdapter(tabPagerAdapter);
			actionBar = getActionBar();
			actionBar.setHomeButtonEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			for (int i = 0; i < 3; i++) {
				actionBar.addTab(actionBar.newTab().setText(tabNames[i])
						.setTabListener(this));
			}
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int postion) {
					actionBar.setSelectedNavigationItem(postion);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			viewPager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}
	}


//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		setContentView(R.layout.activity_main);
//		new LoadCityTask().execute();
//		new LoadUserTask().execute();
//	}
//	
//	class LoadCityTask extends AsyncTask<Void, Void, Void > {
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			try {
//				ArrayList<City> cities = new ArrayList<City>();
//				List<NameValuePair> p = new ArrayList<NameValuePair>();
//				p.add(new BasicNameValuePair("tag", "get_all_cities"));
//				Gson gson = new Gson();
//				String citiesJson = ServiceConnection.getResponce(AppConstant.HOST_URL, p);
//				Type datasetListType = new TypeToken<Collection<City>>() {}.getType();
//		        cities = gson.fromJson(citiesJson, datasetListType);
//		        for (City city : cities) {
//		            Log.i("SoccerNetWork", city.getCity_name());
//		        }
//				
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//			return null;
//			
//		}
//	
//	}
//	
//	class LoadUserTask extends AsyncTask<Void, Void, Void > {
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			String json = "";
//			try {
//				UserProfile user = new UserProfile();
//				List<NameValuePair> p = new ArrayList<NameValuePair>();
//				p.add(new BasicNameValuePair("tag", "login"));
//				p.add(new BasicNameValuePair("username", "quanghuy"));
//				p.add(new BasicNameValuePair("password", "hahaha"));
//				Gson gson = new Gson();
//				json = ServiceConnection.getResponce(AppConstant.HOST_URL, p);
//				user = gson.fromJson(json, UserProfile.class);
//				Log.i("SoccerNetWork", user.getUsername() + " - " + user.getEmail());
//				
//			}
//			catch (Exception e) {
//				try {
//					ResponceStatus status = new ResponceStatus();
//					Gson gson = new Gson();
//					status = gson.fromJson(json, ResponceStatus.class);
//					Log.i("SoccerNetwork", status.getCode() + " - " + status.getMsg());
//				}
//				catch (Exception ex) {
//					ex.printStackTrace();
//				}
//				
//				
//				
//			}
//			return null;
//			
//		}
//	
//	}
