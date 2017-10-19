package com.example.seg.mediaplayer.Singletons;

import android.media.MediaPlayer;

/**
 * Created by seg on 16.10.17.
 */

public class MediaPlayerSingleton {
    private static final MediaPlayer ourInstance = new MediaPlayer();

    public static MediaPlayer getInstance() {
        return ourInstance;
    }

    private MediaPlayerSingleton() {
    }
}
