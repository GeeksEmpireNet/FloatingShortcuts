package net.geekstools.floatshort.PRO.Util.NavAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.geekstools.floatshort.PRO.Automation.Apps.AppAutoFeatures;
import net.geekstools.floatshort.PRO.Automation.Categories.CategoryAutoFeatures;
import net.geekstools.floatshort.PRO.Folders.FoldersHandler;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Shortcuts.HybridAppsList;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryFolders;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryShortcuts;
import net.geekstools.floatshort.PRO.Util.SettingGUI.SettingGUI;

import java.util.ArrayList;

public class ActionListAdapter extends BaseAdapter {

    FunctionsClass functionsClass;
    private Activity activity;
    private Context context;
    private ArrayList<AdapterItems> adapterItems;

    public ActionListAdapter(Activity activity, Context context, ArrayList<AdapterItems> adapterItems) {
        this.activity = activity;
        this.context = context;
        this.adapterItems = adapterItems;

        functionsClass = new FunctionsClass(context, activity);
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
            convertView = mInflater.inflate(R.layout.act_card_list, null);

            viewHolder = new ViewHolder();
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.purchaseItemName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (PublicVariable.themeLightDark) {
            viewHolder.txtTitle.setTextColor(context.getColor(R.color.dark));
        } else if (!PublicVariable.themeLightDark) {
            viewHolder.txtTitle.setTextColor(context.getColor(R.color.light));
        }

        viewHolder.imgIcon.setImageDrawable(adapterItems.get(position).getAppIcon());
        viewHolder.txtTitle.setText(adapterItems.get(position).getCharTitle());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterItems.get(position).getCharTitle().equals(context.getString(R.string.automation))) {
                    if (functionsClass.readPreference("OpenMode", "openClassName", HybridAppsList.class.getSimpleName())
                            .equals(FoldersHandler.class.getSimpleName())) {
                        Intent intent = new Intent(context, CategoryAutoFeatures.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, AppAutoFeatures.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    activity.overridePendingTransition(R.anim.up_down, android.R.anim.fade_out);
                    /*if (!functionsClass.wallpaperStaticLive()) {
                        activity.finish();
                    }*/
                } else if (adapterItems.get(position).getCharTitle().equals(context.getString(R.string.floatingCategory))) {
                    Intent intent = new Intent(context, FoldersHandler.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    activity.overridePendingTransition(android.R.anim.fade_in, R.anim.go_up);
                    activity.finish();
                } else if (adapterItems.get(position).getCharTitle().equals(context.getString(R.string.recoveryShortcuts))) {
                    Intent intent = new Intent(context, RecoveryShortcuts.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(intent);
                } else if (adapterItems.get(position).getCharTitle().equals(context.getString(R.string.recover_category))) {
                    Intent intent = new Intent(context, RecoveryFolders.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(intent);
                } else {
                    Intent intent = new Intent(context, SettingGUI.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                /*if (navDrawerItems.get(position).getCharTitle().equals(context.getString(R.string.automation))) {
                    functionsClass.ShortcutsDialogue(AppAutoFeatures.class, ".Alias.Automation", context.getString(R.string.automation), R.drawable.alias_auto);
                } else if (navDrawerItems.get(position).getCharTitle().equals(context.getString(R.string.floatingCategory))) {
                    functionsClass.ShortcutsDialogue(CategoryHandler.class, ".Alias.CategoryHandler", context.getString(R.string.floatingCategory), R.drawable.alias_categories);
                } else if (navDrawerItems.get(position).getCharTitle().equals(context.getString(R.string.recoveryShortcuts))) {
                    functionsClass.ShortcutsDialogue(RecoveryShortcutsActivity.class, ".Alias.Recover.Shortcuts", context.getString(R.string.recoveryShortcuts), R.drawable.alias_shortcuts_recovery);
                } else if (navDrawerItems.get(position).getCharTitle().equals(context.getString(R.string.recover_category))) {
                    functionsClass.ShortcutsDialogue(RecoveryCategoryActivity.class, ".Alias.Recover.Categories", context.getString(R.string.recover_category), R.drawable.alias_categories_recovery);
                }*/
                return true;
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;
    }
}