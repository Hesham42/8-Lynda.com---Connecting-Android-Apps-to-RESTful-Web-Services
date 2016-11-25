package com.example.guinness.flowerwithgui;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.guinness.flowerwithgui.Model.Flower;
import com.example.guinness.flowerwithgui.Parse.XMLPasrsing;
import com.example.guinness.flowerwithgui.http.HttpManger;

import java.util.ArrayList;
import java.util.List;

public class ParsingBYXML extends ListActivity {
    public static final String PHOTOS_BASE_URL =
            "http://services.hanselandpetal.com/photos/";
    String Url = "http://services.hanselandpetal.com/feeds/flowers.xml";
    ProgressBar Pb;
    //this arrayList to all asynctask
    List<MyTask> tasks;

    List<Flower> flowerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing_byxml);

        Pb = (ProgressBar) findViewById(R.id.pb1);
        Pb.setVisibility(View.INVISIBLE);
        //declaration of the task to know how many tasks doing
        tasks = new ArrayList<MyTask>();
    }

    public void ClassXmlParssing(View view) {

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
            flowerList = XMLPasrsing.parseFeed(content);
//
//            for (Flower flower : flowerList) {
//                try {
//                    String imageUrl = PHOTOS_BASE_URL + flower.getPhoto();
//                    InputStream in = (InputStream) new URL(imageUrl).getContent();
//                    Bitmap bitmap = BitmapFactory.decodeStream(in);
//                    flower.setBitmap(bitmap);
//                    in.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
            return flowerList;
        }


        @Override
        protected void onPostExecute(List<Flower> result) {
            tasks.remove(this);
            if (tasks.size() == 0) {
                Pb.setVisibility(View.INVISIBLE);
            }
            if (result == null) {
                Toast.makeText(ParsingBYXML.this, "Can't connect with the wepservice ", Toast.LENGTH_LONG).show();
                return;
            }
            flowerList = result;
            updateDisplay();
        }

    }
}



