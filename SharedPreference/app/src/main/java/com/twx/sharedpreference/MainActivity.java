package com.twx.sharedpreference;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("num",23);
                editor.putString("str","name");
                editor.putBoolean("boolean",false);
                editor.apply();
            }
        });

        Button restore = (Button) findViewById(R.id.restoreData);
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
               int num = sharedPreferences.getInt("num",0);
                String str = sharedPreferences.getString("str","");
                boolean married = sharedPreferences.getBoolean("boolean",false);
                Log.d("MainActivity", "num is "+num);
                Log.d("MainActivity", "str is "+str);
                Log.d("MainActivity", "boolean is "+married);
            }
        });
    }
}
