package com.example.form.pengajuanpindahwni;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.x.R;

public class ActivityInputPindah extends AppCompatActivity {
    Context context = ActivityInputPindah.this;

    LinearLayout llprosedur, llinput, llriwayat;
    TextView btninput, btnbatal;

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
        setTitle("Pengajuan Pindah Penduduk (WNI)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_input_pindah);

        llprosedur = findViewById(R.id.llprosedur);
        llinput = findViewById(R.id.llinput);
        llriwayat = findViewById(R.id.llriwayat);

        btninput = findViewById(R.id.btninput);
        btninput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llinput.setVisibility(View.VISIBLE);
                llprosedur.setVisibility(View.GONE);
                llriwayat.setVisibility(View.GONE);
            }
        });

        btnbatal = findViewById(R.id.btnbatal);
        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llinput.setVisibility(View.GONE);
                llriwayat.setVisibility(View.VISIBLE);
                llprosedur.setVisibility(View.VISIBLE);
            }
        });
    }
}
