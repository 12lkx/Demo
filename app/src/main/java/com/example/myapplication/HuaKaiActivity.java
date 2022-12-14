package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.SlidingVerification;

public class HuaKaiActivity extends AppCompatActivity {

    private SlidingVerification seekbar;
    private EditText mEtInput;
    private Button mBtnReset;
    private TextView mTvTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hua_kai);
        initView();
        initListener();
    }

    private void initView() {
        seekbar = findViewById(R.id.sb_progress);
        mEtInput = findViewById(R.id.et_input);
        mBtnReset = findViewById(R.id.btn_reset);
        mTvTop = findViewById(R.id.tv_top);
    }

    private void initListener() {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i("cyc", "onStopTrackingTouch: " + seekBar.getProgress());
                if (seekBar.getProgress() >= seekBar.getMax() && !TextUtils.isEmpty(mEtInput.getText().toString().trim())) {
                    seekBar.setThumb(getResources().getDrawable(R.mipmap.check_pass));
                    seekBar.setThumbOffset(seekBar.getMax());
                    seekBar.setProgress(seekBar.getMax());
                    seekBar.setEnabled(false);
                    mTvTop.setVisibility(View.VISIBLE);
                    mBtnReset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it=new Intent();
                            it.setClass(HuaKaiActivity.this,Login.class);
                            startActivity(it);
                        }
                    });

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (seekBar.getProgress() >= seekBar.getMax()) {
                    if (TextUtils.isEmpty(mEtInput.getText().toString().trim())) {
                        seekBar.setThumb(getResources().getDrawable(R.mipmap.seekbar_thumb));
                        seekBar.setThumbOffset(0);
                        seekBar.setProgress(0);
                        Toast.makeText(HuaKaiActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HuaKaiActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    seekBar.setProgress(0);
                }
            }
        });

    }

}
