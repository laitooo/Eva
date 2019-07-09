package zxc.laitooo.eva;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import zxc.laitooo.eva.speechrecognition.OnSpeechRecognitionListener;
import zxc.laitooo.eva.speechrecognition.OnSpeechRecognitionPermissionListener;
import zxc.laitooo.eva.speechrecognition.SpeechRecognition;

public class SendingActivity extends AppCompatActivity {

    Button record;
    EditText note;
    private String text;
    boolean isFinished ;
    SpeechRecognition speechRecognition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        record = (Button)findViewById(R.id.recordingButton);
        note = (EditText)findViewById(R.id.myNote);

        try {
            speechRecognition = new SpeechRecognition(this);
            speechRecognition.setSpeechRecognitionPermissionListener(g);
            speechRecognition.setSpeechRecognitionListener(h);
        }catch (Exception e){
            Toast.makeText(SendingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    OnSpeechRecognitionPermissionListener g = new OnSpeechRecognitionPermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(SendingActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied() {
            Toast.makeText(SendingActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    };

    OnSpeechRecognitionListener h = new OnSpeechRecognitionListener() {
        @Override
        public void OnSpeechRecognitionStarted() {

            Toast.makeText(SendingActivity.this,"Listening started",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnSpeechRecognitionStopped() {
            if (isFinished) {
                Toast.makeText(SendingActivity.this, "Listening stopped", Toast.LENGTH_SHORT).show();
            }else {
                speechRecognition.startSpeechRecognition();
            }
        }

        @Override
        public void OnSpeechRecognitionFinalResult(String finalSentence) {
            text = note.getText().toString();
            note.setText(text+ " " + finalSentence);
            if (!isFinished) {
                speechRecognition.startSpeechRecognition();
            }
        }

        @Override
        public void OnSpeechRecognitionCurrentResult(String currentWord) {

        }

        @Override
        public void OnSpeechRecognitionError(int errorCode, String errorMsg) {
            Toast.makeText(SendingActivity.this,errorMsg,Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognition.stopSpeechRecognition();
    }

    public void record(View view){
        if (isFinished){
            isFinished = false;
            speechRecognition.startSpeechRecognition();
            record.setText("stop");
        }else {
            isFinished = true;
            speechRecognition.stopSpeechRecognition();
            record.setText("start");
        }

    }

    public void send(View view){
        Intent intent = new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }


}
