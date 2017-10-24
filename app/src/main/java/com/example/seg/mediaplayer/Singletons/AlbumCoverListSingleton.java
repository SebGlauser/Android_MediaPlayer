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
 * This singleton is a hash map with the cover and the album id. The main purpose of this
 * class is to improve significantly the performance of the recyclerView
 *
 * @author  Sebastien Glauser
 * @date 18.10.2017
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
     * Return the bitmap associated to a album id
     * @param albumId The album id to get back the bitmap
     * @param context The context that call this function
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
            e.printStackTrace();
        }

        mBitmapMap.put(albumId, bm);
        return bm;
    }
}
