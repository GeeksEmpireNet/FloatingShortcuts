/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 4/27/20 11:18 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Folders.PopupDialogue;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import net.geekstools.floatshort.PRO.Folders.FoldersAdapter.PopupFolderOptionAdapter;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Utils.AdapterItemsData.AdapterItemsPopupOptionsFloatingFolders;
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Utils.Functions.PublicVariable;

import java.util.ArrayList;

public class PopupOptionsFloatingFolders extends Service {

    FunctionsClass functionsClass;
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;

    ViewGroup viewGroup;
    RelativeLayout wholeMenuView, itemMenuView;
    ListView popupOptionsItems;

    int HW, xPosition, yPosition, startIdCommand;
    int statusBarHeight, navBarHeight;
    String folderName, classNameCommand,
            MODE = "Options";
    String[] packagesNames;

    PopupFolderOptionAdapter popupFolderOptionAdapter;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, final int startId) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        navBarHeight = 0;
        int resourceIdNav = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceIdNav > 0) {
            navBarHeight = getResources().getDimensionPixelSize(resourceIdNav);
        }
        statusBarHeight = 0;
        int resourceIdStatus = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceIdStatus > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceIdStatus);
        }

        startIdCommand = intent.getIntExtra("startIdCommand", 0);
        MODE = intent.getStringExtra("MODE");
        classNameCommand = intent.getStringExtra("classNameCommand");
        folderName = intent.getStringExtra("FolderName");

        HW = intent.getIntExtra("HW", 0);
        xPosition = intent.getIntExtra("X", 0);
        yPosition = intent.getIntExtra("Y", 0);

        viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.popup_options_category, null, false);
        wholeMenuView = (RelativeLayout) viewGroup.findViewById(R.id.wholeMenuView);
        itemMenuView = (RelativeLayout) viewGroup.findViewById(R.id.itemMenuView);
        popupOptionsItems = (ListView) viewGroup.findViewById(R.id.popupOptionsItems);

        try {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(50);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            try {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(50);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ArrayList<AdapterItemsPopupOptionsFloatingFolders> adapterItemsPopupOptionsFloatingFolder = new ArrayList<AdapterItemsPopupOptionsFloatingFolders>();
            adapterItemsPopupOptionsFloatingFolder.clear();

            if (MODE.equals("Options")) {
                String[] popupItems = null;
                Drawable[] popupItemsIcon;

                Drawable[] drawables = new Drawable[2];
                drawables[0] = getDrawable(R.drawable.ic_launcher);
                try {
                    drawables[0] = functionsClass.shapesDrawables().mutate();
                    drawables[0].setTint(PublicVariable.primaryColor);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    drawables[0].setAlpha(0);
                }
                drawables[1] = getDrawable(R.drawable.w_pref);
                LayerDrawable popupItemsIconCategory = new LayerDrawable(drawables);

                switch (functionsClass.displaySection(xPosition, yPosition)) {
                    case FunctionsClass.DisplaySection.TopLeft:
                        popupItems = new String[]{
                                getString(R.string.pin_category),
                                getString(R.string.unpin_category),
                                getString(R.string.remove_category),
                        };
                        popupItemsIcon = new Drawable[]{
                                popupItemsIconCategory,
                                popupItemsIconCategory,
                                popupItemsIconCategory
                        };

                        itemMenuView.setX(xPosition - functionsClass.DpToInteger(3));
                        itemMenuView.setY(yPosition + HW);

                        break;
                    case FunctionsClass.DisplaySection.TopRight:
                        popupItems = new String[]{
                                getString(R.string.pin_category),
                                getString(R.string.unpin_category),
                                getString(R.string.remove_category),
                        };
                        popupItemsIcon = new Drawable[]{
                                popupItemsIconCategory,
                                popupItemsIconCategory,
                                popupItemsIconCategory
                        };

                        itemMenuView.setX(xPosition - (functionsClass.DpToInteger(100) - HW) + functionsClass.DpToInteger(3));
                        itemMenuView.setY(yPosition + HW);

                        break;
                    case FunctionsClass.DisplaySection.BottomLeft:
                        popupItems = new String[]{
                                getString(R.string.remove_category),
                                getString(R.string.unpin_category),
                                getString(R.string.pin_category),
                        };
                        popupItemsIcon = new Drawable[]{
                                popupItemsIconCategory,
                                popupItemsIconCategory,
                                popupItemsIconCategory
                        };

                        itemMenuView.setX(xPosition - functionsClass.DpToInteger(3));
                        itemMenuView.setY(yPosition - ((functionsClass.DpToInteger(27) * popupItems.length) + functionsClass.DpToInteger(7)));

                        break;
                    case FunctionsClass.DisplaySection.BottomRight:
                        popupItems = new String[]{
                                getString(R.string.remove_category),
                                getString(R.string.unpin_category),
                                getString(R.string.pin_category),
                        };
                        popupItemsIcon = new Drawable[]{
                                popupItemsIconCategory,
                                popupItemsIconCategory,
                                popupItemsIconCategory
                        };

                        itemMenuView.setX(xPosition - (functionsClass.DpToInteger(100) - HW) + functionsClass.DpToInteger(3));
                        itemMenuView.setY(yPosition - ((functionsClass.DpToInteger(27) * popupItems.length) + functionsClass.DpToInteger(7)));

                        break;
                    default:
                        popupItems = new String[]{
                                getString(R.string.pin_category),
                                getString(R.string.unpin_category),
                                getString(R.string.remove_category),
                        };
                        popupItemsIcon = new Drawable[]{
                                popupItemsIconCategory,
                                popupItemsIconCategory,
                                popupItemsIconCategory
                        };

                        itemMenuView.setX(xPosition - functionsClass.DpToInteger(3));
                        itemMenuView.setY(yPosition + HW);

                        break;
                }
                for (int i = 0; i < popupItems.length; i++) {
                    adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                            popupItems[i],
                            folderName,
                            popupItemsIcon[i]));
                }

                popupFolderOptionAdapter =
                        new PopupFolderOptionAdapter(getApplicationContext(),
                                adapterItemsPopupOptionsFloatingFolder, classNameCommand, startIdCommand);
            } else if (MODE.equals("AppsList")) {
                packagesNames = intent.getStringArrayExtra("PackagesNames");

                Drawable[] drawables = new Drawable[2];
                drawables[0] = getDrawable(R.drawable.ic_launcher);
                try {
                    drawables[0] = functionsClass.shapesDrawables().mutate();
                    drawables[0].setTint(PublicVariable.primaryColor);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    drawables[0].setAlpha(0);
                }
                drawables[1] = getDrawable(R.drawable.w_pref);
                LayerDrawable popupItemsIcon = new LayerDrawable(drawables);

                switch (functionsClass.displaySection(xPosition, yPosition)) {
                    case FunctionsClass.DisplaySection.TopLeft:
                        for (String packageName : packagesNames) {
                            adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                                    functionsClass.appName(packageName),
                                    packageName,
                                    functionsClass.shapedAppIcon(packageName)));
                        }
                        adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                                getString(R.string.edit_category) + " " + folderName,
                                getPackageName(),
                                popupItemsIcon));

                        itemMenuView.setX(xPosition - functionsClass.DpToInteger(3));
                        itemMenuView.setY(yPosition + HW);

                        break;
                    case FunctionsClass.DisplaySection.TopRight:
                        for (String packageName : packagesNames) {
                            adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                                    functionsClass.appName(packageName),
                                    packageName,
                                    functionsClass.shapedAppIcon(packageName)));
                        }
                        adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                                getString(R.string.edit_category) + " " + folderName,
                                getPackageName(),
                                popupItemsIcon));

                        itemMenuView.setX(xPosition - (functionsClass.DpToInteger(100) - HW) + functionsClass.DpToInteger(3));
                        itemMenuView.setY(yPosition + HW);

                        break;
                    case FunctionsClass.DisplaySection.BottomLeft:
                        adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                                getString(R.string.edit_category) + " " + folderName,
                                getPackageName(),
                                popupItemsIcon));
                        for (String packageName : packagesNames) {
                            adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                                    functionsClass.appName(packageName),
                                    packageName,
                                    functionsClass.shapedAppIcon(packageName)));
                        }

                        itemMenuView.setX(xPosition - functionsClass.DpToInteger(3));
                        itemMenuView.setY(yPosition - (functionsClass.DpToInteger(107)));

                        break;
                    case FunctionsClass.DisplaySection.BottomRight:
                        adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                                getString(R.string.edit_category) + " " + folderName,
                                getPackageName(),
                                popupItemsIcon));
                        for (String packageName : packagesNames) {
                            adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                                    functionsClass.appName(packageName),
                                    packageName,
                                    functionsClass.shapedAppIcon(packageName)));
                        }

                        itemMenuView.setX(xPosition - (functionsClass.DpToInteger(100) - HW) + functionsClass.DpToInteger(3));
                        itemMenuView.setY(yPosition - (functionsClass.DpToInteger(107)));

                        break;
                    default:
                        for (String packageName : packagesNames) {
                            adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                                    functionsClass.appName(packageName),
                                    packageName,
                                    functionsClass.shapedAppIcon(packageName)));
                        }
                        adapterItemsPopupOptionsFloatingFolder.add(new AdapterItemsPopupOptionsFloatingFolders(
                                getString(R.string.edit_category) + " " + folderName,
                                getPackageName(),
                                popupItemsIcon));

                        itemMenuView.setX(xPosition);
                        itemMenuView.setY(yPosition + HW + functionsClass.DpToInteger(19));

                        break;
                }

                popupFolderOptionAdapter =
                        new PopupFolderOptionAdapter(getApplicationContext(),
                                adapterItemsPopupOptionsFloatingFolder, classNameCommand, startIdCommand, xPosition, yPosition, HW);
            }

            popupOptionsItems.setAdapter(popupFolderOptionAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        layoutParams = functionsClass.splashRevealParams(
                0,
                0
        );
        windowManager.addView(viewGroup, layoutParams);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    functionsClass.circularRevealViewScreen(
                            wholeMenuView,
                            xPosition,
                            yPosition,
                            true
                    );

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemMenuView.setVisibility(View.VISIBLE);
                        }
                    }, 130);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);

        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopSelf();
                    }
                }, 113);
            }
        });

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        try {
            functionsClass.circularRevealViewScreen(
                    wholeMenuView,
                    xPosition,
                    yPosition,
                    false
            );
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    windowManager.removeViewImmediate(viewGroup);
                }
            }, 333);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
