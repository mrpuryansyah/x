package com.example.form.sinkronisasidatakependudukan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.x.R;

public class ActivitySinkDataKepen extends AppCompatActivity {
    Context context = ActivitySinkDataKepen.this;
    Button btnajukan;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Beranda Sinkronisasi Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sink_data_kepen);

        btnajukan = findViewById(R.id.btnajukan);

        btnajukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivitySinkronisasiDataKependudukan.class);
                startActivityForResult(i, 100);
            }
        });
    }
}