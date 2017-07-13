package com.amator.loaddeno;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private StatusViewManager manager;
    private LinearLayout ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ll = (LinearLayout) findViewById(R.id.ll_container);
        manager = StatusViewManager.createView(this, ll);
        manager.onLoad();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                manager.onNoNet();
            }
        }).start();
        manager.setOnRetryClick(new StatusViewManager.onRetryClick() {
            @Override
            public void onRetryLoad() {
                manager.onLoad();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        manager.onSuccess();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    public void showDialog(View view) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("加载中...");
        dialog.show();
    }

}
