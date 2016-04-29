package kcc.soccernetwork.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import kcc.soccernetwork.adapters.MatchItemAdapter;
import kcc.soccernetwork.adapters.MatchItemSmallAdapter;
import kcc.soccernetwork.dialogs.AddMatchDialog;
import kcc.soccernetwork.dialogs.LoginDialog;
import kcc.soccernetwork.objects.FieldItem;
import kcc.soccernetwork.objects.MatchItem;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.DividerItemDecoration;
import kcc.soccernetwork.utils.GoogleMapFunctions;
import kcc.soccernetwork.utils.ImageLoader;
import kcc.soccernetwork.utils.LoadImageTask;
import kcc.soccernetwork.utils.RecyclerClickListener;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

public class FieldActivity extends AppCompatActivity implements View.OnClickListener, AddMatchDialog.AddMatchListener {
    Button addMatchBtn;
    FieldItem fieldItem;
    ArrayList<MatchItem> matchItemList;
    RecyclerView matchesRecyclerView;
    MatchItemSmallAdapter matchItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        addMatchBtn = (Button) findViewById(R.id.btn_add_match);
        addMatchBtn.setOnClickListener(this);
        TextView operatingTime = (TextView) findViewById(R.id.tv_operating_time);
        TextView address = (TextView) findViewById(R.id.tv_address);
        TextView district = (TextView) findViewById(R.id.tv_district);
        TextView city = (TextView) findViewById(R.id.tv_city);
        TextView phone = (TextView) findViewById(R.id.tv_phone);
        ImageView fieldImage = (ImageView) findViewById(R.id.imv_field);
        ImageView mapStaticImage = (ImageView) findViewById(R.id.imv_map);
        TextView fieldName = (TextView) findViewById(R.id.tv_field_name);
        fieldItem = (FieldItem) getIntent().getSerializableExtra("FieldItem");
        matchesRecyclerView = (RecyclerView) findViewById(R.id.rcv_matches);
        matchItemList = new ArrayList<MatchItem>();
        matchItemAdapter = new MatchItemSmallAdapter(this, matchItemList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        matchesRecyclerView.setLayoutManager(layoutManager);
        matchesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        matchesRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        matchesRecyclerView.setAdapter(matchItemAdapter);
        matchesRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, matchesRecyclerView, new RecyclerClickListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent matchIntent = new Intent(FieldActivity.this, MatchActivity.class);
                matchIntent.putExtra("MatchItem", matchItemAdapter.getMatchItemList().get(position));
                startActivity(matchIntent);
                Snackbar.make(matchesRecyclerView, "Click Item: " + position, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Snackbar.make(matchesRecyclerView, "Long Click Item: " + position, Snackbar.LENGTH_LONG).show();
            }
        }));

        if (fieldItem != null) {
            new MatchGetterTask().execute(fieldItem.getField_id());
            operatingTime.setText(fieldItem.getOpen_time() + " đến " + fieldItem.getClose_time());
            address.setText(fieldItem.getAddress());
            city.setText(fieldItem.getCity_name());
            district.setText(fieldItem.getDistrict_name());
            phone.setText(fieldItem.getPhone_number());
            fieldImage.setImageResource(R.drawable.no_avatar);
            fieldName.setText(fieldItem.getField_name());
            new LoadImageTask(this, fieldImage, R.drawable.no_avatar).execute(UtilConstants.IMAGE_FOLDER_URL + "avatar1.jpg");
            new LoadImageTask(this, mapStaticImage, R.drawable.no_avatar).execute(GoogleMapFunctions.getStaticMapURL(fieldItem.getLatitude(), fieldItem.getLongitude(), UtilConstants.API_KEY));

        }
        getSupportActionBar().setTitle(getString(R.string.field) + " " + fieldItem.getField_name());
    }

    @Override
    public void OnAddMatchSuccess(MatchItem matchItem) {
        matchItemAdapter.addNewMatch(matchItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_match:
                if (CheckLogin.getInstance().getLoginUser() == null) {
                    openLoginDialog();
                }
                else {
                    openAddMatchDialog();
                }
        }
    }

    private void openAddMatchDialog() {
        DialogFragment addMatchDialog = new AddMatchDialog(this, fieldItem, this);
        addMatchDialog.show(getFragmentManager(), "");
    }

    private void openLoginDialog() {
        DialogFragment loginDialog = new LoginDialog(this);
        loginDialog.show(getFragmentManager(), "");
    }

    class MatchGetterTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "get_matches_by_field"));
            nameValuePairs.add(new BasicNameValuePair("field_id", params[0]));
            try {
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                matchItemList = gson.fromJson(reader, new TypeToken<Collection<MatchItem>>(){}.getType());
                Collections.reverse(matchItemList);
                matchItemAdapter.setMatchItemList(matchItemList);
            }
            catch (Exception ex) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            matchItemAdapter.notifyDataSetChanged();
        }
    }

}
