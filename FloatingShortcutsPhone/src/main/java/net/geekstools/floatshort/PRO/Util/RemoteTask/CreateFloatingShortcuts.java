package net.geekstools.floatshort.PRO.Util.RemoteTask;

import android.app.Activity;
import android.os.Bundle;

import net.geekstools.floatshort.PRO.BuildConfig;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

public class CreateFloatingShortcuts extends Activity {

    FunctionsClass functionsClass;

    String packageName, className;

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        functionsClass = new FunctionsClass(getApplicationContext(), CreateFloatingShortcuts.this);

        packageName = getIntent().getStringExtra("PackageName");
        className = getIntent().getStringExtra("ClassName");

        if (functionsClass.loadCustomIcons()) {
            LoadCustomIcons loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
            loadCustomIcons.load();
            if (BuildConfig.DEBUG) {
                System.out.println("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
            }
        }

        functionsClass.runUnlimitedShortcutsServiceHIS(packageName, className);

        finish();
    }
}
