package com.akhil.akhildixit.messages.BroadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.akhil.akhildixit.messages.Http;
import com.akhil.akhildixit.messages.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewMessages extends AppCompatActivity {

    Boolean aBoolean=false;
    IntentFilter intentFilter;
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("BroadcastRecieved",intent.getAction());
            SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm:ss");

            Cursor cursor=context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
            cursor.moveToFirst();
            long sentDate = cursor.getLong(5);
            String sentString = format.format(new Date(sentDate));

            long recievedDate = cursor.getLong(4);
            String recievedString = format.format(new Date(recievedDate));

            String text_message=cursor.getString(12);
            String phone_number=cursor.getString(2);
            Http http=new Http(context,phone_number,text_message,sentString,recievedString);
            http.execute();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_messages);

      intentFilter =new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(broadcastReceiver,intentFilter);
        aBoolean=true;



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!aBoolean)
        {
            if (broadcastReceiver==null)
            {
                this.registerReceiver(broadcastReceiver,intentFilter);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (broadcastReceiver!=null)
        {
            this.unregisterReceiver(broadcastReceiver);
        }
    }
}
