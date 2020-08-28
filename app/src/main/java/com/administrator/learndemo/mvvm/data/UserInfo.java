package com.administrator.learndemo.mvvm.data;

import com.administrator.learndemo.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class UserInfo extends BaseObservable {


    private String userName;
    private String password;

    @Bindable
    public String age;

    public UserInfo(String name, String pass) {
        userName = name;
        password = pass;
    }


    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(com.administrator.learndemo.BR.userName);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;

        notifyPropertyChanged(com.administrator.learndemo.BR.password);
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;

        notifyPropertyChanged(com.administrator.learndemo.BR.age);
    }
}
