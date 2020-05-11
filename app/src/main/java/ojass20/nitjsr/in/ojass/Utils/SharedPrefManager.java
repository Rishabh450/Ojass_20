package ojass20.nitjsr.in.ojass.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static final String SHARED_PREF = "SharedPref";
    private static final String IS_FIRST_OPEN = "isFirstOpen";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String LAST_NOTI_TIME = "lastNotiTime";
    private static final long OLD_TIME = 1517926000L;

    public SharedPrefManager(Context context){
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setIsFirstOpen(boolean isFirstOpen){
        editor.putBoolean(IS_FIRST_OPEN, isFirstOpen).apply();
    }

    public boolean isFirstOpen(){
        return sharedPref.getBoolean(IS_FIRST_OPEN, true);
    }

    public boolean isLoggedIn() {
        return sharedPref.getBoolean(IS_LOGGED_IN, false);
    }

    public void setIsLoggedIn(boolean isLoggedIn){
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn).apply();
    }

    public void setNotiTime(long timeInMillis){
        editor.putLong(LAST_NOTI_TIME, timeInMillis).apply();
    }

    public long getLastNotiTime(){
        return sharedPref.getLong(LAST_NOTI_TIME, OLD_TIME);
    }
}