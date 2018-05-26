package com.akhil.akhildixit.messages;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Http extends AsyncTask {

    TextView upload,uploadsucees;

    Context context;
    String phonenumber;
    String textMessage;
    String sentDate;String recieveddate;
    public Http(Context context, String phonenumber, String textMessage, String sentDate, String recieveddate, TextView upload,TextView uploadsucees)
    {
        this.phonenumber=phonenumber;
        this.textMessage=textMessage;
        this.sentDate=sentDate;
        this.recieveddate=recieveddate;
        this.context=context;
        this.upload=upload;
        this.uploadsucees=uploadsucees;

    }

    public Http(Context context,String phonenumber,String textMessage,String sentDate,String recieveddate)
    {
        this.phonenumber=phonenumber;
        this.textMessage=textMessage;
        this.sentDate=sentDate;
        this.recieveddate=recieveddate;
        this.context=context;

    }
    String data="";
    @Override
    protected String doInBackground(Object[] objects) {
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request= null;
        try {
            request = new Request.Builder().url("http://monkhub.com/forest/message-api-get.php?message_from="+phonenumber+"&text_message="+ URLEncoder.encode(textMessage,"UTF-8")+"&sent_time="+URLEncoder.encode(sentDate,"UTF-8")+"&recieved_time="+URLEncoder.encode(recieveddate,"UTF-8"))
                    .build();

            Response response=okHttpClient.newCall(request).execute();
            data=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Toast.makeText(context,"The message is "+data,Toast.LENGTH_LONG).show();
        Log.e("MessageHere",data);
        POJO.uploaded++;
        if (POJO.chosen.equals("main"))
        {
upload.setText("UPLOADED :: "+POJO.uploaded);
        }
        if (POJO.uploaded==POJO.total && POJO.chosen.equals("main"))
        {
            
            uploadsucees.setVisibility(View.VISIBLE);
        }
    }


}
