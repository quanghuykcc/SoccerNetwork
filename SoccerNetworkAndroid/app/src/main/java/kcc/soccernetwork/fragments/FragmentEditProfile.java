package kcc.soccernetwork.fragments;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.app.DialogFragment;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
import kcc.soccernetwork.dialogs.ChangePassDialog;
import kcc.soccernetwork.dialogs.ChangePhoneNumberDialog;
import kcc.soccernetwork.objects.Rating;
import kcc.soccernetwork.objects.UserProfile;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.LoadImageTask;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;


public class FragmentEditProfile extends Fragment implements View.OnClickListener, ChangePhoneNumberDialog.OnChangePhoneistener {
    UserProfile userLogin;
    RatingBar skill, prestige, friendly;
    TextView skillTv, prestigeTv, friendlyTv;
    TextView phoneNumber;
    public FragmentEditProfile() {
        userLogin = CheckLogin.getInstance().getLoginUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onChangePhoneSuccess(String phone) {
        phoneNumber.setText(phone);
        CheckLogin.getInstance().getLoginUser().setPhone_number(phone);
    }

    @Override
    public void onChangePhoneFailed() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_profile_layout, container, false);
        TextView userName = (TextView) rootView.findViewById(R.id.txt_username);
        TextView password = (TextView) rootView.findViewById(R.id.txt_password);
        TextView email = (TextView) rootView.findViewById(R.id.txt_email);
        phoneNumber = (TextView) rootView.findViewById(R.id.txt_phone_number);
        TextView district = (TextView) rootView.findViewById(R.id.txt_district);
        TextView fullName = (TextView) rootView.findViewById(R.id.txt_full_name);
        ImageView avatar = (ImageView) rootView.findViewById(R.id.imv_user_avatar);
        skill = (RatingBar) rootView.findViewById(R.id.rtb_skill);
        skillTv = (TextView) rootView.findViewById(R.id.tv_skill);
        prestigeTv = (TextView) rootView.findViewById(R.id.tv_prestige);
        friendlyTv = (TextView) rootView.findViewById(R.id.tv_friendly);
        prestige = (RatingBar) rootView.findViewById(R.id.rtb_prestige);
        friendly = (RatingBar) rootView.findViewById(R.id.rtb_friendly);
        Drawable skillDrawable = skill.getProgressDrawable();
        skillDrawable.setColorFilter(Color.parseColor("#0277BD"), PorterDuff.Mode.SRC_ATOP);
        Drawable prestigeDrawable = prestige.getProgressDrawable();
        prestigeDrawable.setColorFilter(Color.parseColor("#0277BD"), PorterDuff.Mode.SRC_ATOP);
        Drawable friendlyDrawable = friendly.getProgressDrawable();
        friendlyDrawable.setColorFilter(Color.parseColor("#0277BD"), PorterDuff.Mode.SRC_ATOP);
        ImageView changePassword = (ImageView) rootView.findViewById(R.id.imv_change_password);
        ImageView changPhoneNumber = (ImageView) rootView.findViewById(R.id.imv_edit_phone_number);
        FloatingActionButton editAvatar = (FloatingActionButton) rootView.findViewById(R.id.fab_edit_avatar);
        editAvatar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0277BD")));
        FloatingActionButton editProfile = (FloatingActionButton) rootView.findViewById(R.id.fab_edit_profile);
        editProfile.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0277BD")));
        changePassword.setOnClickListener(this);
        changPhoneNumber.setOnClickListener(this);
        editAvatar.setOnClickListener(this);
        editProfile.setOnClickListener(this);
        userName.setText(userLogin.getUsername());
        password.setText("******");
        email.setText(userLogin.getEmail());
        phoneNumber.setText(userLogin.getPhone_number());
        district.setText(userLogin.getDistrict_id() + "");
        fullName.setText(userLogin.getFull_name());
        if (userLogin.getAvatar_path() == null || userLogin.getAvatar_path().equals("")) {
            avatar.setImageResource(R.drawable.no_avatar);
        }
        else {
            new LoadImageTask(getActivity(), avatar, R.drawable.no_avatar).execute(UtilConstants.IMAGE_FOLDER_URL + userLogin.getAvatar_path());
        }
        new GetRatingTask().execute(userLogin.getUser_id());
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_change_password:
                showChangePasswordDialog();
                break;
            case R.id.imv_edit_phone_number:
                showChangePhoneNumberDialog();
                break;
            case R.id.fab_edit_avatar:
                break;
            case R.id.fab_edit_profile:
                break;
        }
    }

    private void showChangePasswordDialog() {
        DialogFragment changePasswordDialog = new ChangePassDialog(getActivity());
        changePasswordDialog.show(getActivity().getFragmentManager(), "");
    }

    private void showChangePhoneNumberDialog() {
        DialogFragment changePhoneNumberDialog = new ChangePhoneNumberDialog(getActivity(), this);
        changePhoneNumberDialog.show(getActivity().getFragmentManager(), "");
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
                                skill.setRating(value);
                                skillTv.setText(ratings.get(i).getValue());
                                break;
                            case 1:
                                prestige.setRating(value);
                                prestigeTv.setText(ratings.get(i).getValue());
                                break;
                            case 2:
                                friendly.setRating(value);
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
}
