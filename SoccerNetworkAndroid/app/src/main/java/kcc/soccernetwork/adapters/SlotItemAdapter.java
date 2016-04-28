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
import kcc.soccernetwork.objects.SlotItem;
import kcc.soccernetwork.utils.ImageLoader;
import kcc.soccernetwork.utils.LoadImageTask;
import kcc.soccernetwork.utils.TimeFunctions;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by Administrator on 4/16/2016.
 */
public class SlotItemAdapter extends RecyclerView.Adapter<SlotItemAdapter.SlotItemViewHolder> {
    private Activity activity;
    private ArrayList<SlotItem> slotItemList;
    TimeFunctions timeFunctions;

    public class SlotItemViewHolder extends  RecyclerView.ViewHolder {
        public TextView registerName, numberAttendee, phoneContact, time;
        public ImageView avatar;
        public SlotItemViewHolder(View itemView) {
            super(itemView);
            registerName = (TextView) itemView.findViewById(R.id.tv_person_register);
            numberAttendee = (TextView) itemView.findViewById(R.id.tv_number_attended);
            phoneContact = (TextView) itemView.findViewById(R.id.tv_phone_contact);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            avatar = (ImageView) itemView.findViewById(R.id.imv_slot_avatar);
        }
    }

    public SlotItemAdapter(Activity activity, ArrayList<SlotItem> slotItemList) {
        this.slotItemList = slotItemList;
        this.activity = activity;
        timeFunctions = new TimeFunctions();
    }

    @Override
    public SlotItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_item, parent, false);
        return new SlotItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SlotItemViewHolder holder, int position) {
        SlotItem slotItem = slotItemList.get(position);
        holder.registerName.setText(slotItem.getFull_name());
        holder.phoneContact.setText(slotItem.getPhone_number());
        holder.numberAttendee.setText(slotItem.getQuantity());
        Date timeAttended = timeFunctions.formatDate(slotItem.getCreated());
        holder.time.setText(timeFunctions.calculteDateDif(timeAttended));
        if (slotItem.getAvatar_path() == null || slotItem.getAvatar_path().equals("")) {
            holder.avatar.setImageResource(R.drawable.no_avatar);
        }
        else {
            new LoadImageTask(activity, holder.avatar, R.drawable.no_avatar).execute(UtilConstants.IMAGE_FOLDER_URL + slotItem.getAvatar_path());
        }

    }

    public ArrayList<SlotItem> getSlotItemList() {
        return slotItemList;
    }

    public void setSlotItemList(ArrayList<SlotItem> slotItemList) {
        this.slotItemList = slotItemList;
    }

    public void addNewSlot(SlotItem slotItem) {
        if (slotItemList == null) {
            slotItemList = new ArrayList<SlotItem>();
        }
        slotItemList.add(0, slotItem);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return slotItemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
