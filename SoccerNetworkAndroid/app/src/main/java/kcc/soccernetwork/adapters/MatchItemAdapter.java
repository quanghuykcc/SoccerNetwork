package kcc.soccernetwork.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import kcc.soccernetwork.R;
import kcc.soccernetwork.objects.MatchItem;
import kcc.soccernetwork.utils.ImageLoader;
import kcc.soccernetwork.utils.LoadImageTask;
import kcc.soccernetwork.utils.TimeFunctions;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by Administrator on 4/6/2016.
 */
public class MatchItemAdapter extends RecyclerView.Adapter<MatchItemAdapter.MatchItemViewHolder> {
    private Activity mActivity;
    private ArrayList<MatchItem> mMatchItemList;
    private TimeFunctions timeFunctions;
    public class MatchItemViewHolder extends RecyclerView.ViewHolder {
        public TextView postedUserName, postedTime, time, field, price, attended, district, city, address;
        public ImageView hostAvatar;
        public MatchItemViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.tv_match_time);
            postedUserName = (TextView) itemView.findViewById(R.id.tv_host_name);
            postedTime = (TextView) itemView.findViewById(R.id.tv_time_posted);
            field = (TextView) itemView.findViewById(R.id.tv_field);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            attended = (TextView) itemView.findViewById(R.id.tv_attended);
            district = (TextView) itemView.findViewById(R.id.tv_district);
            city = (TextView) itemView.findViewById(R.id.tv_city);
            address = (TextView) itemView.findViewById(R.id.tv_address);
            hostAvatar = (ImageView) itemView.findViewById(R.id.imv_field);
        }
    }

    @Override
    public MatchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_item, parent, false);
        return new MatchItemViewHolder(itemView);
    }

    public MatchItemAdapter(Activity activity, ArrayList<MatchItem> matchItemList) {
        mActivity = activity;
        mMatchItemList = matchItemList;
        timeFunctions = new TimeFunctions();
    }

    public void setMatchItemList(ArrayList<MatchItem> matchItemList) {
        mMatchItemList = matchItemList;
    }

    public ArrayList<MatchItem> getMatchItemList() {
        return mMatchItemList;
    }

    @Override
    public void onBindViewHolder(MatchItemViewHolder holder, int position) {
        MatchItem matchItem = mMatchItemList.get(position);
        holder.postedUserName.setText(matchItem.getFull_name());
        Date postedDate = timeFunctions.formatDate(matchItem.getCreated());
        holder.postedTime.setText(timeFunctions.calculteDateDif(postedDate));
        holder.time.setText(matchItem.getStart_time() + " - " + matchItem.getEnd_time());
        holder.field.setText(matchItem.getField_name());
        holder.price.setText(matchItem.getPrice());
        if (matchItem.getAttended() == null || matchItem.getAttended().equals("null")) {
            matchItem.setAttended("0");
        }
        holder.attended.setText(matchItem.getAttended() + " / " + matchItem.getMaximum_players());
        holder.district.setText(matchItem.getDistrict_name());
        holder.city.setText(matchItem.getCity_name());
        holder.address.setText(matchItem.getAddress());
        if (matchItem.getAvatar_path() == null || matchItem.getAvatar_path().equals("")) {
            holder.hostAvatar.setImageResource(R.drawable.no_avatar);
        }
        else {
            new LoadImageTask(mActivity, holder.hostAvatar, R.drawable.no_avatar).execute(UtilConstants.IMAGE_FOLDER_URL + matchItem.getAvatar_path());
        }


    }

    @Override
    public int getItemCount() {
        if (mMatchItemList != null) {
            return mMatchItemList.size();
        }
        else {
            return 0;
        }
    }
}
