package com.example.form.pengajuanktpel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.x.R;

public class ActivityKtpEl extends AppCompatActivity {
    Context context = ActivityKtpEl.this;
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
        setTitle("Beranda Pengajuan KTP-EL");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_ktp_el);

        btnajukan = findViewById(R.id.btnajukan);
        btnajukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InputPengajuanKtpEl.class);
                startActivity(i);
            }
        });
    }
}
