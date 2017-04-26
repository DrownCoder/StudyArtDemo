package com.study.studyartdemo.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.study.studyartdemo.R;
import com.study.studyartdemo.databinding.ActivityMainBinding;
import com.study.studyartdemo.service.BookManagerService;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initEvents();
    }
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_messenger:
                    Intent intent_messenger = new Intent(MainActivity.this, MessengerActivity.class);
                    startActivity(intent_messenger);
                    break;
                case R.id.tv_aidl:
                    Intent intent_aidl = new Intent(MainActivity.this, BookManagerActivity.class);
                    startActivity(intent_aidl);
                    break;
            }
        }
    };
    private void initEvents() {
        mBinding.tvMessenger.setOnClickListener(mOnClickListener);
        mBinding.tvAidl.setOnClickListener(mOnClickListener);
    }

}
