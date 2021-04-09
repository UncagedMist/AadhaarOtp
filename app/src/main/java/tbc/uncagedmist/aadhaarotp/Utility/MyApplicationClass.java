package tbc.uncagedmist.aadhaarotp.Utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Map;

public class MyApplicationClass extends Application {

    private static Context context;

    private static AppOpenManager appOpenManager;

    public static Activity mActivity;
    MyNetworkReceiver mNetworkReceiver;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        MobileAds.initialize(this, "ca-app-pub-7920815986886474~1190715980");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("Aadhaar Services", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }


            }
        });

        appOpenManager = new AppOpenManager(this);

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                mNetworkReceiver = new MyNetworkReceiver();
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                mActivity = activity;
                registerNetworkBroadcastForLollipop();
            }

            @Override
            public void onActivityPaused(Activity activity) {
                mActivity = null;
                unregisterReceiver(mNetworkReceiver);
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void registerNetworkBroadcastForLollipop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
}