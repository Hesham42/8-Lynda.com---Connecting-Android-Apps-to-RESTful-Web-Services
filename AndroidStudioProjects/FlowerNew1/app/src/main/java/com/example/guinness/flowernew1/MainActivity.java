package com.example.guinness.flowernew1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guinness.flowernew1.Model.Flower;
import com.example.guinness.flowernew1.Parse.JASONPasrses;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //https://github.com/Hesham42/8-Lynda.com---Connecting-Android-Apps-to-RESTful-Web-Services
    private static String UserName = "feeduser";
    private static String Password = "feedpassword";
    TextView output;
    ProgressBar Ph;
    //this arrayList to all asynctask
    List<MyTask> tasks;

    List<Flower> flowerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        output=(TextView)findViewById(R.id.textView1);
        output.setMovementMethod(new ScrollingMovementMethod());
        Ph = (ProgressBar) findViewById(R.id.progressBar12);
        Ph.setVisibility(View.INVISIBLE);
        //declaration of the task to know how many tasks doing
        tasks = new ArrayList<MyTask>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_do_task) {

            if (isOnline()) {
//   http://services.hanselandpetal.com/feeds/
                //for any code start this link
//                requestData("http://services.hanselandpetal.com/feeds/flowers.xml");
//                requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                requestData("http://services.hanselandpetal.com/secure/flowers.json");
            } else {

                Toast.makeText(this, "network isn't available", Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);

        /*                for(int i=0;i<=100;i++)
//                updateDisplay(" Task done " + i +" ");


// this is serial processing
//     task.execute("hesaham","hesham2","hesham3");

            take about paraller processing
*/
    }

    protected void updateDisplay() {
        if (flowerList != null) {

            for (Flower flower : flowerList) {

                output.append("all information you need to know " + flower.getName() + "\n");
                output.append("flower Name   " + flower.getName() + "\n");
                output.append("flower id       " + flower.getProudctID() + "\n");
                output.append("flower category  " + flower.getCatagory() + "\n");
                output.append("flower insertuction  " + flower.getInstrutions() + "\n");
                output.append("flower photo           " + flower.getPhoto() + "\n");
                output.append("flower price           " + flower.getPrice() + "\n");
                output.append("all information you need to know ");


            }

        }
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

    private class MyTask extends AsyncTask<String, String, String> {
        //that onPreExecute() will be run before doin
        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");

            if (tasks.size() == 0) {
                Ph.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0], UserName, Password);
            return content;
        }

        /**
         * after doing in background  the return type of doInBackground
         */

        @Override
        protected void onPostExecute(String result) {
            tasks.remove(this);
            if (tasks.size() == 0) {
                Ph.setVisibility(View.INVISIBLE);
            }

            if (result == null) {
                Toast.makeText(MainActivity.this, "Can't connect with wepservice ", Toast.LENGTH_LONG).show();
                return;
            }
            //            flowerList = XMLPasrsing.parseFeed(result);
            flowerList = JASONPasrses.parseFeed(result);
            updateDisplay();


        }

        //this onProgressUpdate() take the value from  publishProgress()
        @Override
        protected void onProgressUpdate(String... values) {
//            updateDisplay(values[0]);
        }

    }
}