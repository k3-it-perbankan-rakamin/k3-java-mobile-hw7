package com.example.myrakamin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an Intent to start HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);

        // Start HomeActivity
        startActivity(intent);

        // Finish MainActivity if you want it removed from the back stack
        finish();
    }
}
