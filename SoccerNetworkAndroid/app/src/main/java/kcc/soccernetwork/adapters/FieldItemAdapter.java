package kcc.soccernetwork.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import kcc.soccernetwork.R;
import kcc.soccernetwork.objects.FieldItem;

/**
 * Created by Administrator on 4/13/2016.
 */
public class FieldItemAdapter extends RecyclerView.Adapter<FieldItemAdapter.FieldItemViewHolder> {

    private Activity activity;
    private ArrayList<FieldItem> fieldItemList;
    public class FieldItemViewHolder extends RecyclerView.ViewHolder {
        public TextView operatingTime, address, district, city, phone, fieldName;
        public ImageView fieldImage;
        public FieldItemViewHolder(View itemView) {
            super(itemView);
            operatingTime = (TextView) itemView.findViewById(R.id.tv_operating_time);
            address = (TextView) itemView.findViewById(R.id.tv_address);
            district = (TextView) itemView.findViewById(R.id.tv_district);
            city = (TextView) itemView.findViewById(R.id.tv_city);
            phone = (TextView) itemView.findViewById(R.id.tv_phone);
            fieldImage = (ImageView) itemView.findViewById(R.id.imv_field);
            fieldName = (TextView) itemView.findViewById(R.id.tv_field_name);
        }
    }

    public void setFieldItemList(ArrayList<FieldItem> fieldItemList) {
        this.fieldItemList = fieldItemList;
    }

    public ArrayList<FieldItem> getFieldItemList() {
        return fieldItemList;
    }

    @Override
    public FieldItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.field_item, parent, false);
        return new FieldItemViewHolder(itemView);
    }

    public FieldItemAdapter(Activity activity, ArrayList<FieldItem> fieldItemList) {
        this.activity = activity;
        this.fieldItemList = fieldItemList;
    }

    @Override
    public void onBindViewHolder(FieldItemViewHolder holder, int position) {
        FieldItem fieldItem = fieldItemList.get(position);
        holder.operatingTime.setText(fieldItem.getOpen_time() + " đến " + fieldItem.getClose_time());
        holder.address.setText(fieldItem.getAddress());
        holder.district.setText(fieldItem.getCity_name());
        holder.city.setText(fieldItem.getDistrict_name());
        holder.phone.setText(fieldItem.getPhone_number());
        holder.fieldImage.setImageResource(R.drawable.no_avatar);
        holder.fieldName.setText(fieldItem.getField_name());
    }

    @Override
    public int getItemCount() {
        if (fieldItemList != null) {
            return fieldItemList.size();
        }
        else {
            return 0;
        }

    }
}
