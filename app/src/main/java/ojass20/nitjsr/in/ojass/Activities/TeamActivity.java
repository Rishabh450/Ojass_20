package ojass20.nitjsr.in.ojass.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;

import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

import ojass20.nitjsr.in.ojass.Adapters.TeamMemberAdapter;
import ojass20.nitjsr.in.ojass.Models.TeamMember;
import ojass20.nitjsr.in.ojass.R;
import ojass20.nitjsr.in.ojass.Utils.OnSwipeTouchListener;

import static ojass20.nitjsr.in.ojass.Utils.Utilities.setGlideImage;

public class TeamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TeamMemberAdapter.OnClickItem, View.OnClickListener {
    private static final String TAG = "TeamActivity";
    Spinner teamSpinner;
    RecyclerView bottomList;
    TeamMemberAdapter adapter, upper_adapter;
    ArrayList<TeamMember> list, teamList;
    int FILTER = 0;
    boolean DEVELOPER = false;
    TeamMember MEMBER = null;
    ImageView team_back_button;
    Toolbar toolbar;
    LinearLayout swipeLayout;
    LinearLayoutManager manager1, manager2;
    //    RecyclerView team_upper_list;
    LinearSnapHelper btmSnap, topSnap;
    TabLayout tabLayout;
    ImageView imageView;
    LinearLayout linearLayout;
    ViewPager mPager;
    TeamMemberAdapter mAdapter;
    boolean spinner_bug_flag = false, spinner_bug_flag_for_onTabselect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        //to Initialize all class variables
        init();
        //check for developer page
        //to set Listeners
        setListeners();
//        setBottomList();
//        setUpperList();
//        setSwipeLayout();
//        syncRecyclerViewsAndTabs();
        fetchData();
        //to set Team Member's data
//        getData();
        //Temporary push data
//      fetch data
//      pushData();
        onBack();


    }

    private void init() {
       // swipeLayout = findViewById(R.id.swipe_layout);
        list = new ArrayList<>();
        teamList = new ArrayList<>();
        teamSpinner = findViewById(R.id.team_name);
        ArrayAdapter<CharSequence> spAdapter = new ArrayAdapter<CharSequence>(this,R.layout.spinner_item,getResources().getStringArray(R.array.team_names));
        spAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        teamSpinner.setAdapter(spAdapter);
//        bottomList=findViewById(R.id.teamMemberList);
//        team_upper_list = findViewById(R.id.team_upper_recycler_view);
        team_back_button = findViewById(R.id.team_back_button);
        tabLayout = findViewById(R.id.tabs);
        mPager = findViewById(R.id.team_viewpager);
    }

    private void setTabs() {

        for (int i = 0; i < teamList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText("t" + (i + 1)));
        }
        mAdapter = new TeamMemberAdapter(this,this, teamList);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(mPager);

        // set data
        for (int i = 0; i < teamList.size(); i++) {

            final int temp = i;

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.item_team_tab);
            imageView = tab.getCustomView().findViewById(R.id.single_team_member_image);
            linearLayout = tab.getCustomView().findViewById(R.id.singleItemTeamMember);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onClickItem.onSelected(position);
                    mPager.setCurrentItem(temp);
                }
            });
            setGlideImage(this,teamList.get(i).img,imageView);
//            Glide.with(this).asBitmap().fitCenter().load(teamList.get(i).img).into(imageView);

        }
        syncRecyclerViewsAndTabs();


    }

    private void onBack() {
        team_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        FILTER = position;
//        Log.e("onItemSelected: ", "level 167");
        if(spinner_bug_flag){
            //no extra work needed
            spinner_bug_flag=false;
        }
        else {
            spinner_bug_flag_for_onTabselect = true;
            for (int i = 0; i < teamList.size(); i++) {
                int team_no;
                if (position == 0) {
                    team_no = 0;
                } else if (position == 1) {
                    team_no = 1;
                } else {
                    team_no = (4 + position);
                }
                if (team_no == teamList.get(i).team) {
                    mPager.setCurrentItem(i);
                    break;
                }
            }
        }
        //filter();
    }

    private void syncRecyclerViewsAndTabs() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
                TeamMember temp = teamList.get(tab.getPosition());
                int team_no = temp.team;
                if(team_no==0){
                    team_no=0;
                }
                else if (team_no>=1 && team_no <=5){
                    team_no=1;
                }
                else{
                    team_no-=4;
                }
                if(!spinner_bug_flag_for_onTabselect) {
                    spinner_bug_flag = true;
                }
                else spinner_bug_flag_for_onTabselect=false;
                teamSpinner.setSelection(team_no);
//                Log.e("onTabSelected: ", "selection continues..." + tab.getPosition());


                Animation anim = AnimationUtils.loadAnimation(TeamActivity.this, R.anim.scale_in_tv);

                tabLayout.getTabAt(tab.getPosition()).getCustomView().findViewById(R.id.singleItemTeamMember).startAnimation(anim);
                anim.setFillAfter(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Animation anim = AnimationUtils.loadAnimation(TeamActivity.this, R.anim.scale_out_tv);
                tabLayout.getTabAt(tab.getPosition()).getCustomView().findViewById(R.id.singleItemTeamMember).startAnimation(anim);
                anim.setFillAfter(true);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void fetchData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Team");
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TeamMember m = ds.getValue(TeamMember.class);
//                    Log.d("pubg", "onDataChange: " + m.name);
                    if (!teamList.contains(m))
                        teamList.add(m);

                }
                setTabs();
//                adapter.notifyDataSetChanged();
//                upper_adapter.notifyDataSetChanged();
//                filter();
//                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setListeners() {
        if (!DEVELOPER) {

            teamSpinner.setOnItemSelectedListener(this);
        }
    }

    private void filter() {
//        Log.d(TAG, "filter: " + FILTER);
        teamList.clear();
        for (TeamMember member : list) {
            if (member.team == FILTER)
                teamList.add(member);
        }
        if (teamList.size() > 0)
            //onSelected(0);
        adapter.notifyDataSetChanged();
        upper_adapter.notifyDataSetChanged();
//        Log.e("TAG", String.valueOf(teamList.size()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSelected(int position,boolean side) {
        //MEMBER=teamMember;
//        team_upper_list.scrollToPosition(position);
        //setCard();

        if(side){
            if(mPager.getCurrentItem()!=0){
                mPager.setCurrentItem(mPager.getCurrentItem()-1);
            }
        }
        else {
            if(mPager.getCurrentItem()!=teamList.size()-1){
                mPager.setCurrentItem(mPager.getCurrentItem()+1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.team_member_facebook:
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(MEMBER.facebook));
                if (DEVELOPER)
                    viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(MEMBER.github));
                startActivity(viewIntent);
                break;
            case R.id.team_member_call:
                String phone = MEMBER.call;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                break;
            case R.id.team_member_whatsapp:

                String url = "https://api.whatsapp.com/send?phone=" + MEMBER.whatsapp;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }
}
