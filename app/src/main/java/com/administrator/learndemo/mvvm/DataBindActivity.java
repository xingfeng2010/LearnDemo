package com.administrator.learndemo.mvvm;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import com.administrator.learndemo.R;
import com.administrator.learndemo.databinding.LearnDataBind;
import com.administrator.learndemo.mvvm.data.ObservableGoods;
import com.administrator.learndemo.mvvm.data.UserInfo;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.ObservableMap;

public class DataBindActivity extends AppCompatActivity {

    private LearnDataBind activityDatabindBinding;
    UserInfo xiaoMing;

    private ObservableMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDatabindBinding = DataBindingUtil.setContentView(this, R.layout.activity_databind);

        xiaoMing = new UserInfo("xiaoMing", "tstp456");
        activityDatabindBinding.setUserInfo(xiaoMing);

        UserPresenter userPresenter = new UserPresenter();
        activityDatabindBinding.setPresenter(userPresenter);

        ObservableGoods goods = new ObservableGoods("shixing",1000f,"ttttt");
        activityDatabindBinding.setGoods(goods);


        map = new ObservableArrayMap<>();
        map.put("name","xingMap");
        map.put("age","24");
        activityDatabindBinding.setBindMap(map);

        ObservableList<String> list = new ObservableArrayList<>();
        list.add("list00");
        list.add("list11");
        activityDatabindBinding.setBindList(list);

        activityDatabindBinding.setIndex(0);
        activityDatabindBinding.setKey("name");
    }

    public void changeData(View view) {
        map.put("name", "xingfeng, hi " + new Random().nextInt(100));
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
