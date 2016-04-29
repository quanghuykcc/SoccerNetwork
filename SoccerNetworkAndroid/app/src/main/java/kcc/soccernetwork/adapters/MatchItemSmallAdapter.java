package kcc.soccernetwork.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
public class MatchItemSmallAdapter extends RecyclerView.Adapter<MatchItemSmallAdapter.MatchItemViewHolder> {
    private Activity mActivity;
    private ArrayList<MatchItem> mMatchItemList;
    private TimeFunctions timeFunctions;
    public class MatchItemViewHolder extends RecyclerView.ViewHolder {
        public TextView postedUserName, postedTime, startTime, endTime, price, attended, phone;
        public ImageView hostAvatar;
        public MatchItemViewHolder(View itemView) {
            super(itemView);

            startTime = (TextView) itemView.findViewById(R.id.tv_time_start);
            endTime = (TextView) itemView.findViewById(R.id.tv_time_end);
            postedUserName = (TextView) itemView.findViewById(R.id.tv_user_posted);
            postedTime = (TextView) itemView.findViewById(R.id.tv_time_posted);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            attended = (TextView) itemView.findViewById(R.id.tv_number_attended);
            phone = (TextView) itemView.findViewById(R.id.tv_phone_number);
            hostAvatar = (ImageView) itemView.findViewById(R.id.imv_user_avatar);
        }
    }

    @Override
    public MatchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_item_small, parent, false);
        return new MatchItemViewHolder(itemView);
    }

    public MatchItemSmallAdapter(Activity activity, ArrayList<MatchItem> matchItemList) {
        mActivity = activity;
        mMatchItemList = matchItemList;
        timeFunctions = new TimeFunctions();
    }

    public void addNewMatch(MatchItem matchItem) {
        if (mMatchItemList == null) {
            mMatchItemList = new ArrayList<MatchItem>();
        }
        mMatchItemList.add(0, matchItem);
        notifyDataSetChanged();
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
        holder.startTime.setText(matchItem.getStart_time());
        holder.endTime.setText(matchItem.getEnd_time());
        holder.price.setText(matchItem.getPrice());
        if (matchItem.getAttended() == null || matchItem.getAttended().equals("null")) {
            matchItem.setAttended("0");
        }
        holder.attended.setText(matchItem.getAttended() + " / " + matchItem.getMaximum_players());
        holder.phone.setText(matchItem.getPhone_number());
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
