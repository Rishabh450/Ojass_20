package ojass20.nitjsr.in.ojass.Adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.core.utilities.Utilities;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import ojass20.nitjsr.in.ojass.R;

/**
 * Created by Abhishek on 28-Jan-18.
 */

public class PosterAdapter extends PagerAdapter {

    private Context context;
    private String[] imageUrls, clickUrls;

    public PosterAdapter(Context context, String[] imageUrls, String[] clickUrls){
        this.context = context;
        this.imageUrls = imageUrls;
        this.clickUrls = clickUrls;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LayoutInflater layoutinflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutinflater.inflate(R.layout.item_poster, container, false);
        final ImageView iv = view.findViewById(R.id.iv_poster);
        Log.e("URL",imageUrls[position]);
        Glide.with(context).load(imageUrls[position])
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(iv);
//        Utilities.setPicassoImage(context, imageUrls[position], iv, Constants.RECT_PLACEHOLDER);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
