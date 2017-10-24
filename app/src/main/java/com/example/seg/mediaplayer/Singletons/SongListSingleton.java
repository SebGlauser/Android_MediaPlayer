package com.example.seg.mediaplayer.Singletons;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.seg.mediaplayer.objects.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * This singleton contain the list of song
 *
 * @author Sebastien Glauser
 * @date 20.10.2017
 */

public class SongListSingleton {
    private static List<Song> mSongList = new ArrayList<>();
    private static Boolean mIsPopulated = Boolean.FALSE;


    /**
     * This function is used to return the list of song
     *
     * @return The list of song
     */
    public static List<Song> getInstance() {
        return mSongList;
    }

    private SongListSingleton() {
    }

    public static void populate(Context context) {
        if (mIsPopulated) {
            return;
        }
        mIsPopulated = Boolean.TRUE;

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int album = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int duration = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int data = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            //add songs to list
            do {

                mSongList.add(
                        new Song(context,
                                musicCursor.getLong(idColumn),
                                musicCursor.getLong(album),
                                musicCursor.getString(titleColumn),
                                musicCursor.getString(artistColumn),
                                musicCursor.getLong(duration),
                                musicCursor.getString(data)));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
    }
}
