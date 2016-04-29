package kcc.soccernetwork.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.Collections;
import java.util.List;

import kcc.soccernetwork.R;
import kcc.soccernetwork.objects.Rating;
import kcc.soccernetwork.objects.ResponceMessage;
import kcc.soccernetwork.objects.SlotItem;
import kcc.soccernetwork.objects.UserDetail;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by Administrator on 4/23/2016.
 */
public class RatingDialog extends DialogFragment implements View.OnClickListener {
    RatingBar skillRating, prestigeRating, friendlyRating;
    String userRatedId;
    int numberRating = 0;
    int doneRating = 0;
    ProgressBar pgbLoading;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rating:
                sendRating();
                break;
        }
    }

    private OnRatingListener ratingListener;
    public interface OnRatingListener {
        public void onRatingSuccess();
        public void onRatingFailed();
    }

    public RatingDialog(String userRatedId, OnRatingListener ratingListener ) {
        super();
        this.userRatedId = userRatedId;
        this.ratingListener = ratingListener;
    }

    private void sendRating() {
        String user_rating_id = CheckLogin.getInstance().getLoginUser().getUser_id();
        int skill = (int) skillRating.getRating();
        int prestige = (int) prestigeRating.getRating();
        int friendly = (int) friendlyRating.getRating();
        if (skill > 0) {
            numberRating++;
            new SetRatingTask().execute(user_rating_id, userRatedId, String.valueOf(skill), "0");
        }
        if (prestige > 0) {
            numberRating++;
            new SetRatingTask().execute(user_rating_id, userRatedId, String.valueOf(prestige), "1");
        }
        if (friendly > 0) {
            numberRating++;
            new SetRatingTask().execute(user_rating_id, userRatedId, String.valueOf(friendly), "2");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rating_dialog, null);
        skillRating = (RatingBar) view.findViewById(R.id.rtb_skill);
        prestigeRating = (RatingBar) view.findViewById(R.id.rtb_prestige);
        friendlyRating = (RatingBar) view.findViewById(R.id.rtb_friendly);
        pgbLoading = (ProgressBar) view.findViewById(R.id.pgb_loading);
        Drawable skillDrawable = skillRating.getProgressDrawable();
        skillDrawable.setColorFilter(Color.parseColor("#05BA88"), PorterDuff.Mode.SRC_ATOP);
        Drawable prestigeDrawable = prestigeRating.getProgressDrawable();
        prestigeDrawable.setColorFilter(Color.parseColor("#05BA88"), PorterDuff.Mode.SRC_ATOP);
        Drawable friendlyDrawable = friendlyRating.getProgressDrawable();
        friendlyDrawable.setColorFilter(Color.parseColor("#05BA88"), PorterDuff.Mode.SRC_ATOP);
        Button ratingButton = (Button) view.findViewById(R.id.btn_rating);
        ratingButton.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        new GetRatingTask().execute(CheckLogin.getInstance().getLoginUser().getUser_id(), userRatedId);
        return dialog;
    }


    class GetRatingTask extends AsyncTask<String, Void, ArrayList<Rating>> {

        @Override
        protected void onPreExecute() {
            pgbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Rating> doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "get_user_rating"));
            nameValuePairs.add(new BasicNameValuePair("user_rating_id", params[0]));
            nameValuePairs.add(new BasicNameValuePair("user_rated_id", params[1]));
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
                                break;
                            case 1:
                                prestigeRating.setRating(value);
                                break;
                            case 2:
                                friendlyRating.setRating(value);
                                break;
                        }
                    }
                }
            }
            catch (Exception ex) {

            }
            pgbLoading.setVisibility(View.GONE);
        }
    }


    class SetRatingTask extends AsyncTask<String, Void, ResponceMessage> {

        @Override
        protected void onPreExecute() {
            pgbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected ResponceMessage doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tag", "set_rating"));
                nameValuePairs.add(new BasicNameValuePair("user_rating_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("user_rated_id", params[1]));
                nameValuePairs.add(new BasicNameValuePair("value", params[2]));
                nameValuePairs.add(new BasicNameValuePair("rating_type", params[3]));
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                ResponceMessage responceMessage = gson.fromJson(reader, ResponceMessage.class);
                return responceMessage;
            }
            catch (Exception ex) {
                Log.i("SoccerNetwork", ex.getMessage());
                return null;
            }
        }


        @Override
        protected void onPostExecute(ResponceMessage responceMessage) {
            doneRating++;
            if (doneRating == numberRating) {
                ratingListener.onRatingSuccess();
                dismiss();
            }
        }
    }
}
