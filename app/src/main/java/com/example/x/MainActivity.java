package com.example.x;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.form.adapter.MenuAdapter;
import com.example.form.adapter.model.SubMenu;
import com.example.form.pelaporankematian.ActivityPelaporanKematian;
import com.example.form.pengajuandatangwni.ActivityInputDatang;
import com.example.form.pengajuanktpel.ActivityKtpEl;
import com.example.form.pengajuanpindahwni.ActivityInputPindah;
import com.example.form.sinkronisasidatakependudukan.ActivitySinkDataKepen;
import com.example.form.skttkitap.ActivityFormulirKitap;
import com.example.form.skttkitas.ActivityFormulirKitas;
import com.example.form.tidakmemilikiaktelahir.ActivityDashboardPernyataanAkte;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ArrayList<SubMenu> arrayList = new ArrayList<>();
        MenuAdapter mMenuAdapter = new MenuAdapter(this, arrayList);
        mMenuAdapter.setOnItemClickListener(new MenuAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                switch (position) {
                    case 0:
                        Intent iinputpengajuan = new Intent(MainActivity.this, ActivityKtpEl.class);
                        startActivity(iinputpengajuan);
                        break;
                    case 1:
                        Intent isinkronisasi = new Intent(MainActivity.this, ActivitySinkDataKepen.class);
                        startActivity(isinkronisasi);
                        break;
                    case 2:
                        Intent iinputdatang = new Intent(MainActivity.this, ActivityInputDatang.class);
                        startActivity(iinputdatang);
                        break;
                    case 3:
                        Intent iinputpindah = new Intent(MainActivity.this, ActivityInputPindah.class);
                        startActivity(iinputpindah);
                        break;
                    case 4:
                        Intent ipelaporankematian = new Intent(MainActivity.this, ActivityPelaporanKematian.class);
                        startActivity(ipelaporankematian);
                        break;
                    case 5:
                        Intent itdkmemilikiakte = new Intent(MainActivity.this, ActivityDashboardPernyataanAkte.class);
                        startActivity(itdkmemilikiakte);
                        break;
                    case 6:
                        Intent ikitas = new Intent(MainActivity.this, ActivityFormulirKitas.class);
                        startActivity(ikitas);
                        break;
                    case 7:
                        Intent ikitap = new Intent(MainActivity.this, ActivityFormulirKitap.class);
                        startActivity(ikitap);
                        break;
                    default:
                        Toast.makeText(context, "Activity not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
        recyclerView.setAdapter(mMenuAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        arrayList.add(new SubMenu("Input KTP-EL Rusak", "", "ic_pengajuan_ktp"));
        arrayList.add(new SubMenu("Sinkronisasi Data Kependudukan", "", "ic_sinkronisasi_data"));
        arrayList.add(new SubMenu("Pengajuan Datang WNI", "", "ic_pengajuan_datang"));
        arrayList.add(new SubMenu("Pengajuan Pindah WNI", "", "ic_pengajuan_pindah"));
        arrayList.add(new SubMenu("Formulir Pelaporan Kematian", "", "ic_pelaporan_kematian"));
        arrayList.add(new SubMenu("Pernyataan Tidak Memiliki Akta", "", "ic_tdk_pny_akte"));
        arrayList.add(new SubMenu("Formulir SKTT KITAS", "", "ic_kitas"));
        arrayList.add(new SubMenu("Formulir SKTT KITAP", "", "ic_kitap"));
        mMenuAdapter.notifyDataSetChanged();
    }
}