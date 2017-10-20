package com.example.seg.mediaplayer.objects;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.seg.mediaplayer.Singletons.AlbumCoverListSingleton;


/**
 * @author Sebastien Glauser
 * @version 1.0.0
 * @since 18.10.2017
 * @brief This class is used to instantiate a song
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
     * @return name The title of the song
     * @brief get the title of the song
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The title of the song
     * @brief Set the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The author of the song
     * @brief get the author of the song
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author The author of this title
     * @brief Set the
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return name The id of the song
     * @brief get the id of the song
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id of the song
     * @brief Set the id of the song
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return The album cover note that if there is no cover this function will return NULL
     * @brief Return the cover in Bitmap
     */
    public Bitmap getAlbumart() {
        return AlbumCoverListSingleton.getBitmap(context, album_id);
    }

    /**
     * @return The path of the song
     * @brief get the path of the song
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path The path of the song
     * @brief Set the path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return The duration of the song
     * @brief get the duration of the song
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * @param duration The duration in ms
     * @brief Set the duration
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
