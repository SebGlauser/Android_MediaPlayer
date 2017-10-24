package com.example.seg.mediaplayer.objects;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.seg.mediaplayer.Singletons.AlbumCoverListSingleton;


/**
 * This class is used to instantiate a song
 *
 * @author Sebastien Glauser
 * @date 18.10.2017
 */

public class Song {
    private String name;        ///< The title of the song
    private String author;      ///< The author of this title
    private Long id;            ///< The id of the song
    private Long album_id;      ///< Used to get back the cover
    private String path;        ///< The path of the song
    private Context context;    ///< The context used to send to the AlbumCoverListSingleton
    private Long duration;      ///< The duration in ms

    /**
     * Constructor
     *
     * @param context  The context used to send to the AlbumCoverListSingleton
     * @param id       The id of the song
     * @param album_id Used to get back the cover
     * @param name     The title of the song
     * @param author   The author of this title
     * @param duration The duration in ms
     * @param path     The path of the song
     */
    public Song(Context context, long id, long album_id, String name, String author, long duration, String path) {
        this.context = context;
        this.author = author;
        this.id = id;
        this.album_id = album_id;
        this.name = name;
        this.duration = duration;
        this.path = path;
    }

    /**
     * Get the title of the song
     *
     * @return name The title of the song
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     *
     * @param name The title of the song
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the author of the song
     *
     * @return The author of the song
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the author
     *
     * @param author The author of this title
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the id of the song
     *
     * @return name The id of the song
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of the song
     *
     * @param id The id of the song
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the cover in Bitmap
     *
     * @return The album cover note that if there is no cover this function will return NULL
     */
    public Bitmap getAlbumart() {
        return AlbumCoverListSingleton.getBitmap(context, album_id);
    }

    /**
     * Get the path of the song
     *
     * @return The path of the song
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the path
     *
     * @param path The path of the song
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Get the duration of the song
     *
     * @return The duration of the song
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Set the duration
     *
     * @param duration The duration in ms
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
