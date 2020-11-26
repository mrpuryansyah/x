package com.example.x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class qrcode extends AppCompatActivity {

    private EditText editTextCode;
    private Button buttonBarcode, buttonQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        editTextCode = (EditText) findViewById(R.id.editTextCode);

        buttonBarcode = (Button) findViewById(R.id.buttonBarcode);

        //daftarkan listener untuk memanggil fugnsi scan barcode biasa
        buttonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode("PRODUCT_MODE");
            }
        });

        buttonQRCode = (Button) findViewById(R.id.buttonQRCode);
        //daftarkan listener untuk memanggil fungsi scan qrcode biasa
        buttonQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode("QR_CODE_MODE");
            }
        });
    }

    //method scanBarcode
    private void scanBarcode(String mode) {
        try {
            //buat intent untuk memanggil fungsi scan pada aplikasi zxing
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", mode); // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, 1);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Tanggkap hasil dari scan
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                editTextCode.setText("");
                String contents = data.getStringExtra("SCAN_RESULT");
                Toast.makeText(getBaseContext(), "Hasil :" + contents, Toast.LENGTH_SHORT).show();
                editTextCode.setText(contents);
            }

        }
    }
}
