package kcc.soccernetwork.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import kcc.soccernetwork.utils.BitmapUri;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UploadImage;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by Administrator on 4/13/2016.
 */
public class RegisterDialog extends DialogFragment implements View.OnClickListener {
    Activity mActivity;
    Button registerBtn;
    ImageView userAvatar;
    EditText userNameEdt, passwordEdt, fullNameEdt, emailEdt, phoneNumberEdt;
    String imagePath = "";
    String tempPath = "";
    public RegisterDialog(Activity activity) {
        mActivity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_register, null);
        registerBtn = (Button) view.findViewById(R.id.btn_register);
        Button cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        registerBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        userNameEdt = (EditText) view.findViewById(R.id.et_username);
        passwordEdt = (EditText) view.findViewById(R.id.et_pass_register);
        fullNameEdt = (EditText) view.findViewById(R.id.et_full_name);
        emailEdt = (EditText) view.findViewById(R.id.et_email);
        phoneNumberEdt = (EditText) view.findViewById(R.id.et_phone);
        userAvatar = (ImageView) view.findViewById(R.id.imv_user_avatar);
        userAvatar.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_register:
                String username = userNameEdt.getText().toString().trim();
                String password = passwordEdt.getText().toString().trim();
                String fullName = fullNameEdt.getText().toString().trim();
                String email = emailEdt.getText().toString().trim();
                String phoneNumber = phoneNumberEdt.getText().toString().trim();
                if (checkValidate(username, password, fullName, phoneNumber)) {
                    new RegisterTask().execute(username, password, fullName, email, phoneNumber);
                }
                break;
            case R.id.imv_user_avatar:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                break;

        }
    }

    private boolean checkValidate(String username, String password, String fullName, String phoneNumber) {
        boolean validate = true;
        if (username.equals("")) {
            Snackbar.make(registerBtn, "Bạn cần nhập vào tên đăng nhập", Snackbar.LENGTH_LONG).show();
            validate = false;
        }
        else if (password.equals("")) {
            Snackbar.make(registerBtn, "Bạn cần nhập vào mật khẩu", Snackbar.LENGTH_LONG).show();
            validate = false;
        }
        else if (fullName.equals("")) {
            Snackbar.make(registerBtn, "Bạn cần nhập vào tên đầy đủ", Snackbar.LENGTH_LONG).show();
            validate = false;
        }
        else if (phoneNumber.equals("")) {
            Snackbar.make(registerBtn, "Bạn cần nhập vào số điện thoại", Snackbar.LENGTH_LONG).show();
            validate = false;
        }
        return validate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = data.getData();
                imagePath = getPath(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                userAvatar.setImageBitmap(bitmap);
            }
        }

    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    class UploadAvatarTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            UploadImage uploadImage = new UploadImage();
            uploadImage.uploadFile(tempPath, params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("UploadImage", "Tải ảnh lên thành công");
        }

    }

    class RegisterTask extends AsyncTask<String, Void, ResponceMessage> {
        @Override
        protected ResponceMessage doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tag", "register"));
                nameValuePairs.add(new BasicNameValuePair("username", params[0]));
                nameValuePairs.add(new BasicNameValuePair("password", params[1]));
                nameValuePairs.add(new BasicNameValuePair("full_name", params[2]));
                nameValuePairs.add(new BasicNameValuePair("email", params[3]));
                nameValuePairs.add(new BasicNameValuePair("phone_number", params[4]));
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                ResponceMessage responceMessage = gson.fromJson(reader, ResponceMessage.class);
                return responceMessage;
            }
            catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ResponceMessage responceMessage) {
            if (responceMessage == null || responceMessage.getMessage() == null) {
                Snackbar.make(registerBtn, getString(R.string.register_fail), Snackbar.LENGTH_LONG).show();
            }
            else {
                if (responceMessage.getCode() == 1) {
                    if (!imagePath.equals("")) {
                        Uri bitmapUri = BitmapUri.getLocalBitmapUri(userAvatar, mActivity);
                        tempPath =  bitmapUri.getPath();
                        Log.i("Upload", tempPath);
                        new UploadAvatarTask().execute(responceMessage.getMessage() + "");
                    }
                    Snackbar.make(registerBtn, getString(R.string.register_success), Snackbar.LENGTH_LONG).show();
                    dismiss();
                }
                else {
                    Snackbar.make(registerBtn, responceMessage.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }


        }
    }
}
