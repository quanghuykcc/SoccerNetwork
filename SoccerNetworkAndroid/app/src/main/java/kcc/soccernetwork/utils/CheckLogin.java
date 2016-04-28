package kcc.soccernetwork.utils;

import kcc.soccernetwork.objects.UserProfile;

/**
 * Created by Administrator on 4/12/2016.
 */
public class CheckLogin {
    private static CheckLogin ourInstance = new CheckLogin();

    public static CheckLogin getInstance() {
        return ourInstance;
    }

    private UserProfile mLoginUser ;

    public UserProfile getLoginUser() {
        return mLoginUser;
    }

    public void setLoginUser(UserProfile loginUser) {
        mLoginUser = loginUser;
    }

    private CheckLogin() {
    }
}
