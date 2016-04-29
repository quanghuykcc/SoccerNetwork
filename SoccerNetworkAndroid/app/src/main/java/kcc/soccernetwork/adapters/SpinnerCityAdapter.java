package kcc.soccernetwork.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import kcc.soccernetwork.objects.City;

public class SpinnerCityAdapter extends ArrayAdapter<City> {

    private Context context;
    private ArrayList<City> cities;

    public SpinnerCityAdapter(Context context, int textViewResourceId, ArrayList<City> cities) {
        super(context, textViewResourceId, cities);
        this.context = context;
        this.cities = cities;
    }

    public int getCount(){
        return cities.size();
    }

    public City getItem(int position){
        return cities.get(position);
    }

    public long getItemId(int position){
        return Long.parseLong(cities.get(position).getCity_id());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(16);
        label.setText(cities.get(position).getCity_name());
        return label;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(16);
        label.setText(cities.get(position).getCity_name());

        return label;
    }
}