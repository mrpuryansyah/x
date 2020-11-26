package com.example.form.sinkronisasidatakependudukan;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

public class ActivitySinkronisasiDataKependudukan extends AppCompatActivity {
    Context context = ActivitySinkronisasiDataKependudukan.this;
    Boolean satu = true, dua = true;
    EditText etnokk, etnamapemohon, etket, etnohp;
    TextView tvnamafile;
    Button btnkec, btnupload, btnajukan, btnbatal;

    public static final int UPLOAD = 100, KEC = 150;
    String nokk = "", namapemohon = "", ket = "", nohp = "", nokec = "", namakec = "", namafile = "";
    File filekk;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(context)
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD) {
            if (resultCode == RESULT_OK) {
                Image image = ImagePicker.getFirstImageOrNull(data);
                File fileTemp = new File(image.getPath());
                try {
                    filekk = new Compressor(context).setQuality(60).compressToFile(fileTemp);
                    new ActivitySinkronisasiDataKependudukan.UploadFoto(filekk).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == KEC) {
            if (resultCode == RESULT_OK) {
                nokec = data.getStringExtra("id");
                namakec = data.getStringExtra("value");

                btnkec.setText(namakec);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Sinkronisasi Data Penduduk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sinkronisasi_data_kependudukan);

        etnokk = findViewById(R.id.etnokk);
        etnamapemohon = findViewById(R.id.etnamapemohon);
        etket = findViewById(R.id.etket);
        etnohp = findViewById(R.id.etnohp);
        tvnamafile = findViewById(R.id.tvnamafile);
        btnkec = findViewById(R.id.btnkec);
        btnkec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityPilihWilayah.class);
                i.putExtra("kec", 1);
                startActivityForResult(i, KEC);
            }
        });

        btnupload = findViewById(R.id.btnupload);
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewLayout = inflater.inflate(R.layout.layout_pilih_kamera_galeri, null);
                TextView tvKamera = viewLayout.findViewById(R.id.tvKamera);
                TextView tvGaleri = viewLayout.findViewById(R.id.tvGaleri);

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setView(viewLayout);
                final androidx.appcompat.app.AlertDialog alert = builder.create();

                tvKamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        ImagePicker.cameraOnly().imageDirectory("TangerangLive").start(ActivitySinkronisasiDataKependudukan.this, UPLOAD);
                    }
                });
                tvGaleri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        ImagePicker.create(ActivitySinkronisasiDataKependudukan.this)
                                .returnMode(ReturnMode.GALLERY_ONLY) // set whether pick and / or camera action should return immediate result or not.
                                .toolbarImageTitle("Pilih gambar") // image selection title
                                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                                .includeVideo(false) // Show video on image picker
                                .single() // single mode
                                .enableLog(true) // disabling log
                                .start(UPLOAD); // start image picker activity with request code
                    }
                });
                alert.setCancelable(true);
                alert.show();
            }
        });

        btnajukan = findViewById(R.id.btnajukan);
        btnajukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validasi = true;
                nokk = etnokk.getText().toString();
                if (nokk.equalsIgnoreCase("")) {
                    validasi = false;
                    etnokk.setError("Belum anda isi!");
                }
                namapemohon = etnamapemohon.getText().toString();
                if (namapemohon.equalsIgnoreCase("")) {
                    validasi = false;
                    etnamapemohon.setError("Belum anda isi!");
                }
                ket = etket.getText().toString();
                if (ket.equalsIgnoreCase("")) {
                    validasi = false;
                    etket.setError("Belum anda isi!");
                }
                nohp = etnohp.getText().toString();
                if (nohp.equalsIgnoreCase("")) {
                    validasi = false;
                    etnohp.setError("Belum anda isi!");
                }
                if (nokec.equalsIgnoreCase("") || namakec.equalsIgnoreCase("")) {
                    validasi = false;
                    Toast.makeText(context, "Anda belum memilih kecamatan!", Toast.LENGTH_SHORT).show();
                }
                namafile = tvnamafile.getText().toString();
                if (namafile.equalsIgnoreCase("Tidak ada file terpilih")) {
                    validasi = false;
                    Toast.makeText(context, "Anda belum upload Kartu Keluarga!", Toast.LENGTH_SHORT).show();
                }
                if (validasi) {
                    simpanData();
                }
            }
        });

        btnbatal = findViewById(R.id.btnbatal);
        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
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

        RelativeLayout rlsatu = findViewById(R.id.rlsatu);
        ImageView ivsatu = findViewById(R.id.ivsatu);
        TextView tvsatu = findViewById(R.id.tvsatu);
        rlsatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (satu) {
                    tvsatu.setVisibility(View.VISIBLE);
                    ivsatu.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                    satu = false;
                } else {
                    tvsatu.setVisibility(View.GONE);
                    ivsatu.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
                    satu = true;
                }
            }
        });

        RelativeLayout rldua = findViewById(R.id.rldua);
        ImageView ivdua = findViewById(R.id.ivdua);
        TextView tvdua = findViewById(R.id.tvdua);
        rldua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dua) {
                    tvdua.setVisibility(View.VISIBLE);
                    ivdua.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                    dua = false;
                } else {
                    tvdua.setVisibility(View.GONE);
                    ivdua.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
                    dua = true;
                }
            }
        });
    }

    void simpanData() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Tunggu sebentar...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://service-tlive.tangerangkota.go.id/services/tlive/kependudukan/pengajuan_sinkronisasi_data", new Response.Listener<String>() {
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
                input.put("nokk", nokk);
                input.put("namapemohon", namapemohon);
                input.put("ket", ket);
                input.put("nohp", nohp);
                input.put("nokec", nokec);
                input.put("namakec", namakec);
                input.put("namafile", namafile);
                return input;

            }

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
            String requestURL = "https://service-tlive.tangerangkota.go.id/services/tlive/kependudukan/upload_kk_sinkronisasi_data";
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

                multipart.addFormField("nokk", nokk);
                multipart.addFormField("nama_pemohon", namapemohon);
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
