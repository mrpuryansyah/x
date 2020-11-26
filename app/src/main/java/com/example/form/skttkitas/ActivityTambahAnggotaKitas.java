package com.example.form.skttkitas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.x.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivityTambahAnggotaKitas extends AppCompatActivity {
    Context context = ActivityTambahAnggotaKitas.this;

    final Calendar myCalendar = Calendar.getInstance();
    EditText etno, etnama, etnik, ettempatlahir, ettanggallahir, ethubungan;
    RadioGroup rgjenkel;
    Button btntambah;
    String no = "", nama = "", nik = "", tempatlahir = "", tanggallahir = "", hubungan = "", jenkel = "";

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
        setTitle("Tambah Anggota Keluarga");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_tambah_anggota_kitas);

        etno = findViewById(R.id.etno);
        etnama = findViewById(R.id.etnama);
        etnik = findViewById(R.id.etnik);
        ettempatlahir = findViewById(R.id.ettempatlahir);
        ethubungan = findViewById(R.id.ethubungan);
        rgjenkel = findViewById(R.id.rgjenkel);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tanggallahir = String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth);
                updateLabel();
            }
        };

        ettanggallahir = findViewById(R.id.ettanggallahir);
        ettanggallahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btntambah = findViewById(R.id.btntambah);
        btntambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = rgjenkel.getCheckedRadioButtonId();
                View radioButton = rgjenkel.findViewById(radioButtonID);
                int idx = rgjenkel.indexOfChild(radioButton);
                RadioButton r = (RadioButton) rgjenkel.getChildAt(idx);

                no = etno.getText().toString();
                nama = etnama.getText().toString();
                nik = etnik.getText().toString();
                tempatlahir = ettempatlahir.getText().toString();
                hubungan = ethubungan.getText().toString();
                jenkel = r.getText().toString();

                if (!no.equalsIgnoreCase("") && !nama.equalsIgnoreCase("") && !nik.equalsIgnoreCase("") && !tempatlahir.equalsIgnoreCase("") &&
                        !tanggallahir.equalsIgnoreCase("") && !hubungan.equalsIgnoreCase("") && !jenkel.equalsIgnoreCase("")) {
                    new AlertDialog.Builder(context)
                            .setTitle("Perhatian")
                            .setMessage("Yakin tambah data?")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    intent.putExtra("no", no);
                                    intent.putExtra("nama", nama);
                                    intent.putExtra("nik", nik);
                                    intent.putExtra("tempatlahir", tempatlahir);
                                    intent.putExtra("tanggallahir", tanggallahir);
                                    intent.putExtra("hubungan", hubungan);
                                    intent.putExtra("jenkel", jenkel);
                                    finish();
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
                } else {
                    Toast.makeText(context, "Data belum lengkap!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ettanggallahir.setText(sdf.format(myCalendar.getTime()));
        Log.e("Tanggallahir", tanggallahir);
    }
}