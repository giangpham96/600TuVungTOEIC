package one.marshangeriksen.loloaldrin.myapplication;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class Utils {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    private static final String APP_DIRECTORY = "TOEIC_600_essential_words";

    public static File getOutputFile(Context context, int idFolder, String fileName) {
        File mediaStorageDir;
        if (isExternalStorageWritable()) {
            // External storage location
            mediaStorageDir = new File(
                    Environment.getExternalStorageDirectory(),
                    APP_DIRECTORY);

        } else {
            // Internal storage location
            mediaStorageDir = new File(context.getFilesDir(),
                    APP_DIRECTORY);
        }
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();
        }
        File file;
        if (isExternalStorageWritable()) {
            // External storage location
            file = new File(
                    Environment.getExternalStorageDirectory() + "/" + APP_DIRECTORY,
                    String.valueOf(idFolder));

        } else {
            // Internal storage location
            file = new File(context.getFilesDir() + "/" + APP_DIRECTORY,
                    String.valueOf(idFolder));
        }
        // Create the storage directory if it does not exist
        if (!file.exists()) {
            file.mkdir();
        }
        // Create a media file name
        return new File(file.getPath() + File.separator + fileName + ".ogg");
    }

    public static File getWordFolder(Context context, int idFolder) {
        File file;
        if (isExternalStorageWritable()) {
            // External storage location
            file = new File(
                    Environment.getExternalStorageDirectory() + "/" + APP_DIRECTORY,
                    String.valueOf(idFolder));

        } else {
            // Internal storage location
            file = new File(context.getFilesDir() + "/" + APP_DIRECTORY,
                    String.valueOf(idFolder));
        }
        return file;
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static List<File> listFilesInFolder(File folder) {
        List<File> files = new ArrayList<>();
        if (folder.exists()) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    files.addAll(listFilesInFolder(fileEntry));
                } else {
                    files.add(fileEntry);
                }
            }
        }
        Log.e("file size", files.size() + "");
        return files;
    }
}
