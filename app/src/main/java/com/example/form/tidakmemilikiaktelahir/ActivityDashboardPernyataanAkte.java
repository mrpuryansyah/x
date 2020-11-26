package com.example.form.tidakmemilikiaktelahir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.x.R;

public class ActivityDashboardPernyataanAkte extends AppCompatActivity {
    Context context = ActivityDashboardPernyataanAkte.this;

    TextView tvbuat;

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
        setTitle("Beranda Surat Pernyataan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_dashboard_pernyataan_akte);

        tvbuat = findViewById(R.id.tvbuat);
        tvbuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityInputPernyataan.class);
                startActivityForResult(i, 100);
            }
        });
    }
}