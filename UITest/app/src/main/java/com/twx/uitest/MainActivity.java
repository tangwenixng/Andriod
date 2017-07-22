package com.twx.uitest;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar);
//                int status = progress.getVisibility();
//                int nowPercent = progress.getProgress();
//                if(status == View.GONE)
//                    progress.setVisibility(View.VISIBLE);
//                else
//                    progress.setVisibility(View.GONE);
//                Log.d("当前进度--》》", nowPercent+"");
//                progress.setProgress(nowPercent+10);
                ProgressDialog pd = new ProgressDialog(MainActivity.this);
                pd.setTitle("ProgressDialog Demo");
                pd.setMessage("Loading...");
                pd.setCancelable(true);
                pd.show();



            }
        });
    }
}
