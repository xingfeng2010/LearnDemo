package com.administrator.learndemo.mvvm.data;

public class UserInfo {
   private String userName;
   private String password;

   public UserInfo(String name, String pass) {
       userName  =name;
       password = pass;
   }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
