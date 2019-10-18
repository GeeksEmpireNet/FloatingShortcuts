package net.geekstools.floatshort.PRO.Notifications.NavAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassSecurity;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.UI.FloatingSplash;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.util.ArrayList;

public class PopupShortcutsNotification extends BaseAdapter {

    private Context context;

    FunctionsClass functionsClass;
    FunctionsClassSecurity functionsClassSecurity;

    private ArrayList<NavDrawerItem> navDrawerItems;

    private String packageName, className;
    private int startId, layoutInflator, xPosition, yPosition, HW;

    public PopupShortcutsNotification(Context context, ArrayList<NavDrawerItem> navDrawerItems,
                                      String className, String packageName, int startId, int xPosition, int yPosition, int HW) {
        this.context = context;

        this.navDrawerItems = navDrawerItems;

        this.className = className;
        this.packageName = packageName;

        this.startId = startId;

        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.HW = HW;

        functionsClass = new FunctionsClass(context);
        functionsClassSecurity = new FunctionsClassSecurity(context);

        switch (functionsClass.shapesImageId()) {
            case 1:
                layoutInflator = R.layout.item_popup_notification_droplet;
                break;
            case 2:
                layoutInflator = R.layout.item_popup_notification_circle;
                break;
            case 3:
                layoutInflator = R.layout.item_popup_notification_square;
                break;
            case 4:
                layoutInflator = R.layout.item_popup_notification_squircle;
                break;
            case 0:
                layoutInflator = R.layout.item_popup_notification_noshape;
                break;
            default:
                layoutInflator = R.layout.item_popup_notification_noshape;
                break;
        }
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(layoutInflator, null);

            viewHolder = new ViewHolder();
            viewHolder.notificationItem = (RelativeLayout) convertView.findViewById(R.id.notificationItem);
            viewHolder.notificationBanner = (RelativeLayout) convertView.findViewById(R.id.notificationBanner);
            viewHolder.notificationContent = (RelativeLayout) convertView.findViewById(R.id.notificationContent);
            viewHolder.notificationAppIcon = (ShapesImage) convertView.findViewById(R.id.notificationAppIcon);
            viewHolder.notificationLargeIcon = (ShapesImage) convertView.findViewById(R.id.notificationLargeIcon);
            viewHolder.notificationAppName = (TextView) convertView.findViewById(R.id.notificationAppName);
            viewHolder.notificationTitle = (TextView) convertView.findViewById(R.id.notificationTitle);
            viewHolder.notificationText = (TextView) convertView.findViewById(R.id.notificationText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int itemBackground;
        if (functionsClass.appThemeTransparent() == true) {
            itemBackground = functionsClass.setColorAlpha(PublicVariable.colorLightDark, 50);
        } else {
            itemBackground = PublicVariable.colorLightDark;
        }
        LayerDrawable drawPopupShortcut = (LayerDrawable) context.getDrawable(R.drawable.popup_shortcut_whole);
        Drawable backPopupShortcut = drawPopupShortcut.findDrawableByLayerId(R.id.backtemp);
        backPopupShortcut.setTint(itemBackground);
        viewHolder.notificationItem.setBackground(drawPopupShortcut);
        viewHolder.notificationBanner.setBackground(drawPopupShortcut);

        viewHolder.notificationAppIcon.setImageDrawable(navDrawerItems.get(position).getNotificationAppIcon());
        viewHolder.notificationAppName.setText(navDrawerItems.get(position).getNotificationAppName());
        viewHolder.notificationAppName.append("   " + context.getString(R.string.notificationEmoji));

        viewHolder.notificationLargeIcon.setImageDrawable(navDrawerItems.get(position).getNotificationLargeIcon());
        viewHolder.notificationTitle.setText(navDrawerItems.get(position).getNotificationTitle());
        viewHolder.notificationText.setText(navDrawerItems.get(position).getNotificationText());

        if (PublicVariable.themeLightDark) {
            viewHolder.notificationAppName.setTextColor(functionsClass.manipulateColor(functionsClass.extractVibrantColor(navDrawerItems.get(position).getNotificationAppIcon()), 0.50f));
        } else {
            viewHolder.notificationAppName.setTextColor(functionsClass.manipulateColor(functionsClass.extractVibrantColor(navDrawerItems.get(position).getNotificationAppIcon()), 1.30f));
        }

        viewHolder.notificationTitle.setTextColor(PublicVariable.colorLightDarkOpposite);
        viewHolder.notificationText.setTextColor(PublicVariable.colorLightDarkOpposite);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (functionsClassSecurity.isAppLocked(packageName)) {
                    FunctionsClassSecurity.AuthOpenAppValues.setAuthComponentName(packageName);

                    FunctionsClassSecurity.AuthOpenAppValues.setAuthPositionX(xPosition);
                    FunctionsClassSecurity.AuthOpenAppValues.setAuthPositionY(yPosition);
                    FunctionsClassSecurity.AuthOpenAppValues.setAuthHW(HW);

                    functionsClassSecurity.openAuthInvocation();
                } else {

                    if (functionsClass.splashReveal()) {
                        Intent splashReveal = new Intent(context, FloatingSplash.class);
                        splashReveal.putExtra("packageName", packageName);
                        splashReveal.putExtra("X", xPosition);
                        splashReveal.putExtra("Y", yPosition);
                        splashReveal.putExtra("HW", HW);
                        splashReveal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startService(splashReveal);
                    } else {
                        if (functionsClass.FreeForm()) {
                            functionsClass.openApplicationFreeForm(packageName,
                                    xPosition,
                                    (functionsClass.displayX() / 2),
                                    yPosition,
                                    (functionsClass.displayY() / 2)
                            );
                        } else {
                            functionsClass.appsLaunchPad(packageName);
                        }
                    }
                }

                context.sendBroadcast(new Intent("Hide_PopupListView_Shortcuts_Notification"));
            }
        });
        convertView.setOnTouchListener(new View.OnTouchListener() {
            float xPosition = 0, xMovePosition = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int X = functionsClass.DpToInteger(37);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xPosition = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xMovePosition = motionEvent.getX();
                        int minus = (int) (xMovePosition - xPosition);
                        if (minus < 0) {
                            if (Math.abs(minus) > X) {
                                viewHolder.notificationContent.animate().translationX(-xPosition);
                                context.sendBroadcast(new Intent("Remove_Notification_Key")
                                        .putExtra("notification_key", navDrawerItems.get(position).getNotificationId())
                                        .putExtra("notification_time", navDrawerItems.get(position).getNotificationTime())
                                        .putExtra("notification_package", navDrawerItems.get(position).getNotificationPackage())
                                );
                                context.sendBroadcast(new Intent("Hide_PopupListView_Shortcuts_Notification"));
                            }
                        } else {
                            if (Math.abs(minus) > X) {
                                viewHolder.notificationContent.animate().translationX(xPosition);
                                context.sendBroadcast(new Intent("Remove_Notification_Key")
                                        .putExtra("notification_key", navDrawerItems.get(position).getNotificationId())
                                        .putExtra("notification_time", navDrawerItems.get(position).getNotificationTime())
                                        .putExtra("notification_package", navDrawerItems.get(position).getNotificationPackage())
                                );
                                context.sendBroadcast(new Intent("Hide_PopupListView_Shortcuts_Notification"));
                            }
                        }
                        break;
                }
                return false;
            }
        });

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout notificationItem;
        RelativeLayout notificationBanner;
        RelativeLayout notificationContent;
        ShapesImage notificationAppIcon;
        ShapesImage notificationLargeIcon;
        TextView notificationAppName;
        TextView notificationTitle;
        TextView notificationText;
    }
}
