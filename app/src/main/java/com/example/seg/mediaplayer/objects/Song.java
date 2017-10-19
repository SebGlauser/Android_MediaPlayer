package com.example.seg.mediaplayer.objects;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.example.seg.mediaplayer.R;
import com.example.seg.mediaplayer.Singletons.AlbumCoverListSingleton;

import java.io.FileDescriptor;

/**
 * Created by seg on 16.10.17.
 */

public class Song {
    private String name;
    private String author;
    private Long id;
    private Long album_id;
    private String path;
    private Context context;
    private Long duration;

    public Song(Context context,long id, long album_id, String name , String author, long duration , String path) {
        this.context = context;
        this.author = author;
        this.id = id;
        this.album_id = album_id;
        this.name = name;
        this.duration = duration;
        this.path = path;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bitmap getAlbumart()
    {
        return AlbumCoverListSingleton.getBitmap(context,album_id);
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
