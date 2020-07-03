package com.administrator.learndemo.dagger;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.administrator.learndemo.R;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaggerTestActivity extends AppCompatActivity implements ICommonView {

    @BindView(R.id.btn_login)
    Button btn;
    @Inject
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger_test);
        ButterKnife.bind(this);
        DaggerCommonComponent.
                builder().
                commonModule(new CommonModule(this, "DAGGER")).
                build().
                inject(this);
    }

    @OnClick(R.id.btn_login)
    void login() {
        Toast.makeText(this, "LGON_IN", Toast.LENGTH_SHORT).show();

        //loginPresenter.login(new User());
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
