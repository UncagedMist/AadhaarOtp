package tbc.uncagedmist.aadhaarotp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.util.Calendar;
import java.util.Locale;

import am.appwise.components.ni.NoInternetDialog;
import tbc.uncagedmist.aadhaarotp.Common.Common;
import tbc.uncagedmist.aadhaarotp.Utility.Receiver;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 5152;
    AppCompatButton btnFace,btnE,btnLang, btnWin;

    AdView bottomBanner,aboveBanner;

    NoInternetDialog noInternetDialog;

    private InterstitialAd mInterstitialAd;

    ReviewManager manager;
    ReviewInfo reviewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerAlarm();
        checkAppUpdate();
        loadLocale();
        setContentView(R.layout.activity_main);

        manager = ReviewManagerFactory.create(MainActivity.this);
        noInternetDialog = new NoInternetDialog.Builder(MainActivity.this).build();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7920815986886474/9700310130");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        btnFace = findViewById(R.id.btnFace);
        btnE = findViewById(R.id.btnE);
        btnLang = findViewById(R.id.btnChange);
        btnWin = findViewById(R.id.btnWin);

        aboveBanner =findViewById(R.id.aboveBanner);
        bottomBanner = findViewById(R.id.belowBanner);

        AdRequest adRequest = new AdRequest.Builder().build();

        aboveBanner.loadAd(adRequest);
        bottomBanner.loadAd(adRequest);

        btnFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                    intent.putExtra("url", Common.FACE_AADHAAR);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                    intent.putExtra("url", Common.E_AADHAAR);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAppLang();
            }
        });

        btnWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(Color.parseColor("#008000"));

                openCustomTabs(MainActivity.this,builder.build(),Uri.parse(Common.WIN_URL));
            }
        });

        adMethod();
    }

    private static void openCustomTabs(Activity activity, CustomTabsIntent customTabsIntent, Uri uri)    {
        String packageName = "com.android.chrome";

        try {

            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity,uri);
        }
        catch(ActivityNotFoundException ex) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }
    }

    private void checkAppUpdate() {
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))    {

                    try {
                        appUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE,MainActivity.this,REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void adMethod() {
        bottomBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        });
    }

    private void selectAppLang() {

        LayoutInflater inflater = this.getLayoutInflater();
        View language_layout = inflater.inflate(R.layout.lang_layout,null);

        final AppCompatButton btnEnglish = language_layout.findViewById(R.id.btnEnglish);
        final AppCompatButton btnHindi = language_layout.findViewById(R.id.btnHindi);
        final AppCompatButton btnUrdu = language_layout.findViewById(R.id.btnUrdu);
        final AppCompatButton btnBangla = language_layout.findViewById(R.id.btnBangla);
        final AppCompatButton btnPunjabi = language_layout.findViewById(R.id.btnPunjabi);
        final AppCompatButton btnKannada = language_layout.findViewById(R.id.btnKannada);

        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("Choose Language / भाषा चुनें")
                .withTitleColor("#FFFFFF")
                .withMessage("Select Language...")
                .withDialogColor("#FFFFFF")
                .withDialogColor("#2ea4e7")
                .withEffect(Effectstype.Newspager)
                .isCancelableOnTouchOutside(false)
                .setCustomView(language_layout,this)
                .show();

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("en");
                recreate();
            }
        });

        btnHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("hi");
                recreate();
            }
        });

        btnUrdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("ur");
                recreate();
            }
        });

        btnBangla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("bn");
                recreate();
            }
        });

        btnPunjabi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("pa");
                recreate();
            }
        });

        btnKannada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("kn");
                recreate();
            }
        });
    }

    //set language
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        //shared prefs
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    //load language
    private void loadLocale()   {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }

    @Override
    public void onBackPressed() {
        Task<ReviewInfo> request = manager.requestReviewFlow();

        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful())    {
                    reviewInfo = task.getResult();

                    Task<Void> flow = manager.launchReviewFlow(MainActivity.this,reviewInfo);

                    flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void result) {

                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "ERROR...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
}