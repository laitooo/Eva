package zxc.laitooo.eva.data;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by Laitooo San on 30/06/2019.
 */

public class App {

    String Name;
    String PackageName;
    Drawable Icon;

    public App(String name, String packageName, Drawable icon) {
        Name = name;
        PackageName = packageName;
        Icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public Drawable getIcon() {
        return Icon;
    }

    public void setIcon(Drawable icon) {
        Icon = icon;
    }
}
