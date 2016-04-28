package kcc.soccernetwork.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import java.util.List;

import kcc.soccernetwork.R;
import kcc.soccernetwork.dialogs.RatingDialog;
import kcc.soccernetwork.objects.Rating;
import kcc.soccernetwork.objects.UserDetail;
import kcc.soccernetwork.objects.UserProfile;
import kcc.soccernetwork.utils.ImageLoader;
import kcc.soccernetwork.utils.LoadImageTask;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

public class UserDetailActivity extends AppCompatActivity implements View.OnClickListener, RatingDialog.OnRatingListener {
    TextView email, phoneNumber, district, fullName, city;
    RatingBar skillRating, prestigeRating, friendlyRating;
    TextView skillTv, prestigeTv, friendlyTv;
    ImageView avatar;
    UserDetail userDetail;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_layout);
        email = (TextView) findViewById(R.id.txt_email);
        phoneNumber = (TextView) findViewById(R.id.txt_phone_number);
        district = (TextView) findViewById(R.id.txt_district);
        fullName = (TextView) findViewById(R.id.txt_full_name);
        skillRating = (RatingBar) findViewById(R.id.rtb_skill);
        prestigeRating = (RatingBar) findViewById(R.id.rtb_prestige);
        friendlyRating = (RatingBar) findViewById(R.id.rtb_friendly);
        skillTv = (TextView) findViewById(R.id.tv_skill);
        prestigeTv = (TextView) findViewById(R.id.tv_prestige);
        friendlyTv = (TextView) findViewById(R.id.tv_friendly);
        Drawable skillDrawable = skillRating.getProgressDrawable();
        skillDrawable.setColorFilter(Color.parseColor("#0277BD"), PorterDuff.Mode.SRC_ATOP);
        Drawable prestigeDrawable = prestigeRating.getProgressDrawable();
        prestigeDrawable.setColorFilter(Color.parseColor("#0277BD"), PorterDuff.Mode.SRC_ATOP);
        Drawable friendlyDrawable = friendlyRating.getProgressDrawable();
        friendlyDrawable.setColorFilter(Color.parseColor("#0277BD"), PorterDuff.Mode.SRC_ATOP);
        avatar = (ImageView) findViewById(R.id.imv_user_avatar);
        FloatingActionButton ratingButton = (FloatingActionButton) findViewById(R.id.fab_rating);
        ratingButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0277BD")));
        getSupportActionBar().setTitle("THÔNG TIN NGƯỜI DÙNG");
        ratingButton.setOnClickListener(this);
        Intent intent = getIntent();
        String id = intent.getStringExtra("user_id");
        new GetUserTask().execute(id);
        new GetRatingTask().execute(id);
    }

    @Override
    public void onRatingSuccess() {
        new GetRatingTask().execute(userDetail.getUser_id());
        Toast.makeText(getApplicationContext(), "Đánh giá người chơi thành công", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRatingFailed() {

    }

    private void showRatingDialog() {
        if (userDetail != null) {
            DialogFragment ratingDialog = new RatingDialog(userDetail.getUser_id(), this);
            ratingDialog.show(getFragmentManager(), "");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_rating:
                showRatingDialog();
                break;
        }
    }

    class GetRatingTask extends AsyncTask<String, Void, ArrayList<Rating>> {
        @Override
        protected ArrayList<Rating> doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "get_total_rating"));
            nameValuePairs.add(new BasicNameValuePair("user_rated_id", params[0]));
            try {
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                ArrayList<Rating> ratingList = gson.fromJson(reader, new TypeToken<Collection<Rating>>(){}.getType());
                return ratingList;
            }
            catch (Exception ex) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Rating> ratings) {
            try {
                if (ratings != null) {
                    for (int i = 0; i < ratings.size(); i++) {
                        float value = Float.parseFloat(ratings.get(i).getValue());
                        int ratingType = Integer.parseInt(ratings.get(i).getRating_type());
                        switch (ratingType) {
                            case 0:
                                skillRating.setRating(value);
                                skillTv.setText(ratings.get(i).getValue());
                                break;
                            case 1:
                                prestigeRating.setRating(value);
                                prestigeTv.setText(ratings.get(i).getValue());
                                break;
                            case 2:
                                friendlyRating.setRating(value);
                                friendlyTv.setText(ratings.get(i).getValue());
                                break;
                        }
                    }
                }
            }
            catch (Exception ex) {

            }
        }
    }


    class GetUserTask extends AsyncTask<String, Void, UserDetail> {

        @Override
        protected UserDetail doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tag", "get_user_by_id"));
                nameValuePairs.add(new BasicNameValuePair("user_id", params[0]));
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                UserDetail userDetail = gson.fromJson(reader, UserDetail.class);
                return userDetail;
            }
            catch (Exception ex) {
                Log.i("SoccerNetwork", ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserDetail userDetail) {
            if (userDetail != null && userDetail.getFull_name() != null) {
                UserDetailActivity.this.userDetail = userDetail;
                email.setText(userDetail.getEmail());
                fullName.setText(userDetail.getFull_name());
                phoneNumber.setText(userDetail.getPhone_number());
                district.setText(userDetail.getDistrict_name());
                if (userDetail.getAvatar_path() == null || userDetail.getAvatar_path().equals("")) {
                    avatar.setImageResource(R.drawable.no_avatar);
                }
                else {
                    new LoadImageTask(UserDetailActivity.this, avatar, R.drawable.no_avatar).execute(UtilConstants.IMAGE_FOLDER_URL + userDetail.getAvatar_path());
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
