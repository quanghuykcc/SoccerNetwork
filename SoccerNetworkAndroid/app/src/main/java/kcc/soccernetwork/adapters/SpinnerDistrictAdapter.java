package kcc.soccernetwork.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import kcc.soccernetwork.objects.District;

/**
 * Created by Administrator on 4/28/2016.
 */
public class SpinnerDistrictAdapter extends ArrayAdapter<District> {

    private Context context;
    private ArrayList<District> districts;

    public SpinnerDistrictAdapter(Context context, int textViewResourceId, ArrayList<District> districts) {
        super(context, textViewResourceId, districts);
        this.context = context;
        this.districts = districts;
    }

    public int getCount(){
        return districts.size();
    }

    public District getItem(int position){
        return districts.get(position);
    }

    public long getItemId(int position){
        return Long.parseLong(districts.get(position).getDistrict_id());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(districts.get(position).getDistrict_name());
        return label;
    }

    public void setDistricts(ArrayList<District> districts) {
        this.districts = districts;
    }

    public ArrayList<District> getDistricts() {
        return districts;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(districts.get(position).getDistrict_name());
        return label;
    }
}