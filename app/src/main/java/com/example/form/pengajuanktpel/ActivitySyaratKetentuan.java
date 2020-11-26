package com.example.form.pengajuanktpel;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

public class ActivitySyaratKetentuan extends AppCompatActivity {
    Context context = ActivitySyaratKetentuan.this;
    LinearLayout llpersyaratan;

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
        setTitle("Syarat dan Ketentuan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_syarat_ketentuan);

        llpersyaratan = findViewById(R.id.llpersyaratan);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        getPersyaratan();
    }

    void getPersyaratan() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Tunggu sebentar...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://service-tlive.tangerangkota.go.id/services/tlive/kependudukan/syarat_pengajuan_ktp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponPersyaratan", response);
                progressDialog.dismiss();
                try {
                    JSONObject respon = new JSONObject(response);
                    Boolean success = respon.getBoolean("success");
                    String message = respon.getString("message");
                    if (success) {
                        JSONArray data = respon.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            TextView tvi = new TextView(context);
//                            tvi.setText(i + 1 + ". " + jsonObject.getString("syarat"));
                            tvi.setText(jsonObject.getString("syarat"));
                            tvi.setTextColor(getResources().getColor(R.color.black));
                            tvi.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                            llpersyaratan.addView(tvi);
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
            //            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> input = new HashMap<>();
//                return input;
//
//            }
//            P1 = "xlzh5v6sgrpvqs3os86x2s298fb04z6txo9cntx0";
//            U1 = "r35t4p1";
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + Base64.encodeToString(String.format("%s:%s", "r35t4p12", "8540c5ef27b4afdb197405dc551ce5b5bfcb73bb2").getBytes(), Base64.DEFAULT));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(150000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

}