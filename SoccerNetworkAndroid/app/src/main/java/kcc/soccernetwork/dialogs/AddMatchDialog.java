package kcc.soccernetwork.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kcc.soccernetwork.R;
import kcc.soccernetwork.objects.FieldItem;
import kcc.soccernetwork.objects.MatchItem;
import kcc.soccernetwork.objects.SlotItem;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.TimeFunctions;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by Administrator on 4/13/2016.
 */
public class AddMatchDialog extends DialogFragment implements View.OnClickListener {
    Activity activity;
    SeekBar startTimeSeek, endTimeSeek;
    EditText priceEdt, maximumPlayersEdt;
    TextView startTimeTv, endTimeTv;
    TextView dateTv;
    TextView fieldNameTv;
    int day;
    int month;
    int year;
    FieldItem fieldItem;
    public AddMatchDialog(Activity activity, FieldItem fieldItem) {
        super();
        this.activity = activity;
        this.fieldItem = fieldItem;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_match, null);
        Button okButton = (Button) view.findViewById(R.id.btn_add_match);
        startTimeTv = (TextView) view.findViewById(R.id.tv_begin_time);
        endTimeTv = (TextView) view.findViewById(R.id.tv_end_time);
        startTimeSeek = (SeekBar) view.findViewById(R.id.sb_begin_time);
        startTimeSeek.setMax(UtilConstants.TIME_SCALE.length - 1);
        endTimeSeek = (SeekBar) view.findViewById(R.id.sb_end_time);
        fieldNameTv = (TextView) view.findViewById(R.id.tv_field_name);
        if (fieldItem != null) {
            fieldNameTv.setText("Sân " + fieldItem.getField_name());
        }
        dateTv = (TextView) view.findViewById(R.id.tv_date_start);
        Calendar calendar = Calendar.getInstance();
        showDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        dateTv.setOnClickListener(this);
        endTimeSeek.setMax(UtilConstants.TIME_SCALE.length - 1);
        endTimeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                endTimeTv.setText(UtilConstants.TIME_SCALE[progress]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startTimeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                startTimeTv.setText(UtilConstants.TIME_SCALE[progress]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        startTimeSeek.setProgress(0);
        endTimeSeek.setProgress(0);
        priceEdt = (EditText) view.findViewById(R.id.et_price);
        maximumPlayersEdt = (EditText) view.findViewById(R.id.et_max_person);
        okButton.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void showDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        dateTv.setText(new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year));
    }

    private void openVerifyDialog() {
        DialogFragment verifyDialog = new VerifyDialog(getActivity());
        verifyDialog.show(getFragmentManager(), "");
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
           showDate(dayOfMonth, monthOfYear, year);
        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_match:
                //openVerifyDialog();
                addNewMatch();
                break;
            case R.id.tv_date_start:
                new DatePickerDialog(getActivity(), dateSetListener, year, month, day).show();
                break;
        }
    }

    private void addNewMatch() {
        String price = priceEdt.getText().toString().trim();
        String maximumPlayers = maximumPlayersEdt.getText().toString().trim();
        String[] startTime =  startTimeTv.getText().toString().trim().split(":");
        Calendar startDate = Calendar.getInstance();
        startDate.set(year, month, day, Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]), 0);

        String[] endTime =  endTimeTv.getText().toString().trim().split(":");
        Calendar endDate = Calendar.getInstance();
        endDate.set(year, month, day, Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]), 0);
        if (checkValidate(price, maximumPlayers, startDate, endDate)) {
            TimeFunctions timeFunctions = new TimeFunctions();
            String startDateStr = timeFunctions.toDateString(startDate);
            String endDateStr = timeFunctions.toDateString(endDate);
            Log.i("SoccerNetwork", "Start time: " + startDateStr);
            Log.i("SoccerNetwork", "End time: " + endDateStr);
            new AddMatchTask().execute(fieldItem.getField_id(), CheckLogin.getInstance().getLoginUser().getUser_id(), maximumPlayers, price, startDateStr, endDateStr);
        }
    }

    private boolean checkValidate(String price, String maximumPlayers, Calendar startDate, Calendar endDate) {
        try {
            Integer.parseInt(price);
        }
        catch (NumberFormatException ex) {
            Toast.makeText(getActivity(), "Giá không hợp lệ", Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            Integer.parseInt(maximumPlayers);
        }
        catch (NumberFormatException ex) {
            Toast.makeText(getActivity(), "Số người chơi không hợp lệ", Toast.LENGTH_LONG).show();
            return false;
        }

        if (startDate.getTimeInMillis() > endDate.getTimeInMillis()) {
            Toast.makeText(getActivity(), "Thời gian bắt đầu phải trước thời gian kết thúc", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    class AddMatchTask extends AsyncTask<String, Void, MatchItem> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected MatchItem doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tag", "add_match"));
                nameValuePairs.add(new BasicNameValuePair("field_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("host_id", params[1]));
                nameValuePairs.add(new BasicNameValuePair("maximum_players", params[2]));
                nameValuePairs.add(new BasicNameValuePair("price", params[3]));
                nameValuePairs.add(new BasicNameValuePair("start_time", params[4]));
                nameValuePairs.add(new BasicNameValuePair("end_time", params[5]));

                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                MatchItem matchItem = gson.fromJson(reader, MatchItem.class);
                return matchItem;
            }
            catch (Exception ex) {
                Log.i("SoccerNetwork", ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(MatchItem matchItem) {
            if (matchItem == null || matchItem.getFull_name() == null) {
                Toast.makeText(activity, "Tạo trận đấu thất bại", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(activity, "Tạo trận đấu mới thành công", Toast.LENGTH_LONG).show();
                dismiss();
            }
        }
    }

}
