package com.example.seg.mediaplayer.Singletons;

import android.media.MediaPlayer;

/**
 * This singleton contain the MediaPlayer
 *
 * @author Sebastien Glauser
 * @date 18.10.2017
 */

public class MediaPlayerSingleton {
    private static final MediaPlayer ourInstance = new MediaPlayer();

    public static MediaPlayer getInstance() {
        return ourInstance;
    }

    private MediaPlayerSingleton() {
    }
}
