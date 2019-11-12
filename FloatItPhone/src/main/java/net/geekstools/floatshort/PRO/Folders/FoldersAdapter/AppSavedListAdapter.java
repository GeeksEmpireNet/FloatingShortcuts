/*
 * Copyright © 2019 By Geeks Empire.
 *
 * Created by Elias Fazel on 11/11/19 7:18 PM
 * Last modified 11/11/19 7:16 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Folders.FoldersAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.GeneralAdapters.AdapterItems;

import java.util.ArrayList;

public class AppSavedListAdapter extends BaseAdapter {

    FunctionsClass functionsClass;
    int splitNumber = 1, layoutInflater;
    private Context context;
    private Activity activity;
    private ArrayList<AdapterItems> adapterItems;

    public AppSavedListAdapter(Activity activity, Context context, ArrayList<AdapterItems> adapterItems, int splitNumber) {
        this.activity = activity;
        this.context = context;
        this.adapterItems = adapterItems;
        this.splitNumber = splitNumber;

        functionsClass = new FunctionsClass(context, activity);

        switch (functionsClass.shapesImageId()) {
            case 1:
                layoutInflater = R.layout.item_saved_app_droplet;
                break;
            case 2:
                layoutInflater = R.layout.item_saved_app_circle;
                break;
            case 3:
                layoutInflater = R.layout.item_saved_app_square;
                break;
            case 4:
                layoutInflater = R.layout.item_saved_app_squircle;
                break;
            case 0:
                layoutInflater = R.layout.item_saved_app_noshape;
                break;
        }
    }

    @Override
    public int getCount() {
        return adapterItems.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterItems.get(position);
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
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.iconItem);
            viewHolder.textAppName = (TextView) convertView.findViewById(R.id.itemAppName);
            viewHolder.deleteItem = (Button) convertView.findViewById(R.id.deleteItem);
            viewHolder.confirmItem = (Button) convertView.findViewById(R.id.confirmItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (functionsClass.returnAPI() < 24) {
            viewHolder.confirmItem.setVisibility(View.INVISIBLE);
        }

        LayerDrawable drawConfirm = (LayerDrawable) context.getDrawable(R.drawable.ripple_effect_confirm);
        Drawable backConfirm = drawConfirm.findDrawableByLayerId(R.id.backtemp);
        backConfirm.setTint(PublicVariable.primaryColorOpposite);
        viewHolder.confirmItem.setBackground(drawConfirm);
        viewHolder.textAppName.setTextColor(context.getColor(R.color.light));

        viewHolder.imgIcon.setImageDrawable(adapterItems.get(position).getAppIcon());
        viewHolder.textAppName.setText(adapterItems.get(position).getAppName());

        viewHolder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.deleteFile(adapterItems.get(position).getPackageName()
                        + PublicVariable.categoryName);
                functionsClass.removeLine(PublicVariable.categoryName,
                        adapterItems.get(position).getPackageName());
                context.sendBroadcast(new Intent(context.getString(R.string.checkboxActionAdvance)));
                context.sendBroadcast(new Intent(context.getString(R.string.counterActionAdvance)));
            }
        });
        viewHolder.confirmItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (splitNumber == 1) {
                    functionsClass.saveFile(PublicVariable.categoryName + ".SplitOne", adapterItems.get(position).getPackageName());
                } else if (splitNumber == 2) {
                    functionsClass.saveFile(PublicVariable.categoryName + ".SplitTwo", adapterItems.get(position).getPackageName());
                }
                context.sendBroadcast(new Intent(context.getString(R.string.splitActionAdvance)));
                context.sendBroadcast(new Intent(context.getString(R.string.visibilityActionAdvance)));
            }
        });

        RippleDrawable rippleDrawableBack = (RippleDrawable) context.getDrawable(R.drawable.saved_app_shortcut_whole);
        Drawable gradientDrawableBack = rippleDrawableBack.findDrawableByLayerId(R.id.backtemp);
        Drawable gradientDrawableBackMask = rippleDrawableBack.findDrawableByLayerId(android.R.id.mask);

        rippleDrawableBack.setColor(ColorStateList.valueOf(PublicVariable.colorLightDark));
        gradientDrawableBack.setTint(PublicVariable.primaryColorOpposite);
        gradientDrawableBackMask.setTint(PublicVariable.colorLightDark);

        viewHolder.items.setBackground(rippleDrawableBack);

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout items;
        ImageView imgIcon;
        TextView textAppName;
        Button deleteItem;
        Button confirmItem;
    }
}