package ojass20.nitjsr.in.ojass.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import ojass20.nitjsr.in.ojass.R;
import ojass20.nitjsr.in.ojass.Utils.SharedPrefManager;

public class SplashScreen extends AppCompatActivity {

    private static final int UPDATE_REQUEST_CODE = 101;
    private SharedPrefManager sharedPrefManager;
    int SPLASH_DISPLAY_LENGTH = 2000;
    ImageView circle, arrow, yr, pi;
    private static final int WALKTHROUGH = 1;
    private static final int LOGIN = 2;
    private static final int DASHBOARD = 3;
    private int destinationFlag;
    private AppUpdateManager appUpdateManager;
    private int val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences("updateFlag",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("update",true);
        editor.commit();
//        val = getIntent().getIntExtra("Caller", -1);
//        Log.e("Hey", "" + val);
//        if (val == 0) {
//            Intent intent = new Intent(SplashScreen.this, NotificationActivity.class);
////            intent.putExtra("Caller", 0);
//            startActivity(intent);
//            finish();
//            return;
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        changeStatusBarColor(this);

        circle = findViewById(R.id.circle);
        arrow = findViewById(R.id.arrow);
        yr = findViewById(R.id.yr);
        pi = findViewById(R.id.pi);
        destinationFlag = getDestinationActivity();
        animation();
        doTheDelayStuff();
    }

    public static void changeStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void animation() {

//        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(mImageView, "scaleX", 5.0F, 1.0F);
//        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        scaleXAnimation.setDuration(1200);
//
//        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(mImageView, "scaleY", 5.0F, 1.0F);
//        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        scaleYAnimation.setDuration(1200);
//
//        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mImageView, "alpha", 0.0F, 1.0F);
//        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        alphaAnimation.setDuration(1200);
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
//        animatorSet.start();
        Animation anim_circle = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.slide_in_bottom_splash);
        Animation anim_arrow = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.slide_in_top_splash);
        Animation anim_pi = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fade_in_splash);
        circle.startAnimation(anim_circle);
        arrow.startAnimation(anim_arrow);
        pi.startAnimation(anim_pi);
        yr.startAnimation(anim_pi);
        yr.startAnimation(anim_pi);
    }

    private void doTheDelayStuff() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inAppUpdate();
                switch (destinationFlag) {
                    case DASHBOARD:
                        moveToMainActivity();
                        break;
                    case LOGIN:
                        moveToLoginPage();
                        break;
                    case WALKTHROUGH:
                        moveToWalkthrough();
                        break;
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void inAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {

                Log.e("AVAILABLE_VERSION_CODE", appUpdateInfo.availableVersionCode() + "");
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.

                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                appUpdateInfo,
                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                AppUpdateType.IMMEDIATE,
                                // The current activity making the update request.
                                SplashScreen.this,
                                // Include a request code to later monitor this update request.
                                UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException ignored) {

                    }
                }
            }
        });

        appUpdateManager.registerListener(installStateUpdatedListener);

    }

    //lambda operation used for below listener
    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState installState) {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                SplashScreen.this.popupSnackbarForCompleteUpdate();
            } else
                Log.e("UPDATE", "Not downloaded yet");
        }
    };
//    private void inAppUpdate() {
//        // Creates instance of the manager.
//        appUpdateManager = AppUpdateManagerFactory.create(this);
//
//        // Returns an intent object that you use to check for an update.
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        // Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
//            @Override
//            public void onSuccess(AppUpdateInfo appUpdateInfo) {
//
//                Log.e("AVAILABLE_VERSION_CODE", appUpdateInfo.availableVersionCode()+"");
//                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                        // For a flexible update, use AppUpdateType.FLEXIBLE
//                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                    // Request the update.
//
//                    try {
//                        appUpdateManager.startUpdateFlowForResult(
//                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
//                                appUpdateInfo,
//                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
//                                AppUpdateType.IMMEDIATE,
//                                // The current activity making the update request.
//                                SplashScreen.this,
//                                // Include a request code to later monitor this update request.
//                                UPDATE_REQUEST_CODE);
//                    } catch (IntentSender.SendIntentException ignored) {
//
//                    }
//                }
//            }
//        });
//
//        appUpdateManager.registerListener(installStateUpdatedListener);
//
//    }
//    //lambda operation used for below listener
//    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
//        @Override
//        public void onStateUpdate(InstallState installState) {
//            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
//                SplashScreen.this.popupSnackbarForCompleteUpdate();
//            } else
//                Log.e("UPDATE", "Not downloaded yet");
//        }
//    };


    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "Update almost finished!",
                        Snackbar.LENGTH_INDEFINITE);
        //lambda operation used for below action
        snackbar.setAction(this.getString(R.string.restart), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }

    private int getDestinationActivity() {
        sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.isFirstOpen()) {
            sharedPrefManager.setIsFirstOpen(false);
            return WALKTHROUGH;
        } else {
            if (sharedPrefManager.isLoggedIn()) {
                return DASHBOARD;
            } else {
                return LOGIN;
            }
        }
    }

    private void moveToLoginPage() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void moveToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void moveToWalkthrough() {
        startActivity(new Intent(this, WalkThroughActivity.class));
        finish();
    }
}