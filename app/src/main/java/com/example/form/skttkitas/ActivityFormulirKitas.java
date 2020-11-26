package com.example.form.skttkitas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.x.R;

import java.util.ArrayList;

public class ActivityFormulirKitas extends AppCompatActivity {
    Context context = ActivityFormulirKitas.this;

    LinearLayout llinput, llriwayat, llanggota;
    TextView btninput, btnbatal, btntambah, tvket;
    ModelData data;
    AdapterAnggota adapter;
    ArrayList<ModelData> arrayList;
    RecyclerView recycler_view_orang;

    String no = "", nama = "", nik = "", tempatlahir = "", tanggallahir = "", hubungan = "", jenkel = "";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            no = data.getStringExtra("no");
            nama = data.getStringExtra("nama");
            nik = data.getStringExtra("nik");
            tempatlahir = data.getStringExtra("tempatlahir");
            tanggallahir = data.getStringExtra("tanggallahir");
            hubungan = data.getStringExtra("hubungan");
            jenkel = data.getStringExtra("jenkel");

            arrayList.add(new ModelData(no, nama, nik, tempatlahir, tanggallahir, hubungan, jenkel));
            tvket.setVisibility(View.GONE);
            Log.e("array", arrayList.toString() + "");

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.list_kitas_anggota, null);
            TextView tvnama = viewLayout.findViewById(R.id.tvnama);
            ImageView ivdelete = viewLayout.findViewById(R.id.ivdelete);
            tvnama.setText(nama);
            ivdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String find = tvnama.getText().toString();
                    new AlertDialog.Builder(context)
                            .setTitle("Perhatian")
                            .setMessage("Yakin ingin hapus anggota?")
                            .setCancelable(false)
                            .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int n) {
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        if (arrayList.get(i).nama.equalsIgnoreCase(find)) {
                                            arrayList.remove(i);
                                            llanggota.removeView(viewLayout);
                                        }
                                    }
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
            });

            viewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String find = tvnama.getText().toString();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).nama.equalsIgnoreCase(find)) {

                        }
                    }
                }
            });
            llanggota.addView(viewLayout);
        }
    }

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
        setTitle("FORMULIR PENDAFTARAN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_formulir_kitas);

        llanggota = findViewById(R.id.llanggota);
        tvket = findViewById(R.id.tvket);

        llinput = findViewById(R.id.llinput);
        llriwayat = findViewById(R.id.llriwayat);
        arrayList = new ArrayList<>();

        btninput = findViewById(R.id.btninput);
        btninput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llinput.setVisibility(View.VISIBLE);
                llriwayat.setVisibility(View.GONE);
            }
        });

        btnbatal = findViewById(R.id.btnbatal);
        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llriwayat.setVisibility(View.VISIBLE);
                llinput.setVisibility(View.GONE);
            }
        });

        btntambah = findViewById(R.id.btntambah);
        btntambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityTambahAnggotaKitas.class);
                startActivityForResult(i, 100);
            }
        });
    }

    class ModelData {

        //        String id;
        String nomer;
        String nama;
        String nik;
        String jeniskelamin;
        String tempatlahir;
        String tanggallahir;
        String hubungandenganpemohon;

        public ModelData(String no, String nama, String nik, String tempatlahir, String tanggallahir, String hubungan, String jenkel) {
            this.nomer = no;
            this.nama = nama;
            this.nik = nik;
            this.tempatlahir = tempatlahir;
            this.tanggallahir = tanggallahir;
            this.hubungandenganpemohon = hubungan;
            this.jeniskelamin = jenkel;
        }

//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }

        public String getNomer() {
            return nomer;
        }

        public void setNomer(String nomer) {
            this.nomer = nomer;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getNik() {
            return nik;
        }

        public void setNik(String nik) {
            this.nik = nik;
        }

        public String getJeniskelamin() {
            return jeniskelamin;
        }

        public void setJeniskelamin(String jeniskelamin) {
            this.jeniskelamin = jeniskelamin;
        }

        public String getTempatlahir() {
            return tempatlahir;
        }

        public void setTempatlahir(String tempatlahir) {
            this.tempatlahir = tempatlahir;
        }

        public String getTanggallahir() {
            return tanggallahir;
        }

        public void setTanggallahir(String tanggallahir) {
            this.tanggallahir = tanggallahir;
        }

        public String getHubungandenganpemohon() {
            return hubungandenganpemohon;
        }

        public void setHubungandenganpemohon(String hubungandenganpemohon) {
            this.hubungandenganpemohon = hubungandenganpemohon;
        }

        @Override
        public String toString() {
            return "ModelData{" +
                    "nomer='" + nomer + '\'' +
                    ", nama='" + nama + '\'' +
                    ", nik='" + nik + '\'' +
                    ", jeniskelamin='" + jeniskelamin + '\'' +
                    ", tempatlahir='" + tempatlahir + '\'' +
                    ", tanggallahir='" + tanggallahir + '\'' +
                    ", hubungandenganpemohon='" + hubungandenganpemohon + '\'' +
                    '}';
        }
    }
}

