package com.example.x.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.x.R;

public class ErrorResponse {

    private Context context;
    private String defaultMessage;

    public ErrorResponse(Context context) {
        this.context = context;
        defaultMessage = context.getResources().getString(R.string.volley_error_terjadi_kesalahan);
    }

    public void showDefaultError(){
        Toast.makeText(context, defaultMessage, Toast.LENGTH_SHORT).show();
    }

    public void showVolleyError(VolleyError error){

        String responseBody = "";
        try {
            responseBody = new String(error.networkResponse.data, "UTF-8");
            new LogConsole(responseBody);
        } catch (Exception e){

        }

        String message = defaultMessage;
        if(error.getMessage()!=null) Log.e("error", error.getMessage());
        if(error instanceof NoConnectionError) {
            if(error instanceof NoConnectionError) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                    message = context.getResources().getString(R.string.volley_error_terjadi_kesalahan);
                else message = context.getResources().getString(R.string.volley_error_tidak_ada_internet);
            }
        } else if(error instanceof NetworkError) {
            message = context.getResources().getString(R.string.volley_error_network_error);
        } else if(error instanceof ServerError) {
            message = context.getResources().getString(R.string.volley_error_server_error);
        } else if(error instanceof TimeoutError){
            message = context.getResources().getString(R.string.volley_error_timeout);
        } else if( error instanceof AuthFailureError) {
            message = context.getResources().getString(R.string.volley_error_terjadi_kesalahan);
        } else if( error instanceof ParseError) {
            message = context.getResources().getString(R.string.volley_error_terjadi_kesalahan);
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public String getDefaultErrorMessage(){
        return defaultMessage;
    }

    public String getVolleyErrorMessage(VolleyError error){
        String responseBody = "";
        try {
            responseBody = new String(error.networkResponse.data, "UTF-8");
            new LogConsole(responseBody);
        } catch (Exception e){

        }
        String message = defaultMessage;
        if(error.getMessage()!=null) Log.e("error", error.getMessage());
        if(error instanceof NoConnectionError) {
            if(error instanceof NoConnectionError) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                    message = context.getResources().getString(R.string.volley_error_terjadi_kesalahan);
                else message = context.getResources().getString(R.string.volley_error_tidak_ada_internet);
            }
        } else if(error instanceof NetworkError) {
            message = context.getResources().getString(R.string.volley_error_network_error);
        } else if(error instanceof ServerError) {
            message = context.getResources().getString(R.string.volley_error_server_error);
        } else if(error instanceof TimeoutError){
            message = context.getResources().getString(R.string.volley_error_timeout);
        } else if( error instanceof AuthFailureError) {
            message = context.getResources().getString(R.string.volley_error_terjadi_kesalahan);
        } else if( error instanceof ParseError) {
            message = context.getResources().getString(R.string.volley_error_terjadi_kesalahan);
        }
        return message;
    }
}
