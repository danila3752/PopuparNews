package com.example.popuparnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.popuparnews.models.Articles;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    List<Articles> articles;

    private OnItemClickListener onItemClickListener;


    public Adapter(Context context, List<Articles> articles) {
        this.context = context;
        this.articles = articles;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;

    }

    public interface OnItemClickListener{
        void onItemClickListener(View view,int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Articles a = articles.get(position);
       // holder.position=position;
        String imageUrl = a.getUrlToImage();
        String url = a.getUrl();
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();
        Glide.with(context).load(imageUrl).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
               holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
               holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).transition(DrawableTransitionOptions.withCrossFade()).into(holder.imageView);
       // Picasso.with(context).load(imageUrl).into(holder.imageView);
//holder.source.setText();
        holder.tvTitle.setText(a.getTitle());
        holder.desc.setText(a.getDescription());
        holder.author.setText(a.getAuthor());
        holder.published_at.setText(Utils.DateFormat(a.getPublishedAt()));
        holder.tvDate.setText("\u2022" + Utils.DateToTimeFormat(a.getPublishedAt()));

        holder.setData(a);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle, tvSource, tvDate,desc,author,published_at,source;
        ImageView imageView;

        RecyclerView recyclerView;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(androidx.core.R.id.title);
            tvSource = itemView.findViewById(R.id.source);
            tvDate = itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.img);
            desc = itemView.findViewById(R.id.descr);
            published_at = itemView.findViewById(R.id.publishedAt);
            author = itemView.findViewById(R.id.author);
            source = itemView.findViewById(R.id.source);
            recyclerView = itemView.findViewById(R.id.RecyclerView);
            progressBar=itemView.findViewById(R.id.prograss_load_photo);

        }


        public  void setData(Articles article){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context,NewsDetailActive.class);
                    //    Articles article=articles.get(a);
                    intent.putExtra("url",article.getUrl());
                    intent.putExtra("title",article.getTitle());
                    intent.putExtra("img",article.getUrlToImage());
                    intent.putExtra("date",article.getPublishedAt());
                    intent.putExtra("author",article.getAuthor());

                    context.startActivity(intent);
                }
            });
        }
        @Override
        public void onClick(View v) {

        }
    }


    public String dateTime(String t) {
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}


// holder.source.setText(((Source) model.getSource()).getName());