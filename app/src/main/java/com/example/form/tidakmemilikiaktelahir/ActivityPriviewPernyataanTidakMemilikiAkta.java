package com.example.form.tidakmemilikiaktelahir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.x.R;

public class ActivityPriviewPernyataanTidakMemilikiAkta extends AppCompatActivity {
    Context context = ActivityPriviewPernyataanTidakMemilikiAkta.this;
    String nik = "", nama = "", alamat = "", agama = "", almarhum = "", pekerjaan = "";
    TextView tvnama, tvnik, tvagama, tvalamat, tvpekerjaan, tvalmarhum;

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
        setTitle("Preview");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_priview_pernyataan_tidak_memiliki_akta);

        tvnama = findViewById(R.id.tvnama);
        tvnik = findViewById(R.id.tvnik);
        tvagama = findViewById(R.id.tvagama);
        tvalamat = findViewById(R.id.tvalamat);
        tvpekerjaan = findViewById(R.id.tvpekerjaan);
        tvalmarhum = findViewById(R.id.tvalmarhum);

        Bundle exstras = getIntent().getExtras();
        nik = exstras.getString("nik");
        nama = exstras.getString("nama");
        alamat = exstras.getString("alamat");
        agama = exstras.getString("agama");
        almarhum = exstras.getString("almarhum");
        pekerjaan = exstras.getString("pekerjaan");

        tvnama.setText(nama);
        tvnik.setText(nik);
        tvagama.setText(agama);
        tvalamat.setText(alamat);
        tvpekerjaan.setText(pekerjaan);
        tvalmarhum.setText(almarhum);
    }
}