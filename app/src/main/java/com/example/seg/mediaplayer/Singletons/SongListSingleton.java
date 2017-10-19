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
 * Created by seg on 18.10.17.
 */

public class SongListSingleton {
    private static final SongListSingleton ourInstance = new SongListSingleton();
    private static List<Song> mSongList = new ArrayList<>();
    private static Boolean mIsPopulated = Boolean.FALSE;

    public static List<Song> getInstance() {
        return mSongList;
    }

    private SongListSingleton() {
    }

    public static void populate(Context context) {
        if( mIsPopulated ) {
            return;
        }
        mIsPopulated = Boolean.TRUE;

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int album = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int duration = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
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
    }
}
