package com.example.form.pengajuanktpel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.example.x.R;
import com.example.x.Utils.ActivityFullscreenImageV2;
import com.example.x.Utils.ActivityPilihWilayah;
import com.example.x.Utils.ErrorResponse;
import com.example.x.Utils.LogConsole;
import com.example.x.Utils.MultipartUtilityV5;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class InputPengajuanKtpEl extends AppCompatActivity {
    Context context = InputPengajuanKtpEl.this;
    boolean satu = true, dua = true, tiga = true, empat = true;
    TextView tvnamafile;
    EditText etnik, etnama, etket, etnikpemohon, etnamapemohon, ettlp;

    String nik = "", nama = "", ket = "", nikpemohon = "", namapemohon = "", tlp = "", namafile = "";
    String no_kec = "", nama_kec = "";

    Button btnkec, btnajukan, btnupload, btnbatal;
    File fileKtp;

    public static final int REQCODE_KEC = 10, REQUEST_CODE_KTP = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQCODE_KEC) {
            if (resultCode == RESULT_OK) {
                no_kec = data.getStringExtra("id");
                nama_kec = data.getStringExtra("value");

                btnkec.setText(nama_kec);
            }
        }
        if (requestCode == REQUEST_CODE_KTP) {
            if (resultCode == RESULT_OK) {
                Image image = ImagePicker.getFirstImageOrNull(data);
                File fileTemp = new File(image.getPath());
                try {
                    fileKtp = new Compressor(context).setQuality(60).compressToFile(fileTemp);
                    new UploadFoto(fileKtp).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Perhatian")
                        .setMessage("Data yang sudah diinput akan dihapus, yakin ingin keluar?")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Pengajuan KTP-El");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_input_pengajuan_ktp_el);

        etnik = findViewById(R.id.etnik);
        etnama = findViewById(R.id.etnama);
        etket = findViewById(R.id.etket);
        etnikpemohon = findViewById(R.id.etnikpemohon);
        etnamapemohon = findViewById(R.id.etnamapemohon);
        ettlp = findViewById(R.id.ettlp);
        tvnamafile = findViewById(R.id.tvnamafile);

        btnkec = findViewById(R.id.btnkec);
        btnkec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityPilihWilayah.class);
                i.putExtra("kec", 1);
                startActivityForResult(i, REQCODE_KEC);
            }
        });

        btnajukan = findViewById(R.id.btnajukan);
        btnajukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validasi = true;
                nik = etnik.getText().toString();
                if (nik.equalsIgnoreCase("")) {
                    validasi = false;
                    etnik.setError("NIK Belum anda isi");
                }
                nama = etnama.getText().toString();
                if (nama.equalsIgnoreCase("")) {
                    validasi = false;
                    etnama.setError("Nama Belum anda isi");
                }

                ket = etket.getText().toString();
                if (ket.equalsIgnoreCase("")) {
                    validasi = false;
                    etket.setError("Keterangan Belum anda isi");
                }
                nikpemohon = etnikpemohon.getText().toString();
                if (nikpemohon.equalsIgnoreCase("")) {
                    validasi = false;
                    etnikpemohon.setError("NIK pemohon Belum anda isi");
                }
                namapemohon = etnamapemohon.getText().toString();
                if (namapemohon.equalsIgnoreCase("")) {
                    validasi = false;
                    etnamapemohon.setError("Nama pemohon Belum anda isi");
                }
                tlp = ettlp.getText().toString();
                if (tlp.equalsIgnoreCase("")) {
                    validasi = false;
                    ettlp.setError("tlp Belum anda isi");
                }
                if (no_kec.equalsIgnoreCase("") || nama_kec.equalsIgnoreCase("")) {
                    validasi = false;
                    Toast.makeText(context, "Kecamatan belum anda pilih", Toast.LENGTH_SHORT).show();
                }
                namafile = tvnamafile.getText().toString();
                if (namafile.equalsIgnoreCase("Tidak ada file terpilih")) {
                    validasi = false;
                    Toast.makeText(context, "Anda belum upload persyaratan!", Toast.LENGTH_SHORT).show();
                }

                if (validasi) {
                    new AlertDialog.Builder(context)
                            .setTitle("Perhatian")
                            .setMessage("Pastikan data yang Anda masukkan sudah benar. Simpan data?")
                            .setCancelable(false)
                            .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    simpanData();
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
                    Toast.makeText(context, "Periksa Kembali isian anda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnupload = findViewById(R.id.btnupload);
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etnikpemohon.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Tambahkan nik terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewLayout = inflater.inflate(R.layout.layout_pilih_kamera_galeri, null);
                TextView tvKamera = viewLayout.findViewById(R.id.tvKamera);
                TextView tvGaleri = viewLayout.findViewById(R.id.tvGaleri);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(viewLayout);
                final AlertDialog alert = builder.create();

                tvKamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        ImagePicker.cameraOnly().imageDirectory("TangerangLive").start(InputPengajuanKtpEl.this, REQUEST_CODE_KTP);
                    }
                });
                tvGaleri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        ImagePicker.create(InputPengajuanKtpEl.this)
                                .returnMode(ReturnMode.GALLERY_ONLY) // set whether pick and / or camera action should return immediate result or not.
                                .toolbarImageTitle("Pilih gambar") // image selection title
                                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                                .includeVideo(false) // Show video on image picker
                                .single() // single mode
                                .enableLog(true) // disabling log
                                .start(REQUEST_CODE_KTP); // start image picker activity with request code
                    }
                });
                alert.setCancelable(true);
                alert.show();
            }
        });

        btnbatal = findViewById(R.id.btnbatal);
        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Perhatian")
                        .setMessage("Data yang sudah diinput akan dihapus, yakin ingin keluar?")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
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
            }
        });
    }

    void simpanData() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Tunggu sebentar...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://service-tlive.tangerangkota.go.id/services/tlive/kependudukan/pengajuan_ktp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponAjukan", response);
                progressDialog.dismiss();
                try {
                    JSONObject respon = new JSONObject(response);
                    Boolean success = respon.getBoolean("success");
                    String message = respon.getString("message");
                    if (success) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent();
                        setResult(RESULT_OK, i);
                        finish();
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                    new ErrorResponse(context.getApplicationContext()).getDefaultErrorMessage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ErrorResponse(context.getApplicationContext()).showVolleyError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> input = new HashMap<>();
                input.put("nik", nik);
                input.put("nama", nama);
                input.put("ket", ket);
                input.put("nikpemohon", nikpemohon);
                input.put("namapemohon", namapemohon);
                input.put("tlp", tlp);
                input.put("namafile", namafile);
                input.put("no_kec", no_kec);
                input.put("nama_kec", nama_kec);
                return input;

            }

            //            P1 = "xlzh5v6sgrpvqs3os86x2s298fb04z6txo9cntx0";
//            U1 = "r35t4p1";
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + Base64.encodeToString(String.format("%s:%s", "r35t4p1", "xlzh5v6sgrpvqs3os86x2s298fb04z6txo9cntx0").getBytes(), Base64.DEFAULT));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(150000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    protected class UploadFoto extends AsyncTask<String, Integer, String> {

        File file;
        ProgressDialog progressDialog;

        public UploadFoto(File file) {
            this.file = file;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Tunggu sebentar...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = "https://service-tlive.tangerangkota.go.id/services/tlive/kependudukan/upload_ktp_pengajuan";
            MultipartUtilityV5 multipart = null;
            String response = "";
            try {
                multipart = new MultipartUtilityV5(requestURL, charset, new MultipartUtilityV5.FileUploadListener() {
                    @Override
                    public void onUpdateProgress(int percentage, long kb) {
                        publishProgress(percentage);
                    }

                    @Override
                    public boolean isCanceled() {
                        return false;
                    }
                }, "r35t4p12", "8540c5ef27b4afdb197405dc551ce5b5bfcb73bb2");

                multipart.addFormField("nik", nik);
                multipart.addFormField("nik_pemohon", nikpemohon);
                multipart.addFilePart("file", file);

                response = multipart.finish();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            new LogConsole("ResponUpload", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                if (jsonObject.has("success")) {
                    if (jsonObject.getBoolean("success")) {

                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        String url = jsonObjectData.getString("url");
                        String filename = jsonObjectData.getString("filename");

                        tvnamafile.setText(filename);
                        tvnamafile.setTextColor(getResources().getColor(R.color.blue));
                        tvnamafile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(context, ActivityFullscreenImageV2.class).putExtra("url", url));

                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, new ErrorResponse(context).getDefaultErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress((int) (values[0]));
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.dismiss();
        }
    }
}
