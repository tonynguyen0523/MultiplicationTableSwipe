package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void letsPlay(View view){
        Intent playIntent = new Intent(this, PlayActivity.class);
        startActivity(playIntent);
        Toast.makeText(this,"LETS PLAY!", Toast.LENGTH_SHORT).show();
    }
}
