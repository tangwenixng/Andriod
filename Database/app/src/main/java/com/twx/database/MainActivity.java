package com.twx.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    MyDatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new MyDatabaseHelper(this, "BookStore.db", null,1);

        Button button = (Button) findViewById(R.id.createDB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              SQLiteDatabase db =  databaseHelper.getWritableDatabase();
            }
        });

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db =  databaseHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("name","android first book");
                cv.put("author","GuoLin");
                cv.put("price",78.50);
                cv.put("pages",580);
                db.insert("book",null,cv);

                cv.clear();

                cv.put("name","baiyexing");
                cv.put("author","dashi");
                cv.put("price",25);
                cv.put("pages",600);
                db.insert("book",null,cv);
            }
        });

        Button update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db =  databaseHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();

                cv.put("price",90.05);
                db.update("book",cv,"name=?",new String[]{"android first book"});
            }
        });

        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db =  databaseHelper.getWritableDatabase();
                db.delete("book","author=?",new String[]{"dashi"});
                Cursor book = db.query("book", null, null, null, null, null, null, null);
            }
        });

        Button query = (Button) findViewById(R.id.query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db =  databaseHelper.getWritableDatabase();
                Cursor book = db.query("book", null, null, null, null, null, null, null);
                if(book.moveToFirst()){
                    do{
                        String author = book.getString(book.getColumnIndex("author"));
                        Log.d("Author-->>", author);
                    }while (book.moveToNext());
                }
            }
        });
    }
}
