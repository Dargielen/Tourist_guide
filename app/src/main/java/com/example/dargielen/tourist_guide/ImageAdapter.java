package com.example.dargielen.tourist_guide;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by dargielen on 04.05.2017.
 */

public class ImageAdapter extends BaseAdapter{

    private Context mContext;
    private String [] mImages;

    public ImageAdapter(Context c, String[] images) {
        mContext = c;
        mImages = images;
    }

    public int getCount() {
        return mImages.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(mImages[position]).placeholder(R.drawable.progress_animation).error(R.drawable.nophoto).into(imageView);
        //imageView.setImageResource(mImages[position]);
        return imageView;
    }

}
