package kcc.soccernetwork.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import kcc.soccernetwork.R;
import kcc.soccernetwork.objects.ResponceMessage;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by Administrator on 4/23/2016.
 */
public class ChangePhoneNumberDialog extends DialogFragment implements View.OnClickListener {

    Activity mActivity;
    EditText newPhoneNumber;
    EditText retypeNewPhoneNumber;
    ProgressBar progressBar;
    String newPhoneStr = "";
    private OnChangePhoneistener mChangePhoneListener;
    public interface OnChangePhoneistener {
        public void onChangePhoneSuccess(String phone);
        public void onChangePhoneFailed();
    }
    public ChangePhoneNumberDialog() {
        super();
    }

    public ChangePhoneNumberDialog(Activity activity,OnChangePhoneistener changePhoneListener ) {
        super();
        this.mActivity = activity;
        this.mChangePhoneListener = changePhoneListener;
    }
    public ChangePhoneNumberDialog(Activity activity) {
        super();
        this.mActivity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_phone,null);
        newPhoneNumber =(EditText)view.findViewById(R.id.et_new_phone);
        retypeNewPhoneNumber =(EditText)view.findViewById(R.id.et_comfirm_phone);
        progressBar = (ProgressBar) view.findViewById(R.id.pgb_loading);
        Button okButton = (Button) view.findViewById(R.id.bt_change_phone);
        okButton.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_change_phone:
                String newPhone = newPhoneNumber.getText().toString();
                String comfirmPhone = retypeNewPhoneNumber.getText().toString();
                Log.i("news phone ",newPhone);
                if (checkValidate(newPhone, comfirmPhone)) {
                    new ChangePhoneTask().execute(CheckLogin.getInstance().getLoginUser().getUser_id(),newPhone);
                }
                break;
        }
    }

    private boolean checkValidate(String newPhone, String corfimPhone) {
        boolean validate = true;
        if (newPhone.equals("")) {
            newPhoneNumber.setError("Chưa nhập số điện thoại.");
//            Toast.makeText(mActivity, "Chưa nhập số điện thoại mới.", Toast.LENGTH_LONG).show();
            validate = false;
        }
        else if (corfimPhone.equals("")) {
            retypeNewPhoneNumber.setError("Chưa nhập xác nhận số điện thoại");
//            Toast.makeText(mActivity, "Chưa nhập xác nhận số điện thoại.", Toast.LENGTH_LONG).show();
            validate = false;
        }
        else if (!newPhone.equals(corfimPhone)) {
            retypeNewPhoneNumber.setError("Không giống số điện thoại ở trên.");
//            Toast.makeText(mActivity, "Xác nhận số điện thoại không chính xác.", Toast.LENGTH_LONG).show();
            validate = false;
        }

        return validate;
    }

    class ChangePhoneTask extends AsyncTask<String,Void,ResponceMessage> {
        @Override
        protected ResponceMessage doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tag", "change_phone_number"));
                nameValuePairs.add(new BasicNameValuePair("user_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("phone_number", params[1]));
                newPhoneStr = params[1];
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                ResponceMessage message = gson.fromJson(reader, ResponceMessage.class);
                return message;
            }
            catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ResponceMessage responceMessage) {
            if (responceMessage != null && responceMessage.getMessage() != null) {
                if (responceMessage.getCode() == 1) {
                    Toast.makeText(mActivity, "Thay đổi số điện thoại thành công", Toast.LENGTH_LONG).show();
                    mChangePhoneListener.onChangePhoneSuccess(newPhoneStr);
                    dismiss();
                }
                else {
                    Toast.makeText(mActivity, responceMessage.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(mActivity, "Thay đổi số điện thoại thất bại.", Toast.LENGTH_LONG).show();
            }

        }
    }
}
