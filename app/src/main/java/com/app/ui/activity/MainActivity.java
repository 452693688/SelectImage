package com.app.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.guomin.app.seletcimage.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.image_btn).setOnClickListener(this);
        findViewById(R.id.image_btn2).setOnClickListener(this);
    }

    private PhotoManager photo;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_btn2:
                Manager manager = new Manager();
                manager.getConfig(this);
                break;
            case R.id.image_btn:
                if (photo == null) {
                    photo = new PhotoManager(this);
                }
                photo.corpSquare();
                break;
        }

    }
}
