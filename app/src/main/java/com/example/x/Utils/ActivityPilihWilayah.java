package com.example.x.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.x.R;
import com.example.x.Utils.clearableEdittext.ClearableEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ActivityPilihWilayah extends AppCompatActivity {

    private ClearableEditText editTextCariList;
    private ListView listView;
    private ArrayList<PtnKeyValue> arrayList;
    private AdapterList adapter;
    private boolean stat;
    private static int delayLoadingMillis = 300;
    public static final int REQCODE_PROV = 1, REQCODE_KAB = 2, REQCODE_KEC = 3, REQCODE_KEL = 4;
    private Context mContext = ActivityPilihWilayah.this;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_wilayah);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pilih Wilayah");

        arrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewPilih);

        editTextCariList = (ClearableEditText) findViewById(R.id.editTextCariList);
        editTextCariList.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                ArrayList<PtnKeyValue> arrTemp = new ArrayList<>();
                PtnKeyValue keyValueTemp;

                for (int i = 0; i < arrayList.size(); i++) {
                    String key = arrayList.get(i).getKey();
                    String value = arrayList.get(i).getValue();
                    stat = Pattern.compile(Pattern.quote(s.toString().trim()), Pattern.CASE_INSENSITIVE).matcher(value).find();
                    if (stat) {
                        keyValueTemp = new PtnKeyValue(key, value);
                        arrTemp.add(keyValueTemp);
                    }
                }
                AdapterList a = new AdapterList(ActivityPilihWilayah.this, arrTemp);
                listView.setAdapter(a);
                a.notifyDataSetChanged();
            }
        });

        int reqCode = getIntent().getIntExtra("requestCode", 0);
        int kec = getIntent().getIntExtra("kec", 0);

        if (kec == 1) {
            getAllKecamatanTangerangKota();
        }

        if (reqCode == REQCODE_PROV)
            getPropinsi();
        else if (reqCode == REQCODE_KAB) {
            String no_prop = getIntent().getStringExtra("no_prop");
            getKabupatenKota(no_prop);
        } else if (reqCode == REQCODE_KEC) {
            String no_prop = getIntent().getStringExtra("no_prop");
            String no_kab = getIntent().getStringExtra("no_kab");
            getKecamatan(no_prop, no_kab);
        } else if (reqCode == REQCODE_KEL) {
            String no_prop = getIntent().getStringExtra("no_prop");
            String no_kab = getIntent().getStringExtra("no_kab");
            String no_kec = getIntent().getStringExtra("no_kec");
            getKelurahan(no_prop, no_kab, no_kec);
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Helpers.hideSoftKeyBoard(mContext, view);
            TextView txtId = (TextView) view.findViewById(R.id.txtListId);
            TextView txtValue = (TextView) view.findViewById(R.id.txtListValue);
            String id = txtId.getText().toString();
            String value = txtValue.getText().toString();
//            Toast.makeText(ActivityPilihWilayah.this, id+" "+value, Toast.LENGTH_SHORT).show();
            Intent data = new Intent();
            data.putExtra("id", id);
            data.putExtra("value", value);
            setResult(RESULT_OK, data);
            finish();
        }
    };

    void getPropinsi() {

        arrayList = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu sebentar...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://opendatav2.tangerangkota.go.id/services/tlive/wilayah/propinsi_all", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject result;
                Log.d("response", response);
                String message = "";
                try {
                    result = new JSONObject(response);
                    if (result.has("message")) message = result.getString("message");
                    if (result.has("success"))
                        if (result.has("success")) {
                            PtnKeyValue keyValue;
                            JSONArray jsonArray = result.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                keyValue = new PtnKeyValue(jsonObject.getString("NO_PROP"), jsonObject.getString("NAMA_PROP"));
                                arrayList.add(keyValue);
                            }
                            adapter = new AdapterList(ActivityPilihWilayah.this, arrayList);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(onItemClickListener);

                        } else {
                            Toast.makeText(ActivityPilihWilayah.this, message, Toast.LENGTH_SHORT).show();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new ErrorResponse(ActivityPilihWilayah.this).showDefaultError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new ErrorResponse(ActivityPilihWilayah.this).showVolleyError(error);
            }
        }
        );

        stringRequest.setTag("getPropinsi");
        RequestQueue requeue = Volley.newRequestQueue(ActivityPilihWilayah.this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                requeue.add(stringRequest);
            }
        }, delayLoadingMillis);
    }

    void getKabupatenKota(String no_prop) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu sebentar...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://opendatav2.tangerangkota.go.id/services/tlive/wilayah/kabupaten/" + no_prop, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                JSONObject result;
                Log.d("response", response);
                String message = "";
                try {
                    result = new JSONObject(response);
                    if (result.has("message")) message = result.getString("message");
                    if (result.has("success"))
                        if (result.has("success")) {

                            PtnKeyValue keyValue;
                            JSONArray jsonArray = result.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                keyValue = new PtnKeyValue(jsonObject.getString("NO_KAB"), jsonObject.getString("NAMA_KAB"));
                                if (no_prop.equals("36")) {
                                    if (!jsonObject.getString("NO_KAB").equals("71"))
                                        arrayList.add(keyValue);
                                } else {
                                    arrayList.add(keyValue);
                                }
                            }
                            adapter = new AdapterList(ActivityPilihWilayah.this, arrayList);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(onItemClickListener);

                        } else {
                            Toast.makeText(ActivityPilihWilayah.this, message, Toast.LENGTH_SHORT).show();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new ErrorResponse(ActivityPilihWilayah.this).showDefaultError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new ErrorResponse(ActivityPilihWilayah.this).showVolleyError(error);
            }
        }
        );

        stringRequest.setTag("getKabupaten");
        RequestQueue requeue = Volley.newRequestQueue(ActivityPilihWilayah.this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                requeue.add(stringRequest);
            }
        }, delayLoadingMillis);
    }

    void getKecamatan(String no_prop, String no_kab) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu sebentar...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://opendatav2.tangerangkota.go.id/services/tlive/wilayah/kecamatan/" + no_prop + "/" + no_kab, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                JSONObject result;
                Log.d("response", response);
                String message = "";
                try {
                    result = new JSONObject(response);
                    if (result.has("message")) message = result.getString("message");
                    if (result.has("success"))
                        if (result.has("success")) {

                            PtnKeyValue keyValue;
                            JSONArray jsonArray = result.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                keyValue = new PtnKeyValue(jsonObject.getString("NO_KEC"), jsonObject.getString("NAMA_KEC"));
                                arrayList.add(keyValue);
                            }
                            adapter = new AdapterList(ActivityPilihWilayah.this, arrayList);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(onItemClickListener);

                        } else {
                            Toast.makeText(ActivityPilihWilayah.this, message, Toast.LENGTH_SHORT).show();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new ErrorResponse(ActivityPilihWilayah.this).showDefaultError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new ErrorResponse(ActivityPilihWilayah.this).showVolleyError(error);
            }
        }
        );

        stringRequest.setTag("getKecamatan");
        RequestQueue requeue = Volley.newRequestQueue(ActivityPilihWilayah.this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                requeue.add(stringRequest);
            }
        }, delayLoadingMillis);
    }

    void getKelurahan(String no_prop, String no_kab, String no_kec) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu sebentar...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://opendatav2.tangerangkota.go.id/services/tlive/wilayah/kelurahan/" + no_prop + "/" + no_kab + "/" + no_kec, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                JSONObject result;
                Log.d("response", response);
                String message = "";
                try {
                    result = new JSONObject(response);
                    if (result.has("message")) message = result.getString("message");
                    if (result.has("success"))
                        if (result.has("success")) {

                            PtnKeyValue keyValue;
                            JSONArray jsonArray = result.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                keyValue = new PtnKeyValue(jsonObject.getString("NO_KEL"), jsonObject.getString("NAMA_KEL"));
                                arrayList.add(keyValue);
                            }
                            adapter = new AdapterList(ActivityPilihWilayah.this, arrayList);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(onItemClickListener);
                        } else {
                            Toast.makeText(ActivityPilihWilayah.this, message, Toast.LENGTH_SHORT).show();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new ErrorResponse(ActivityPilihWilayah.this).showDefaultError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new ErrorResponse(ActivityPilihWilayah.this).showVolleyError(error);
            }
        }
        );

        stringRequest.setTag("getKelurahan");
        RequestQueue requeue = Volley.newRequestQueue(ActivityPilihWilayah.this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                requeue.add(stringRequest);
            }
        }, delayLoadingMillis);
    }

    void getAllKecamatanTangerangKota() {

        arrayList = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu sebentar...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://opendatav2.tangerangkota.go.id/services/wilayah/kecamatan/no_prop/36/no_kab/71/format/json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject result;
                Log.d("response", response);
                String message = "";
                try {
                    result = new JSONObject(response);
                    if (result.has("message")) message = result.getString("message");
                    if (result.has("success"))
                        if (result.has("success")) {
                            PtnKeyValue keyValue;
                            JSONArray jsonArray = result.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                keyValue = new PtnKeyValue(jsonObject.getString("NO_KEC"), jsonObject.getString("NAMA_KEC"));
                                arrayList.add(keyValue);
                            }
                            adapter = new AdapterList(ActivityPilihWilayah.this, arrayList);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(onItemClickListener);

                        } else {
                            Toast.makeText(ActivityPilihWilayah.this, message, Toast.LENGTH_SHORT).show();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new ErrorResponse(ActivityPilihWilayah.this).showDefaultError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new ErrorResponse(ActivityPilihWilayah.this).showVolleyError(error);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + Base64.encodeToString(String.format("%s:%s", "r35t51kd4", "5ksnpcua5x6z79yk5xgbtkg89a4zdwc8ym7p2f4z").getBytes(), Base64.DEFAULT));
                return headers;
            }
        };

        stringRequest.setTag("getAllKecamatanTangerang");
        RequestQueue requeue = Volley.newRequestQueue(ActivityPilihWilayah.this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                requeue.add(stringRequest);
            }
        }, delayLoadingMillis);
    }

    class AdapterList extends BaseAdapter {

        private Context context;
        private ArrayList<PtnKeyValue> arrayList;
        private LayoutInflater inflater;

        public AdapterList(Context context, ArrayList<PtnKeyValue> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_pilih, null);
            }
            TextView txtId = (TextView) view.findViewById(R.id.txtListId);
            TextView txtValue = (TextView) view.findViewById(R.id.txtListValue);
            txtId.setText(arrayList.get(i).getKey());
            txtValue.setText(arrayList.get(i).getValue());

            return view;
        }
    }
}