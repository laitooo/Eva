package zxc.laitooo.eva;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BackgroundActivity extends AppCompatActivity {

    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        c = BackgroundActivity.this;
        setContentView(R.layout.activity_background);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int wi = dm.widthPixels;
        int hi = dm.heightPixels;
        getWindow().setLayout(200,200);


        /*View v = View.inflate(BackgroundActivity.this,R.layout.activity_background,null);
        v.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        //view.getWidth()
        //DisplayMetrics dm = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(dm);
        int wi2 = v.getMeasuredWidth();
        int hi2 = v.getMeasuredHeight();
        getWindow().setLayout( wi2 + 10,  hi2+10);*/

    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

            View view = getWindow().getDecorView();
            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
            lp.gravity = Gravity.CENTER | Gravity.TOP;
            lp.y = 100;

            getWindowManager().updateViewLayout(view, lp);

    }
}
