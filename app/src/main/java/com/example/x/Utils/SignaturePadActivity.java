package com.example.x.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.x.R;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;

public class SignaturePadActivity extends AppCompatActivity {

    private SignaturePad signaturePad;
    private TextView textViewKembali, textViewBersihkan, textViewSelesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_pad);

        signaturePad = findViewById(R.id.signaturePad);
        textViewKembali = findViewById(R.id.textViewKembali);
        textViewBersihkan = findViewById(R.id.textViewBersihkan);
        textViewSelesai = findViewById(R.id.textViewSelesai);

        textViewKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textViewBersihkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
            }
        });

        textViewSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(signaturePad.isEmpty()){
                    Toast.makeText(SignaturePadActivity.this, "Silakan masukkan tanda tangan anda", Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap bitmap = signaturePad.getSignatureBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    Intent intent = new Intent();
                    intent.putExtra("bitmap", byteArray);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
