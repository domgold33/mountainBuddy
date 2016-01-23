package com.proto.buddy.mountainbuddyv2.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.proto.buddy.mountainbuddyv2.conf.AppConfig;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteDatabaseHelper {

    private Context context;
    private static String TAG = "----------- RemoteDatabaseHelper ----------------";

    public RemoteDatabaseHelper (Context context){
        this.context = context.getApplicationContext();
        new HttpAsyncTask().execute("http://78.47.217.227/mbapi/routes/");
        //GET(AppConfig.URL_REMOTE_SERVER_ROUTES);
    }

    public static String GET(String urlToRead){
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return result.toString();
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, "Received!", Toast.LENGTH_LONG).show();
            Log.d("Remote DB", "****************** " + result);
        }
    }
}

