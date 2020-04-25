package com.example.param.secretmessage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String,Void,String> {
    Context ctx;
    AlertDialog alertDialog;
    BackgroundTask(Context ctx){
        this.ctx=ctx;
    }
    @Override
    protected void onPreExecute() {
        alertDialog=new AlertDialog.Builder(ctx).create();
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url="http://10.0.2.2/webapp/enter.php";
        String retrieve_url="http://10.0.2.2/webapp/retrieve.php";
        String method=params[0];
        if (method.equals("register")) {
            String message=params[1];
            String key=params[2];
            String encrypted=params[3];
            try {
                URL url=new URL(reg_url);
                try {
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                    String data= URLEncoder.encode("OriginalMessage","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8") + "&"+ URLEncoder.encode("Password","UTF-8") + "=" + URLEncoder.encode(key,"UTF-8") + "&"+ URLEncoder.encode("Encrypted","UTF-8") + "=" + URLEncoder.encode(encrypted,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS=httpURLConnection.getInputStream();
                    IS.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return "Insertion Success";
        }
        else if (method.equals("retrieve")) {
            String encrypted=params[1];
            String key=params[2];
            try {
                URL url=new URL(retrieve_url);
                try {
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data=URLEncoder.encode("Encrypted","UTF-8") + "=" + URLEncoder.encode(encrypted,"UTF-8") + "&" +
                            URLEncoder.encode("Key","UTF-8") + "=" + URLEncoder.encode(key,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String response="";
                    String line="";
                    while ((line=bufferedReader.readLine())!=null){
                        response+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Insertion Success")) {
            Toast.makeText(ctx, "Encryption Successful", Toast.LENGTH_SHORT).show();
        }


        else if (!result.equals("")){
            Intent intent = new Intent(ctx, Message.class);
            intent.putExtra("Decrypted", result);
            intent.putExtra("key",1);
            ctx.startActivity(intent);
        }
        else{
            alertDialog.setTitle("Decryption Unsuccessful");
            alertDialog.setMessage("Incorrect Message/Key");
            alertDialog.show();
        }

    }
}
