package com.sty.ne.irregularview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        findViewById(R.id.idv_1).setOnClickListener(this);
        findViewById(R.id.irv_1).setOnClickListener(this);
        findViewById(R.id.irv_2).setOnClickListener(this);
        findViewById(R.id.irv_3).setOnClickListener(this);
        findViewById(R.id.irv_4).setOnClickListener(this);
        findViewById(R.id.imv_1).setOnClickListener(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TouchAndMoveActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(mToast != null) {
            mToast.cancel();
        }
        switch (v.getId()) {
            case R.id.idv_1:
                mToast = Toast.makeText(this, v.getTag(v.getId()).toString(), Toast.LENGTH_SHORT);
                break;
            case R.id.irv_1:
                mToast = Toast.makeText(this, "red", Toast.LENGTH_SHORT);
                break;
            case R.id.irv_2:
                mToast = Toast.makeText(this, "yellow", Toast.LENGTH_SHORT);
                break;
            case R.id.irv_3:
                mToast = Toast.makeText(this, "green", Toast.LENGTH_SHORT);
                break;
            case R.id.irv_4:
                mToast = Toast.makeText(this, "blue", Toast.LENGTH_SHORT);
                break;
            case R.id.imv_1:
                mToast = Toast.makeText(this, v.getTag(v.getId()).toString(), Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
        mToast.show();
    }
}
