package ojass20.nitjsr.in.ojass.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import ojass20.nitjsr.in.ojass.R;
import ojass20.nitjsr.in.ojass.Utils.TouchImageView;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import static ojass20.nitjsr.in.ojass.Utils.Utilities.setGlideImage;

public class ActivityZoomableImage extends AppCompatActivity {
    TouchImageView imageView;
    Toolbar toolbar;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideStatusBar();
        setContentView(R.layout.activity_zoomable_image);
        String img_src = getIntent().getStringExtra("img");
        imageView = findViewById(R.id.zoom_img);
        toolbar = findViewById(R.id.toolbar);
        pb = findViewById(R.id.zoom_img_progress);
        pb.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ojass'20");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        Glide.with(this).load(img_src).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                pb.setVisibility(View.GONE);
                return false;
            }
        }).placeholder(R.mipmap.ic_placeholder).fitCenter().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
    }

    private void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ActivityCompat.finishAfterTransition(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
