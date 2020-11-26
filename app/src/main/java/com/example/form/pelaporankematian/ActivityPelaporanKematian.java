package com.example.form.pelaporankematian;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.example.x.R;
import com.example.x.Utils.ErrorResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class ActivityPelaporanKematian extends AppCompatActivity {
    Context context = ActivityPelaporanKematian.this;

    private static final int REQUEST_CODE_CAMERA_AND_WRITE = 0, REQUEST_CODE_SIGNATURE_PAD = 1;
    private File fileFoto = null, fileTtd = null;

    TextView btnbatal, btnbuat;
    LinearLayout llriwayat, llinput;

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
        setTitle("Formulir Pelaporan Kematian");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_pelaporan_kematian);

        llriwayat = findViewById(R.id.llriwayat);
        llinput = findViewById(R.id.llinput);

        btnbatal = findViewById(R.id.btnbatal);
        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llriwayat.setVisibility(View.VISIBLE);
                llinput.setVisibility(View.GONE);
            }
        });

        btnbuat = findViewById(R.id.btnbuat);
        btnbuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llinput.setVisibility(View.VISIBLE);
                llriwayat.setVisibility(View.GONE);
            }
        });

    }

    void getPersyaratan() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Tunggu sebentar...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://service-pegov.tangerangkota.go.id/api/brt/persyaratan", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponPersyaratan", response);
                progressDialog.dismiss();
                try {
                    JSONObject respon = new JSONObject(response);
                    Boolean success = respon.getBoolean("success");
                    String message = respon.getString("message");
                    if (success) {
                        JSONObject data = respon.getJSONObject("data");
                        JSONArray arraypersyaratan = data.getJSONArray("persyaratan");
                        for (int i = 0; i < arraypersyaratan.length(); i++) {
                            JSONObject jsonObject = arraypersyaratan.getJSONObject(i);
                            TextView tvi = new TextView(context);
                            tvi.setText(i + 1 + ". " + jsonObject.getString("harus"));
                            tvi.setTextColor(getResources().getColor(R.color.black));
                            tvi.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

//                            llpersyaratan.addView(tvi);
                        }
                    }
                } catch (JSONException e) {
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + Base64.encodeToString(String.format("%s:%s", "r35t4p1", "xlzh5v6sgrpvqs3os86x2s298fb04z6txo9cntx0").getBytes(), Base64.DEFAULT));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

//    protected class UploadFoto extends AsyncTask<String, Integer, String> {
//
//        ProgressDialog progressDialog;
//        String idPegawai, status, tipe;
//        File file;
//
//        public UploadFoto(String idPegawai, String status, String tipe, File file) {
//            this.idPegawai = idPegawai;
//            this.status = status;
//            this.tipe = tipe;
//            this.file = file;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("Tunggu sebentar...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String charset = "UTF-8";
////            String requestURL = Api.setFotoAbsen;
//            MultipartUtilityV5 multipart = null;
//            String response = "";
//            try {
////                multipart = new MultipartUtilityV5(requestURL, charset, new MultipartUtilityV5.FileUploadListener() {
////                    @Override
////                    public void onUpdateProgress(int percentage, long kb) {
////                        publishProgress(percentage);
////                    }
////
////                    @Override
////                    public boolean isCanceled() {
////                        return false;
////                    }
////                }, BuildConfig.U1, BuildConfig.P1);
////
//                multipart.addFormField("id_pegawai", idPegawai);
//                multipart.addFormField("status", status);
//                multipart.addFormField("type", tipe);
//                multipart.addFilePart("file", file);
//                response = multipart.finish();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            progressDialog.dismiss();
//            new LogConsole("response", s);
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                if(jsonObject.has("success")) {
//
//                    if(jsonObject.getBoolean("success")){
////                        String url = jsonObject.getString("data");
////                        if(tipe.equals("img")){
////                            Picasso.with(context).load(file)
////                                    .placeholder(R.drawable.ic_img_default)
////                                    .error(R.drawable.no_photo_available)
////                                    .into(imageViewFoto);
////                        } else
//                            if(tipe.equals("ttd")){
//                            Picasso.with(context).load(file)
//                                    .placeholder(R.drawable.ic_img_default)
//                                    .error(R.drawable.no_photo_available)
//                                    .into(ivttd);
//                        }
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(context, new ErrorResponse(context).getDefaultErrorMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setProgress((int) (values[0]));
//        }
//
//        @Override
//        protected void onCancelled(String s) {
//            super.onCancelled(s);
//            progressDialog.dismiss();
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            progressDialog.dismiss();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
//            if (resultCode == RESULT_OK) {
//                Image image = ImagePicker.getFirstImageOrNull(data);
//                File fileTemp = new File(image.getPath());
//                try {
//                    fileFoto = new Compressor(context).compressToFile(fileTemp);
//                    fileTemp.delete();
////                    Bitmap bmImg = BitmapFactory.decodeFile(fileFoto.getPath());
////                    imageViewFoto.setImageBitmap(bmImg);
////                    textViewCancelFoto.setVisibility(View.VISIBLE);
//                    new UploadFoto(sessionManagerPortal.getSikda_idpegawai(), status, "img", fileFoto).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        if (requestCode == REQUEST_CODE_SIGNATURE_PAD && resultCode == RESULT_OK) {
            byte[] byteArray = data.getByteArrayExtra("bitmap");
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            fileTtd = new File(this.getCacheDir(), ts + ".jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(fileTtd);
                fos.write(byteArray);
                fos.flush();
                fos.close();
//                new UploadDokumen(TAG_TTD, fileTtd).execute();
                fileTtd = new Compressor(context).compressToFile(fileTtd);
                Bitmap bmImg = BitmapFactory.decodeFile(fileTtd.getPath());
//                ivttd.setImageBitmap(bmImg);
//                ivttd.setVisibility(View.VISIBLE);

//                new UploadFoto("1", "1", "ttd", fileTtd).execute();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
