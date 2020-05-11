package ojass20.nitjsr.in.ojass.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.viewpager.widget.PagerAdapter;
import ojass20.nitjsr.in.ojass.R;

public class IntroAdapter extends PagerAdapter {

    private int[] slideLayouts;
    private int[] slideLayoutsImageView;
    private Context context;
    private ImageView iv;
    private static final int sliderImg[] = {
            R.mipmap.intro_qr,
            R.mipmap.intro_map,
            R.mipmap.intro_profile,
            R.mipmap.intro_home
    };

    public IntroAdapter(Context context, int[] slideLayouts, int[] slideLayoutsImageView){
        this.context = context;
        this.slideLayouts = slideLayouts;
        this.slideLayoutsImageView = slideLayoutsImageView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(slideLayouts[position], container, false);
        iv = view.findViewById(slideLayoutsImageView[position]);

        Glide.with(context).load(sliderImg[position]).into(iv);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return slideLayouts.length;
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