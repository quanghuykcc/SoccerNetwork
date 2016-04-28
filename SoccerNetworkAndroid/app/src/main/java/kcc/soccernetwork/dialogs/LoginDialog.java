package kcc.soccernetwork.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import kcc.soccernetwork.objects.FieldItem;
import kcc.soccernetwork.objects.UserProfile;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.GCMClientManager;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by Administrator on 4/12/2016.
 */
public class LoginDialog extends DialogFragment implements View.OnClickListener {
    EditText mUsernameEdt, mPasswordEdt;
    CheckBox mRememberUserCkb;
    TextView mForgetPasswordTv;
    Activity mActivity;
    Button loginBtn, registerBtn;
    public LoginDialog() {
        super();
    }

    public LoginDialog(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.login_layout, null);
        loginBtn = (Button) view.findViewById(R.id.btn_login);
        registerBtn = (Button) view.findViewById(R.id.btn_register);
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        mUsernameEdt = (EditText) view.findViewById(R.id.et_username);
        mPasswordEdt = (EditText) view.findViewById(R.id.et_password);
        mRememberUserCkb = (CheckBox) view.findViewById(R.id.cb_keep_user);
        mForgetPasswordTv = (TextView) view.findViewById(R.id.tv_forget_pass);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void openRegisterDialog() {
        DialogFragment registerDialog = new RegisterDialog(getActivity());
        registerDialog.show(getFragmentManager(), "");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                final String[] gcmRegId = {""};
                try {
                    GCMClientManager pushClientManager = new GCMClientManager(getActivity(), UtilConstants.PROJECT_NUMBER);
                    pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                        @Override
                        public void onSuccess(String registrationId, boolean isNewRegistration) {

                            Log.d("Registration id", registrationId);
                            gcmRegId[0] = registrationId;
                        }

                        @Override
                        public void onFailure(String ex) {
                            super.onFailure(ex);
                        }
                    });
                }
                catch (Exception ex) {

                }
                String username = mUsernameEdt.getText().toString().trim();
                String password = mPasswordEdt.getText().toString().trim();

                if (checkValidate(username, password)) {
                    new LoginTask().execute(username, password, gcmRegId[0]);
                }
                break;
            case R.id.btn_register:
                openRegisterDialog();
                break;
        }
    }

    private boolean checkValidate(String username, String password) {
        boolean validate = true;
        if (username.equals("")) {
            Snackbar.make(loginBtn, "Bạn cần nhập vào tên đăng nhập", Snackbar.LENGTH_LONG).show();
            validate = false;
        }
        else if (password.equals("")) {
            Snackbar.make(loginBtn, "Bạn cần nhập vào mật khẩu", Snackbar.LENGTH_LONG).show();
            validate = false;
        }

        return validate;
    }

    class LoginTask extends AsyncTask<String, Void, UserProfile> {
        @Override
        protected UserProfile doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tag", "login"));
                nameValuePairs.add(new BasicNameValuePair("username", params[0]));
                nameValuePairs.add(new BasicNameValuePair("password", params[1]));
                nameValuePairs.add(new BasicNameValuePair("gcm_reg_id", params[2]));
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                UserProfile loginUser = gson.fromJson(reader, UserProfile.class);
                Log.i("SoccerNetwork", loginUser.toString());
                return loginUser;
            }
            catch (Exception ex) {
                Log.i("SoccerNetwork", ex.getMessage());
                return null;
            }
        }
        @Override
        protected void onPostExecute(UserProfile userProfile) {
            if (userProfile == null || userProfile.getUsername() == null) {
                Snackbar.make(loginBtn, getString(R.string.login_fail), Snackbar.LENGTH_LONG).show();
            }
            else {
                CheckLogin.getInstance().setLoginUser(userProfile);
                if (mRememberUserCkb.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("remembered_user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("password", userProfile.getPassword());
                    editor.putString("username", userProfile.getUsername());
                    editor.commit();
                }

                Snackbar.make(loginBtn, getString(R.string.login_message), Snackbar.LENGTH_LONG).show();
                dismiss();
            }
        }
    }
}
