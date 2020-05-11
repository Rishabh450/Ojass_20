package ojass20.nitjsr.in.ojass.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import ojass20.nitjsr.in.ojass.Activities.ActivityZoomableImage;
import ojass20.nitjsr.in.ojass.Activities.MainActivity;
import ojass20.nitjsr.in.ojass.Fragments.CommentsFragment;
import ojass20.nitjsr.in.ojass.Models.FeedPost;
import ojass20.nitjsr.in.ojass.Models.Likes;
import ojass20.nitjsr.in.ojass.R;
import static ojass20.nitjsr.in.ojass.Utils.Utilities.makeTextViewResizable;
import static ojass20.nitjsr.in.ojass.Utils.Utilities.setGlideImageAdjustedSize;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CustomViewHolder> {

    private static final int MAX_LINES = 3;
    private static final String TWO_SPACES = "  ";
    public Context context;
    private ArrayList<FeedPost> feedPosts;
    private String mcurrentuid;
    //private boolean is_already_liked=false;
    private String mpost_id = "";
    public CommentClickInterface clickInterface;
    private FragmentManager manager;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;
    String muid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout feedLayout;
        public TextView subevent_name, like_text, eventname, time, content;
        public ImageView like_icon, postImage;
        public LinearLayout like_layout, comment_layout, share_layout;
        RelativeLayout postImageView;
        ProgressBar progressBar;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            feedLayout = (LinearLayout) itemView;
            progressBar = itemView.findViewById(R.id.feed_post_image_progress_bar);
            postImageView = itemView.findViewById(R.id.feed_post_image_parent);
            postImage = itemView.findViewById(R.id.feed_post_image);
            subevent_name = itemView.findViewById(R.id.feed_post_sub_event_name);
            eventname = itemView.findViewById(R.id.feed_post_event_name);
            content = itemView.findViewById(R.id.feed_post_content);
            like_text = itemView.findViewById(R.id.like_textview);
            like_icon = itemView.findViewById(R.id.like_icon);
            like_layout = itemView.findViewById(R.id.feed_post_upvote);
            comment_layout = itemView.findViewById(R.id.comments_post);
            share_layout = itemView.findViewById(R.id.feed_post_share);
            time = itemView.findViewById(R.id.feed_post_time);

        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout feedLayout = (LinearLayout) (LayoutInflater.from(context).inflate(
                R.layout.feed_item, parent, false));
        return new CustomViewHolder(feedLayout);
    }

    public FeedAdapter(Context context, FragmentManager manager, ArrayList<FeedPost> mfeedPosts, String currentuid, MainActivity mainActivity, RecyclerView recyclerView) {
        this.context = context;
        this.feedPosts = mfeedPosts;
        this.mcurrentuid = currentuid;
        this.manager = manager;
        this.mainActivity = mainActivity;
        clickInterface = (CommentClickInterface) context;
        this.recyclerView = recyclerView;
        Log.e("VIVZ", "FeedAdapter: called COUNT = " + mfeedPosts.size());
    }

    @Override
    public int getItemCount() {
        return feedPosts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        FeedPost fp = feedPosts.get(position);
        holder.eventname.setText(feedPosts.get(position).getEvent());
        holder.subevent_name.setText(feedPosts.get(position).getSubEvent());
        holder.content.setText(feedPosts.get(position).getContent());
        makeTextViewResizable(holder.content, 3, "read more", true, recyclerView, position, fp);
        holder.like_text.setText(feedPosts.get(position).getLikes().size() + " Likes");
        holder.postImageView.setVisibility(View.VISIBLE);
        holder.progressBar.setVisibility(View.GONE);
        Log.e("Hey", feedPosts.get(position).getImageURL());
        if (feedPosts.get(position).getImageURL() == null || feedPosts.get(position).getImageURL().equals("")) {
            holder.postImageView.setVisibility(View.GONE);
        } else {
            setGlideImageAdjustedSize(context, feedPosts.get(position).getImageURL(), holder.postImage);
        }
        mpost_id = feedPosts.get(position).getPostid();

        //is_already_liked=feedPosts.get(position).isIs_already_liked();
        if (feedPosts.get(position).isIs_already_liked() == true) {
            holder.like_icon.setImageResource(R.drawable.upvoted);
        } else {
            holder.like_icon.setImageResource(R.drawable.upvote);
        }

        //Log.i("onBindViewHolder: ", is_already_liked + "");

        long post_time = Integer.parseInt(feedPosts.get(position).getTimestamp());
        long curr_time = System.currentTimeMillis() / 1000;
        long diff = curr_time - post_time;
        String suffix, prefix;
        if (diff < 5) {
            prefix = "just now";
            suffix = "";
        } else if (diff < 60) {
            suffix = "s ago";
            prefix = diff + "";
        } else if (diff < 3600) {
            suffix = "m ago";
            prefix = diff / 60 + "";
        } else if (diff < 86400) {
            suffix = "hr ago";
            prefix = diff / 3600 + "";
        } else if (diff < 2628003) {
            suffix = "d ago";
            prefix = diff / 86400 + "";
        } else if (diff < 31536000) {
            suffix = "mo ago";
            prefix = diff / 2628003 + "";
        } else {
            suffix = "y ago";
            prefix = diff / 31536000 + "";
        }

        holder.time.setText(prefix + suffix);


        final DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Feeds");

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityZoomableImage.class);
                i.putExtra("img", feedPosts.get(position).getImageURL());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((MainActivity) context,
                        holder.postImage, "feedsImg");
                context.startActivity(i, options.toBundle());
            }
        });
        holder.comment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.setIsCommentsFragmentOpen();
                CommentsFragment fragment = new CommentsFragment(context, manager,
                        feedPosts.get(position).getPostid(), mainActivity);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.no_anim);
                transaction.add(R.id.home_container, fragment);
                transaction.commit();
            }
        });

        holder.share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Log.e(TAG, "setxml: peremission prob");
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);


                } else {
                    Uri imgUri = null;
                    Drawable dr = ((ImageView) holder.postImage).getDrawable();
                    if (dr != null) {
                        Bitmap imgBitmap = drawableToBitmap(dr);
                        String imgBitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), imgBitmap, "title", null);
                        imgUri = Uri.parse(imgBitmapPath);


                    }
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, imgUri);

                    sendIntent.putExtra(Intent.EXTRA_TEXT, feedPosts.get(position).getEvent() + "\n" +
                            feedPosts.get(position).getSubEvent() + "\n" +
                            feedPosts.get(position).getContent());
                    sendIntent.setType("*/*");
                    context.startActivity(Intent.createChooser(sendIntent, "Share this article via:"));
                }

            }
        });

        holder.like_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("PostNo",Integer.toString(position));
//                editor.apply();

                //HashMap<String,Object> nc=new HashMap<>();
                if (feedPosts.get(position).isIs_already_liked()) {
                    Log.e("TAG", "onClick: level 2");
                    //nc.put(mcurrentuid,null);
                    dref.child(feedPosts.get(position).getPostid()).child("likes").child(mcurrentuid).setValue(null);

                    for (Likes lk : feedPosts.get(position).getLikes()) {
                        if (lk.getUser_id().equals(muid)) {
                            feedPosts.get(position).getLikes().remove(lk);
                            break;
                        }
                    }

                    holder.like_text.setText(feedPosts.get(position).getLikes().size() + " Likes");
                    feedPosts.get(position).setIs_already_liked(false);

                    holder.like_icon.setImageResource(R.drawable.upvote);
                } else {
                    Log.e("TAG", "onClick: level 6");
                    dref.child(feedPosts.get(position).getPostid()).child("likes").child(mcurrentuid).setValue(mcurrentuid);


                    feedPosts.get(position).getLikes().add(new Likes(muid));
                    holder.like_text.setText(feedPosts.get(position).getLikes().size() + " Likes");
                    feedPosts.get(position).setIs_already_liked(true);
                    holder.like_icon.setImageResource(R.drawable.upvoted);
                }

            }
        });

    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;

    }

    public interface CommentClickInterface {
        void onCommentClick();
    }
}