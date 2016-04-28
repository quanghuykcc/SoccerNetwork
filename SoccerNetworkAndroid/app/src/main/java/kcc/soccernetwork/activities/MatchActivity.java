package kcc.soccernetwork.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import kcc.soccernetwork.adapters.SlotItemAdapter;
import kcc.soccernetwork.dialogs.JoinMatchDialog;
import kcc.soccernetwork.dialogs.LoginDialog;
import kcc.soccernetwork.dialogs.VerifyDialog;
import kcc.soccernetwork.objects.MatchItem;
import kcc.soccernetwork.objects.SlotItem;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.DividerItemDecoration;
import kcc.soccernetwork.utils.GoogleMapFunctions;
import kcc.soccernetwork.utils.ImageLoader;
import kcc.soccernetwork.utils.LoadImageTask;
import kcc.soccernetwork.utils.RecyclerClickListener;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

public class MatchActivity extends AppCompatActivity implements View.OnClickListener, JoinMatchDialog.OnJoinMatchListener {
    private MatchItem match;
    private TextView postedUserName, postedTime, time, field, price, attended, district, city, address;
    private ImageView hostAvatar;
    private ImageView imageMap;
    private RecyclerView slotItemRcv;
    private SlotItemAdapter slotItemAdapter;
    private ArrayList<SlotItem> slotItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        match  = (MatchItem) getIntent().getSerializableExtra("MatchItem");
        Button joinMatchBtn = (Button) findViewById(R.id.btn_join_match);
        time = (TextView) findViewById(R.id.tv_match_time);
        postedUserName = (TextView) findViewById(R.id.tv_host_name);
        postedTime = (TextView) findViewById(R.id.tv_time_posted);
        field = (TextView) findViewById(R.id.tv_field);
        price = (TextView) findViewById(R.id.tv_price);
        attended = (TextView) findViewById(R.id.tv_attended);
        district = (TextView) findViewById(R.id.tv_district);
        city = (TextView) findViewById(R.id.tv_city);
        imageMap = (ImageView) findViewById(R.id.imv_map);
        address = (TextView) findViewById(R.id.tv_address);
        hostAvatar = (ImageView) findViewById(R.id.imv_field);
        hostAvatar.setOnClickListener(this);
        if (match != null) {
            postedUserName.setText(match.getFull_name());
            postedTime.setText(match.getCreated());
            time.setText(match.getStart_time() + " - " + match.getEnd_time());
            field.setText(match.getField_name());
            price.setText(match.getPrice());
            attended.setText(match.getAttended() + " / " + match.getMaximum_players());
            district.setText(match.getDistrict_name());
            city.setText(match.getCity_name());
            address.setText(match.getAddress());
            new LoadImageTask(this, hostAvatar, R.drawable.no_avatar).execute(UtilConstants.IMAGE_FOLDER_URL + match.getAvatar_path());
            new LoadImageTask(this, imageMap, R.drawable.no_avatar).execute(GoogleMapFunctions.getStaticMapURL(match.getLatitude(), match.getLongitude(), UtilConstants.API_KEY));
        }
        joinMatchBtn.setOnClickListener(this);
        slotItemRcv = (RecyclerView) findViewById(R.id.rcv_slots);
        slotItemRcv.setHasFixedSize(true);
        slotItemList = new ArrayList<SlotItem>();
        slotItemAdapter = new SlotItemAdapter(this, slotItemList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        slotItemRcv.setLayoutManager(layoutManager);
        slotItemRcv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        slotItemRcv.addOnItemTouchListener(new RecyclerClickListener(this, slotItemRcv, new RecyclerClickListener.ItemClickListener(){
            @Override
            public void onClick(View view, int position) {
                Intent userIntent = new Intent(MatchActivity.this, UserDetailActivity.class);
                userIntent.putExtra("user_id", slotItemAdapter.getSlotItemList().get(position).getUser_id());
                startActivity(userIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        slotItemRcv.setItemAnimator(new DefaultItemAnimator());
        slotItemRcv.setAdapter(slotItemAdapter);
        new SlotsGetterTask().execute(match.getMatch_id());
        getSupportActionBar().setTitle(getString(R.string.match_upper));

    }

    private void openLoginDialog() {
        DialogFragment loginDialog = new LoginDialog(this);
        loginDialog.show(getFragmentManager(), "");
    }

    private void openJoinMatchDialog() {
        DialogFragment joinMatchDialog = new JoinMatchDialog(this, match, this);
        joinMatchDialog.show(getFragmentManager(), "");
    }

    @Override
    public void joinMatchSuccess(SlotItem slotItem) {
        slotItemAdapter.addNewSlot(slotItem);
        int newAttender = Integer.parseInt(match.getAttended()) + Integer.parseInt(slotItem.getQuantity());
        match.setAttended(newAttender + "");
        attended.setText(newAttender + " / " + match.getMaximum_players());
        Toast.makeText(getApplicationContext(), "Bạn đã tham gia vào trận đấu thành công", Toast.LENGTH_LONG).show();
    }

    @Override
    public void joinMatchFailed() {
        Toast.makeText(getApplicationContext(), "Tham gia vào trận đấu thất bại", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_join_match:
                if (CheckLogin.getInstance().getLoginUser() == null) {
                    openLoginDialog();
                }
                else {
                    openJoinMatchDialog();
                }
                break;
            case R.id.imv_field:
                Intent userIntent = new Intent(MatchActivity.this, UserDetailActivity.class);
                userIntent.putExtra("user_id", match.getUser_id());
                startActivity(userIntent);
                break;
        }
    }

    public class SlotsGetterTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "get_slots_by_match_id"));
            nameValuePairs.add(new BasicNameValuePair("match_id", params[0]));
            try {
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                slotItemList = gson.fromJson(reader, new TypeToken<Collection<SlotItem>>(){}.getType());
                Collections.reverse(slotItemList);
                slotItemAdapter.setSlotItemList(slotItemList);
            }
            catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            slotItemAdapter.notifyDataSetChanged();
        }
    }

}
