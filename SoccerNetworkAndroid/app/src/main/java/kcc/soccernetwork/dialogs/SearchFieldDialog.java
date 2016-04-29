package kcc.soccernetwork.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import kcc.soccernetwork.R;
import kcc.soccernetwork.adapters.SpinnerCityAdapter;
import kcc.soccernetwork.adapters.SpinnerDistrictAdapter;
import kcc.soccernetwork.objects.City;
import kcc.soccernetwork.objects.District;
import kcc.soccernetwork.objects.MatchItem;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by Administrator on 4/28/2016.
 */
public class SearchFieldDialog extends DialogFragment implements View.OnClickListener {
    Spinner citySpinner;
    Spinner districtSpinner;
    ProgressBar pbgLoading;
    SpinnerCityAdapter cityAdapter;
    ArrayList<City> cities;
    SpinnerDistrictAdapter districtAdapter;
    ArrayList<District> districts;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_field:
                if (districtSpinner.getSelectedItem() != null) {
                    String districtId = ((District)districtSpinner.getSelectedItem()).getDistrict_id();
                    searchFieldListener.OnSearchField("", districtId);
                    dismiss();
                }

            break;
        }
    }

    public interface SearchFieldListener {
        public void OnSearchField(String cityId, String districtId);
    }

    private SearchFieldListener searchFieldListener;
    private Activity activity;

    public SearchFieldDialog(Activity activity, SearchFieldListener searchFieldListener) {
        super();
        this.activity = activity;
        this.searchFieldListener = searchFieldListener;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.search_field_layout, null);
        Button searchFieldBtn = (Button) view.findViewById(R.id.btn_search_field);
        searchFieldBtn.setOnClickListener(this);
        citySpinner = (Spinner) view.findViewById(R.id.sp_city);
        pbgLoading = (ProgressBar) view.findViewById(R.id.pgb_loading);
        cities = new ArrayList<City>();
        districts = new ArrayList<District>();
        cityAdapter = new SpinnerCityAdapter(getActivity(), android.R.layout.simple_spinner_item, cities);
        districtAdapter = new SpinnerDistrictAdapter(getActivity(), android.R.layout.simple_spinner_item, districts);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetDistrictByCity().execute(cityAdapter.getCities().get(position).getCity_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new GetAllCity().execute();
        districtSpinner = (Spinner) view.findViewById(R.id.sp_district);
        pbgLoading = (ProgressBar) view.findViewById(R.id.pgb_loading);
        districtSpinner.setAdapter(districtAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return builder.create();
    }

    class GetAllCity extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "get_all_cities"));
            try {
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                cities = gson.fromJson(reader, new TypeToken<Collection<City>>(){}.getType());
                cityAdapter.setCities(cities);
            }
            catch (Exception ex) {

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
           pbgLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pbgLoading.setVisibility(View.GONE);
            if (cityAdapter.getCities() != null && cityAdapter.getCities().size() > 0) {
                cityAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(activity, "Không có thành phố được tìm thấy", Toast.LENGTH_LONG).show();
            }


        }
    }

    class GetDistrictByCity extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "get_district_by_city_id"));
            nameValuePairs.add(new BasicNameValuePair("city_id", params[0]));
            try {
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                districts = gson.fromJson(reader, new TypeToken<Collection<District>>(){}.getType());
                districtAdapter.setDistricts(districts);
            }
            catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            pbgLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pbgLoading.setVisibility(View.GONE);
            if (districtAdapter.getDistricts() != null && districtAdapter.getDistricts().size() > 0) {
                districtAdapter.notifyDataSetChanged();
            }
            else {
                //Toast.makeText(activity, "Không có quận / huyện được tìm thấy", Toast.LENGTH_LONG).show();
            }


        }
    }

}



