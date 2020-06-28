package com.administrator.learndemo.drawable;

import android.os.Bundle;
import android.widget.ImageView;

import com.administrator.learndemo.R;

import androidx.appcompat.app.AppCompatActivity;

public class DrawableActivity extends AppCompatActivity {

    private ImageView circle_img, round_imge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);

//        circle_img = (ImageView) this.findViewById(R.id.iv_circle);
//        round_imge = (ImageView) this.findViewById(R.id.iv_round_circle);
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl);
//        circle_img.setImageDrawable(new CircleImageDrawable(bitmap));
//        round_imge.setImageDrawable(new RoundImageDrawable(bitmap));
    }
}
