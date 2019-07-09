package zxc.laitooo.eva;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import zxc.laitooo.eva.data.App;
import zxc.laitooo.eva.data.Contact;
import zxc.laitooo.eva.data.GetFile;

/**
 * Created by Laitooo San on 30/06/2019.
 */

public class Executor {

    private Context context;
    private PhoneSettings phoneSettings;
    private TextToSpeech tts;
    private Locale locale;
    private int SECONDS_DELAYED = 500;


    public Executor(Context context) {
        this.context = context;
        phoneSettings = new PhoneSettings(context);
        locale = Locale.ENGLISH;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    tts.setLanguage(locale  );
                }
            }
        });


    }

    public void execute(String order){
        order = order.toLowerCase();
        Log.e("EXECUTER","execute()");
        chat(order);
    }

    public void chat(String order){
        Log.e("EXECUTER","chat()");

        switch (order){
            case "hi eva":
            case "hi":
            case "hi there":
            case "hello eva":
            case "hello":
            case "hello there":
                speakText("hi user, how you doing");
                break;
            case "hi eva how you doing":
            case "hello eva how you doing":
            case "how are you doing":
            case "how you doing":
            case "how are you":
            case "how are ya":
                speakText("Iam very well , what about ya");
                break;
            case "i love you":
            case "i love you eva":
                speakText("I love you too, but as a friend");
                break;
            case "can you be my girlfriend":
                speakText("lol, i am just virtual");
                break;
            default:
                executeSentence(order);
                break;
        }
    }

    public void executeSentence(String order){
        Log.e("EXECUTER","executeSentence()");
        switch (order) {
            case "turn on wifi":
            case "turn wifi on":
            case "turn on wi-fi":
            case "turn wi-fi on":
            case "open wifi":
            case "open wi-fi":
                phoneSettings.wifi_on();
                break;
            case "turn off wi-fi":
            case "turn wi-fi off":
            case "turn off wifi":
            case "turn wifi off":
            case "close wifi":
            case "close wi-fi":
                phoneSettings.wifi_off();
                break;
            case "turn bluetooth on":
            case "turn on bluetooth":
            case "open bluetooth":
                phoneSettings.bluetooth_on();
                break;
            case "turn bluetooth off":
            case "turn off bluetooth":
            case "close bluetooth":
                phoneSettings.bluetooth_off();
                break;
            case "increase brightness":
            case "high brightness":
            case "maximum brightness":
                phoneSettings.controlBrightness(255);
                break;
            case "medium brightness":
            case "normal brightness":
                phoneSettings.controlBrightness(120);
                break;
            case "decrease brightness":
            case "low brightness":
            case "minimum brightness":
                phoneSettings.controlBrightness(20);
                break;
            case "record note":
            case "record notes":
            case "record a note":
            case "write note":
            case "write notes":
            case "write a note":
            case "send message":
            case "send messages":
            case "send a message":
            case "write message":
            case "write messages":
            case "write a message":
                context.startActivity(new Intent(context,SendingActivity.class));
                break;
            case "record lectures":
            case "record this lecture":
            case "record the lecture":
            case "record lecture":
                context.startActivity(new Intent(context,RecordingActivity.class));
                break;
            case "clear messages":
            case "clear screen":
                MainActivity.clearMessages();
                break;

            default:
                executeWord(order);
                break;
        }
    }

    public void executeWord(String order){
        String[] strings = order.split(" ");
        String word = strings[0];
        Log.e("EXECUTER","executeWord()");

        switch (word) {
            case "open":
                String appName = order.substring(5);
                if (!openApp(appName))
                    openFile(appName);
                //Toast.makeText(c,"cant find " + appName,Toast.LENGTH_LONG).show();
                break;
            case "call":
                String contactName = order.substring(5);
                if (!callContact(contactName))
                    speakText("Couldn't find " + contactName + "'s contact info");
                break;
            case "play":
                String filename = order.substring(5);
                if(!openFile(filename))
                    //Toast.makeText(c,"cant find " + filename,Toast.LENGTH_LONG).show();
                    break;
            case "how":
            case "what":
            case "why":
            case "who":
            case "whom":
            case "when":
            case "which":
                searchAbout(order);
                break;
            case "say":
                speakText(order.substring(4));
                break;

            case "back":
                context.startActivity(new Intent(context,BackgroundActivity.class));
                break;

            default:
                speakText("Couldn't understand what you said");
                break;
        }
    }

    private boolean openApp(String appName) {
        for (App app : MainActivity.myApps) {
            if (appName.equals(app.getName().toLowerCase())) {
                try {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ass) {
                    speakText(ass.getMessage());
                }
                return true;
            }
        }
        return false;
    }

    private boolean callContact(String contactName) {
        for (Contact aa : MainActivity.myContacts) {
            if (contactName.toLowerCase().equals(aa.getName().toLowerCase())) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + aa.getPhone()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                }
                context.startActivity(i);
                return true;
            }
        }
        return false;
    }

    public void speakText(final String text){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                MainActivity.addMessage(false,text);
            }
        },SECONDS_DELAYED);
    }

    private void searchAbout(String strings) {
        String k = "https://www.google.com/search?q=";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(k + strings));
        context.startActivity(i);
    }

    private boolean openFile(String fileName) {
        File file = null;
        GetFile getFile = new GetFile(fileName,context);

        try {
            file = getFile.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            speakText("error finding file ");
        }

        if (file == null){
            speakText("can't find " + fileName);
            return false;
        }

        Uri j = Uri.fromFile(file);
        Intent i = new Intent(Intent.ACTION_VIEW);
        switch (FilenameUtils.getExtension(file.getName())) {
            case "mp3":
            case "mpeg":
            case "mpeg4":
            case "aac":
            case "wav":
            case "ogg":
            case "midi":
            case "x-ms-wma":
                i.setDataAndType(j, "audio");
                context.startActivity(i);
                break;
            case "m4v":
            case "mp4":
            case "x-ms-wmv":
            case "x-msvideo":
                i.setDataAndType(j, "video");
                context.startActivity(i);
                break;
            case "jpg":
            case "png":
            case "jpeg":
            case "gif":
                i.setDataAndType(j, "image/*");
                context.startActivity(i);
                break;
            case "pdf":
                i.setDataAndType(j, "application/pdf");
                context.startActivity(i);
                break;
            case "txt":
            case "rc":
            case "cfg":
            case "csv":
            case "conf":
                i.setDataAndType(j, "text/plain");
                context.startActivity(i);
                break;
            case "apk":
                i.setDataAndType(j, "application/vnd.android.package-archive");
                context.startActivity(i);
                break;
            case "xml":
                i.setDataAndType(j, "text/xml");
                context.startActivity(i);
                break;
            case "html":
            case "htm":
                i.setDataAndType(j, "text/html");
                context.startActivity(i);
                break;
            default:
                speakText("Can't open file\n bad extension");
                break;
        }
        return true;
    }


}
