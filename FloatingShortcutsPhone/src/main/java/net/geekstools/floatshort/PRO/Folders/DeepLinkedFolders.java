package net.geekstools.floatshort.PRO.Folders;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;


public class DeepLinkedFolders extends Activity {

    String CategoryName;

    private String htmlSymbol = "/";
    private String htmlSymbolDelete = "%20%7C%20Floating%20Category";

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        FunctionsClass functionsClass = new FunctionsClass(getApplicationContext(), DeepLinkedFolders.this);

        PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);
        PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, this.getResources().getDisplayMetrics());

        try {
            String incomingURI = getIntent().getDataString();
            FunctionsClassDebug.Companion.PrintDebug("URI >> " + incomingURI);
            CategoryName = incomingURI.substring(incomingURI.lastIndexOf(htmlSymbol) + 1);
            CategoryName = CategoryName.replace(htmlSymbolDelete, "");

            functionsClass.runUnlimitedFolderService(CategoryName);
        } catch (Exception e) {
            finish();
            return;
        }

        finish();
    }
}
