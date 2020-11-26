package com.example.x.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.x.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

//import com.github.piasy.biv.loader.ImageLoader;
//import com.github.piasy.biv.view.BigImageView;

public class ActivityFullscreenImageV2 extends AppCompatActivity {

    private Context context = this;
    File fileFromUrl;
    Bitmap bitmapFromUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image_v2);

        ImageView imageViewClose = findViewById(R.id.imageViewClose);
        ImageView imageViewDownload = findViewById(R.id.imageViewDownload);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        PhotoView photoView = findViewById(R.id.photoView);

//        BigImageView bigImageView = findViewById(R.id.mBigImage);
//        bigImageView.setInitScaleType(BigImageView.INIT_SCALE_TYPE_CENTER_INSIDE);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        ImageLoader.Callback myImageLoaderCallback = new ImageLoader.Callback() {
//            @Override
//            public void onCacheHit(int imageType, File image) {
//                // Image was found in the cache
//            }
//
//            @Override
//            public void onCacheMiss(int imageType, File image) {
//                // Image was downloaded from the network
//            }
//
//            @Override
//            public void onStart() {
//                // Image download has started
//            }
//
//            @Override
//            public void onProgress(int progress) {
//                // Image download progress has changed
//            }
//
//            @Override
//            public void onFinish() {
//                // Image download has finished
//            }
//
//            @Override
//            public void onSuccess(File image) {
//                // Image was retrieved successfully (either from cache or network)
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFail(Exception error) {
//                // Image download failed
//                progressBar.setVisibility(View.GONE);
//            }
//        };
//        bigImageView.setImageLoaderCallback(myImageLoaderCallback);

        if(getIntent().hasExtra("url")){

            final String url = getIntent().getStringExtra("url");
            if(getIntent().hasExtra("can_download")){
                if(getIntent().getBooleanExtra("can_download", false)){
                    imageViewDownload.setVisibility(View.VISIBLE);
                    imageViewDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Target target = new Target() {
                                @Override
                                public void onPrepareLoad(Drawable arg0) {
                                    return;
                                }
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                                    bitmapFromUrl = bitmap;
                                    String fileName = url.substring(url.lastIndexOf('/') + 1);
                                    fileFromUrl = new File(Environment.getExternalStorageDirectory().getPath() +"/Download/" + fileName);
                                    try {
                                        fileFromUrl.createNewFile();
                                        FileOutputStream ostream = new FileOutputStream(fileFromUrl);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                                        ostream.close();
                                        scanFile(fileFromUrl.getAbsolutePath());
                                        Toast.makeText(context, "File berhasil di unduh pada folder Donwload", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
//                                        MyToast.show(context, "Gagal mengunduh file");
                                        Toast.makeText(context, "Gagal mengunduh file", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onBitmapFailed(Drawable arg0) {
                                    return;
                                }
                            };
                            Picasso.with(context).load(url).into(target);

                        }
                    });
                } else imageViewDownload.setVisibility(View.GONE);
            } else imageViewDownload.setVisibility(View.GONE);

            Picasso.with(this).load(url).into(photoView);

        } else if(getIntent().hasExtra("file")){
            File file = (File)getIntent().getSerializableExtra("file");
//            bigImageView.showImage(Uri.parse("file://" + file.getAbsolutePath()));

            Picasso.with(this).load(file).into(photoView);

        } else finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(fileFromUrl!=null) fileFromUrl.delete();
    }

    private void scanFile(String path) {

        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("Tag", "Scan finished. You can view the image in the gallery now.");
                    }
                });
    }
}
