package com.example.x.Utils;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MultipartUtilityV5 {

    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;
    private static final int BUFFER_SIZE = 1024;

    private long lastProgressUpdateTime = 0;
    FileUploadListener listener;

    public String mURL;

    public interface FileUploadListener {
        void onUpdateProgress(int percentage, long kb);
        boolean isCanceled();
    }

    public MultipartUtilityV5(String requestURL, String charset, FileUploadListener listener) throws IOException {
        this.charset = charset;
        this.listener = listener;
        mURL = requestURL;
        // creates a unique boundary based on time stamp
        boundary = "" + System.currentTimeMillis() + "";

        URL url = new URL(requestURL);
        httpConn = null;
        if (url.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            httpConn = https;
        } else {
            httpConn = (HttpURLConnection) url.openConnection();
        }

        // httpConn.setConnectTimeout(TIME_OUT);
        //httpConn.setReadTimeout(TIME_OUT);
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        // httpConn.setChunkedStreamingMode(BUFFER_SIZE);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        httpConn.setRequestProperty("Connection", "Keep-Alive");

//        Map<String, List<String>> map = httpConn.getHeaderFields();
//        for(String key: map.keySet()){
//            // System.out.println(key + "=" + map.get(key));
//            List<String> value = map.get(key);
//            for(int i=0; i<value.size(); i++){
//                Log.e("paling atas i"+(i+1), key + ":" + value.get(i));
//            }
//        }

        outputStream = httpConn.getOutputStream();

        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    public MultipartUtilityV5(String requestURL, String charset, FileUploadListener listener, String username, String password) throws IOException {
        this.charset = charset;
        this.listener = listener;
        mURL = requestURL;
        // creates a unique boundary based on time stamp
        boundary = "" + System.currentTimeMillis() + "";

        URL url = new URL(requestURL);
        httpConn = null;
        if (url.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            httpConn = https;
        } else {
            httpConn = (HttpURLConnection) url.openConnection();
        }

        // httpConn.setConnectTimeout(TIME_OUT);
        //httpConn.setReadTimeout(TIME_OUT);
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        // httpConn.setChunkedStreamingMode(BUFFER_SIZE);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        httpConn.setRequestProperty("Connection", "Keep-Alive");

        String userPassword = username + ":" + password;
        byte[] data = userPassword.getBytes("UTF-8");
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        httpConn.setRequestProperty("Authorization", "Basic " + base64);

//        Map<String, List<String>> map = httpConn.getHeaderFields();
//        for(String key: map.keySet()){
//            // System.out.println(key + "=" + map.get(key));
//            List<String> value = map.get(key);
//            for(int i=0; i<value.size(); i++){
//                Log.e("paling atas i"+(i+1), key + ":" + value.get(i));
//            }
//        }

        outputStream = httpConn.getOutputStream();

        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }



    public void addFilePart(String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        outputStream.flush();

        try {

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            long totalRead = 0;
            long totalSize = uploadFile.length();

            int read;
            while ((read = inputStream.read(buffer)) > 0) {
                totalRead += read;
                int percentage = (int) ((totalRead / (float) totalSize) * 100);
                outputStream.write(buffer, 0, read);
                Log.e("totalRead", String.valueOf(totalRead));

                long now = System.currentTimeMillis();
                if (lastProgressUpdateTime == 0 || lastProgressUpdateTime < now - 100) {
                    lastProgressUpdateTime = now;

                    Log.e("asdasdasd", totalRead + " " + " " + percentage);
                    if (listener != null) this.listener.onUpdateProgress(percentage, totalRead);
                }
            }
            outputStream.flush();
            inputStream.close();



        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        writer.append(LINE_FEED);
        writer.flush();
    }

    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    public String finish() throws IOException {

        StringBuffer response = new StringBuffer();
        // writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        int status = httpConn.getResponseCode();
//        Map<String, List<String>> map = httpConn.getHeaderFields();
//        for(String key: map.keySet()){
//            // System.out.println(key + "=" + map.get(key));
//            List<String> value = map.get(key);
//            for(int i=0; i<value.size(); i++){
//                Log.e("yang bawah i"+(i+1), key + ":" + value.get(i));
//            }
//        }

        Log.e("status", httpConn.getResponseCode()+" "+httpConn.getResponseMessage());

        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            httpConn.disconnect();

        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getErrorStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            httpConn.disconnect();
            // throw new IOException("Server returned non-OK status: " + status);
        }
        Log.e("response", response.toString());
        return response.toString();
    }
}
