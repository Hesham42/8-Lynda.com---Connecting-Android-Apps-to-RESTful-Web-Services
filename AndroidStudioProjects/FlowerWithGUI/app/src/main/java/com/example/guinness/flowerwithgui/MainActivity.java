package com.example.guinness.flowerwithgui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
//    Button XMLButton,JasonButton,JasonSecureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        XMLButton=(Button)findViewById(R.id.XMLPARSING);
//        JasonButton=(Button)findViewById(R.id.JasonParssing);
//        JasonSecureButton=(Button)findViewById(R.id.JasonParssingSecure);
    }


public void XMLparssing (View view)
{
    Intent XML=new Intent(this,ParsingBYXML.class);
    startActivity(XML);
}
public void Jasonparssing (View view)
{
    Intent Jason=new Intent(this,ParsingBYJASON.class);
    startActivity(Jason);
}
public void jasonParssingSecure (View view)
{
    Intent JasonSecure=new Intent(this,ParsingBYJasonSECURE.class);
    startActivity(JasonSecure);
}
}
