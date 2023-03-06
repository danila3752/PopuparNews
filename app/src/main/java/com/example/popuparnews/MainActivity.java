package com.example.popuparnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.popuparnews.models.Articles;
import com.example.popuparnews.models.News;
import com.example.popuparnews.models.api.ApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    final String API_KEY = "1b80024796f14d3491eb573da30fb6b8";
    Adapter adapter;
    List<Articles>  articles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.RecyclerView);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final String country = getCountry();







retrieveJson(country,API_KEY);

    }

    public void retrieveJson(String country, String apiKey){

Call<News> call= ApiClient.getInstance().getApi().getHeadlines(country,apiKey);


        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){
                   // articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(MainActivity.this,articles);
                    recyclerView.setAdapter(adapter);
                }else {  Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("onFailure",t.getMessage(),t);
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }



}