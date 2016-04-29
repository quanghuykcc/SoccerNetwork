package kcc.soccernetwork.activities;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import kcc.soccernetwork.R;
import kcc.soccernetwork.dialogs.LoginDialog;
import kcc.soccernetwork.fragments.FragmentAboutUs;
import kcc.soccernetwork.fragments.FragmentChangePass;
import kcc.soccernetwork.fragments.FragmentDrawer;
import kcc.soccernetwork.fragments.FragmentEditProfile;
import kcc.soccernetwork.fragments.FragmentHomeSoccer;
import kcc.soccernetwork.objects.ResponceMessage;
import kcc.soccernetwork.objects.UserProfile;
import kcc.soccernetwork.utils.CheckLogin;
import kcc.soccernetwork.utils.GCMClientManager;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        loginIfRemembered();

        // display the first navigation drawer view on app launch
        displayView(0);
    }

    private void loginIfRemembered() {
        SharedPreferences preferences = getSharedPreferences ("remembered_user",MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        if (!username.equals("")) {
            final String[] gcmRegId = {""};
            try {
                GCMClientManager pushClientManager = new GCMClientManager(this, UtilConstants.PROJECT_NUMBER);
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
            new LoginTask().execute(username, password, gcmRegId[0]);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_search:
                Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_logout:
                logout();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        if (CheckLogin.getInstance().getLoginUser() != null) {
            new LogoutTask().execute(CheckLogin.getInstance().getLoginUser().getUsername());
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.not_yet_login), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void showLoginDialog() {
        DialogFragment loginDialog = new LoginDialog(this);
        loginDialog.show(getFragmentManager(), "");
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new FragmentHomeSoccer();
                title = getString(R.string.titlehome);
                break;
            case 1:

                break;
            case 2:
                if (CheckLogin.getInstance().getLoginUser() == null) {
                    showLoginDialog();
                }
                else {
                    fragment = new FragmentEditProfile();
                    title = getString(R.string.title_editprofile);
                }

                break;
            case 3:
                fragment = new FragmentChangePass();
                title = getString(R.string.title_setting);
                break;
            case 4:
                fragment = new FragmentAboutUs();
                title = getString(R.string.title_aboutus);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    class LogoutTask extends AsyncTask<String, Void, ResponceMessage> {
        @Override
        protected ResponceMessage doInBackground(String... params) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tag", "logout"));
                nameValuePairs.add(new BasicNameValuePair("username", params[0]));
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
                Toast.makeText(getApplicationContext(), "Lỗi đăng xuất tài khoản", Toast.LENGTH_LONG).show();
            }
            else if (responceMessage.getCode() == 1) {
                Toast.makeText(getApplicationContext(), getString(R.string.logout_success), Toast.LENGTH_LONG).show();
                CheckLogin.getInstance().setLoginUser(null);
                SharedPreferences preferences = getSharedPreferences ("remembered_user",MODE_PRIVATE);
                SharedPreferences.Editor editor =  preferences.edit();
                editor.putString("username", "");
                editor.putString("password", "");
                editor.commit();
            }
            else {
                Toast.makeText(getApplicationContext(), responceMessage.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
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
                Toast.makeText(getApplicationContext(), "Bạn đã đăng nhập thất bại", Toast.LENGTH_LONG).show();
            }
            else {
                CheckLogin.getInstance().setLoginUser(userProfile);
                Toast.makeText(getApplicationContext(), "Bạn đã đăng nhập thành công", Toast.LENGTH_LONG).show();
            }
        }
    }

}