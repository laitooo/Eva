package zxc.laitooo.eva.records;

import android.media.AudioRecord;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileDescriptor;

/**
 * Created by Laitooo San on 01/07/2019.
 */

public class Record {

    File file;

    public Record(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
