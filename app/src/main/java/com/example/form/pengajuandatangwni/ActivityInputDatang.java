package com.example.form.pengajuandatangwni;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.x.R;
import com.example.x.Utils.ActivityPilihWilayah;

public class ActivityInputDatang extends AppCompatActivity {
    Context context = ActivityInputDatang.this;

    TextView btnbatal, btninput, tvfilekk, tvfilektp, tvfilesuratnikah, tvfileakta, tvfileskpwni;
    Button btnprov, btnkab, btnkec, btnkel, btnuploadkk, btnuploadktp, btnuploadsuratnikah, btnuploadakta, btnuploadskpwni;
    EditText etnoskpwni, etnikpemohon, etnamapemohon, etjmlanggota, etnokk, etalamat, etrt, etrw, etkodepos, etemail, etnohp;
    LinearLayout llprosedur, llinput, llriwayat;

    String idprov = "", namaprov = "", idkab = "", namakab = "", idkec = "", namakec = "", idkel = "", namakel = "";
    String noskpwni = "", nikpemohon = "", namapemohon = "", jmlanggota = "", nokk = "", alamat = "", rt = "", rw = "", kodepos = "", email = "", nohp = "";
    String filekk = "", filektp = "", filesuratnikah = "", fileakta = "", fileskpwni = "";

    public static final int REQCODE_PROV = 1, REQCODE_KAB = 2, REQCODE_KEC = 3, REQCODE_KEL = 4;


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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQCODE_PROV) {
            if (resultCode == RESULT_OK) {
                idprov = data.getStringExtra("id");
                namaprov = data.getStringExtra("value");

                btnprov.setText(namaprov);
            }
        }
        if (requestCode == REQCODE_KAB) {
            if (resultCode == RESULT_OK) {
                idkab = data.getStringExtra("id");
                namakab = data.getStringExtra("value");

                btnkab.setText(namakab);
            }
        }
        if (requestCode == REQCODE_KEC) {
            if (resultCode == RESULT_OK) {
                idkec = data.getStringExtra("id");
                namakec = data.getStringExtra("value");

                btnkec.setText(namakec);
            }
        }
        if (requestCode == REQCODE_KEL) {
            if (resultCode == RESULT_OK) {
                idkel = data.getStringExtra("id");
                namakel = data.getStringExtra("value");

                btnkel.setText(namakel);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Pengajuan Datang Penduduk (WNI)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_input_datang);

        btnbatal = findViewById(R.id.btnbatal);
        btninput = findViewById(R.id.btninput);
        llinput = findViewById(R.id.llinput);
        llprosedur = findViewById(R.id.llprosedur);
        llriwayat = findViewById(R.id.llriwayat);

        tvfilekk = findViewById(R.id.tvfilekk);
        tvfilektp = findViewById(R.id.tvfilektp);
        tvfilesuratnikah = findViewById(R.id.tvfilesuratnikah);
        tvfileakta = findViewById(R.id.tvfileakta);
        tvfileskpwni = findViewById(R.id.tvfileskpwni);

        btnuploadkk = findViewById(R.id.btnuploadkk);
        btnuploadktp = findViewById(R.id.btnuploadktp);
        btnuploadsuratnikah = findViewById(R.id.btnuploadsuratnikah);
        btnuploadakta = findViewById(R.id.btnuploadakta);
        btnuploadskpwni = findViewById(R.id.btnuploadskpwni);
        etnoskpwni = findViewById(R.id.etnoskpwni);
        etnikpemohon = findViewById(R.id.etnikpemohon);
        etnamapemohon = findViewById(R.id.etnamapemohon);
        etjmlanggota = findViewById(R.id.etjmlanggota);
        etnokk = findViewById(R.id.etnokk);
        etalamat = findViewById(R.id.etalamat);
        etrt = findViewById(R.id.etrt);
        etrw = findViewById(R.id.etrw);
        etkodepos = findViewById(R.id.etkodepos);
        etemail = findViewById(R.id.etemail);
        etnohp = findViewById(R.id.etnohp);

        btnprov = findViewById(R.id.btnprov);
        btnprov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityPilihWilayah.class);
                i.putExtra("requestCode", REQCODE_PROV);
                startActivityForResult(i, REQCODE_PROV);
            }
        });
        btnkab = findViewById(R.id.btnkab);
        btnkab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityPilihWilayah.class);
                intent.putExtra("requestCode", REQCODE_KAB);
                intent.putExtra("no_prop", idprov);
                startActivityForResult(intent, REQCODE_KAB);

            }
        });
        btnkec = findViewById(R.id.btnkec);
        btnkec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityPilihWilayah.class);
                intent.putExtra("requestCode", REQCODE_KEC);
                intent.putExtra("no_prop", idprov);
                intent.putExtra("no_kab", idkab);
                startActivityForResult(intent, REQCODE_KEC);
            }
        });
        btnkel = findViewById(R.id.btnkel);
        btnkel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityPilihWilayah.class);
                intent.putExtra("requestCode", REQCODE_KEL);
                intent.putExtra("no_prop", idprov);
                intent.putExtra("no_kab", idkab);
                intent.putExtra("no_kec", idkec);
                startActivityForResult(intent, REQCODE_KEL);
            }
        });

        btninput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llinput.setVisibility(View.VISIBLE);
                llprosedur.setVisibility(View.GONE);
                llriwayat.setVisibility(View.GONE);
            }
        });

        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llinput.setVisibility(View.GONE);
                llprosedur.setVisibility(View.VISIBLE);
                llriwayat.setVisibility(View.VISIBLE);
            }
        });
    }
}
