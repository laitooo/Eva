package zxc.laitooo.eva.data;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Laitooo San on 30/06/2019.
 */

public class GetFile extends AsyncTask<Void,Void,File> {

    String[] AcceptedExtensions = new String[]{
            "mp3", "mpeg", "mpeg4", "aac", "wav", "ogg" , "midi", "x-ms-wma" , "m4v", "mp4" ,
            "x-ms-wmv", "x-msvideo", "jpg", "png", "jpeg", "gif", "pdf", "txt", "rc", "cfg",
            "csv", "conf", "apk", "xml", "html", "htm"
    };

    private String fileName;
    private File path;
    private File foundFile;
    private Context context;

    public GetFile(String fileName,Context context) {
        this.fileName = fileName;
        this.context = context;
        path = new File("mnt/sdcard/");
    }

    @Override
    protected File doInBackground(Void... params) {
        foundFile = getFile(path,fileName);
        return foundFile;
    }

    private File getFile(File paths,String name){
        File[] files = paths.listFiles();
        if (files.length >0) {
            for (File f : files) {

                if (f.isDirectory()) {
                    Log.e("FILE","DIRECTORY");
                    File tmp = getFile(f,name);
                    if (tmp != null)
                        return tmp;
                }else {
                    Log.e("FILE","FILE : " + f.getName().toLowerCase() + " searched :" + name.toLowerCase());
                    if (FilenameUtils.getBaseName(f.getName()).toLowerCase().equals(name.toLowerCase())){
                            Log.e("FILE","FOUND");
                            return f;
                    }
                }
            }
        }

        return null;
    }

    public boolean isFileAccepted(String name){
        for (String AcceptedExtension : AcceptedExtensions) {
            if (AcceptedExtension.equals(name))
                return true;
        }
        return false;
    }
}
