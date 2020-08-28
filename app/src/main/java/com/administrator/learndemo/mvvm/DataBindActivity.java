package com.administrator.learndemo.mvvm;

import android.os.Bundle;
import android.text.Editable;
import android.widget.Toast;

import com.administrator.learndemo.R;
import com.administrator.learndemo.databinding.LearnDataBind;
import com.administrator.learndemo.mvvm.data.UserInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class DataBindActivity extends AppCompatActivity {

    private LearnDataBind activityDatabindBinding;
    UserInfo xiaoMing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDatabindBinding = DataBindingUtil.setContentView(this, R.layout.activity_databind);

        xiaoMing = new UserInfo("xiaoMing", "tstp456");
        activityDatabindBinding.setUserInfo(xiaoMing);

        UserPresenter userPresenter = new UserPresenter();
        activityDatabindBinding.setPresenter(userPresenter);
    }

    public class UserPresenter {
        public void onUserNamekClick(UserInfo user) {
            Toast.makeText(DataBindActivity.this, "用户名：" + user.getUserName(), Toast.LENGTH_SHORT).show();
        }

        public void afterTextChanged(Editable s) {
            xiaoMing.setUserName(s.toString());
            activityDatabindBinding.setUserInfo(xiaoMing);
        }

        public void afterUserPasswordChanged(Editable s) {
            xiaoMing.setPassword(s.toString());
            activityDatabindBinding.setUserInfo(xiaoMing);
        }

        public void onChangeUserNameClick() {
            xiaoMing.setUserName(xiaoMing.getUserName() + "**");
        }

        public void onChangeUserPassClick() {
            xiaoMing.setUserName(xiaoMing.getPassword() + "--");
        }

        public void onChangeUserAgeClick() {
            xiaoMing.setUserName(xiaoMing.getAge() + "..");
        }
    }
}
