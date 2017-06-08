package com.example.regis.servicelocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
Button trackUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        trackUser =(Button)findViewById(R.id.track);
        trackUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,MyLocationTracker.class);
        Toast.makeText(getApplicationContext(), "tracking begins", Toast.LENGTH_SHORT).show();
        startService(i);

    }
}
