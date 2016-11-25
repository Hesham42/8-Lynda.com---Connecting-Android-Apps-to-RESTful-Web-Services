
package com.example.guinness.flowerwithgui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guinness.flowerwithgui.Model.Flower;
import com.example.guinness.flowerwithgui.Parse.JASONPasrses;
import com.example.guinness.flowerwithgui.http.HttpManger;

import java.util.ArrayList;
import java.util.List;

public class ParsingBYJASON extends AppCompatActivity {
    String Url="http://services.hanselandpetal.com/feeds/flowers.json";
    TextView output;
    ProgressBar Pb;
    //this arrayList to all asynctask
    List<MyTask> tasks;

    List<Flower> flowerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing_byjason);
        output=(TextView)findViewById(R.id.textJASONPARSING);
        output.setMovementMethod(new ScrollingMovementMethod());
        Pb = (ProgressBar) findViewById(R.id.progressBar);
        Pb.setVisibility(View.INVISIBLE);
        //declaration of the task to know how many tasks doing
        tasks = new ArrayList<MyTask>();
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
    private class MyTask extends AsyncTask<String,String,String> {


        @Override
        protected void onPreExecute() {
            if(tasks.size()==0)
            {
            Pb.setVisibility(View.VISIBLE);
            }
        tasks.add(this);
        }

        @Override
        protected String doInBackground(String... strings) {
           String content = HttpManger.getData(strings[0]);
            return content;
        }



        @Override
        protected void onPostExecute(String result) {
        tasks.remove(this);
            if (tasks.size()==0)
            {
            Pb.setVisibility(View.INVISIBLE);
            }
        if (result==null)
        {
         Toast.makeText(ParsingBYJASON.this,"Can't connect with the wepservice ",Toast.LENGTH_LONG).show();
            return;
        }
        flowerList= JASONPasrses.parseFeed(result);
            updateDisplay();
        }

    }
}
