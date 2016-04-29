package kcc.soccernetwork.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
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
import kcc.soccernetwork.objects.UserProfile;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by Nang on 23/4/2559.
 */
public class ChangePassDialog extends DialogFragment implements View.OnClickListener {
    EditText mOldPass, mNewPass, mComfirmPass;
    Button changePassBtn;
    Activity mActivity;
    ProgressBar pbgLoading;
    String passwordNew = "";
    public ChangePassDialog(){super();}
    public ChangePassDialog(Activity activity){this.mActivity=activity;}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_pass,null);
        mOldPass =(EditText)view.findViewById(R.id.et_old_pass);
        mNewPass =(EditText)view.findViewById(R.id.et_new_pass);
        mComfirmPass =(EditText)view.findViewById(R.id.et_comfirm_pass);
        changePassBtn = (Button) view.findViewById(R.id.bt_change_pass);
        changePassBtn.setOnClickListener(this);
        pbgLoading = (ProgressBar) view.findViewById(R.id.pgb_loading);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_change_pass:
                String oldPassword = mOldPass.getText().toString();
                String newPassword = mNewPass.getText().toString();
                String confirmPassword = mComfirmPass.getText().toString();
                if (checkValidate(oldPassword, newPassword, confirmPassword)) {
                    new ChangePassTask().execute(CheckLogin.getInstance().getLoginUser().getUser_id(),newPassword);
                }
                break;
        }
    }

    private boolean checkValidate(String oldPassword, String newPassword, String confirmPassword) {
        boolean validate = true;
        if (oldPassword.equals("")) {
            Toast.makeText(mActivity, "Chưa nhập mật khẩu hiện tại", Toast.LENGTH_LONG).show();
            validate = false;
        }

        else if (!CheckLogin.getInstance().getLoginUser().getPassword().equals(oldPassword)) {
            Toast.makeText(mActivity, "Mật khẩu hiện tại không chính xác", Toast.LENGTH_LONG).show();
            validate = false;
        }

        else if (newPassword.equals("")) {
            Toast.makeText(mActivity, "Chưa nhập mật khẩu mới", Toast.LENGTH_LONG).show();
            validate = false;
        }

        else if (confirmPassword.equals("")) {
            Toast.makeText(mActivity, "Chưa nhập xác nhận mật khẩu mới", Toast.LENGTH_LONG).show();
            validate = false;
        }

        else if (!confirmPassword.equals(newPassword)) {
            Toast.makeText(mActivity, "Mật khẩu xác nhận và mật khẩu mới không giống nhau", Toast.LENGTH_LONG).show();
            validate = false;
        }

        else if (newPassword.length() < 6) {
            Toast.makeText(mActivity, "Mật khẩu không được dưới 6 ký tự", Toast.LENGTH_LONG).show();
            validate = false;
        }

        return validate;
    }

    class ChangePassTask extends AsyncTask<String,Void,ResponceMessage>{
        @Override
        protected ResponceMessage doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tag", "change_password"));
                nameValuePairs.add(new BasicNameValuePair("user_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("password", params[1]));
                passwordNew = params[1];
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
            pbgLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ResponceMessage responceMessage) {
           if (responceMessage != null && responceMessage.getMessage() != null) {
               if (responceMessage.getCode() == 1) {
                   Toast.makeText(mActivity, "Thay đổi mật khẩu thành công", Toast.LENGTH_LONG).show();
                   SharedPreferences preferences = getActivity().getSharedPreferences("remembered_user", Context.MODE_PRIVATE);
                   if (!preferences.getString("password", "").equals("")) {
                       SharedPreferences.Editor editor = preferences.edit();
                       editor.putString("password", passwordNew);
                       editor.commit();
                   }
                   dismiss();
               }
               else {
                   Toast.makeText(mActivity, responceMessage.getMessage(), Toast.LENGTH_LONG).show();
               }
           }
           else {
               Toast.makeText(mActivity, "Thay đổi mật khẩu thất bại", Toast.LENGTH_LONG).show();
           }

        }
    }
}
