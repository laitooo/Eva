package zxc.laitooo.eva;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import zxc.laitooo.eva.speechrecognition.OnSpeechRecognitionListener;
import zxc.laitooo.eva.speechrecognition.OnSpeechRecognitionPermissionListener;
import zxc.laitooo.eva.speechrecognition.SpeechRecognition;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ArrayList<Messages> messages;
    RecyclerView recyclerView;
    MessagesAdapter adapter;
    boolean fromUser;
    Context c;
    SpeechRecognition speechRecognition;
    boolean appReady;


    List<ApplicationInfo> appsList;
    List<String> appNames;
    ArrayList<contact> myContacts;
    ArrayList<File> myFiles;
    BluetoothAdapter bluetoothAdapter;
    WifiManager wifiManager;



    private int progress;
    private ContentResolver resolver;
    private Window window;


    private TextToSpeech tts;
    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");


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


        c = getApplicationContext();
        fromUser = false;
        locale = Locale.ENGLISH;
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    tts.setLanguage(locale  );
                }
            }
        });

        try {
            speechRecognition = new SpeechRecognition(MainActivity.this);
            speechRecognition.setSpeechRecognitionPermissionListener(g);
            speechRecognition.setSpeechRecognitionListener(h);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        messages = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycyler_speaking);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessagesAdapter(c, messages);
        recyclerView.setAdapter(adapter);


        appReady = false;
        new phoneData().execute();


        ImageButton button = (ImageButton) findViewById(R.id.imageButton2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appReady) {
                    try {
                        speechRecognition.startSpeechRecognition();
                    }catch (Exception e){
                        Toast.makeText(c, "error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(c, "the app isn't ready", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        resolver = getContentResolver();
        window = getWindow();
        try {
            progress = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        }catch (Settings.SettingNotFoundException b ){
            Toast.makeText(getApplicationContext(),b.getMessage(),Toast.LENGTH_SHORT).show();
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



    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
            messages.add(new Messages(fromUser, finalSentence));
            adapter.notifyDataSetChanged();
            if (fromUser)
                fromUser = false;
            else
                fromUser = true;

            resultOfSpeech(finalSentence);
        }

        @Override
        public void OnSpeechRecognitionCurrentResult(String currentWord) {
            //b.setText(currentWord);
        }

        @Override
        public void OnSpeechRecognitionError(int errorCode, String errorMsg) {
            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
        }
    };

    public class phoneData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            PackageManager packageManager = getPackageManager();
            appsList = packageManager.getInstalledApplications(0);
            appNames = getAppNames(appsList);
            myContacts = new ArrayList<>();
            loadContacts();
            myFiles = new ArrayList<>();
            File path = new File(Environment.getExternalStorageDirectory() +"");
            getFiles(path);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            appReady = true;
            Toast.makeText(c, "The app is ready", Toast.LENGTH_SHORT).show();
        }
    }

    public void resultOfSpeech(String result) {
        result = result.toLowerCase();
        String[] strings = result.split(" ");
        String word = strings[0];

        switch (result) {
            case "hi eva":
                speakText("hi user, how you doing");
                break;
            case "turn on wifi":
            case "turn wifi on":
            case "turn on wi-fi":
            case "turn wi-fi on":
                wifi_on();
                break;
            case "turn off wi-fi":
            case "turn wi-fi off":
            case "turn off wifi":
            case "turn wifi off":
                wifi_off();
                break;
            case "turn bluetooth on":
            case "turn on bluetooth":
                bluetooth_on();
                break;
            case "turn bluetooth off":
            case "turn off bluetooth":
                bluetooth_off();
                break;
            case "increase brightness":
            case "high brightness":
                controlBrightness(255);
                break;
            case "medium brightness":
                controlBrightness(120);
                break;
            case "decrease brightness":
            case "low brightness":
                controlBrightness(20);
                break;
            case "record note":
            case "record notes":
            case "write note":
            case "write notes":
                startActivity(new Intent(MainActivity.this,NotesActivity.class));
                break;
            case "record lectures":
            case "record this lecture":
            case "record the lecture":
            case "record lecture":
                startActivity(new Intent(MainActivity.this,RecordingLecturesActivity.class));
                break;
        }


        switch (word) {
            case "open":
                String appName = result.substring(5);
                if (!openApp(appName))
                    if (!openFile(appName))
                        Toast.makeText(c,"cant find " + appName,Toast.LENGTH_LONG).show();
                break;
            case "call":
                String contactName = result.substring(5);
                if (!callContact(contactName))
                    Toast.makeText(c,"cant find " + contactName,Toast.LENGTH_LONG).show();
                break;
            case "play":
                String filename = result.substring(5);
                if(!openFile(filename))
                    Toast.makeText(c,"cant find " + filename,Toast.LENGTH_LONG).show();
                break;
            case "how":
            case "what":
            case "why":
            case "who":
            case "whom":
            case "when":
            case "which":
                searchAbout(result);
                break;

        }
    }

    public ArrayList<String> getAppNames(List<ApplicationInfo> infoList) {
        ArrayList<String> oi = new ArrayList<>();
        for (ApplicationInfo ee : infoList) {
            if (ee != null) {
                CharSequence f = ee.loadLabel(getPackageManager());
                oi.add((String)f);
            }
        }
        return oi;
    }

    private boolean openApp(String appName) {
        for (int i = 0; i < appNames.size(); i++) {
            String ff = appNames.get(i).toLowerCase();
            if (appName.equals(ff)) {
                try {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(appsList.get(i).packageName);
                    startActivity(intent);
                } catch (ActivityNotFoundException ass) {
                    Toast.makeText(getApplicationContext(), ass.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }
        return false;
    }

    public void loadContacts() {
        Cursor cursor = null;
        ContentResolver resolver = getContentResolver();
        //checkP(Manifest.permission.READ_CONTACTS,123);
        cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        assert cursor != null;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                contact a = new contact(null, null);

                String _id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                a.setName(name);

                int phone = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (phone > 0) {
                    checkP(Manifest.permission.READ_CONTACTS, 123);
                    Cursor cursor2 = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{_id}, null);


                    while (cursor2.moveToNext()) {
                        a.setPhone(cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }

                    cursor2.close();
                }

                myContacts.add(a);
            }
            cursor.close();
        }

    }

    private boolean callContact(String contactName) {
        for (contact aa : myContacts) {
            if (contactName.toLowerCase().equals(aa.getName().toLowerCase())) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + aa.getPhone()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                }
                startActivity(i);
                return true;
            }
        }
        return false;
    }

    public void speakText(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void searchAbout(String strings) {
        String k = "https://www.google.com/search?q=";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(k + strings));
        startActivity(i);
    }

    public void getFiles(File paths){
        File[] files = paths.listFiles();
        if (files.length >0) {
            for (File f : files) {

                if (f.isDirectory()) {
                    getFiles(f);
                }else {
                    //messages.add(new Messages(fromUser, f.getName()));
                    //adapter.notifyDataSetChanged();
                    switch (FilenameUtils.getExtension(f.getName())){

                        case "mp3" :
                        case "mpeg" :
                        case "mpeg4" :
                        case "aac" :
                        case "wav" :
                        case "ogg" :
                        case "midi" :
                        case "x-ms-wma" :
                        case "m4v" :
                        case "mp4" :
                        case "x-ms-wmv" :
                        case "x-msvideo" :
                        case "jpg" :
                        case "png" :
                        case "jpeg" :
                        case "gif" :
                        case "pdf" :
                        case "txt" :
                        case "rc" :
                        case "cfg" :
                        case "csv" :
                        case "conf" :
                        case "apk" :
                        case "xml" :
                        case "html" :
                        case "htm" :
                            myFiles.add(f);
                            break;
                        default:
                            break;
                    }
                }
            }

        }
    }

    private boolean openFile(String fileName) {
        for (File ff : myFiles){
            if (comparingFiles(ff,fileName)){
                Uri j = Uri.fromFile(ff);
                Intent i = new Intent(Intent.ACTION_VIEW);
                switch (FilenameUtils.getExtension(ff.getName())) {
                    case "mp3":
                    case "mpeg":
                    case "mpeg4":
                    case "aac":
                    case "wav":
                    case "ogg":
                    case "midi":
                    case "x-ms-wma":
                        i.setDataAndType(j, "audio");
                        startActivity(i);
                        break;
                    case "m4v":
                    case "mp4":
                    case "x-ms-wmv":
                    case "x-msvideo":
                        i.setDataAndType(j, "video");
                        startActivity(i);
                        break;
                    case "jpg":
                    case "png":
                    case "jpeg":
                    case "gif":
                        i.setDataAndType(j, "image/*");
                        startActivity(i);
                        break;
                    case "pdf":
                        i.setDataAndType(j, "application/pdf");
                        startActivity(i);
                        break;
                    case "txt":
                    case "rc":
                    case "cfg":
                    case "csv":
                    case "conf":
                        i.setDataAndType(j, "text/plain");
                        startActivity(i);
                        break;
                    case "apk":
                        i.setDataAndType(j, "application/vnd.android.package-archive");
                        startActivity(i);
                        break;
                    case "xml":
                        i.setDataAndType(j, "text/xml");
                        startActivity(i);
                        break;
                    case "html":
                    case "htm":
                        i.setDataAndType(j, "text/html");
                        startActivity(i);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "cant open file", Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        }
        return false;
    }

    public boolean comparingFiles(File a,String b){
        String e = FilenameUtils.getPrefix(a.getName());
        return a.getName().toLowerCase().equals(b.toLowerCase() + e);
    }

    public void wifi_on() {
        wifiManager.setWifiEnabled(true);
    }

    public void wifi_off() {
        wifiManager.setWifiEnabled(false);
    }

    public void bluetooth_on() {
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"device doesn't support bluetooth",Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(i);
        }
    }

    public void bluetooth_off() {
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"device doesnt support bluetooth",Toast.LENGTH_SHORT).show();
        }else {
            bluetoothAdapter.disable();
        }
    }

    public void controlBrightness(int p){
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, progress);
        WindowManager.LayoutParams l = window.getAttributes();
        l.screenBrightness = p / (float) 255;
        window.setAttributes(l);
    }
}
