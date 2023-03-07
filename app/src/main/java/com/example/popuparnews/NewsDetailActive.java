package com.example.popuparnews;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class NewsDetailActive extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
private ImageView imageView;
private TextView appbar_title,appbar_subtitle,date,time,title;
private boolean isHidetToBarView=false;
private FrameLayout date_behavior;

private LinearLayout titleAppbar;
private AppBarLayout appBarLayout;
private Toolbar toolbar;
private String mUrl,mImg,mTitle,mDate,mSource,mAuthor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_active);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout=findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        date_behavior=findViewById(R.id.date_behavior);
        titleAppbar=findViewById(R.id.appbar);
        appbar_title=findViewById(R.id.title_on_appbar);
        appbar_subtitle=findViewById(R.id.subtitle_on_appbar);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        title=findViewById(R.id.title);

        Intent intent=getIntent();
        mUrl=intent.getStringExtra("url");
        mImg=intent.getStringExtra("img");
        mTitle=intent.getStringExtra("title");
        mDate=intent.getStringExtra("date");
        mAuthor=intent.getStringExtra("author");

        RequestOptions requestOptions=new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this).load(mImg).apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);

        appbar_subtitle.setText(mUrl);
        date.setText(Utils.DateFormat(mDate));
        title.setText(mTitle);

        String author=null;
        if(mAuthor!=null && mAuthor!=""){
            mAuthor= " \u2023 "+ mAuthor;
        }else author="";

        time.setText(author+" \u2023 "+ Utils.DateToTimeFormat(mDate));

        initWebView(mUrl);

    }

    private void initWebView(String url){
        WebView webView=findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll=appBarLayout.getTotalScrollRange();
        float percetage=(float) Math.abs(verticalOffset)/(float) maxScroll;

if(percetage== 1f && isHidetToBarView ){

    date_behavior.setVisibility(View.GONE);
    titleAppbar.setVisibility(View.VISIBLE);
    isHidetToBarView=!isHidetToBarView;
}else if(percetage< 1f && isHidetToBarView ){

    date_behavior.setVisibility(View.VISIBLE);
    titleAppbar.setVisibility(View.GONE);
    isHidetToBarView=!isHidetToBarView;
}

    }
}