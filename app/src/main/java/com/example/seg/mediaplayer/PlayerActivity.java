package com.example.seg.mediaplayer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.seg.mediaplayer.Singletons.MediaPlayerSingleton;
import com.example.seg.mediaplayer.Singletons.SongListSingleton;
import com.example.seg.mediaplayer.objects.Song;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * This activity can play, change, pause a song.
 *
 * @author Sebastien Glauser
 * @date  20.10.2017
 */



public class PlayerActivity extends AppCompatActivity {

    // Intent constant 
    public static String SONG_INDEX = "SONG_INDEX";

    // Song objects 
    private List<Song> mSongList = SongListSingleton.getInstance();
    private MediaPlayer mMediaPlayer = MediaPlayerSingleton.getInstance();
    private int mCurrentSongIndex;
    private boolean isPaused;

    // Lock to the seek bar
    private boolean isTracking = false;

    // Layout items 
    private TextView title, artist, duration, timeProgression;
    private ImageView img;
    private ImageButton previous_btn, play_pause_btn, next_btn;
    private SeekBar seekProgress;
    public LinearLayout rootView;

    private SeekBarAsyncTaskRunner mSeekBarAsyncUpdater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Get back item from layout
        title = (TextView) findViewById(R.id.song_title);
        artist = (TextView) findViewById(R.id.song_artist);
        duration = (TextView) findViewById(R.id.duration);
        timeProgression = (TextView) findViewById(R.id.progression);
        img = (ImageView) findViewById(R.id.cover);
        seekProgress = (SeekBar) findViewById(R.id.bar_progression);
        previous_btn = (ImageButton) findViewById(R.id.previous_btn);
        play_pause_btn = (ImageButton) findViewById(R.id.play_pause_btn);
        next_btn = (ImageButton) findViewById(R.id.next_btn);
        rootView = (LinearLayout) findViewById(R.id.root_layout);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.wi30)));
        setStatusBarGradient(this);


        // Get back data from the intent
        Intent data = getIntent();
        mCurrentSongIndex = data.getExtras().getInt("SONG_INDEX");

        // Start the song
        startSong();

        // When a song is finished start the next one
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mCurrentSongIndex < mSongList.size() - 1) {
                    mCurrentSongIndex++;
                    startSong();
                }
            }
        });

        // On click on the listener
        seekProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isTracking)
                    mMediaPlayer.seekTo(progress);
            }

            // Without this lock the player bug every time we set the position
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTracking = false;
            }
        });

        // On next btn pressed, start the next song
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSongIndex < mSongList.size() - 1) {
                    mCurrentSongIndex++;
                    startSong();
                }
            }
        });

        // On previous btn pressed, start the previous song
        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSongIndex > 1) {
                    mCurrentSongIndex--;
                    startSong();
                }

            }
        });

        // On pause play btn pressed, play or paused the current song
        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    continueSong();
                } else {
                    pauseSong();
                }
            }
        });


        mSeekBarAsyncUpdater = new SeekBarAsyncTaskRunner();
        mSeekBarAsyncUpdater.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSeekBarAsyncUpdater.cancel(true);
    }

    /**
     * This function start the song pointed by the mCurrentSongIndex and update information
     */
    public void startSong() {

        // get back the current song
        Song song = getSongFromIndex();

        if (song == null)
            return;
        SimpleDateFormat sdf;
        if (song.getDuration() < 3600000) {
            sdf = new SimpleDateFormat("mm:ss");
        } else {
            sdf = new SimpleDateFormat("hh:mm:ss");
        }

        duration.setText(sdf.format(new Date(song.getDuration())));
        timeProgression.setText(sdf.format(new Date(0)));

        // Set information in the view
        title.setText(song.getName());
        artist.setText(song.getAuthor());

        if (song.getAlbumart() != null)
            img.setImageBitmap(song.getAlbumart());
        else
            img.setImageResource(R.drawable.ic_music_note_black_24dp);

        // Start the song
        try {
            Uri uri = Uri.parse("file:///" + song.getPath());
            if(mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(getApplicationContext(), uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Change the state and the button
        isPaused = false;
        play_pause_btn.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    private Song getSongFromIndex() {
        Song song = null;
        try {
            song = mSongList.get(mCurrentSongIndex);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return song;
    }

    /**
     * This function pause the song and adapt the view
     */
    public void pauseSong() {
        // Pause the button and start the player
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPaused = true;
            play_pause_btn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    /**
     * Restart the song
     */
    public void continueSong() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            isPaused = false;
            play_pause_btn.setImageResource(R.drawable.ic_pause_black_24dp);
        }
    }


    /**
     * Change the background by an animated gradient
     *
     * @param activity the activity to get the window
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarGradient(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.wi30)));
            Window window = activity.getWindow();
            rootView.setBackgroundColor(0x00000000);
            AnimationDrawable background = (AnimationDrawable) activity.getResources().getDrawable(R.drawable.annimation_list);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.wi30));
            window.setNavigationBarColor(activity.getResources().getColor(R.color.wi30));
            window.setBackgroundDrawable(background);

            background.setEnterFadeDuration(2000);
            background.setExitFadeDuration(4000);
            background.start();
        }
    }

    /**
     * Inner class to update the seek bar while the music is playing
     */
    private class SeekBarAsyncTaskRunner extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // The isCancelled is needed if you want to kill the thread
            while (mMediaPlayer != null && !isCancelled()) {
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    publishProgress(mMediaPlayer.getCurrentPosition());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                // Update the view
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

                if (mMediaPlayer.getDuration() < 3600000) {
                    sdf = new SimpleDateFormat("mm:ss");
                }

                int mediaPos_new = mMediaPlayer.getCurrentPosition();
                int mediaMax_new = mMediaPlayer.getDuration();
                seekProgress.setMax(mediaMax_new);
                seekProgress.setProgress(mediaPos_new);
                timeProgression.setText(sdf.format(new Date(mediaPos_new)));
            }
        }

    }
}

