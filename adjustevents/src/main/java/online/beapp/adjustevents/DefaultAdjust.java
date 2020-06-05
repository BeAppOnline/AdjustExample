package online.beapp.adjustevents;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.android.billingclient.api.Purchase;
import com.google.gson.Gson;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class DefaultAdjust implements iAdjust {
    private static final String TAG = DefaultAdjust.class.getSimpleName();

    private Context context;
    private String adjust_success_event, adjust_error_event, adjust_restore_event;

    public DefaultAdjust(Context context, String successToken, String errorToken, String restoreToken) {
        this.adjust_success_event = successToken;
        this.adjust_error_event = errorToken;
        this.adjust_restore_event = restoreToken;
        this.context = context;
    }

    @Override
    public void sendSuccessEvent(Purchase purchase, int responseCode) {
        String token = adjust_success_event;
        AdjustEvent adjustEvent = getParamsAdjust(token,purchase);
        adjustEvent.addCallbackParameter("eventValue", "OK " + getReason(responseCode));
        Adjust.trackEvent(adjustEvent);
        Log.w(TAG,"sendSuccessEvent: " + token + " responseCode: " + getReason(responseCode) + ", eventValue: OK");

    }

    @Override
    public void sendFailureEvent(int responseCode, String reason, @Nullable Purchase purchase) {
        String token = adjust_error_event;
        AdjustEvent adjustEvent = new AdjustEvent(token);
        if (purchase != null) {
            adjustEvent = getParamsAdjust(token, purchase);
            adjustEvent.addCallbackParameter("eventValue", "Failed" + getReason(responseCode));
            Adjust.trackEvent(adjustEvent);
        } else {
            adjustEvent.addCallbackParameter("eventValue", "Failed" + getReason(responseCode));
            adjustEvent.addCallbackParameter("inAppSku", "com.smartmobiletech.picseditor.subs");
            if (getUser().isFromDeeplink())
                adjustEvent.addCallbackParameter("inAppDeeplink", getUser().getDeeplinkURL());
            Adjust.trackEvent(adjustEvent);
            Log.w(TAG, "sendFailureEvent " + token + " Reason: " + reason);
        }
    }

    @Override
    public void sendRestoreEvent(Purchase purchase, int responseCode) {
        String token = adjust_restore_event;
        AdjustEvent adjustEvent = getParamsAdjust(token, purchase);
        Adjust.trackEvent(adjustEvent);
        Log.w(TAG, "sendRestoreEvent: " + token + "responseCode: " + responseCode);
    }

    private AdjustEvent getParamsAdjust(String token, Purchase purchase) {
        Log.w(TAG, "Setup Params");
        AdjustEvent adjustEvent = new AdjustEvent(token);
        adjustEvent.addCallbackParameter("inAppPurchaseTime", String.valueOf(purchase.getPurchaseTime()));
        adjustEvent.addCallbackParameter("inAppPurchaseToken", purchase.getPurchaseToken());
        adjustEvent.addCallbackParameter("inAppSku", purchase.getSku());
        adjustEvent.addCallbackParameter("inAppPackageName", purchase.getPackageName());
        adjustEvent.addCallbackParameter("inAppDeveloperPayload", purchase.getDeveloperPayload());
        if (getUser().isFromDeeplink()) {
            adjustEvent.addCallbackParameter("inAppDeeplink", getUser().getDeeplinkURL());
        }
        return adjustEvent;
    }

    private String getReason(int responseCode) {
        switch (responseCode) {
            case -3:
                return "SERVICE_TIMEOUT";
            case -2:
                return "FEATURE_NOT_SUPPORTED";
            case -1:
                return "SERVICE_DISCONNECTED";
            case 0:
                return "OK";
            case 1:
                return "USER_CANCELED";
            case 2:
                return "SERVICE_UNAVAILABLE";
            case 3:
                return "BILLING_UNAVAILABLE";
            case 4:
                return "ITEM_UNAVAILABLE";
            case 5:
                return "DEVELOPER_ERROR";
            case 6:
                return "ERROR";
            case 7:
                return "ITEM_ALREADY_OWNED";
            case 8:
                return "ITEM_NOT_OWNED";

            default:
                return "Unknown";
        }
    }

    private User getUser() {
        Gson gson = new Gson();
        String userJSON = SharedPreferences.getUser(context);
        if (userJSON.equals("empty")) {
            return new User(false,false);
        }
        return gson.fromJson(SharedPreferences.getUser(context),User.class);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ResponseCode {
        int SERVICE_TIMEOUT = -3;
        int FEATURE_NOT_SUPPORTED = -2;
        int SERVICE_DISCONNECTED = -1;
        int OK = 0;
        int USER_CANCELED = 1;
        int SERVICE_UNAVAILABLE = 2;
        int BILLING_UNAVAILABLE = 3;
        int ITEM_UNAVAILABLE = 4;
        int DEVELOPER_ERROR = 5;
        int ERROR = 6;
        int ITEM_ALREADY_OWNED = 7;
        int ITEM_NOT_OWNED = 8;
    }
}
