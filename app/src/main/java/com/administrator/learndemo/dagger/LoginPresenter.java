package com.administrator.learndemo.dagger;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

public class LoginPresenter {
    ICommonView iView;
    private String iName;

    @Inject
    public LoginPresenter(ICommonView iView, String iName) {
        this.iView = iView;
        this.iName = iName;
    }

    public void login(User user) {
        Context mContext = iView.getContext();
        Toast.makeText(mContext, "login..." + iName, Toast.LENGTH_SHORT).show();
    }
}
