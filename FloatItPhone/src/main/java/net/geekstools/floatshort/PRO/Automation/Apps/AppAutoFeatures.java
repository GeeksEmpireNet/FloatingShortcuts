/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 11/11/20 10:50 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Automation.Apps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import net.geekstools.floatshort.PRO.Automation.Folders.FolderAutoFeatures;
import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Utils.AdapterItemsData.AdapterItems;
import net.geekstools.floatshort.PRO.Utils.Functions.ApplicationThemeController;
import net.geekstools.floatshort.PRO.Utils.Functions.Debug;
import net.geekstools.floatshort.PRO.Utils.Functions.FileIO;
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClassLegacy;
import net.geekstools.floatshort.PRO.Utils.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Utils.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureConstants;
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureListenerConstants;
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureListenerInterface;
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.SwipeGestureListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppAutoFeatures extends AppCompatActivity implements View.OnClickListener, GestureListenerInterface {

    FunctionsClassLegacy functionsClassLegacy;
    FileIO fileIO;

    ApplicationThemeController applicationThemeController;
    ApplicationThemeController.Utils functionsClassThemeUtils;

    ListView actionElementsList;
    RelativeLayout fullActionButton, MainView;
    LinearLayout indexView, autoIdentifier;
    RelativeLayout loadingSplash;
    ProgressBar loadingBarLTR;
    TextView desc, popupIndex;
    Button wifi, bluetooth, gps, nfc, time, autoApps, autoCategories;

    ScrollView nestedScrollView;
    RecyclerView loadView;

    ScrollView nestedIndexScrollView;

    List<ApplicationInfo> applicationInfoList;
    Map<String, Integer> mapIndexFirstItem, mapIndexLastItem;
    Map<Integer, String> mapRangeIndex;

    ArrayList<AdapterItems> adapterItems;
    RecyclerView.Adapter adapter;
    LinearLayoutManager recyclerViewLayoutManager;

    String PackageName;
    String AppName = "Application";
    String AppTime = "00:00";
    Drawable AppIcon;

    int color, pressColor;

    SwipeGestureListener swipeGestureListener;

    LoadCustomIcons loadCustomIcons;

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        setContentView(R.layout.auto_apps);

        loadView = (RecyclerView) findViewById(R.id.recyclerListView);
        nestedScrollView = (ScrollView) findViewById(R.id.nestedScrollView);


        indexView = (LinearLayout) findViewById(R.id.indexView);
        autoIdentifier = (LinearLayout) findViewById(R.id.autoid);
        autoIdentifier.bringToFront();
        MainView = (RelativeLayout) findViewById(R.id.MainView);
        fullActionButton = (RelativeLayout) findViewById(R.id.fullActionViews);
        actionElementsList = (ListView) findViewById(R.id.acttionElementsList);
        autoApps = (Button) findViewById(R.id.autoApps);
        autoApps.bringToFront();
        autoCategories = (Button) findViewById(R.id.autoCategories);
        autoCategories.bringToFront();
        wifi = (Button) findViewById(R.id.wifi);
        bluetooth = (Button) findViewById(R.id.bluetooth);
        gps = (Button) findViewById(R.id.gps);
        nfc = (Button) findViewById(R.id.nfc);
        time = (Button) findViewById(R.id.time);

        popupIndex = (TextView) findViewById(R.id.popupIndex);
        nestedIndexScrollView = (ScrollView) findViewById(R.id.nestedIndexScrollView);

        swipeGestureListener = new SwipeGestureListener(getApplicationContext(), AppAutoFeatures.this);

        functionsClassLegacy = new FunctionsClassLegacy(getApplicationContext());
        fileIO = new FileIO(getApplicationContext());

        applicationThemeController = new ApplicationThemeController(getApplicationContext());
        functionsClassThemeUtils = applicationThemeController.new Utils();

        functionsClassLegacy.loadSavedColor();
        functionsClassLegacy.checkLightDarkTheme();

        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        loadView.setLayoutManager(recyclerViewLayoutManager);

        if (functionsClassLegacy.returnAPI() >= 26) {
            if (!functionsClassLegacy.ControlPanel()) {
                Snackbar snackbar = Snackbar.make(MainView, Html.fromHtml("<big>" + getString(R.string.enableControlPanel) + "</big>"), Snackbar.LENGTH_INDEFINITE)
                        .setAction(Html.fromHtml("<b>" + getString(R.string.enable).toUpperCase() + "</b>"), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("stable", true);
                                editor.apply();
                                startService(new Intent(getApplicationContext(), BindServices.class));
                            }
                        });
                snackbar.setActionTextColor(PublicVariable.colorLightDarkOpposite);
                snackbar.setActionTextColor(getColor(R.color.red));

                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.BOTTOM_TOP,
                        new int[]{
                                Color.TRANSPARENT,
                                functionsClassLegacy.setColorAlpha(PublicVariable.primaryColor, 207),
                                Color.TRANSPARENT
                        });

                View view = snackbar.getView();
                view.setBackground(gradientDrawable/*functionsClass.setColorAlpha(functionsClass.mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), 180)*/);

                snackbar.show();
            }
        }

        applicationThemeController.setThemeColorAutomationFeature(AppAutoFeatures.this, MainView, functionsClassLegacy.appThemeTransparent());

        if (functionsClassLegacy.customIconsEnable()) {
            loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClassLegacy.customIconPackageName());
        }

        adapterItems = new ArrayList<AdapterItems>();
        mapIndexFirstItem = new LinkedHashMap<String, Integer>();
        mapIndexLastItem = new LinkedHashMap<String, Integer>();
        mapRangeIndex = new LinkedHashMap<Integer, String>();

        autoApps.setTextColor(getColor(R.color.light));
        autoCategories.setTextColor(getColor(R.color.light));
        if (PublicVariable.themeLightDark /*light*/ && functionsClassLegacy.appThemeTransparent() /*transparent*/) {
            autoApps.setTextColor(getColor(R.color.dark));
            autoCategories.setTextColor(getColor(R.color.dark));
        }

        RippleDrawable rippleDrawableShortcuts = (RippleDrawable) getDrawable(R.drawable.draw_shortcuts);
        Drawable gradientDrawableShortcutsForeground = rippleDrawableShortcuts.findDrawableByLayerId(R.id.foregroundItem);
        Drawable gradientDrawableShortcutsBackground = rippleDrawableShortcuts.findDrawableByLayerId(R.id.backgroundItem);
        Drawable gradientDrawableMaskShortcuts = rippleDrawableShortcuts.findDrawableByLayerId(android.R.id.mask);

        if (functionsClassLegacy.appThemeTransparent()) {
            rippleDrawableShortcuts.setColor(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
            gradientDrawableShortcutsForeground.setTint(
                    functionsClassLegacy.setColorAlpha(
                            functionsClassLegacy.mixColors(
                                    PublicVariable.primaryColor, PublicVariable.colorLightDark,
                                    0.75f), functionsClassThemeUtils.wallpaperStaticLive() ? 245 : 113)
            );
            gradientDrawableShortcutsBackground.setTint(functionsClassLegacy.setColorAlpha(PublicVariable.primaryColor, functionsClassThemeUtils.wallpaperStaticLive() ? 150 : 155));
            gradientDrawableMaskShortcuts.setTint(PublicVariable.primaryColorOpposite);
        } else {
            rippleDrawableShortcuts.setColor(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
            gradientDrawableShortcutsForeground.setTint(PublicVariable.primaryColor);
            gradientDrawableShortcutsBackground.setTint(PublicVariable.primaryColor);
            gradientDrawableMaskShortcuts.setTint(PublicVariable.primaryColorOpposite);
        }
        autoApps.setBackground(rippleDrawableShortcuts);

        RippleDrawable rippleDrawableCategories = (RippleDrawable) getDrawable(R.drawable.draw_categories);
        Drawable gradientDrawableCategoriesForeground = rippleDrawableCategories.findDrawableByLayerId(R.id.foregroundItem);
        Drawable gradientDrawableCategoriesBackground = rippleDrawableCategories.findDrawableByLayerId(R.id.backgroundItem);
        Drawable gradientDrawableMaskCategories = rippleDrawableCategories.findDrawableByLayerId(android.R.id.mask);

        if (functionsClassLegacy.appThemeTransparent()) {
            rippleDrawableCategories.setColor(ColorStateList.valueOf(PublicVariable.primaryColor));
            gradientDrawableCategoriesForeground.setTint(functionsClassLegacy.setColorAlpha(PublicVariable.primaryColorOpposite, 255));
            if (functionsClassLegacy.returnAPI() > 21) {
                gradientDrawableCategoriesBackground.setTint(functionsClassLegacy.setColorAlpha(PublicVariable.primaryColorOpposite, 175));
            } else {
                gradientDrawableShortcutsBackground.setTint(functionsClassLegacy.setColorAlpha(PublicVariable.primaryColorOpposite, 175));
            }
            gradientDrawableMaskCategories.setTint(PublicVariable.primaryColor);
        } else {
            rippleDrawableCategories.setColor(ColorStateList.valueOf(PublicVariable.primaryColor));
            gradientDrawableCategoriesForeground.setTint(PublicVariable.primaryColorOpposite);
            gradientDrawableCategoriesBackground.setTint(PublicVariable.primaryColorOpposite);
            gradientDrawableMaskCategories.setTint(PublicVariable.primaryColor);
        }
        autoCategories.setBackground(rippleDrawableCategories);

        ImageView floatingLogo = (ImageView) findViewById(R.id.loadingLogo);
        LayerDrawable drawFloatingLogo = (LayerDrawable) getDrawable(R.drawable.draw_floating_logo);
        Drawable backFloatingLogo = drawFloatingLogo.findDrawableByLayerId(R.id.backgroundTemporary);
        backFloatingLogo.setTint(PublicVariable.primaryColorOpposite);
        floatingLogo.setImageDrawable(drawFloatingLogo);

        loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
        if (functionsClassLegacy.appThemeTransparent() == true) {
            loadingSplash.setBackgroundColor(Color.TRANSPARENT);
        } else {
            loadingSplash.setBackgroundColor(getWindow().getNavigationBarColor());
        }

        loadingBarLTR = (ProgressBar) findViewById(R.id.loadingProgress);
        desc = (TextView) findViewById(R.id.loadingDescription);
        Typeface face = Typeface.createFromAsset(getAssets(), "upcil.ttf");
        desc.setTypeface(face);

        if (PublicVariable.themeLightDark) {
            loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.darkMutedColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            desc.setTextColor(getColor(R.color.dark));
        } else if (!PublicVariable.themeLightDark) {
            loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.vibrantColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            desc.setTextColor(getColor(R.color.light));
        }

        autoCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublicVariable.autoID = null;
                try {
                    functionsClassLegacy.navigateToClass(AppAutoFeatures.this, FolderAutoFeatures.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_right, R.anim.slide_to_left));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (PublicVariable.themeLightDark) {
            color = PublicVariable.vibrantColor;
            pressColor = PublicVariable.darkMutedColor;
        } else if (!PublicVariable.themeLightDark) {
            color = PublicVariable.darkMutedColor;
            pressColor = PublicVariable.vibrantColor;
        }

        if (PublicVariable.autoID != null) {
            final LayerDrawable drawWifi = (LayerDrawable) getDrawable(R.drawable.draw_wifi);
            final Drawable backWifi = drawWifi.findDrawableByLayerId(R.id.backgroundTemporary);

            final LayerDrawable drawBluetooth = (LayerDrawable) getDrawable(R.drawable.draw_bluetooth);
            final Drawable backBluetooth = drawBluetooth.findDrawableByLayerId(R.id.backgroundTemporary);

            final LayerDrawable drawGPS = (LayerDrawable) getDrawable(R.drawable.draw_gps);
            final Drawable backGPS = drawGPS.findDrawableByLayerId(R.id.backgroundTemporary);

            final LayerDrawable drawNfc = (LayerDrawable) getDrawable(R.drawable.draw_nfc);
            final Drawable backNfc = drawNfc.findDrawableByLayerId(R.id.backgroundTemporary);

            final LayerDrawable drawTime = (LayerDrawable) getDrawable(R.drawable.draw_time);
            final Drawable backTime = drawTime.findDrawableByLayerId(R.id.backgroundTemporary);

            wifi.setBackground(drawWifi);
            bluetooth.setBackground(drawBluetooth);
            gps.setBackground(drawGPS);
            nfc.setBackground(drawNfc);
            time.setBackground(drawTime);

            if (PublicVariable.autoID.equals(getString(R.string.wifi))) {
                backWifi.setTint(pressColor);
                wifi.setBackground(drawWifi);

                autoWiFi();

                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.bluetooth))) {
                backBluetooth.setTint(pressColor);
                bluetooth.setBackground(drawBluetooth);

                autoBluetooth();

                backWifi.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.gps))) {
                backGPS.setTint(pressColor);
                gps.setBackground(drawGPS);

                autoGPS();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.nfc))) {
                backNfc.setTint(pressColor);
                nfc.setBackground(drawNfc);

                autoNfc();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.time))) {
                backTime.setTint(pressColor);
                time.setBackground(drawTime);

                autoTime();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final LayerDrawable drawWifi = (LayerDrawable) getDrawable(R.drawable.draw_wifi);
        final Drawable backWifi = drawWifi.findDrawableByLayerId(R.id.backgroundTemporary);

        final LayerDrawable drawBluetooth = (LayerDrawable) getDrawable(R.drawable.draw_bluetooth);
        final Drawable backBluetooth = drawBluetooth.findDrawableByLayerId(R.id.backgroundTemporary);

        final LayerDrawable drawGPS = (LayerDrawable) getDrawable(R.drawable.draw_gps);
        final Drawable backGPS = drawGPS.findDrawableByLayerId(R.id.backgroundTemporary);

        final LayerDrawable drawNfc = (LayerDrawable) getDrawable(R.drawable.draw_nfc);
        final Drawable backNfc = drawNfc.findDrawableByLayerId(R.id.backgroundTemporary);

        final LayerDrawable drawTime = (LayerDrawable) getDrawable(R.drawable.draw_time);
        final Drawable backTime = drawTime.findDrawableByLayerId(R.id.backgroundTemporary);

        if (PublicVariable.themeLightDark) {
            color = PublicVariable.vibrantColor;
            pressColor = PublicVariable.darkMutedColor;
        } else if (!PublicVariable.themeLightDark) {
            color = PublicVariable.darkMutedColor;
            pressColor = PublicVariable.vibrantColor;
        }

        backWifi.setTint(color);
        backBluetooth.setTint(color);
        backGPS.setTint(color);
        backNfc.setTint(color);
        backTime.setTint(color);

        wifi.setBackground(drawWifi);
        bluetooth.setBackground(drawBluetooth);
        gps.setBackground(drawGPS);
        nfc.setBackground(drawNfc);
        time.setBackground(drawTime);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backWifi.setTint(pressColor);
                wifi.setBackground(drawWifi);

                autoWiFi();

                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            }
        });
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBluetooth.setTint(pressColor);
                bluetooth.setBackground(drawBluetooth);

                autoBluetooth();

                backWifi.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backGPS.setTint(pressColor);
                gps.setBackground(drawGPS);

                autoGPS();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            }
        });
        nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backNfc.setTint(pressColor);
                nfc.setBackground(drawNfc);

                autoNfc();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                time.setBackground(drawTime);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backTime.setTint(pressColor);
                time.setBackground(drawTime);

                autoTime();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (fileIO.automationFeatureEnable()) {
            startService(new Intent(getApplicationContext(), BindServices.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PublicVariable.autoID = null;
    }

    @Override
    public void onBackPressed() {
        try {
            functionsClassLegacy.CheckSystemRAM(AppAutoFeatures.this);

            functionsClassLegacy.overrideBackPressToMain(AppAutoFeatures.this, AppAutoFeatures.this);
            overridePendingTransition(android.R.anim.fade_in, R.anim.go_up);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSwipeGesture(@NotNull GestureConstants gestureConstants, @NotNull MotionEvent downMotionEvent, @NotNull MotionEvent moveMotionEvent, float initVelocityX, float initVelocityY) {
        if (gestureConstants instanceof GestureConstants.SwipeHorizontal) {
            switch (((GestureConstants.SwipeHorizontal) gestureConstants).getHorizontalDirection()) {
                case GestureListenerConstants.SWIPE_RIGHT: {

                    break;
                }
                case GestureListenerConstants.SWIPE_LEFT: {

                    functionsClassLegacy.navigateToClass(AppAutoFeatures.this, FolderAutoFeatures.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_right, R.anim.slide_to_left));

                    AppAutoFeatures.this.finish();

                    break;
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        swipeGestureListener.onTouchEvent(motionEvent);


        return super.dispatchTouchEvent(motionEvent);
    }

    @Override
    public void onSingleTapUp(@NotNull MotionEvent motionEvent) {

    }

    @Override
    public void onLongPress(@NotNull MotionEvent motionEvent) {

    }

    public void autoWiFi() {
        PublicVariable.autoID = getString(R.string.wifi);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {android.Manifest.permission.ACCESS_WIFI_STATE};
                requestPermissions(gpsPermissions, 888);
                return;
            }
        }

        LoadAutoApplications loadAutoApplications = new LoadAutoApplications();
        loadAutoApplications.execute();
    }

    public void autoBluetooth() {
        PublicVariable.autoID = getString(R.string.bluetooth);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {android.Manifest.permission.BLUETOOTH};
                requestPermissions(gpsPermissions, 999);
                return;
            }
        }

        LoadAutoApplications loadAutoApplications = new LoadAutoApplications();
        loadAutoApplications.execute();
    }

    public void autoGPS() {
        PublicVariable.autoID = getString(R.string.gps);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {android.Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(gpsPermissions, 777);
                return;
            }
        }

        LoadAutoApplications loadAutoApplications = new LoadAutoApplications();
        loadAutoApplications.execute();
    }

    public void autoNfc() {
        PublicVariable.autoID = getString(R.string.nfc);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(Manifest.permission.NFC) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {Manifest.permission.NFC};
                requestPermissions(gpsPermissions, 888);
                return;
            }
        }

        LoadAutoApplications loadAutoApplications = new LoadAutoApplications();
        loadAutoApplications.execute();
    }

    public void autoTime() {
        PublicVariable.autoID = getString(R.string.time);

        LoadAutoApplications loadAutoApplications = new LoadAutoApplications();
        loadAutoApplications.execute();
    }

    /******************************************/
    private class LoadAutoApplications extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
            loadingSplash.setVisibility(View.VISIBLE);
            if (functionsClassLegacy.appThemeTransparent() == true) {
                loadingSplash.setBackgroundColor(Color.TRANSPARENT);
            } else {
                loadingSplash.setBackgroundColor(getWindow().getNavigationBarColor());
            }

            loadingBarLTR = (ProgressBar) findViewById(R.id.loadingProgress);
            desc = (TextView) findViewById(R.id.loadingDescription);
            Typeface face = Typeface.createFromAsset(getAssets(), "upcil.ttf");
            desc.setTypeface(face);

            if (PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.darkMutedColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            } else if (!PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.vibrantColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            }


            indexView.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                applicationInfoList = getApplicationContext().getPackageManager().getInstalledApplications(0);
                Collections.sort(applicationInfoList, new ApplicationInfo.DisplayNameComparator(getPackageManager()));

                adapterItems = new ArrayList<AdapterItems>();
                mapIndexFirstItem = new LinkedHashMap<String, Integer>();

                if (functionsClassLegacy.customIconsEnable()) {
                    loadCustomIcons.load();
                    Debug.Companion.PrintDebug("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIconsNumber());
                }

                for (int appInfo = 0; appInfo < applicationInfoList.size(); appInfo++) {
                    if (getApplicationContext().getPackageManager().getLaunchIntentForPackage(applicationInfoList.get(appInfo).packageName) != null) {
                        try {
                            PackageName = applicationInfoList.get(appInfo).packageName;
                            AppName = functionsClassLegacy.applicationName(PackageName);
                            AppIcon = functionsClassLegacy.customIconsEnable() ? loadCustomIcons.getDrawableIconForPackage(PackageName, functionsClassLegacy.shapedAppIcon(PackageName)) : functionsClassLegacy.shapedAppIcon(PackageName);

                            if (getFileStreamPath(PackageName + ".Time").exists()) {
                                AppTime = fileIO.readFile(PackageName + ".Time");
                                String tempTimeHour = AppTime.split(":")[0];
                                String tempTimeMinute = AppTime.split(":")[1];
                                if (Integer.parseInt(tempTimeMinute) < 10) {
                                    AppTime = tempTimeHour + ":" + "0" + tempTimeMinute;
                                }
                            }

                            adapterItems.add(new AdapterItems(AppName, PackageName, AppIcon, AppTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                adapter = new AppAutoListAdapter(AppAutoFeatures.this, getApplicationContext(), adapterItems);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            loadView.setAdapter(adapter);
            registerForContextMenu(loadView);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                    loadingSplash.setVisibility(View.INVISIBLE);
                    loadingSplash.startAnimation(anim);
                    autoIdentifier.setVisibility(View.VISIBLE);
                }
            }, 100);

            LoadApplicationsIndex loadApplicationsIndex = new LoadApplicationsIndex();
            loadApplicationsIndex.execute();
        }
    }

    private class LoadApplicationsIndex extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            indexView.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int itemCount = 0; itemCount < adapterItems.size(); itemCount++) {
                try {
                    String index = (adapterItems.get(itemCount).getAppName()).substring(0, 1).toUpperCase();
                    if (mapIndexFirstItem.get(index) == null) {
                        mapIndexFirstItem.put(index, itemCount);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            TextView textView = (TextView) getLayoutInflater()
                    .inflate(R.layout.fast_scroller_side_index_item, null);
            List<String> indexList = new ArrayList<String>(mapIndexFirstItem.keySet());
            for (String index : indexList) {
                textView = (TextView) getLayoutInflater()
                        .inflate(R.layout.fast_scroller_side_index_item, null);
                textView.setText(index.toUpperCase());
                textView.setTextColor(PublicVariable.colorLightDarkOpposite);
                indexView.addView(textView);
            }

            TextView finalTextView = textView;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    int upperRange = (int) (indexView.getY() - finalTextView.getHeight());
                    for (int i = 0; i < indexView.getChildCount(); i++) {
                        String indexText = ((TextView) indexView.getChildAt(i)).getText().toString();
                        int indexRange = (int) (indexView.getChildAt(i).getY() + indexView.getY() + finalTextView.getHeight());
                        for (int jRange = upperRange; jRange <= (indexRange); jRange++) {
                            mapRangeIndex.put(jRange, indexText);
                        }

                        upperRange = indexRange;
                    }

                    setupFastScrollingIndexing();
                }
            }, 700);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupFastScrollingIndexing() {
        Drawable popupIndexBackground = getDrawable(R.drawable.ic_launcher_balloon).mutate();
        popupIndexBackground.setTint(PublicVariable.primaryColorOpposite);
        popupIndex.setBackground(popupIndexBackground);

        nestedIndexScrollView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
        nestedIndexScrollView.setVisibility(View.VISIBLE);

        float popupIndexOffsetY = PublicVariable.statusBarHeight + PublicVariable.actionBarHeight + (functionsClassLegacy.UsageStatsEnabled() ? functionsClassLegacy.DpToInteger(7) : functionsClassLegacy.DpToInteger(7));
        nestedIndexScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if (functionsClassLegacy.litePreferencesEnabled()) {

                        } else {
                            String indexText = mapRangeIndex.get(((int) motionEvent.getY()));

                            if (indexText != null) {
                                popupIndex.setY(motionEvent.getRawY() - popupIndexOffsetY);
                                popupIndex.setText(indexText);
                                popupIndex.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                                popupIndex.setVisibility(View.VISIBLE);
                            }
                        }

                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if (functionsClassLegacy.litePreferencesEnabled()) {

                        } else {
                            String indexText = mapRangeIndex.get(((int) motionEvent.getY()));

                            if (indexText != null) {
                                if (!popupIndex.isShown()) {
                                    popupIndex.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                                    popupIndex.setVisibility(View.VISIBLE);
                                }
                                popupIndex.setY(motionEvent.getRawY() - popupIndexOffsetY);
                                popupIndex.setText(indexText);

                                try {
                                    nestedScrollView.smoothScrollTo(
                                            0,
                                            ((int) loadView.getChildAt(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY())))).getY())
                                    );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (popupIndex.isShown()) {
                                    popupIndex.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                    popupIndex.setVisibility(View.INVISIBLE);
                                }
                            }
                        }

                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if (functionsClassLegacy.litePreferencesEnabled()) {
                            try {
                                nestedScrollView.smoothScrollTo(
                                        0,
                                        ((int) loadView.getChildAt(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY())))).getY())
                                );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (popupIndex.isShown()) {
                                try {
                                    nestedScrollView.smoothScrollTo(
                                            0,
                                            ((int) loadView.getChildAt(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY())))).getY())
                                    );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                popupIndex.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                popupIndex.setVisibility(View.INVISIBLE);
                            }
                        }

                        break;
                    }
                }
                return true;
            }
        });
    }
}
