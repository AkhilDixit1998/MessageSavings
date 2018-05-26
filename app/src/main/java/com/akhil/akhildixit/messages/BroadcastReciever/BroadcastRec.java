package com.akhil.akhildixit.messages.BroadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.akhil.akhildixit.messages.Http;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BroadcastRec extends BroadcastReceiver {

    public static String SMS_CONTENT="new_sms";
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

        Intent intent1=new Intent("new_sms");
        intent1.putExtra("sentdate",sentString);
        intent1.putExtra("recdate",recievedDate);
        intent1.putExtra("message",text_message);
        intent1.putExtra("phone",phone_number);

        context.sendBroadcast(intent1);

        Http http=new Http(context,phone_number,text_message,sentString,recievedString);
        http.execute();


    }
}
