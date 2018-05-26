package com.akhil.akhildixit.messages;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.akhil.akhildixit.messages.BroadcastReciever.NewMessages;

import info.hoang8f.widget.FButton;

public class HomeActivity extends AppCompatActivity {

    FButton allMessage,newMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        allMessage=findViewById(R.id.all_sms);
        newMessages=findViewById(R.id.new_sms);

        allMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                POJO.chosen="main";
                Intent intent=new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        newMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                POJO.chosen="new";
                Intent intent=new Intent(HomeActivity.this, NewMessages.class);
                startActivity(intent);

            }
        });
    }
}
