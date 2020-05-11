package ojass20.nitjsr.in.ojass.Activities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.github.islamkhsh.CardSliderAdapter;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import ojass20.nitjsr.in.ojass.Models.GurugyanItem;
import ojass20.nitjsr.in.ojass.R;

public class GurugyanActivity extends AppCompatActivity {

    int itemCount;
    ArrayList<GurugyanItem> itemList;

    LinearLayout viewGroup;
    CardSliderViewPager viewPager;
    ProgressBar progressBar;
    com.github.islamkhsh.CardSliderIndicator mCardSliderIndicator;
    com.github.islamkhsh.CardSliderViewPager mCardSliderViewPager;

    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gurugyan);
        setComingSoon();


//        viewGroup = findViewById(R.id.gurugyan_pages);
//        viewPager = findViewById(R.id.gurugyan_viewPager);
//        progressBar = findViewById(R.id.gurugyan_progress_bar);
//        itemList = new ArrayList<>();
//        mCardSliderIndicator = findViewById(R.id.gurugyan_indicator);
//        mCardSliderViewPager = findViewById(R.id.gurugyan_viewPager);

//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCardSliderViewPager.getLayoutParams();
//        layoutParams.height = (int) MainActivity.convertDpToPixel(600, getApplicationContext());
//        mCardSliderViewPager.setLayoutParams(layoutParams);
//
//        layoutParams = (RelativeLayout.LayoutParams) mCardSliderIndicator.getLayoutParams();
//        layoutParams.setMargins(0, (int) MainActivity.convertDpToPixel(640, getApplicationContext()), 0, 0);
//        mCardSliderIndicator.setLayoutParams(layoutParams);

//        setUpFirebaseListeners();
    }
    private void setComingSoon() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setTitle("Gurugyan");
    }
    private void setUpFirebaseListeners() {
        setUpChildEventListener();
        FirebaseDatabase.getInstance().getReference().child("GuruGyan/count").
                addListenerForSingleValueEvent(new ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                       itemCount = dataSnapshot.getValue(Integer.class);
                                                       FirebaseDatabase.getInstance().getReference().child("GuruGyan/events").
                                                               addChildEventListener(childEventListener);
                                                   }

                                                   @Override
                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                   }
                                               }
                );
    }

    private void setUpChildEventListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot,
                                     @Nullable String s) {
                GurugyanItem item = dataSnapshot.getValue(GurugyanItem.class);
                itemList.add(item);
                if (itemList.size() == itemCount) {
                    updateUI();
                    removeChildEventListener();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot,
                                       @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot,
                                     @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void updateUI() {
        sortItems();
        viewPager.setAdapter(new ViewPagerAdapter(itemList, this));
        viewGroup.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void sortItems() {
        Collections.sort(itemList);
    }

    private void removeChildEventListener() {
        FirebaseDatabase.getInstance().getReference().child("GuruGyan/events").removeEventListener(
                childEventListener
        );
    }

    private class ViewPagerAdapter extends CardSliderAdapter<GurugyanItem> {

        Context context;

        ViewPagerAdapter(ArrayList<GurugyanItem> items, Context context) {
            super(items);
            this.context = context;
        }

        @Override
        public int getItemContentLayout(int i) {
            return R.layout.gurugyan_item;
        }

        @Override
        public void bindView(int i, View view, GurugyanItem gurugyanItem) {
            ImageView background = view.findViewById(R.id.gurugyan_card_background);
            TextView day = view.findViewById(R.id.gurugyan_card_day);
            TextView title = view.findViewById(R.id.gurugyan_card_title);
            TextView description = view.findViewById(R.id.gurugyan_card_description);

            Glide.with(context).load(gurugyanItem.getUrl()).into(background);
            day.setText("Day " + gurugyanItem.getDay());
            title.setText(gurugyanItem.getTitle());
            description.setText(gurugyanItem.getDescription());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
