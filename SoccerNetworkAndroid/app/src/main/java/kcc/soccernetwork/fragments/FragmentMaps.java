package kcc.soccernetwork.fragments;

import android.app.Dialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kcc.soccernetwork.R;
import kcc.soccernetwork.objects.FieldItem;
import kcc.soccernetwork.utils.GoogleMapFunctions;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

public class FragmentMaps extends Fragment implements LocationListener{
    GoogleMap maps;
    ArrayList<FieldItem> fieldItemList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
        }else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);

            // Getting GoogleMap object from the fragment
            maps = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            maps.setMyLocationEnabled(true);
            maps.getUiSettings().setZoomControlsEnabled(true);
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            try {
                Location location = locationManager.getLastKnownLocation(provider);

                if (location != null) {
                    onLocationChanged(location);
                }
                locationManager.requestLocationUpdates(provider, 20000, 0, this);
            }catch (SecurityException e){

            }
        }
        GoogleMapFunctions googleMapFunctions = new GoogleMapFunctions(maps, getActivity());
        googleMapFunctions.moveCameraToCurrentPosition(getActivity());
        new FieldGetterTask().execute();
        return v;
    }
    @Override
    public void onLocationChanged(Location location) {
        /*// Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        maps.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        maps.animateCamera(CameraUpdateFactory.zoomTo(15));*/

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    class FieldGetterTask extends AsyncTask<Void, Void, ArrayList<FieldItem>> {

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected ArrayList<FieldItem> doInBackground(Void... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "get_all_fields"));
            String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(jsonString));
            reader.setLenient(true);
            fieldItemList = gson.fromJson(reader, new TypeToken<Collection<FieldItem>>(){}.getType());

            return fieldItemList;
        }

        @Override
        protected void onPostExecute(ArrayList<FieldItem> fieldItems) {
            GoogleMapFunctions googleMapFunctions = new GoogleMapFunctions(maps, getActivity());
            for (int i = 0; i < fieldItems.size(); i++) {
                double latitude = Double.parseDouble(fieldItems.get(i).getLatitude());
                double longitude = Double.parseDouble(fieldItems.get(i).getLongitude());
                String fieldName = fieldItems.get(i).getField_name();
                String address = fieldItems.get(i).getAddress();
                googleMapFunctions.addMarker(latitude, longitude, fieldName, address);
            }
        }
    }

}

