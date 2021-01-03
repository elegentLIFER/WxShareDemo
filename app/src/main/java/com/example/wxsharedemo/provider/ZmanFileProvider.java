package com.example.wxsharedemo.provider;

import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.example.wxsharedemo.Constants;

import java.io.File;
import java.io.FileNotFoundException;

public class ZmanFileProvider extends FileProvider {
    public static final String AUTHORITY = "com.example.wxsharedemo.fileProvider";
    private static final String[] COLUMNS = {
            OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE, "_data"};

//    @Override
//    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
//                        @Nullable String[] selectionArgs,
//                        @Nullable String sortOrder) {
//        File file = getFileForUri(uri);
//
//        if (projection == null) {
//            projection = COLUMNS;
//        }
//        String[] cols = new String[projection.length];
//        Object[] values = new Object[projection.length];
//        int i = 0;
//        for (String col : projection) {
//            if (OpenableColumns.DISPLAY_NAME.equals(col)) {
//                cols[i] = OpenableColumns.DISPLAY_NAME;
//                values[i++] = file.getName();
//            } else if (OpenableColumns.SIZE.equals(col)) {
//                cols[i] = OpenableColumns.SIZE;
//                values[i++] = file.length();
//            } else if (MediaStore.MediaColumns.DATA.equals(col)) {
//                cols[i] = MediaStore.MediaColumns.DATA;
//                values[i++] = file.getAbsolutePath();
//            }
//        }
//
//        cols = copyOf(cols, i);
//        values = copyOf(values, i);
//
//        final MatrixCursor cursor = new MatrixCursor(cols, 1);
//        cursor.addRow(values);
//
//        return cursor;
//    }

    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        return super.openFile(uri, mode);
    }

    private static String[] copyOf(String[] original, int newLength) {
        final String[] result = new String[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }

    private static Object[] copyOf(Object[] original, int newLength) {
        final Object[] result = new Object[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }

    public File getFileForUri(Uri uri) {
        String path = uri.getEncodedPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        final int splitIndex = path.indexOf('/', 1);
        path = Uri.decode(path.substring(splitIndex + 1));

        if (uri.toString().contains(AUTHORITY + File.separator + "cache")) {
            File dir = new File(Constants.getDataPath(getContext()));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(dir, path);
        }
        return new File(Environment.getExternalStorageDirectory(), path);
    }
}
