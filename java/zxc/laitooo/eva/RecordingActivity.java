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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import zxc.laitooo.eva.records.Record;
import zxc.laitooo.eva.records.RecordsAdapter;

public class RecordingActivity extends AppCompatActivity {

    Button start,stop;
    private MediaRecorder rec;
    private String fileName ;
    File[] records;
    File path;

    String EVA_PATH = "/Eva";
    String RECORDS_PATH = "/Records";
    ArrayList<Record> recordsList;
    RecyclerView recyclerView;
    RecordsAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_lectures);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Recorder");

        start = (Button) findViewById(R.id.buttonStart);
        stop = (Button) findViewById(R.id.buttonStop);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_record);


        stop.setEnabled(true);
        stop.setEnabled(false);

        File main = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + EVA_PATH);
        if (!main.exists())
            main.mkdir();

        path = new File(main,RECORDS_PATH);
        if (!path.exists()) {
            path.mkdir();
        }

        recordsList = new ArrayList<>();

        File[] files = path.listFiles();
        for (int i=0;i<files.length;i++){
            Log.e("RRRRR",files[i].getName());
            recordsList.add(new Record(files[i]));
        }

        adapter = new RecordsAdapter(RecordingActivity.this,recordsList);

        recyclerView.setLayoutManager(new LinearLayoutManager(RecordingActivity.this));
        recyclerView.setAdapter(adapter);

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

                int number = path.listFiles().length+1;
                fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + EVA_PATH
                        + RECORDS_PATH + "/Record " + number + ".mp3";


                rec.setOutputFile(fileName);

                try {
                    rec.prepare();
                    rec.start();
                    Toast.makeText(RecordingActivity.this, "started recoding", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(RecordingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RecordingActivity.this, "Record saved successfully", Toast.LENGTH_SHORT).show();
                stop.setEnabled(false);
                start.setEnabled(true);

                File[] files = path.listFiles();
                for (int i=0;i<files.length;i++){
                    recordsList.add(new Record(files[i]));
                }

                adapter.notifyDataSetChanged();
            }
        });

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
