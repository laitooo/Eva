package zxc.laitooo.eva;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import zxc.laitooo.eva.data.App;
import zxc.laitooo.eva.data.Contact;
import zxc.laitooo.eva.data.GetApps;
import zxc.laitooo.eva.data.GetContacts;
import zxc.laitooo.eva.messages.Message;
import zxc.laitooo.eva.messages.MessagesAdapter;
import zxc.laitooo.eva.messages.MessagesHandler;
import zxc.laitooo.eva.speechrecognition.OnSpeechRecognitionListener;
import zxc.laitooo.eva.speechrecognition.OnSpeechRecognitionPermissionListener;
import zxc.laitooo.eva.speechrecognition.SpeechRecognition;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    static ArrayList<Contact> myContacts;
    static ArrayList<App> myApps;

    Executor executor;
    static MessagesHandler messagesHandler;

    static ArrayList<Message> messages;
    static RecyclerView recyclerView;
    static MessagesAdapter adapter;
    boolean fromUser;
    static Context c;
    SpeechRecognition speechRecognition;
    boolean appReady;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setTitle("");

        executor = new Executor(MainActivity.this);
        messagesHandler = new MessagesHandler(MainActivity.this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        checkP(Manifest.permission.CALL_PHONE, 1111);
        checkP(Manifest.permission.READ_CONTACTS, 1112);
        checkP(Manifest.permission.READ_EXTERNAL_STORAGE, 1113);
        checkP(Manifest.permission.WRITE_SETTINGS,1114);
        checkP(Manifest.permission.BLUETOOTH,1115);
        checkP(Manifest.permission.ACCESS_WIFI_STATE,1116);
        checkP(Manifest.permission.CHANGE_WIFI_STATE,1117);


        c = MainActivity.this;
        fromUser = false;

        try {
            speechRecognition = new SpeechRecognition(MainActivity.this);
            speechRecognition.setSpeechRecognitionPermissionListener(g);
            speechRecognition.setSpeechRecognitionListener(h);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        messages = messagesHandler.getMessages();
        recyclerView = (RecyclerView) findViewById(R.id.recycyler_speaking);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessagesAdapter(c, messages);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(messages.size()-1);



        appReady = true;

        final ImageButton button = (ImageButton) findViewById(R.id.voice_order);
        final EditText text = (EditText) findViewById(R.id.text_order);

        text.setSingleLine(true);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")){
                    button.setImageResource(R.drawable.icon_mic);
                }else {
                    button.setImageResource(R.drawable.icon_send);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appReady) {
                    if (text.getText().toString().equals("")) {
                        try {
                            speechRecognition.startSpeechRecognition();
                        } catch (Exception e) {
                            //executor.speakText(e.getMessage());
                        }
                    }else {
                        addMessage(true,text.getText().toString());
                        executor.execute(text.getText().toString());
                        text.setText("");
                    }
                }else {
                    Toast.makeText(c, "the app isn't ready", Toast.LENGTH_SHORT).show();
                }
            }
        });



        try {
            myContacts = new GetContacts(c).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            myContacts = new ArrayList<>();
            Toast.makeText(c, "cant load contacts", Toast.LENGTH_SHORT).show();
        }

        try {
            myApps = new GetApps(c).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            myApps = new ArrayList<>();
            Toast.makeText(c, "cant load apps", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.do_call:

                break;
            case R.id.do_search:

                break;
            case R.id.do_chat:

                break;
            case R.id.do_send:

                break;
            case R.id.do_open_app:

                break;
            case R.id.do_play:

                break;
            case R.id.do_record:

                break;
            case R.id.do_open_file:

                break;
            case R.id.do_wifi:

                break;
            case R.id.do_bluetooth:

                break;
            case R.id.do_brightness:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkP(String permission, int code) {
        int perm = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            perm = checkSelfPermission(permission);
        }
        if (perm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{permission}, code);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // IF  the selected permissions is ready
        if (requestCode == 1111){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Permission bluetooth granted",Toast.LENGTH_LONG).show();
            }
        }
    }

    OnSpeechRecognitionPermissionListener g = new OnSpeechRecognitionPermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied() {
            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    };

    OnSpeechRecognitionListener h = new OnSpeechRecognitionListener() {
        @Override
        public void OnSpeechRecognitionStarted() {
            Toast.makeText(MainActivity.this, "Listening started", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnSpeechRecognitionStopped() {
            Toast.makeText(MainActivity.this, "Listening stopped", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnSpeechRecognitionFinalResult(String finalSentence) {
            addMessage(true,finalSentence);
            executor.execute(finalSentence);
        }

        @Override
        public void OnSpeechRecognitionCurrentResult(String currentWord) {
            //b.setText(currentWord);
        }

        @Override
        public void OnSpeechRecognitionError(int errorCode, String errorMsg) {
            executor.speakText(errorMsg);
        }
    };

    public static void addMessage(boolean fromUser,String text){
        Message message = new Message(fromUser,text);
        messages.add(message);
        messagesHandler.addMessage(message);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messages.size()-1);
    }

    public static void clearMessages(){
        MessagesHandler manager = new MessagesHandler(c);
        manager.clearMessages();
        messages.clear();
        adapter.notifyDataSetChanged();
    }

}
