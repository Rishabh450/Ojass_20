package ojass20.nitjsr.in.ojass.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ojass20.nitjsr.in.ojass.Activities.MainActivity;
import ojass20.nitjsr.in.ojass.Models.Comments;
import ojass20.nitjsr.in.ojass.R;

public class CommentsFragment extends Fragment {

    private Context context;
    private FragmentManager fragmentManager;

    private Toolbar mtoolbar;
    private ImageView back;
    private RecyclerView comments_recycler_view;
    private LinearLayoutManager linearLayoutManager;

    private CircleImageView self_dp;
    private EditText self_comment_text;
    private TextView send_comment_button;

    private ArrayList<Comments> comment_list;

    private DatabaseReference dref;
    private FirebaseAuth mauth;
    private String current_user_id;
    private String current_post_id;
    private boolean flag=false;

    private MainActivity mainActivity;

    public CommentsFragment() {
        // Required empty public constructor
    }

    public CommentsFragment(Context context, FragmentManager fragmentManager, String current_post_id, MainActivity mainActivity) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.current_post_id = current_post_id;
        this.mainActivity = mainActivity;
    }

    public static CommentsFragment newInstance(Context context, FragmentManager fragmentManager, String current_post_id, MainActivity mainActivity) {
        CommentsFragment fragment = new CommentsFragment(context, fragmentManager, current_post_id, mainActivity);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        initialize(view);


        mauth = FirebaseAuth.getInstance();
        Glide.with(this)
                .load(mauth.getCurrentUser().getPhotoUrl().toString())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        self_dp.setImageResource(R.drawable.ic_mood_black_24dp);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(self_dp);

        comment_list = new ArrayList<>();

        dref = FirebaseDatabase.getInstance().getReference().child("Feeds").child(current_post_id).child("comments");
        current_user_id = mauth.getCurrentUser().getDisplayName();

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment_list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String msenderid = ds.child("senderId").getValue().toString();
                    String msendername = ds.child("senderUserName").getValue().toString();
                    String msenderurl = ds.child("senderImageUrl").getValue().toString();

                    String mmessage = ds.child("message").getValue().toString();
                    String mtime = findTimeDifference(ds.child("time").getValue().toString());
                    //findImageUrl(msenderid,mmessage,mtime,msendername, msenderurl);

                    Comments mcomment = new Comments(msenderid, mmessage, mtime,msendername, msenderurl);
                    comment_list.add(mcomment);

                }
                flag=true;
                setuprecyclerview();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        send_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_can_comment_more();
            }
        });

        //onBackPress(container);
        return view;
    }

    private void user_can_comment_more() {
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count=0;
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if(ds.child("senderId").getValue().toString().equals(mauth.getCurrentUser().getUid())){
                        count++;
                    }
                }
                if(count<5){
                    sendComment();
                }
                else{
                    Toast.makeText(context, "You have already reached your comment limit on this post.", Toast.LENGTH_SHORT).show();
                    self_comment_text.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void findImageUrl(final String msenderid, final String mmsg, final String mtime, final String msendername, final String msenderurl) {
//
//        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Users");
//        mref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    Log.e("1 onDataChange: ",msenderid);
//                    Log.e("2onDataChange: ",ds.getKey());
//                    if(ds.getKey().equals(msenderid)) {
//                        Log.e("3 onDataChange: ","level23");
//                        String ans = ds.child("photoUrl").getValue().toString();
//                        Comments mcomment = new Comments(msenderid, mmsg, mtime,ans);
//                        comment_list.add(mcomment);
//                        if(flag) {
//                            setuprecyclerview();
//                        }
//                        Log.e("4 onDataChange: "," count of comments "+comment_list.size());
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void onBackPress(View container) {
        container.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    closeFragment();
                    return  true;
                }
                return false;
            }
        });
    }

    private String findTimeDifference(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa", Locale.getDefault());
        String mTime = sdf.format(new Date());

        try {
            Date end = sdf.parse(mTime);
            Date start = sdf.parse(time);
            long different = end.getTime() - start.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedDays > 0) {
                return Long.toString(elapsedDays) + " days";
            } else if (elapsedHours > 0) {
                return Long.toString(elapsedHours) + " hrs";
            } else if (elapsedMinutes > 0) {
                return Long.toString(elapsedMinutes) + " min";
            } else
                return Long.toString(elapsedSeconds) + " sec";

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "24 min";
    }

    public Toolbar getToolbar() {
        return mtoolbar;
    }

    private void sendComment() {
        if (validate()) {
            HashMap<String, Object> hs = new HashMap<>();
            hs.put("senderId", mauth.getCurrentUser().getUid());
            hs.put("message", self_comment_text.getText().toString().trim());

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            hs.put("time", currentDateandTime);
            hs.put("senderImageUrl", mauth.getCurrentUser().getPhotoUrl().toString());
            hs.put("senderUserName", mauth.getCurrentUser().getDisplayName());

            String push_id = dref.push().getKey().toString();
            dref.child(push_id).setValue(hs).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        self_comment_text.setText("");
                        comments_recycler_view.scrollToPosition(0);
                    } else {
                        Toast.makeText(context, "Comment not sent...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(context, "Add a comment", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate() {
        if (self_comment_text.getText().toString().trim().equals("")) {
            return false;
        }
        return true;
    }

    public void setuprecyclerview() {
        ArrayList<Comments> temp = new ArrayList<>();
        for (int i = comment_list.size() - 1; i >= 0; i--) {
            temp.add(comment_list.get(i));
        }

        linearLayoutManager = new LinearLayoutManager(context);
        comments_recycler_view.setLayoutManager(linearLayoutManager);
        comments_adapter myadap = new comments_adapter(temp);
        comments_recycler_view.setAdapter(myadap);
    }

    private void initialize(View view) {
        mtoolbar = view.findViewById(R.id.toolbar_comment_activity);
        back = view.findViewById(R.id.comment_back_button);
        comments_recycler_view = view.findViewById(R.id.comments_recycler_view);
        self_dp = view.findViewById(R.id.self_profile_pic);
        self_comment_text = view.findViewById(R.id.self_comment_edittext);
        send_comment_button = view.findViewById(R.id.comment_send_button);
        (view.findViewById(R.id.comment_back_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
    }

    void closeFragment(){
        mainActivity.unsetIsCommentsFragmentOpen();
        self_comment_text.clearFocus();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.no_anim, R.anim.slide_out_bottom);
        transaction.remove(CommentsFragment.this).commit();
    }
    private void send_description_via_intent() {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("Feeds").child(current_post_id);
    }

    class comments_adapter extends RecyclerView.Adapter<comments_adapter.myviewholder> {
        ArrayList<Comments> mdatalist;

        public comments_adapter(ArrayList<Comments> list_of_data) {
            this.mdatalist = list_of_data;
        }

        @NonNull
        @Override
        public comments_adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            comments_adapter.myviewholder mviewholder = new comments_adapter.myviewholder(view);
            return mviewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull final comments_adapter.myviewholder holder, int position) {
            holder.username.setText(mdatalist.get(position).getSenderUserName());
            holder.time.setText(mdatalist.get(position).getTime());
            holder.content.setText(mdatalist.get(position).getMessage());

            Glide.with(context)
                    .load(mdatalist.get(position).getSenderImageUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.sender_pic.setImageResource(R.drawable.ic_mood_black_24dp);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.sender_pic);

        }

        @Override
        public int getItemCount() {
            return mdatalist.size();
        }

        public class myviewholder extends RecyclerView.ViewHolder {
            TextView username, time, content;
            CircleImageView sender_pic;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                username = itemView.findViewById(R.id.comment_item_sender);
                time = itemView.findViewById(R.id.comment_item_time);
                content = itemView.findViewById(R.id.comment_item_message);
                sender_pic = itemView.findViewById(R.id.comment_item_photo);
            }
        }

    }

}
