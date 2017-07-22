package com.twx.filepersistence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EditText content =(EditText) findViewById(R.id.saveData);
        save(content.getText().toString());
    }

    public  void save(String content){
        BufferedWriter writer = null;
        try {
            FileOutputStream outputStream = openFileOutput("twxTestData", MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer!=null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            Log.d("save content","complete!");
        }
    }
}
