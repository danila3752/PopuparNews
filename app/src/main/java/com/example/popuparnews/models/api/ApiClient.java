package com.example.popuparnews.models.api;

import android.widget.Toast;

import com.example.popuparnews.MainActivity;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static ApiClient apiClient;

    private OkHttpClient okhttp() {
        HttpLoggingInterceptor httpLoggingInterceptor =new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okhttp = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        return okhttp;
    }

    private Retrofit getApiClient() {
        return new Retrofit.Builder().baseUrl(BASE_URL).client(okhttp()).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized ApiClient getInstance() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }


    public ApiInterface getApi() {
        return getApiClient().create(ApiInterface.class);
    }
}
