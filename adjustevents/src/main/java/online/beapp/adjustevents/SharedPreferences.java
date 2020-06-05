package online.beapp.adjustevents;

import android.content.Context;

public class SharedPreferences {

    private static final String USER_KEY = "USER_KEY";
    private static String SHARED_PREFS_FILE_NAME = "ADJUST_FILE_PREFS";

    private static android.content.SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static String getUser(Context context) {
        return getPrefs(context).getString(USER_KEY,"empty");
    }

    public static void setUser(Context context, String user) {
        getPrefs(context).edit().putString(USER_KEY, user).apply();
    }
}
