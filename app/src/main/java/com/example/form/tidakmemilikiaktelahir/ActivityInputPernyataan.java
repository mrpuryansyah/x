package com.example.form.tidakmemilikiaktelahir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.x.R;

public class ActivityInputPernyataan extends AppCompatActivity {
    Context context = ActivityInputPernyataan.this;

    EditText etnik, etnama, etalamat, etagama, etalmarhum, etpekerjaan;
    TextView tvpreview, tvkirim;
    String nik = "", nama = "", alamat = "", agama = "", almarhum = "", pekerjaan = "";

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
        setTitle("Buat Surat Pernyataan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_input_pernyataan);

        etnik = findViewById(R.id.etnik);
        etnama = findViewById(R.id.etnama);
        etalamat = findViewById(R.id.etalamat);
        etagama = findViewById(R.id.etagama);
        etalmarhum = findViewById(R.id.etalmarhum);
        etpekerjaan = findViewById(R.id.etpekerjaan);

        tvpreview = findViewById(R.id.tvpreview);
        tvpreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nik = etnik.getText().toString();
                nama = etnama.getText().toString();
                alamat = etalamat.getText().toString();
                agama = etagama.getText().toString();
                pekerjaan = etpekerjaan.getText().toString();
                almarhum = etalmarhum.getText().toString();
                if (!nik.equals("") || !nama.equals("") || !alamat.equals("") || !agama.equals("") || !almarhum.equals("")) {
                    Intent i = new Intent(context, ActivityPriviewPernyataanTidakMemilikiAkta.class);
                    i.putExtra("nik", nik);
                    i.putExtra("nama", nama);
                    i.putExtra("alamat", alamat);
                    i.putExtra("agama", agama);
                    i.putExtra("pekerjaan", pekerjaan);
                    i.putExtra("almarhum", almarhum);
                    startActivity(i);
                } else {
                    Toast.makeText(context, "Data belum lengkap", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvkirim = findViewById(R.id.tvkirim);
        tvkirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nik = etnik.getText().toString();
                nama = etnama.getText().toString();
                alamat = etalamat.getText().toString();
                agama = etagama.getText().toString();
                pekerjaan = etpekerjaan.getText().toString();
                almarhum = etalmarhum.getText().toString();

                if (nik.equals("") || nama.equals("") || alamat.equals("") || agama.equals("") || almarhum.equals("")) {

                } else {

                }
            }
        });
    }
}