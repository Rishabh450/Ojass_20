package ojass20.nitjsr.in.ojass.Fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import ojass20.nitjsr.in.ojass.Activities.EventsActivity;
import ojass20.nitjsr.in.ojass.Activities.GurugyanActivity;
import ojass20.nitjsr.in.ojass.Activities.ItineraryActivity;
import ojass20.nitjsr.in.ojass.Activities.MainActivity;
import ojass20.nitjsr.in.ojass.Activities.MapsActivity;
import ojass20.nitjsr.in.ojass.Models.HomePage;
import ojass20.nitjsr.in.ojass.R;

public class HomeFragment extends Fragment implements
        GestureDetector.OnGestureListener, View.OnClickListener {
    private static final long ANIM_DURATION = 500;
    private HomeFragInterface fragInterface;
    private RelativeLayout cancelBtn;
    private ArrayList<HomePage> mItems = new ArrayList<>();
    private int mInd;
    private ImageView bigCircle, c1, c2, c3, c4;
    private ArrayList<ImageView> mCircles = new ArrayList<>();
    private GestureDetectorCompat mDetector;
    private RelativeLayout swipeArea;
    private ConstraintLayout cl;
    private ImageView swipeImage1, swipeImage2, mFakeBackground1, mFakeBackground2, mFakeImage, mLeftArrow, mRightArrow;
    private TextView txt, mHeading;
    TranslateAnimation mAnimation;
    private ImageView mPullDown;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        onCancel();
        setUpArrayList();
        setUpAnimationForImageView(mPullDown);
        swipeArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mDetector != null) {
                    mDetector.onTouchEvent(event);
                    return true;
                }
                return false;
            }
        });

        if (mInd == 0)
            mLeftArrow.setColorFilter(Color.parseColor("#a9a9a9"));
        else
            mLeftArrow.setColorFilter(Color.parseColor("#ffffff"));

        if (mInd == mItems.size() - 1)
            mRightArrow.setColorFilter(Color.parseColor("#a9a9a9"));
        else
            mRightArrow.setColorFilter(Color.parseColor("#ffffff"));

        mLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mLeftArrow.setClickable(false);
                    swipeLeft();
                }
            }
        });

        mRightArrow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                mRightArrow.setClickable(false);
                swipeRight();
            }
        });

        setUpView(0);
        detectBottomTabClick();

        mHeading.setText(mItems.get(mInd).getmTitle());

        return view;
    }

    private void init(View view) {
        fragInterface = (HomeFragInterface) getActivity();
        cancelBtn = view.findViewById(R.id.cancel_layout);
        bigCircle = view.findViewById(R.id.bg_circle);
        swipeArea = view.findViewById(R.id.swipe_area);
        swipeImage1 = view.findViewById(R.id.img_swipe1);
        swipeImage2 = view.findViewById(R.id.img_swipe2);
        mFakeBackground1 = view.findViewById(R.id.fake_background1);
        mFakeBackground2 = view.findViewById(R.id.fake_background2);
        mHeading = view.findViewById(R.id.heading);
        mFakeImage = view.findViewById(R.id.fake_image);
        mLeftArrow = view.findViewById(R.id.left_arrow);
        mRightArrow = view.findViewById(R.id.right_arrow);

        mPullDown = view.findViewById(R.id.pull_down);
        txt = view.findViewById(R.id.home_frag_text);
        cl = view.findViewById(R.id.cl);
        c1 = view.findViewById(R.id.img1);
        c2 = view.findViewById(R.id.img2);
        c3 = view.findViewById(R.id.img3);
        c4 = view.findViewById(R.id.img4);

        mCircles.add(c1);
        mCircles.add(c2);
        mCircles.add(c3);
        mCircles.add(c4);
        mDetector = new GestureDetectorCompat(getContext(), this);
    }
    private void setUpAnimationForImageView(ImageView mImageView) {

        mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.005f);
        mAnimation.setDuration(700);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        mPullDown.startAnimation(mAnimation);
    }
    private void onCancel() {
        mPullDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragInterface.onCancel();
            }
        });
    }

    private void setUpArrayList() {
        mItems.add(new HomePage("Events", "#9B03FB", 0, R.drawable.ic_launcher_background, 0, R.drawable.square_events_images, R.drawable.violet_back));//purple
        mItems.add(new HomePage("Gurugyan", "#FB0303", 1, R.drawable.ic_launcher_foreground, -90, R.drawable.square_gurugyan_image, R.drawable.red_back));//red
        mItems.add(new HomePage("Itinerary", "#03FB2C", 2, R.drawable.ic_launcher_background, -180, R.drawable.square_itinerary_image, R.drawable.green_back));//green
        mItems.add(new HomePage("Maps", "#0F03FB", 3, R.drawable.ic_launcher_foreground, -270, R.drawable.square_maps_image, R.drawable.blue_back));//blue
    }

    private void detectBottomTabClick() {
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void swipeRight() {
        int u = mInd;
        mInd++;
        if (mInd >= mItems.size()) {
            mInd = 0;
            setUpView(-3);
        } else
            setUpView(-1);

        if (mInd == mItems.size() - 1)
            mRightArrow.setColorFilter(Color.parseColor("#a9a9a9"));
        else
            mRightArrow.setColorFilter(Color.parseColor("#ffffff"));

        mFakeBackground1.setImageDrawable(getActivity().getDrawable(mItems.get(u).getmBackground()));
        mFakeBackground2.setImageDrawable(getActivity().getDrawable(mItems.get(mInd).getmBackground()));

        imgAnimationLeft();
        setUpAnimationForTextView(-1, System.currentTimeMillis(), txt.getText().toString().toUpperCase());
        final RotateAnimation rotate = new RotateAnimation(mItems.get(u).getmAngle(), mItems.get(mInd).getmAngle(), Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        rotate.setFillAfter(true);

        final Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        fadeIn.setFillAfter(true);
        fadeOut.setFillAfter(true);
        swipeImage2.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                swipeImage1.startAnimation(rotate);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                swipeImage2.setImageDrawable(getActivity().getDrawable(mItems.get(mInd).getmImageSpecificId()));
                swipeImage2.startAnimation(fadeIn);
                mRightArrow.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        setTxtRight();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void swipeLeft() {
        int u = mInd;
        mInd--;
        if (mInd < 0) {
            mInd = mItems.size() - 1;
            setUpView(3);
        } else
            setUpView(1);

        if (mInd == 0)
            mLeftArrow.setColorFilter(Color.parseColor("#a9a9a9"));
        else
            mLeftArrow.setColorFilter(Color.parseColor("#ffffff"));

        mFakeBackground1.setImageDrawable(getActivity().getDrawable(mItems.get(u).getmBackground()));
        mFakeBackground2.setImageDrawable(getActivity().getDrawable(mItems.get(mInd).getmBackground()));

        imgAnimationRight();
        setUpAnimationForTextView(1, System.currentTimeMillis(), txt.getText().toString().toUpperCase());
        final RotateAnimation rotate = new RotateAnimation(mItems.get(u).getmAngle(), mItems.get(mInd).getmAngle(), Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        rotate.setFillAfter(true);
//        swipeImage1.startAnimation(rotate);

        final Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        fadeIn.setFillAfter(true);
        fadeOut.setFillAfter(true);
        swipeImage2.startAnimation(fadeOut);


        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                swipeImage1.startAnimation(rotate);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                swipeImage2.setImageDrawable(getActivity().getDrawable(mItems.get(mInd).getmImageSpecificId()));
                swipeImage2.startAnimation(fadeIn);
                mLeftArrow.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        setTxtLeft();
    }

    private void setTxtRight() {
        txt.setText(mItems.get(mInd).getmTitle());
    }

    private void setTxtLeft() {
        txt.setText(mItems.get(mInd).getmTitle());
    }

    private void setUpAnimationForTextView(final int code, final long mainTime, String curr) {
        long tempTime = System.currentTimeMillis();
        if (tempTime - mainTime > 500) {
            txt.setText(mItems.get(mInd).getmTitle());
            return;
        }
        String temp = " ";
        for (int i = 0; i < curr.length(); i++) {
            char ch = curr.charAt(i);
            if (code == 1) {
                if (ch == 'Z')
                    temp = temp + 'A';
                else {
                    int u = (int) ch;
                    u++;
                    temp = temp + (char) u;
                }
            } else {
                if (ch == 'A')
                    temp = temp + 'Z';
                else {
                    int u = (int) ch;
                    u--;
                    temp = temp + (char) u;
                }
            }
        }
        temp = temp.trim();
        txt.setText(temp);
        //Log.e(LOG_TAG, temp);
        final String x = temp;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpAnimationForTextView(code, mainTime, x.toUpperCase());
            }
        }, 10);
    }

    private void imgAnimationRight() {
//        Animation leftExit = AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_left);
//        mFakeBackground1.startAnimation(leftExit);
        Animation leftEntry = AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_left);
        mFakeBackground2.startAnimation(leftEntry);
    }

    private void imgAnimationLeft() {
//        Animation rightExit = AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_right);
//        mFakeBackground1.startAnimation(rightExit);
        Animation rightEntry = AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_right);
        mFakeBackground2.startAnimation(rightEntry);
    }

    public void setUpView(int counter) {
        if (counter == 0) {
            txt.setText(mItems.get(mInd).getmTitle());

        }
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        for (int i = 0; i < mCircles.size(); i++) {
            mCircles.get(i).setColorFilter(Color.parseColor(mItems.get(i).getmCircleColor()));
            animators.add(animateView(mCircles.get(i), 500, counter * 60));
            if (i != mInd) {
                animators.add(changeRadius(mCircles.get(i), 500, 128));
                animators.add(changeHeight(mCircles.get(i), 500, 20));
                animators.add(changeWidth(mCircles.get(i), 500, 20));
            } else {
                animators.add(changeRadius(mCircles.get(i), 500, 128));
                animators.add(changeHeight(mCircles.get(i), 500, 30));
                animators.add(changeWidth(mCircles.get(i), 500, 30));
            }
        }
        for (int i = 0; i < animators.size(); i++) {
            ValueAnimator anim = animators.get(i);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mDetector = null;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mDetector = new GestureDetectorCompat(getContext(), HomeFragment.this);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
        }
    }

    private ValueAnimator animateView(final ImageView imageView, long duration, long angle) {
        ConstraintLayout.LayoutParams lP = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt((int) lP.circleAngle, (int) lP.circleAngle + (int) angle);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.circleAngle = val;
                imageView.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    private ValueAnimator changeRadius(final ImageView imageView, long duration, int newRadius) {
        ConstraintLayout.LayoutParams lP = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt((int) lP.circleRadius, (int) MainActivity.convertDpToPixel(newRadius, getContext()));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.circleRadius = val;
                imageView.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    private ValueAnimator changeHeight(final ImageView imageView, long duration, int newHeight) {
        ConstraintLayout.LayoutParams lP = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt((int) lP.height, (int) MainActivity.convertDpToPixel(newHeight, getContext()));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.height = val;
                imageView.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    private ValueAnimator changeWidth(final ImageView imageView, long duration, int newWidth) {
        ConstraintLayout.LayoutParams lP = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt((int) lP.width, (int) MainActivity.convertDpToPixel(newWidth, getContext()));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.width = val;
                imageView.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        Log.e("HomeFrag", mFakeImage.getLeft() + " " + mFakeImage.getRight() + " " + mFakeImage.getTop() + " " + mFakeImage.getBottom());
//        Log.e("HomeFrag", e.getX() + " " + e.getY());
        if (((int) e.getX() >= mFakeImage.getLeft() && (int) e.getX() <= mFakeImage.getRight()) && ((int) e.getY() >= mFakeImage.getTop() && (int) e.getY() <= mFakeImage.getBottom())) {
            Log.e("HomeFrag", "I'm called");
            switch (mInd) {
                case 0:
                    startActivity(new Intent(getContext(), EventsActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(getContext(), GurugyanActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(getContext(), ItineraryActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(getContext(), MapsActivity.class));
                    break;
                default:
//                    Log.e("LOL", "Bhai sahab ye kis line mein aa gye aap?");
            }
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() < e2.getX()) {
            swipeLeft();
        } else {
            swipeRight();
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int iClicked = -1;
        for (int i = 0; i < mCircles.size(); i++) {
            if (v.getId() == mCircles.get(i).getId()) {
                iClicked = i;
                break;
            }
        }
        if (iClicked > mInd)
            swipeRight();
        else if (iClicked < mInd)
            swipeLeft();
//        return true;
    }

    public interface HomeFragInterface {
        void onCancel(); // close fragment
    }
}