package com.example.form.pengajuanktpel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityDashboardPengajuanKtpEl extends AppCompatActivity {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            if (resultCode == RESULT_OK){
                getData();
            }
        }
    }

    Context context = ActivityDashboardPengajuanKtpEl.this;

    TextView tvajukan, tvselengkapnya, tvblm;
    RelativeLayout rlsyarat;
    RecyclerView recycler_view;
    private Adapter adapter;
    private ArrayList<Mdata> arrayList;

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
        setTitle("Beranda Pengajuan KTP-EL");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_dashboard_pengajuan_ktp_el);

        tvblm = findViewById(R.id.tvblm);
        rlsyarat = findViewById(R.id.rlsyarat);
        rlsyarat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivitySyaratKetentuan.class);
                startActivity(i);
            }
        });
        tvajukan = findViewById(R.id.tvajukan);
        tvajukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InputPengajuanKtpEl.class);
                startActivityForResult(i, 100);
            }
        });
        tvselengkapnya = findViewById(R.id.tvselengkapnya);
        tvselengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivitySelengkapnyaPengajuanKtpEl.class);
                startActivity(i);
            }
        });

        arrayList = new ArrayList<>();
        recycler_view = findViewById(R.id.recycler_view);
        adapter = new Adapter(this, arrayList);
        adapter.setOnItemClickListener(new Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycler_view.setAdapter(adapter);
        getData();
    }

    void getData() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Tunggu sebentar...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://service-tlive.tangerangkota.go.id/services/tlive/kependudukan/riwayat_pengajuan_ktp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Responriwayat", response);
                progressDialog.dismiss();
                try {
                    JSONObject respon = new JSONObject(response);
                    Boolean success = respon.getBoolean("success");
                    String message = respon.getString("message");
                    if (success) {
                        JSONArray data = respon.getJSONArray("data");
                        tvblm.setVisibility(View.GONE);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String nik = jsonObject.getString("nik");
                            String nama = jsonObject.getString("nama");
                            String ket = jsonObject.getString("ket");
                            String tlp = jsonObject.getString("tlp");
                            String no_kec = jsonObject.getString("no_kec");
                            String nama_file = jsonObject.getString("nama_file");
                            String nama_kec = jsonObject.getString("nama_kec");
                            String nik_pemohon = jsonObject.getString("nik_pemohon");
                            String nama_pemohon = jsonObject.getString("nama_pemohon");
                            String status = jsonObject.getString("status");
                            String is_delete = jsonObject.getString("is_delete");
                            arrayList.add(new Mdata(id, nik, nama, ket, tlp, no_kec, nama_file, nama_kec, nik_pemohon, nama_pemohon, status, is_delete));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                input.put("nik", "4");
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

    static class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

        private static Adapter.ClickListener clickListener;
        private ArrayList<Mdata> arrayList;
        private Context context;

        static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView tvnik, tvnama, tvkec, tvstatus;
            ImageView ivlampiran;

            MyViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                tvnik = view.findViewById(R.id.tvnik);
                tvnama = view.findViewById(R.id.tvnama);
                tvkec = view.findViewById(R.id.tvkec);
                ivlampiran = view.findViewById(R.id.ivlampiran);
                tvstatus = view.findViewById(R.id.tvstatus);
            }

            @Override
            public void onClick(View v) {
                clickListener.onItemClick(getAdapterPosition(), v);
            }

            @Override
            public boolean onLongClick(View v) {
                clickListener.onItemLongClick(getAdapterPosition(), v);
                return false;
            }
        }

        void setOnItemClickListener(Adapter.ClickListener clickListener) {
            Adapter.clickListener = clickListener;
        }

        public interface ClickListener {
            void onItemClick(int position, View v);

            void onItemLongClick(int position, View v);
        }

        Adapter(Context context, ArrayList<Mdata> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_pengajuan_ktp, parent, false);
            return new Adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {
            holder.tvnama.setText(arrayList.get(position).getNama_pemohon());
            holder.tvnik.setText(arrayList.get(position).getNik_pemohon());
            holder.tvkec.setText(arrayList.get(position).getNama_kec());
            String status = arrayList.get(position).getStatus();
            if (status.equalsIgnoreCase("0")) {
                //pending
                holder.tvstatus.setText("Pending");
                holder.tvstatus.setTextColor(ContextCompat.getColor(context, R.color.md_yellow_800));
            } else if (status.equalsIgnoreCase("1")) {
                //terima
                holder.tvstatus.setText("Terima");
                holder.tvstatus.setTextColor(ContextCompat.getColor(context, R.color.md_green_500));
            } else {
                //tolak
                holder.tvstatus.setText("Tolak");
                holder.tvstatus.setTextColor(ContextCompat.getColor(context, R.color.md_red_500));
            }
            Picasso.with(context).load(arrayList.get(position).getNama_file()).placeholder(R.drawable.ic_img_default).error(R.drawable.no_photo_available).into(holder.ivlampiran);

            holder.ivlampiran.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
    }

    class Mdata {

        String id;
        String nik;
        String nama;
        String ket;
        String tlp;
        String no_kec;
        String nama_file;
        String nama_kec;
        String nik_pemohon;
        String nama_pemohon;
        String status;
        String is_delete;

        public Mdata(String id, String nik, String nama, String ket, String tlp, String no_kec, String nama_file, String nama_kec, String nik_pemohon, String nama_pemohon, String status, String is_delete) {
            this.id = id;
            this.nik = nik;
            this.nama = nama;
            this.ket = ket;
            this.tlp = tlp;
            this.no_kec = no_kec;
            this.nama_file = nama_file;
            this.nama_kec = nama_kec;
            this.nik_pemohon = nik_pemohon;
            this.nama_pemohon = nama_pemohon;
            this.status = status;
            this.is_delete = is_delete;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNik() {
            return nik;
        }

        public void setNik(String nik) {
            this.nik = nik;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getKet() {
            return ket;
        }

        public void setKet(String ket) {
            this.ket = ket;
        }

        public String getTlp() {
            return tlp;
        }

        public void setTlp(String tlp) {
            this.tlp = tlp;
        }

        public String getNo_kec() {
            return no_kec;
        }

        public void setNo_kec(String no_kec) {
            this.no_kec = no_kec;
        }

        public String getNama_file() {
            return nama_file;
        }

        public void setNama_file(String nama_file) {
            this.nama_file = nama_file;
        }

        public String getNama_kec() {
            return nama_kec;
        }

        public void setNama_kec(String nama_kec) {
            this.nama_kec = nama_kec;
        }

        public String getNik_pemohon() {
            return nik_pemohon;
        }

        public void setNik_pemohon(String nik_pemohon) {
            this.nik_pemohon = nik_pemohon;
        }

        public String getNama_pemohon() {
            return nama_pemohon;
        }

        public void setNama_pemohon(String nama_pemohon) {
            this.nama_pemohon = nama_pemohon;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(String is_delete) {
            this.is_delete = is_delete;
        }

        @Override
        public String toString() {
            return "Mdata{" +
                    "id='" + id + '\'' +
                    ", nik='" + nik + '\'' +
                    ", nama='" + nama + '\'' +
                    ", ket='" + ket + '\'' +
                    ", tlp='" + tlp + '\'' +
                    ", no_kec='" + no_kec + '\'' +
                    ", nama_file='" + nama_file + '\'' +
                    ", nama_kec='" + nama_kec + '\'' +
                    ", nik_pemohon='" + nik_pemohon + '\'' +
                    ", nama_pemohon='" + nama_pemohon + '\'' +
                    ", status='" + status + '\'' +
                    ", is_delete='" + is_delete + '\'' +
                    '}';
        }
    }
}