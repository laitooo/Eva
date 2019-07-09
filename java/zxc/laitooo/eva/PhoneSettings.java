package zxc.laitooo.eva;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Laitooo San on 01/07/2019.
 */

public class PhoneSettings {
    
    private Context context;
    private WifiManager wifiManager;
    private BluetoothAdapter bluetoothAdapter;
    private ContentResolver contentResolver;
    private Window window;
    private int progress;

    public PhoneSettings(Context context) {
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wifiManager = (WifiManager)context.getSystemService(WIFI_SERVICE);
        contentResolver = context.getContentResolver();
        window = ((Activity)context).getWindow();

        try {
            progress = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        }catch (Settings.SettingNotFoundException b ){
            Toast.makeText(context,b.getMessage(),Toast.LENGTH_SHORT).show();
            progress = 0;
        }
    }

    public void wifi_on() {
        wifiManager.setWifiEnabled(true);
    }

    public void wifi_off() {
        wifiManager.setWifiEnabled(false);
    }

    public void bluetooth_on() {
        if (bluetoothAdapter == null) {
            Toast.makeText(context,"device doesn't support bluetooth",Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(i);
        }
    }

    public void bluetooth_off() {
        if (bluetoothAdapter == null) {
            Toast.makeText(context,"device doesnt support bluetooth",Toast.LENGTH_SHORT).show();
        }else {
            bluetoothAdapter.disable();
        }
    }

    public void controlBrightness(int p){
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, progress);
        WindowManager.LayoutParams l = window.getAttributes();
        l.screenBrightness = p / (float) 255;
        window.setAttributes(l);
    }
}
