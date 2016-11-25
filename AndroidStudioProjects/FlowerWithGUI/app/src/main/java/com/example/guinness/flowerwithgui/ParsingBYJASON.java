package com.example.guinness.flowerwithgui;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.guinness.flowerwithgui.Model.Flower;
import com.example.guinness.flowerwithgui.Parse.JASONPasrses;
import com.example.guinness.flowerwithgui.http.HttpManger;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class ParsingBYJASON extends ListActivity {
    public static final String PHOTOS_BASE_URL =
            "http://services.hanselandpetal.com/photos/";
    String Url = "http://services.hanselandpetal.com/feeds/flowers.json";
    ProgressBar Pb;
    //this arrayList to all asynctask
    List<MyTask> tasks;

    List<Flower> flowerList;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing_byjason);
        Pb = (ProgressBar) findViewById(R.id.progressBar);
        Pb.setVisibility(View.INVISIBLE);
        //declaration of the task to know how many tasks doing
        tasks = new ArrayList<MyTask>();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void CalassJasonParssing(View view) {

        if (isOnline()) {
//   http://services.hanselandpetal.com/feeds/
            //for any code start this link;
            requestData(Url);
        } else {

            Toast.makeText(this, "network isn't available", Toast.LENGTH_LONG).show();
        }

    }


    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);

    }

    protected void updateDisplay() {
//user FlowerAdapter to display data
        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.itemtool, flowerList);
        setListAdapter(adapter);

    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ParsingBYJASON Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class MyTask extends AsyncTask<String, String, List<Flower>> {


        @Override
        protected void onPreExecute() {
            if (tasks.size() == 0) {
                Pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<Flower> doInBackground(String... strings) {
            String content = HttpManger.getData(strings[0]);
            flowerList = JASONPasrses.parseFeed(content);

            return flowerList;
        }

        @Override
        protected void onPostExecute(List<Flower> result) {
            tasks.remove(this);
            if (tasks.size() == 0) {
                Pb.setVisibility(View.INVISIBLE);
            }
            if (result == null) {
                Toast.makeText(ParsingBYJASON.this, "Can't connect with the wepservice ", Toast.LENGTH_LONG).show();
                return;
            }
            flowerList = result;
            updateDisplay();
        }

    }
}
