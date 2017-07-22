package com.twx.uipractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<>();

    RecyclerView recyclerView;
    MsgAdapter adapter;
    EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMsg();

        recyclerView = (RecyclerView)findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(adapter);


        inputText = (EditText)findViewById(R.id.input_text);
        Button send = (Button)findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content,Msg.TYPE_SEND);
                    msgList.add(msg);

                    adapter.notifyItemInserted(msgList.size()-1);

                    recyclerView.scrollToPosition(msgList.size()-1);

                    inputText.setText("");

                }
            }
        });
    }

    private void initMsg() {
        Msg msg1 = new Msg("Hello guy", Msg.TYPE_RECEIVE);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello,who are you?",Msg.TYPE_SEND);
        msgList.add(msg2);
        Msg msg3 = new Msg("I'm Tom,Nice meet to you!",Msg.TYPE_RECEIVE);
        msgList.add(msg3);
    }
}
