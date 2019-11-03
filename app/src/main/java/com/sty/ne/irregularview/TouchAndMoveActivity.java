package com.sty.ne.irregularview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class TouchAndMoveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_and_move_view);

        findViewById(R.id.touch_move_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag(v.getId());
                if(tag == 1) {
                    Toast.makeText(TouchAndMoveActivity.this, "点击区域内", Toast.LENGTH_SHORT).show();;
                }else {
                    Toast.makeText(TouchAndMoveActivity.this, "点击区域外", Toast.LENGTH_SHORT).show();;
                }
            }
        });
    }
}
