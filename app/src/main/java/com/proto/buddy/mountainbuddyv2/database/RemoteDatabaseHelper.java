package com.proto.buddy.mountainbuddyv2.database;

import android.app.DownloadManager;
import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RemoteDatabaseHelper {

    private static RemoteDatabaseHelper instance;

    private static Context context;
    private static String TAG = "---------------- RemoteDatabaseHelper ";
    public static final String URL_REMOTE_SERVER = "http://78.47.217.227/mountainbuddy/api/v1/";
    public static final String URL_REMOTE_SERVER_ROUTES = "http://78.47.217.227/mountainbuddy/api/v1/routes";
    private static RequestQueue queue;

    private static final int ONE_MB = 1024 * 1024;

    private static Gson gson;

    public RemoteDatabaseHelper (Context context){
        this.context = context.getApplicationContext();
        gson = new Gson();
//        queue = Volley.newRequestQueue(context);
        Cache cache = new DiskBasedCache(context.getCacheDir(), ONE_MB);
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

    }

    public static synchronized RemoteDatabaseHelper getInstance(Context context){
        if(instance == null){
            instance = new RemoteDatabaseHelper(context);
        }
        return instance;
    }

    /**
     * sends HTTP GET request to given url
     * @param url url of table
     */
    public static void GET(String url){

        //TODO: need GSON or other library to parse JSONObjects

        Log.d(TAG, "starting get request to:  " + url);

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>()
            {
                @Override
                public void onResponse(JSONArray response) {
                    // display response
                    Log.d(TAG, response.toString());
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "ERROR: " + error.getMessage());
                }
            }
        );

        // add it to the RequestQueue
        queue.add(getRequest);
    }

    /**
     * sends HTTP GET to given url/:id
     * @param url url of table
     * @param id id of object to be retrieved
     * @param classOfObject class of the object
     */
    public static void GETONE(String url, int id, final Class classOfObject){
        Log.d(TAG, "Starting single request on " + url + "/" + id);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url + "/" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TODO: parse JSONObject, somehow pass it to requester
                        Log.d(TAG, response.toString());
                        Object responseObject = gson.fromJson(response.toString(), classOfObject);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.getMessage());
                    }
                }
        );
        queue.add(req);
    }

    /**
     * sends HTTP POST request to given url, passing the given object
     * @param url url of table
     * @param obj object to be posted
     * @throws JSONException if given object can not be parsed to valid JSON
     */
    public static void POST(String url, Object obj) throws JSONException {
        //parse given object to JSONObject and send as body...
        JSONObject jsonToSend = new JSONObject(gson.toJson(obj));
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonToSend,
            new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response) {
                    // response
                    Log.d("Response", response.toString());
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                    Log.d(TAG, "ERROR: " + error.getMessage());
                }
            }
        );
//        {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("name", "Alif");
//                params.put("domain", "http://itsalif.info");
//
//                return params;
//            }
//        };
        queue.add(postRequest);
    }

    /**
     * sends HTTP PUT request on given url/:id
     * @param url url of table
     * @param id id of object to be updated
     * @param obj new version of old object
     * @throws JSONException if the passed object can not be parsed to valid JSON
     */
    public static void PUT(String url, int id, Object obj) throws JSONException {
        JSONObject jsonToSend = new JSONObject(gson.toJson(obj));
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonToSend,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "ERROR: " + error.getMessage());
                    }
                }
        );
//        {
//
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String> ();
//                params.put("name", "Alif");
//                params.put("domain", "http://itsalif.info");
//
//                return params;
//            }
//
//        };

        queue.add(putRequest);
    }

    /**
     * sends HTTP DELETE request to given url/:id
     * @param url url (path of table to be removed from)
     * @param id id of object to be removed
     */
    public static void DELETE(String url, int id){
        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    // response
                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error.
                    Log.d(TAG, "ERROR: " + error.getMessage());
                }
            }
        );
        queue.add(dr);
    }

    /*public class HttpAsyncTask extends AsyncTask<String, Void, String> {
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
    }*/
}

