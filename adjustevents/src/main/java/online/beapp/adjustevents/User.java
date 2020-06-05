package online.beapp.adjustevents;

public class User {

    private boolean isDeeplink;
    private boolean isPurchased;
    private String deeplinkURL;


    public User(boolean isDeeplink, boolean isPurchased) {
        this.isDeeplink = isDeeplink;
        this.isPurchased = isPurchased;

    }

    public boolean isFromDeeplink() {
        return isDeeplink;
    }

    public void setFromDeeplink(boolean deeplink) {
        isDeeplink = deeplink;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public void setStartSubscription() {
    }

    public void setEndSubscription() {
    }

    public void setDeeplinkURL(String deeplinkURL) {
        this.deeplinkURL = deeplinkURL;
    }

    public String getDeeplinkURL() {
        return this.deeplinkURL;
    }

}