package com.example.seg.mediaplayer.Singletons;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import com.example.seg.mediaplayer.R;

import java.io.FileDescriptor;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @Author Sebastien Glauser
 * @date 18.10.2017
 * @brief This singleton is a hashmap with the cover and the album id. The main purpose of this
 * class is to improve significantly the performance of the recyclerView
 */

public class AlbumCoverListSingleton {
    private static final AlbumCoverListSingleton ourInstance = new AlbumCoverListSingleton();

    static AlbumCoverListSingleton getInstance() {
        return ourInstance;
    }

    static HashMap<Long, Bitmap> mBitmapMap = new HashMap<>();

    private AlbumCoverListSingleton() {
    }

    /**
     * @brief T
     */

    public static Bitmap getBitmap(Context context, Long albumId) {
        Iterator myVeryOwnIterator = mBitmapMap.keySet().iterator();
        while (myVeryOwnIterator.hasNext()) {
            Long key = (Long) myVeryOwnIterator.next();
            if (key == albumId)
                return mBitmapMap.get(key);
        }
        Bitmap bm = null;
        try {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);

            ParcelFileDescriptor pfd = context.getContentResolver()
                    .openFileDescriptor(uri, "r");

            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);
            } else {
                return null;
            }

        } catch (Exception e) {
        }

        mBitmapMap.put(albumId, bm);
        return bm;
    }
}
