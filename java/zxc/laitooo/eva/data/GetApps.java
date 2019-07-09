package zxc.laitooo.eva.data;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laitooo San on 30/06/2019.
 */

public class GetApps extends AsyncTask<Void,Void,ArrayList<App>> {

    private Context context;

    public GetApps(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<App> doInBackground(Void... params) {
        ArrayList<App> apps = new ArrayList<>();

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfos = packageManager.
                getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app:applicationInfos){
            try {
                if (packageManager.getLaunchIntentForPackage(app.packageName) != null){
                    try {
                        Drawable icon = context.getPackageManager().
                                getApplicationIcon(app.packageName);
                        apps.add(new App((String)app.loadLabel(packageManager),app.packageName,icon));
                    } catch (PackageManager.NameNotFoundException ne) {
                        //AppsList.add(new AppInfo(app.name,null,app.packageName));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("GetApps","exception happened e:" + e.getMessage());
            }



        }
        return apps;
    }

    @Override
    protected void onPostExecute(ArrayList<App> apps) {
        super.onPostExecute(apps);
        Log.e("GetApps","apps loaded");
    }
}
