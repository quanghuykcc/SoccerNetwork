package kcc.soccernetwork.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import kcc.soccernetwork.R;
import kcc.soccernetwork.objects.MatchItem;
import kcc.soccernetwork.objects.SlotItem;
import kcc.soccernetwork.objects.UserProfile;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by Administrator on 4/17/2016.
 */
public class JoinMatchDialog extends DialogFragment implements View.OnClickListener{
    private Activity activity;
    private MatchItem matchItem;
    private TextView numberAttenderTv;
    private SeekBar numberAttenderSb;
    public JoinMatchDialog(Activity activity, MatchItem matchItem, OnJoinMatchListener joinMatchListener) {
        super();
        this.activity = activity;
        this.matchItem = matchItem;
        this.joinMatchListener = joinMatchListener;
    }

    private OnJoinMatchListener joinMatchListener;

    public interface OnJoinMatchListener {
        public void joinMatchSuccess(SlotItem slotItem);
        public void joinMatchFailed();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.join_match_layout, null);
        Button okButton = (Button) view.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(this);
        numberAttenderTv = (TextView) view.findViewById(R.id.tv_number_attender);
        numberAttenderSb = (SeekBar) view.findViewById(R.id.sb_number_attender);
        try {
            if (matchItem != null) {
                if (matchItem.getAttended() == null || matchItem.getAttended().equals("null")) {
                    numberAttenderSb.setMax(Integer.parseInt(matchItem.getMaximum_players()) - 1);

                }
                else {
                    numberAttenderSb.setMax(Integer.parseInt(matchItem.getMaximum_players()) - Integer.parseInt(matchItem.getAttended()) - 1);
                }
            }

        }
        catch (Exception ex) {

        }
        numberAttenderSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numberAttenderTv.setText(progress + 1 + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        numberAttenderTv.setText("1");
        numberAttenderSb.setProgress(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);



        return dialog;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                String matchId = matchItem.getMatch_id();
                String userId = CheckLogin.getInstance().getLoginUser().getUser_id();
                String quantity = numberAttenderSb.getProgress() + 1 + "";
                new AddSlotTask().execute(matchId, userId, quantity);
                break;
        }
    }

    class AddSlotTask extends AsyncTask<String, Void, SlotItem> {
        @Override
        protected SlotItem doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tag", "add_slot"));
                nameValuePairs.add(new BasicNameValuePair("match_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("user_id", params[1]));
                nameValuePairs.add(new BasicNameValuePair("quantity", params[2]));
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                SlotItem slotItem = gson.fromJson(reader, SlotItem.class);
                return slotItem;
            }
            catch (Exception ex) {
                Log.i("SoccerNetwork", ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(SlotItem slotItem) {
            if (slotItem != null && slotItem.getPhone_number() != null) {
                Log.i("SoccerNetwork", slotItem.toString());
                joinMatchListener.joinMatchSuccess(slotItem);
                JoinMatchDialog.this.dismiss();
            }
            else {
                joinMatchListener.joinMatchFailed();
            }
        }
    }
}
