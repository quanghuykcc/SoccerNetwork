package com.enddev.soccernetwork;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.enddev.soccernetwork.models.City;
import com.enddev.soccernetwork.models.ResponceStatus;
import com.enddev.soccernetwork.models.UserProfile;
import com.enddev.soccernetwork.utils.AppConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.enddev.soccernetwork.utils.ServiceConnection;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		new LoadCityTask().execute();
		new LoadUserTask().execute();
	}
	
	class LoadCityTask extends AsyncTask<Void, Void, Void > {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				ArrayList<City> cities = new ArrayList<City>();
				List<NameValuePair> p = new ArrayList<NameValuePair>();
				p.add(new BasicNameValuePair("tag", "get_all_cities"));
				Gson gson = new Gson();
				String citiesJson = ServiceConnection.getResponce(AppConstant.HOST_URL, p);
				Type datasetListType = new TypeToken<Collection<City>>() {}.getType();
		        cities = gson.fromJson(citiesJson, datasetListType);
		        for (City city : cities) {
		            Log.i("SoccerNetWork", city.getCity_name());
		        }
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return null;
			
		}
	
	}
	
	class LoadUserTask extends AsyncTask<Void, Void, Void > {

		@Override
		protected Void doInBackground(Void... params) {
			String json = "";
			try {
				UserProfile user = new UserProfile();
				List<NameValuePair> p = new ArrayList<NameValuePair>();
				p.add(new BasicNameValuePair("tag", "login"));
				p.add(new BasicNameValuePair("username", "quanghuy"));
				p.add(new BasicNameValuePair("password", "hahaha"));
				Gson gson = new Gson();
				json = ServiceConnection.getResponce(AppConstant.HOST_URL, p);
				user = gson.fromJson(json, UserProfile.class);
				Log.i("SoccerNetWork", user.getUsername() + " - " + user.getEmail());
				
			}
			catch (Exception e) {
				try {
					ResponceStatus status = new ResponceStatus();
					Gson gson = new Gson();
					status = gson.fromJson(json, ResponceStatus.class);
					Log.i("SoccerNetwork", status.getCode() + " - " + status.getMsg());
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				
				
				
			}
			return null;
			
		}
	
	}
}
