package com.example.popuparnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.view.View;
import android.widget.SearchView;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popuparnews.models.Articles;
import com.example.popuparnews.models.News;
import com.example.popuparnews.models.api.ApiClient;
import com.example.popuparnews.models.api.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    final String API_KEY = "1b80024796f14d3491eb573da30fb6b8";

    private TextView topheadlines;
    Adapter adapter;
    final String country = getCountry();
    List<Articles> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        topheadlines=findViewById(R.id.Topheadlines);
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OnloadingSwipeRefresh("");
    }

    public void retrieveJson(final String keyword) {
topheadlines.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        String language=Utils.getLanguage();
        Call<News> call;
        if(keyword.length()>0){
            call=ApiClient.getInstance().getApi().getSearchNews(keyword,language,"publishedAt",API_KEY);
        }else {
            call = ApiClient.getInstance().getApi().getHeadlines(country, API_KEY);
        }


        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if(!articles.isEmpty()){
                            articles.clear();
                    }
                    articles = response.body().getArticles();


                    adapter = new Adapter(MainActivity.this, articles);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                    //initListener();
                    topheadlines.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    topheadlines.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                topheadlines.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }





    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView=(SearchView) menu.findItem(R.id.search).getActionView();
        MenuItem searchMenuItem=menu.findItem(R.id.search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search latest news...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>2){

                    OnloadingSwipeRefresh(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false,false);
        return true;
    }

    @Override
    public void onRefresh() {
        retrieveJson("");
    }

    private void OnloadingSwipeRefresh(final String keyword){
        swipeRefreshLayout.post (
            new Runnable(){


                @Override
                public void run() {
                    retrieveJson(keyword);
                }
            });
    }
}