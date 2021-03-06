/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 4/27/20 11:21 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Folders.FoldersAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Utils.AdapterItemsData.AdapterItemsPopupOptionsFloatingFolders;
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Utils.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Utils.UI.FloatingSplash;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.util.ArrayList;

public class PopupFolderOptionAdapter extends BaseAdapter {

    Context context;

    FunctionsClass functionsClass;

    ArrayList<AdapterItemsPopupOptionsFloatingFolders> adapterItemPopupOptionsFloatingFolders;
    String className;

    int startId, layoutInflater, xPosition, yPosition, HW;


    public PopupFolderOptionAdapter(Context context, ArrayList<AdapterItemsPopupOptionsFloatingFolders> adapterItemPopupOptionsFloatingFolders, String className, int startId) {
        this.context = context;
        this.adapterItemPopupOptionsFloatingFolders = adapterItemPopupOptionsFloatingFolders;
        this.className = className;
        this.startId = startId;

        functionsClass = new FunctionsClass(context);

        switch (functionsClass.shapesImageId()) {
            case 1:
                layoutInflater = R.layout.item_popup_category_droplet;
                break;
            case 2:
                layoutInflater = R.layout.item_popup_category_circle;
                break;
            case 3:
                layoutInflater = R.layout.item_popup_category_square;
                break;
            case 4:
                layoutInflater = R.layout.item_popup_category_squircle;
                break;
            case 5:
                layoutInflater = R.layout.item_popup_category_cut_circle;
                break;
            case 0:
                layoutInflater = R.layout.item_popup_category_noshape;
                break;
        }
    }

    public PopupFolderOptionAdapter(Context context, ArrayList<AdapterItemsPopupOptionsFloatingFolders> adapterItemPopupOptionsFloatingFolders, String className, int startId,
                                    int xPosition, int yPosition, int HW) {
        this.context = context;
        this.adapterItemPopupOptionsFloatingFolders = adapterItemPopupOptionsFloatingFolders;
        this.className = className;
        this.startId = startId;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.HW = HW;

        functionsClass = new FunctionsClass(context);

        switch (functionsClass.shapesImageId()) {
            case 1:
                layoutInflater = R.layout.item_popup_category_droplet;
                break;
            case 2:
                layoutInflater = R.layout.item_popup_category_circle;
                break;
            case 3:
                layoutInflater = R.layout.item_popup_category_square;
                break;
            case 4:
                layoutInflater = R.layout.item_popup_category_squircle;
                break;
            case 5:
                layoutInflater = R.layout.item_popup_category_cut_circle;
                break;
            case 0:
                layoutInflater = R.layout.item_popup_category_noshape;
                break;
        }
    }

    @Override
    public int getCount() {
        return adapterItemPopupOptionsFloatingFolders.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterItemPopupOptionsFloatingFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(layoutInflater, null);

            viewHolder = new ViewHolder();
            viewHolder.items = (RelativeLayout) convertView.findViewById(R.id.items);
            viewHolder.imgIcon = (ShapesImage) convertView.findViewById(R.id.iconItem);
            viewHolder.textAppName = (TextView) convertView.findViewById(R.id.itemAppName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imgIcon.setImageDrawable(adapterItemPopupOptionsFloatingFolders.get(position).getItemIcon());
        viewHolder.textAppName.setText(adapterItemPopupOptionsFloatingFolders.get(position).getItemTitle());

        viewHolder.imgIcon.setImageAlpha(PublicVariable.transparencyEnabled ? 157 : 250);
        viewHolder.textAppName.setAlpha(PublicVariable.transparencyEnabled ? 0.77f : 1.0f);

        int itemsListColor;
        if (functionsClass.appThemeTransparent() == true) {
            itemsListColor = functionsClass.setColorAlpha(PublicVariable.colorLightDark, 157);
        } else {
            itemsListColor = PublicVariable.colorLightDark;
        }

        LayerDrawable drawPopupShortcut = (LayerDrawable) context.getDrawable(R.drawable.popup_shortcut_whole);
        Drawable backPopupShortcut = drawPopupShortcut.findDrawableByLayerId(R.id.backgroundTemporary);
        backPopupShortcut.setTint(itemsListColor);
        viewHolder.items.setBackground(drawPopupShortcut);
        viewHolder.textAppName.setTextColor(PublicVariable.colorLightDarkOpposite);

        viewHolder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterItemPopupOptionsFloatingFolders.get(position).getItemTitle().contains(context.getString(R.string.remove_category))) {
                    context.sendBroadcast(new Intent("Remove_Category_" + className).putExtra("startId", startId));
                } else if (adapterItemPopupOptionsFloatingFolders.get(position).getItemTitle().contains(context.getString(R.string.pin_category))) {
                    context.sendBroadcast(new Intent("Pin_App_" + className).putExtra("startId", startId));
                } else if (adapterItemPopupOptionsFloatingFolders.get(position).getItemTitle().contains(context.getString(R.string.unpin_category))) {
                    context.sendBroadcast(new Intent("Unpin_App_" + className).putExtra("startId", startId));
                } else {
                    if (functionsClass.splashReveal()) {
                        Intent splashReveal = new Intent(context, FloatingSplash.class);
                        splashReveal.putExtra("packageName", adapterItemPopupOptionsFloatingFolders.get(position).getItemAppPackageName());
                        splashReveal.putExtra("X", xPosition);
                        splashReveal.putExtra("Y", yPosition);
                        splashReveal.putExtra("HW", HW);
                        context.startService(splashReveal);
                    } else {
                        functionsClass.appsLaunchPad(adapterItemPopupOptionsFloatingFolders.get(position).getItemAppPackageName());
                    }
                }
                context.sendBroadcast(new Intent("Hide_PopupListView_Category"));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout items;
        ShapesImage imgIcon;
        TextView textAppName;
    }
}
