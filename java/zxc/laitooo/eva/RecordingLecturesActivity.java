package zxc.laitooo.eva;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecordingLecturesActivity extends AppCompatActivity {

    Button start,stop;
    private MediaRecorder rec;
    private String fileName ;
    ListView listView;
    File[] records;
    File path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_lectures);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        start = (Button) findViewById(R.id.buttonStart);
        stop = (Button) findViewById(R.id.buttonStop);
        listView = (ListView) findViewById(R.id.listView);


        stop.setEnabled(true);
        stop.setEnabled(false);


        File jh = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Train");
        if (!jh.exists()) {
            jh.mkdir();
        }

        File location = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Train");
        path = new File(Environment.getExternalStorageDirectory() + "/Train");

        records = path.listFiles();

        ArrayAdapter<String> a = new ArrayAdapter<String>(getApplicationContext(), R.layout.teext,
                R.id.teext, getName(toArrays(records)));
        listView.setAdapter(a);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(records[position]), "audio");
                startActivity(i);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 19) {
                    checkP(Manifest.permission.RECORD_AUDIO, 1234);
                }
                rec = new MediaRecorder();
                rec.setAudioSource(MediaRecorder.AudioSource.MIC);
                rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                rec.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

                Long sec = System.currentTimeMillis() / 1000;
                String ts = sec.toString();
                fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Train/" + "record" + ts + ".mp3";

                rec.setOutputFile(fileName);

                try {
                    rec.prepare();
                    rec.start();
                } catch (IOException e) {
                    Toast.makeText(RecordingLecturesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                start.setEnabled(false);
                stop.setEnabled(true);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rec.stop();
                rec.release();
                Toast.makeText(RecordingLecturesActivity.this, "Record saved successfully", Toast.LENGTH_SHORT).show();
                stop.setEnabled(false);
                start.setEnabled(true);
                records = path.listFiles();

                ArrayAdapter<String> a = new ArrayAdapter<String>(getApplicationContext(), R.layout.teext,
                        R.id.teext, getName(toArrays(records)));
                listView.setAdapter(a);
            }
        });

    }

    public ArrayList<String> getName(ArrayList<File> files){
        ArrayList<String> strings = new ArrayList<>();
        for (File g : files){
            strings.add(g.getName());
        }
        return  strings;
    }

    public ArrayList<File> toArrays(File[] files){
        ArrayList<File> strings = new ArrayList<>();
        for (int k=0;k<files.length;k++){
            strings.add(files[k]);
        }
        return  strings;
    }

    public void checkP(String permission,int code){
        int perm = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            perm = checkSelfPermission(permission);
        }
        if (perm != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{permission},code);
            }
        }
    }
}
