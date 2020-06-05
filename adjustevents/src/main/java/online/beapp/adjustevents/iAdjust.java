package online.beapp.adjustevents;

import androidx.annotation.Nullable;
import com.android.billingclient.api.Purchase;

public interface iAdjust {
    void sendSuccessEvent(Purchase purchase, int responseCode);
    void sendFailureEvent(int responseCode, String reason, @Nullable Purchase purchase);
    void sendRestoreEvent(Purchase purchase, int responseCode);
}
