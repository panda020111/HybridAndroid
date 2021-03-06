package com.example.hybridwebview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn_1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://m.baidu.com");
                startActivity(intent);
            }
        });

        btn = findViewById(R.id.btn_2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://192.168.31.102:8080/");
//                intent.putExtra("url", "http://172.17.61.133:8080/");
                startActivity(intent);
            }
        });
    }
}
