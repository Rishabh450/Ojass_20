package ojass20.nitjsr.in.ojass.Utils;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import ojass20.nitjsr.in.ojass.Activities.NotificationActivity;
import ojass20.nitjsr.in.ojass.Activities.SplashScreen;

public class OjassApplication extends Application {
    private static OjassApplication mInstance;

    public OjassApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public static synchronized OjassApplication getInstance() {
        return mInstance;
    }

    class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            Intent intent = new Intent(OjassApplication.this,NotificationActivity.class);
            intent.putExtra("nottap",true);
            startActivity(intent);
        }
    }
}
