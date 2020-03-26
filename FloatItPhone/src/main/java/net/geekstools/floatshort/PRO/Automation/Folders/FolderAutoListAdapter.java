/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 3/26/20 2:51 PM
 * Last modified 3/26/20 2:17 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Automation.Folders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import net.geekstools.floatshort.PRO.Automation.Alarms.TimeDialogue;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Utils.AdapterItemsData.AdapterItems;
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Utils.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Utils.UI.CustomIconManager.LoadCustomIcons;

import java.io.File;
import java.util.ArrayList;

public class FolderAutoListAdapter extends RecyclerView.Adapter<FolderAutoListAdapter.ViewHolder> {

    private Context context;
    private Activity activity;

    FunctionsClass functionsClass;

    ImageView imageView;
    RelativeLayout freqLayout;

    View view;
    ViewHolder viewHolder;

    LoadCustomIcons loadCustomIcons;

    String autoIdAppend;

    private ArrayList<AdapterItems> adapterItems;

    public FolderAutoListAdapter(Activity activity, Context context, ArrayList<AdapterItems> adapterItems) {
        this.activity = activity;
        this.context = context;
        this.adapterItems = adapterItems;

        functionsClass = new FunctionsClass(context);

        if (PublicVariable.autoID != null) {
            if (PublicVariable.autoID.equals(context.getString(R.string.wifi_category))) {
                autoIdAppend = "Wifi";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.bluetooth_category))) {
                autoIdAppend = "Bluetooth";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.gps_category))) {
                autoIdAppend = "Gps";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.nfc_category))) {
                autoIdAppend = "Nfc";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.time_category))) {
                autoIdAppend = "Time";
            }
        }

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(context, functionsClass.customIconPackageName());
        }
    }

    @Override
    public FolderAutoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.auto_categories_items, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolderBinder, final int position) {

        RelativeLayout categoryItem = viewHolderBinder.categoryItem;

        viewHolderBinder.categoryName.setTextColor(PublicVariable.colorLightDarkOpposite);
        viewHolderBinder.categoryName.setHintTextColor(functionsClass.setColorAlpha(PublicVariable.colorLightDarkOpposite, 175));

        viewHolderBinder.autoChoice.setButtonTintList(ColorStateList.valueOf(PublicVariable.colorLightDarkOpposite));

        final String nameCategory = adapterItems.get(position).getCategory();
        final String[] categoryPackages = adapterItems.get(position).getPackageNames();

        viewHolderBinder.categoryName.setText(adapterItems.get(position).getCategory());
        viewHolderBinder.timeView.setText(String.valueOf(nameCategory.charAt(0)).toUpperCase());

        if (nameCategory.equals(context.getPackageName())) {
            try {
                viewHolderBinder.timeView.setText(context.getString(R.string.index_item));
                viewHolderBinder.categoryName.setText("");
                viewHolderBinder.selectedApp.removeAllViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File autoFile = context.getFileStreamPath(nameCategory);
            if (autoFile.exists() && autoFile.isFile()) {
                viewHolderBinder.selectedApp.removeAllViews();
                int previewItems = 7;
                if (categoryPackages.length < 7) {
                    previewItems = categoryPackages.length;
                }
                for (int i = 0; i < previewItems; i++) {
                    freqLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.selected_apps_item, null);
                    imageView = functionsClass.initShapesImage(freqLayout, R.id.appSelectedItem);
                    imageView.setImageDrawable(functionsClass.loadCustomIcons() ?
                            loadCustomIcons.getDrawableIconForPackage(categoryPackages[i], functionsClass.shapedAppIcon(categoryPackages[i]))
                            :
                            functionsClass.shapedAppIcon(categoryPackages[i]));
                    viewHolderBinder.selectedApp.addView(freqLayout);
                }
            }
        }

        try {
            if (PublicVariable.autoID.equals(context.getString(R.string.time_category))) {
                File autoFile = context.getFileStreamPath(nameCategory + ".Time");
                viewHolderBinder.autoChoice.setChecked(false);
                viewHolderBinder.timeView.setTextSize(50);
                if (autoFile.exists()) {
                    viewHolderBinder.autoChoice.setChecked(true);
                    viewHolderBinder.timeView.setTextSize(15);
                    viewHolderBinder.timeView.setText(adapterItems.get(position).getTimes());
                    viewHolderBinder.timeView.setVisibility(View.VISIBLE);
                } else {
                    viewHolderBinder.autoChoice.setChecked(false);
                    viewHolderBinder.timeView.setTextSize(50);
                }
            } else {
                File autoFile = context.getFileStreamPath(nameCategory + "." + autoIdAppend);
                viewHolderBinder.autoChoice.setChecked(false);
                if (autoFile.exists()) {
                    viewHolderBinder.autoChoice.setChecked(true);
                } else {
                    viewHolderBinder.autoChoice.setChecked(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        categoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PublicVariable.autoID == null) {
                    Toast.makeText(context, context.getString(R.string.retry), Toast.LENGTH_LONG).show();
                } else {
                    if (PublicVariable.autoID.equals(context.getString(R.string.time_category))) {
                        final String nameCategory = adapterItems.get(position).getCategory();

                        File autoFile = context.getFileStreamPath(nameCategory + "." + autoIdAppend);
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    adapterItems.get(position).getCategory() + "." + autoIdAppend);
                            functionsClass.removeLine(
                                    adapterItems.get(position).getTimes(),
                                    adapterItems.get(position).getCategory());
                            if (functionsClass.countLineInnerFile(adapterItems.get(position).getCategory()) == 0) {
                                context.deleteFile(adapterItems.get(position).getTimes());
                            }

                            functionsClass.removeLine(".times.clocks", adapterItems.get(position).getTimes());
                            viewHolderBinder.autoChoice.setChecked(false);
                            viewHolderBinder.timeView.setTextSize(50);
                            viewHolderBinder.timeView.setText(String.valueOf(adapterItems.get(position).getCategory().charAt(0)).toUpperCase());
                        } else {
                            viewHolderBinder.autoChoice.setChecked(true);
                            context.startActivity(
                                    new Intent(context, TimeDialogue.class)
                                            .putExtra("content", adapterItems.get(position).getCategory())
                                            .putExtra("type", "CATEGORY")
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    } else {
                        final String nameCategory = adapterItems.get(position).getCategory();
                        File autoFile = context.getFileStreamPath(nameCategory + "." + autoIdAppend);
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    adapterItems.get(position).getCategory() + "." + autoIdAppend);
                            functionsClass.removeLine(".auto" + autoIdAppend + "Category", adapterItems.get(position).getCategory());
                            viewHolderBinder.autoChoice.setChecked(false);
                        } else {
                            functionsClass.saveFile(
                                    adapterItems.get(position).getCategory() + "." + autoIdAppend,
                                    adapterItems.get(position).getCategory());
                            functionsClass.saveFileAppendLine(
                                    ".auto" + autoIdAppend + "Category",
                                    adapterItems.get(position).getCategory());
                            viewHolderBinder.autoChoice.setChecked(true);
                        }
                    }
                }
            }
        });

        RippleDrawable drawItem = (RippleDrawable) context.getDrawable(R.drawable.ripple_effect_category_logo);
        drawItem.setDrawableByLayerId(R.id.category_logo_layer, functionsClass.shapesDrawablesCategory(viewHolderBinder.timeView));
        drawItem.setDrawableByLayerId(android.R.id.mask, functionsClass.shapesDrawablesCategory(viewHolderBinder.timeView));
        Drawable categoryLogoLayer = (Drawable) drawItem.findDrawableByLayerId(R.id.category_logo_layer);
        Drawable categoryMask = (Drawable) drawItem.findDrawableByLayerId(android.R.id.mask);
        categoryLogoLayer.setTint(PublicVariable.primaryColorOpposite);
        categoryMask.setTint(PublicVariable.primaryColor);
        viewHolderBinder.timeView.setBackground(drawItem);

        RippleDrawable drawCategories = (RippleDrawable) context.getDrawable(R.drawable.auto_category);
        Drawable backCategories = drawCategories.findDrawableByLayerId(R.id.category_item);
        Drawable backCategoriesRipple = drawCategories.findDrawableByLayerId(android.R.id.mask);
        backCategories.setTint(PublicVariable.colorLightDark);
        backCategoriesRipple.setTint(PublicVariable.colorLightDarkOpposite);
        drawCategories.setColor(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
        drawCategories.setAlpha(7);
        categoryItem.setBackground(drawCategories);
    }

    @Override
    public int getItemCount() {
        return adapterItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout categoryItem;
        LinearLayout selectedApp;
        EditText categoryName;
        TextView timeView;
        CheckBox autoChoice;

        public ViewHolder(View view) {
            super(view);
            categoryItem = (RelativeLayout) view.findViewById(R.id.categoryItem);
            selectedApp = (LinearLayout) view.findViewById(R.id.selectedApps);
            categoryName = (EditText) view.findViewById(R.id.categoryName);
            timeView = (TextView) view.findViewById(R.id.time);
            autoChoice = (CheckBox) view.findViewById(R.id.autoChoice);
        }
    }
}
