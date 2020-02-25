package com.devahmed.demo.animeapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.devahmed.demo.animeapp.Models.Image;
import com.devahmed.demo.animeapp.R;

import java.util.List;
import java.util.Random;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    private List<Image> ImagesList;
    private int [] placeHolders = {R.color.pl0,

            R.color.pl1,

            R.color.pl2,

            R.color.pl3,

            R.color.pl4,

            R.color.pl5,

            R.color.pl6,

            R.color.pl7,

            R.color.pl8,

            R.color.pl9,

            R.color.pl10,

            R.color.pl11,

            R.color.pl12,

            R.color.pl13,

            R.color.pl14,

            R.color.pl15,

            R.color.pl16,

            R.color.pl17,

            R.color.pl18,

            R.color.pl19,

            R.color.pl20, };

    public HomeRecyclerAdapter(List<Image> ImagesList) {
        this.ImagesList = ImagesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_image, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image model = ImagesList.get(position);
        Glide.with(holder.imageView.getContext())
                .load(model.getImageUrl())
//                .override(600 , 200)
                .placeholder(placeHolders[generateNum(0 , placeHolders.length - 1)])
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return ImagesList.size();
    }

    public void setList(List<Image> newList) {
        ImagesList = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.row_image);
        }
    }

    private int generateNum(int min , int max){
        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        return i1;
    }
}