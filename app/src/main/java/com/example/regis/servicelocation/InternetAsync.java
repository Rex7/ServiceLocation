package com.example.regis.servicelocation;

import android.os.AsyncTask;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class InternetAsync extends AsyncTask<Void,Void,Boolean> {
        TransferData transferData;
        private int res=-1;
        public InternetAsync(TransferData transferData){
            this.transferData=transferData;
        }

    @Override
    protected Boolean doInBackground(Void... params) {
        HttpURLConnection  httpUrlConnection = null;
        try{
            URL url = new URL("https://www.hostinger.in/");
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            httpUrlConnection   = (HttpURLConnection)urlConnection;

            res=httpUrlConnection.getResponseCode();
            Log.v("Response code ","is "+res);
            return true;

        }
        catch(Exception ex){
            return false;
        }
        finally {
            if(httpUrlConnection!=null){
                httpUrlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        transferData.setData(aBoolean);

    }
}
