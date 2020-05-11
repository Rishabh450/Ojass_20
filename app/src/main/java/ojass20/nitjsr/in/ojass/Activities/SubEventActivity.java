package ojass20.nitjsr.in.ojass.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Batch;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import ojass20.nitjsr.in.ojass.Adapters.SubEventsAdapter;
import ojass20.nitjsr.in.ojass.Fragments.EventBottomSheet;
import ojass20.nitjsr.in.ojass.Models.BranchHeadModal;
import ojass20.nitjsr.in.ojass.Models.SubEventsModel;
import ojass20.nitjsr.in.ojass.R;
import ojass20.nitjsr.in.ojass.Utils.Constants;
import ojass20.nitjsr.in.ojass.Utils.RecyclerClickInterface;

import static ojass20.nitjsr.in.ojass.Utils.Constants.SubEventsList;
import static ojass20.nitjsr.in.ojass.Utils.Constants.eventNames;
import static ojass20.nitjsr.in.ojass.Utils.Constants.mBranchEvents;
import static ojass20.nitjsr.in.ojass.Utils.Constants.subscribedEvents;
import static ojass20.nitjsr.in.ojass.Utils.Utilities.setGlideImage;

public class SubEventActivity extends AppCompatActivity {
    private ImageView mAboutLayout, mHeadLayout;
    private RecyclerView rView;
    private ArrayList<SubEventsModel> data = new ArrayList<>();
    private int mainEventPosition;
    private ImageView iv;
    private FrameLayout frag_frame;
    private static boolean bottomSheetOpen = false;
    private static int toolbarIconColor = Color.BLACK;
    private Animation fadeIn, fadeOut;
    Toolbar toolbar;
    public static int position;
    public static String actionBarTitle;
    private boolean isOpen;
    private ArrayList<LinearLayout> mLL;
    private ArrayList<ImageView> mProfile;
    private ArrayList<ImageView> mWhatsapp;
    private ArrayList<ImageView> mCall;
    private ArrayList<TextView> mName;
    private TextView mAbout, mHeading;
    private View mDivider1, mDivider2;
    private ArrayList<String> mSubEventName;
    private SubEventsAdapter mAdapter;
    private String mEventName;
    private FirebaseAuth mAuth;
    private ArrayList<ValueAnimator> animators;
    private Menu menu;
    private SpeedDialView sv_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_event);
        init();

        //get intent get main event
        mainEventPosition = getIntent().getIntExtra("event_id", 0); //from intent
        mSubEventName = getIntent().getStringArrayListExtra("sub_event_name");
        rView.setLayoutManager(new LinearLayoutManager(this));

        manageToolbar();
        setUpFabItems();
        //on item click
        RecyclerClickInterface mInterface = new RecyclerClickInterface() {
            @Override
            public void onLayoutClick(View v, int position) {
                getSupportActionBar().setTitle(SubEventsList.get(mainEventPosition).get(position));
                showBottomSheet();
                bottomSheetOpen = true;
                getPostion(position);
            }
        };

        if (mSubEventName != null) {
            showBottomSheet();
            bottomSheetOpen = true;
            getPostion(mSubEventName.get(0));
        }

        mAdapter = new SubEventsAdapter(this, getData(), mInterface);
        rView.setAdapter(mAdapter);
        //set Image
        iv.setImageResource(Constants.eventImg[mainEventPosition]);

        sv_fab.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener()
        {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem)
            {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_layout, viewGroup, false);
                mLL = new ArrayList<>();
                mLL.add((LinearLayout) dialogView.findViewById(R.id.one));
                mLL.add((LinearLayout) dialogView.findViewById(R.id.two));
                mLL.add((LinearLayout) dialogView.findViewById(R.id.three));
                mAbout = dialogView.findViewById(R.id.about_data);
                if (actionItem.getId() == R.id.item1) {
                    //Toast.makeText(SubEventActivity.this, "About clicked dumbass", Toast.LENGTH_SHORT).show();
                    mLL.get(0).setVisibility(View.GONE);
                    mLL.get(1).setVisibility(View.GONE);
                    mLL.get(2).setVisibility(View.GONE);
                    mAbout.setVisibility(View.VISIBLE);
                    Rect displayRectangle = new Rect();
                    Window window = SubEventActivity.this.getWindow();
                    window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SubEventActivity.this, R.style.CustomAlertDialog);

//                dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
//                dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();


                    mHeading = dialogView.findViewById(R.id.heading);
                    SpannableString s = new SpannableString(Constants.eventNames.get(mainEventPosition));
                    s.setSpan(new UnderlineSpan(), 0, s.length(), 0);
                    mHeading.setText(s);
                    mAbout.setText(Html.fromHtml(MainActivity.branchData.get(Constants.eventNames.get(mainEventPosition)).getAbout()));

                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    alertDialog.show();
                    Button dismiss_button = dialogView.findViewById(R.id.dismiss_dialog);
                    dismiss_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (alertDialog.isShowing()) alertDialog.dismiss();
                        }
                    });

                    return true;
                } else if (actionItem.getId() == R.id.item2)
                {
                    //Toast.makeText(SubEventActivity.this, "Heads clicked dumbass", Toast.LENGTH_SHORT).show();
                    mAbout.setVisibility(View.GONE);
                    Rect displayRectangle = new Rect();
                    Window window = SubEventActivity.this.getWindow();
                    window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SubEventActivity.this, R.style.CustomAlertDialog);

                    final ArrayList<BranchHeadModal> bhNames = MainActivity.branchData.get(Constants.eventNames.get(mainEventPosition)).getBranchHead();
                    dialogView.setMinimumWidth((int) (displayRectangle.width() / 1.5f * 1f));
                    dialogView.setMinimumHeight((int) (displayRectangle.height() / 5f * 1f));
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();

                    mProfile = new ArrayList<>();
                    mCall = new ArrayList<>();
                    mWhatsapp = new ArrayList<>();
                    mName = new ArrayList<>();
                    mHeading = dialogView.findViewById(R.id.heading);
                    String s = "Segment Head";
                    Log.e("Segment", mEventName);
                    if (mBranchEvents.contains(mEventName.trim()))
                        s = "Branch Head";
                    mHeading.setText(s);


                    mProfile.add((ImageView) dialogView.findViewById(R.id.img1));
                    mProfile.add((ImageView) dialogView.findViewById(R.id.img2));
                    mProfile.add((ImageView) dialogView.findViewById(R.id.img3));

                    mWhatsapp.add((ImageView) dialogView.findViewById(R.id.whatsapp1));
                    mWhatsapp.add((ImageView) dialogView.findViewById(R.id.whatsapp2));
                    mWhatsapp.add((ImageView) dialogView.findViewById(R.id.whatsapp3));

                    mName.add((TextView) dialogView.findViewById(R.id.name1));
                    mName.add((TextView) dialogView.findViewById(R.id.name2));
                    mName.add((TextView) dialogView.findViewById(R.id.name3));

                    mCall.add((ImageView) dialogView.findViewById(R.id.call1));
                    mCall.add((ImageView) dialogView.findViewById(R.id.call2));
                    mCall.add((ImageView) dialogView.findViewById(R.id.call3));

                    mDivider1 = dialogView.findViewById(R.id.divider1);
                    mDivider2 = dialogView.findViewById(R.id.divider2);
                    if (bhNames.size() == 1) {
                        mLL.get(0).setVisibility(View.VISIBLE);
                        mName.get(0).setText(bhNames.get(0).getName());

                        final Intent intent = new Intent(Intent.ACTION_DIAL);
                        for (int j = 0; j < 1; j++) {
                            final int i = j;
                            mCall.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent.setData(Uri.parse("tel:" + bhNames.get(i).getCn()));
                                    startActivity(intent);
                                }
                            });
                            mWhatsapp.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = "https://api.whatsapp.com/send?phone=" + "+91" + bhNames.get(i).getWn();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });
                            try {
                                setGlideImage(SubEventActivity.this, bhNames.get(i).getImg(), mProfile.get(i));
                            } catch (Exception e) {
                                Log.d("hello", bhNames.get(i).getName());
                            }
                        }
                    } else if (bhNames.size() == 2) {
                        mLL.get(0).setVisibility(View.VISIBLE);
                        mLL.get(1).setVisibility(View.VISIBLE);
                        mName.get(0).setText(bhNames.get(0).getName());
                        mName.get(1).setText(bhNames.get(1).getName());
                        mDivider1.setVisibility(View.VISIBLE);
                        final Intent intent = new Intent(Intent.ACTION_DIAL);
                        for (int j = 0; j < 2; j++) {
                            final int i = j;
                            mCall.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent.setData(Uri.parse("tel:" + bhNames.get(i).getCn()));
                                    startActivity(intent);
                                }
                            });
                            mWhatsapp.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = "https://api.whatsapp.com/send?phone=" + "+91" + bhNames.get(i).getWn();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });
                            try {
                                setGlideImage(SubEventActivity.this, bhNames.get(i).getImg(), mProfile.get(i));
                            } catch (Exception e) {
                                Log.d("hello", bhNames.get(i).getName());
                            }
                        }
                    } else {
                        mLL.get(0).setVisibility(View.VISIBLE);
                        mLL.get(1).setVisibility(View.VISIBLE);
                        mLL.get(2).setVisibility(View.VISIBLE);
                        mDivider1.setVisibility(View.VISIBLE);
                        mDivider2.setVisibility(View.VISIBLE);
                        mName.get(0).setText(bhNames.get(0).getName());
                        mName.get(1).setText(bhNames.get(1).getName());
                        mName.get(2).setText(bhNames.get(2).getName());
                        final Intent intent = new Intent(Intent.ACTION_DIAL);
                        for (int j = 0; j < 3; j++) {
                            final int i = j;
                            mCall.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent.setData(Uri.parse("tel:" + bhNames.get(i).getCn()));
                                    startActivity(intent);
                                }
                            });
                            mWhatsapp.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = "https://api.whatsapp.com/send?phone=" + "+91" + bhNames.get(i).getWn();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });
                            try {
                                setGlideImage(SubEventActivity.this, bhNames.get(i).getImg(), mProfile.get(i));
                            } catch (Exception e) {
                                Log.d("hello", bhNames.get(i).getName());
                            }
                        }
                    }
                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    alertDialog.show();
                    TextView dismiss_button = dialogView.findViewById(R.id.dismiss_dialog);
                    dismiss_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (alertDialog.isShowing()) alertDialog.dismiss();
                        }
                    });

                    return true;
                }
                return false;
            }
        });


    }

    private void setUpFabItems() {
        manageToolbar();
        sv_fab.addActionItem(new SpeedDialActionItem.Builder(R.id.item1, getResources().getDrawable(R.drawable.ic_info_black_24dp))
                .setFabBackgroundColor(Color.WHITE)
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.navHeader, getTheme()))
                .setLabel("About")
                .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.navHeader, getTheme()))
                .setLabelBackgroundColor(Color.WHITE)
                .setLabelClickable(true)
                .create());
        String s = "Segment Head";
        Log.e("Segment", mEventName);
        if (mBranchEvents.contains(mEventName.trim()))
            s = "Branch Head";
        sv_fab.addActionItem(new SpeedDialActionItem.Builder(R.id.item2, getResources().getDrawable(R.drawable.ic_group_black_24dp))
                .setFabBackgroundColor(Color.WHITE)
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.navHeader, getTheme()))
                .setLabel(s)
                .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.navHeader, getTheme()))
                .setLabelBackgroundColor(Color.WHITE)
                .setLabelClickable(true)
                .create());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (bottomSheetOpen) {
            hideBottomSheet();
            bottomSheetOpen = false;
        } else {
            mAdapter = null;
            finishAfterTransition();
        }
    }

    private void manageToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(Constants.eventNames.get(mainEventPosition));
        mEventName = Constants.eventNames.get(mainEventPosition);
        //manage toolbar icons and text color
        BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
        Palette.from(drawable.getBitmap()).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                Palette.Swatch textSwatch = palette.getDarkMutedSwatch();

                if (textSwatch != null) {
                    toolbarIconColor = textSwatch.getTitleTextColor();
                    toolbar.setTitleTextColor(textSwatch.getTitleTextColor());
                }
            }
        });
    }

    private void init() {
        sv_fab = findViewById(R.id.speedDial);
        mAuth = FirebaseAuth.getInstance();
        rView = findViewById(R.id.sub_rv);
        iv = findViewById(R.id.transition_img);
        frag_frame = findViewById(R.id.fragment_layout);
        toolbar = findViewById(R.id.toolbar);
        isOpen = false;
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        animators = new ArrayList<>();

    }

    ArrayList<SubEventsModel> getData() {
        Log.e("SubEvent", SubEventsList.size() + "");
        HashMap<Integer, ArrayList<String>> subEventsName = SubEventsList;
        data.clear();
        Log.e("xx", "called again " + subEventsName.size());
        HashSet<String> hashSet = new HashSet<>();
        for (int i = 0; i < subEventsName.get(mainEventPosition).size(); i++) {
            if (!hashSet.contains(subEventsName.get(mainEventPosition).get(i))) {
                hashSet.add(subEventsName.get(mainEventPosition).get(i));
                data.add(new SubEventsModel(subEventsName.get(mainEventPosition).get(i)));
            }
        }
        return data;
    }

    public void showBottomSheet() {

        EventBottomSheet bottomSheet = new EventBottomSheet();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.no_anim);
        transaction.add(R.id.fragment_layout, bottomSheet);
        transaction.commit();

        //Change toolbar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        toolbar.setBackgroundColor(Color.BLACK);
    }

    private void hideBottomSheet() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.no_anim, R.anim.slide_out_bottom);
        transaction.remove(f).commit();

        //change toolbar
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        backArrow.setColorFilter(toolbarIconColor, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        toolbar.setTitle(Constants.eventNames.get(mainEventPosition));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subevents_menu, menu);
        this.menu = menu;
        if (checkSubscribe()) {
            menu.findItem(R.id.subscribe).setIcon(R.drawable.ic_subscibed);
        } else {
            menu.findItem(R.id.subscribe).setIcon(R.drawable.ic_unsubscribed);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (bottomSheetOpen) {
                hideBottomSheet();
                bottomSheetOpen = false;

            } else {
                //go to EventsActivity with transition
                finishAfterTransition();
            }
        } else if (item.getItemId() == R.id.subscribe) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SubscribedEvents");
            //onSubscribe
            Log.e("SUBSCRIBE", eventNames.get(mainEventPosition));
            item.setEnabled(false);
//            final MenuItem menuItem = menu.findItem(R.id.subscribe);
//            showDialog
            if (checkSubscribe()) {
                //unsubscribe
                Log.e("TAB", "LOL");

                unSubscribeEvent(ref);
            } else {
                //subscribe

                String key = ref.push().getKey();
                Map<String, String> val = new HashMap<>();
                val.put("name", eventNames.get(mainEventPosition));
                val.put("uid", mAuth.getUid());
                ref.child(key).setValue(val).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        subscribedEvents.add(eventNames.get(mainEventPosition));
                        Toast.makeText(SubEventActivity.this, "Subscribed to " + eventNames.get(mainEventPosition), Toast.LENGTH_SHORT).show();
                        menu.findItem(R.id.subscribe).setIcon(R.drawable.ic_subscibed);
                        item.setEnabled(true);
                    }
                });

            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void unSubscribeEvent(final DatabaseReference ref) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("uid").getValue(String.class).compareTo(mAuth.getUid()) == 0
                                && ds.child("name").getValue(String.class).compareTo(eventNames.get(mainEventPosition)) == 0
                        ) {
                            ds.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    menu.findItem(R.id.subscribe).setIcon(R.drawable.ic_unsubscribed);
                                    menu.findItem(R.id.subscribe).setEnabled(true);
                                    Toast.makeText(SubEventActivity.this, "Unsubscribed from " + eventNames.get(mainEventPosition), Toast.LENGTH_SHORT).show();
                                    Iterator<String> it = subscribedEvents.iterator();
                                    while (it.hasNext()) {
                                        String s = it.next();
                                        if (s.compareTo(eventNames.get(mainEventPosition)) == 0)
                                            it.remove();
                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean checkSubscribe() {

        if (subscribedEvents.contains(eventNames.get(mainEventPosition))) {
            return true;

        }
        return false;
    }

    public void getPostion(int pos) {
        String event = SubEventsList.get(mainEventPosition).get(pos).trim();
        Log.e("this", "" + event);
        for (int i = 0; i < MainActivity.data.size(); i++) {
            try {
                if (event.equalsIgnoreCase(MainActivity.data.get(i).getName().trim())) {
                    position = i;
                    break;
                }
            } catch (Exception e) {
                Log.e("Sub", e.getLocalizedMessage());
            } finally {
                continue;
            }
        }
    }

    public void getPostion(String subEvent) {
        for (int i = 0; i < MainActivity.data.size(); i++) {
            try {
                if (subEvent.equalsIgnoreCase(MainActivity.data.get(i).getName().trim())) {
                    position = i;
                    toolbar.setTitle(MainActivity.data.get(i).getName().trim());
                    toolbar.setTitle(subEvent);
                    break;
                }
            } catch (Exception e) {
                Log.e("Sub", e.getLocalizedMessage());
            } finally {
                continue;
            }
        }
    }
}
