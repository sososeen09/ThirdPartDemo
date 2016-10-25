package com.longge.thirdpartdemo.dagger2.present;

import android.util.Log;

import com.longge.thirdpartdemo.dagger2.model.User;
import com.longge.thirdpartdemo.dagger2.model.UserManager;
import com.longge.thirdpartdemo.dagger2.view.IUserView;

import javax.inject.Inject;

/**
 * Created by long on 2016/10/24.
 */

public class DaggerPresenter {
    private UserManager userManager;
    private IUserView iUserView;


    @Inject
    public DaggerPresenter(UserManager userManager) {
        Log.d("TAG", "DaggerPresenter: ");
        this.userManager = userManager;
    }



    public User getUser() {
        return userManager.getUser();
    }

    public void getUserName() {
        iUserView.setUserName(getUser().name);
    }

    public void setUserView(IUserView iUserView) {
        this.iUserView = iUserView;
    }


}
