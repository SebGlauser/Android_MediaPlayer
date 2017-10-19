package com.example.seg.mediaplayer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.seg.mediaplayer.Singletons.MediaPlayerSingleton;
import com.example.seg.mediaplayer.Singletons.SongListSingleton;
import com.example.seg.mediaplayer.objects.Song;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class PlayerActivity extends AppCompatActivity {

    public static String SONG_INDEX = "SONG_INDEX";

    private List<Song> mSongList = SongListSingleton.getInstance();
    private MediaPlayer mMediaPlayer = MediaPlayerSingleton.getInstance();

    private int mCurrentSongIndex;
    private boolean isPaused;

    public TextView title, artist, duration, timeProgression;
    public ImageView img;
    public ImageButton previous_btn,play_pause_btn,next_btn;
    public SeekBar seekProgress;
    private boolean isTrakickng = false;

    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.wi30)));
        setStatusBarGradient(this);


        title = (TextView) findViewById(R.id.song_title);
        artist = (TextView) findViewById(R.id.song_artist);
        duration = (TextView) findViewById(R.id.duration);
        timeProgression = (TextView) findViewById(R.id.progression);
        img = (ImageView) findViewById(R.id.cover);
        seekProgress = (SeekBar) findViewById(R.id.bar_progression);

        previous_btn = (ImageButton) findViewById(R.id.previous_btn);
        play_pause_btn = (ImageButton) findViewById(R.id.play_pause_btn);
        next_btn = (ImageButton) findViewById(R.id.next_btn);



        // Get back data from the intent
        Intent data = getIntent();
        mCurrentSongIndex = data.getExtras().getInt("SONG_INDEX");

        // Start the song
        startSong();

        // When a song is finished start the next one
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if ( mCurrentSongIndex < mSongList.size() - 1 ){
                    mCurrentSongIndex++;
                    startSong();
                }
            }
        });

        // On click on the listener
        seekProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(isTrakickng)
                    mMediaPlayer.seekTo(progress);
            }

            // Without this lock the player bug every time we set the position
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrakickng = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrakickng = false;
            }
        });

        // On next btn pressed, start the next song
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mCurrentSongIndex < mSongList.size() - 1 ){
                    mCurrentSongIndex++;
                    startSong();
                }
            }
        });

        // On previous btn pressed, start the previous song
        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSongIndex > 1){
                    mCurrentSongIndex--;
                    startSong();
                }

            }
        });

        // On pause play btn pressed, play or paused the current song
        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused)
                {
                    continueSong();
                }
                else
                {
                    pauseSong();
                }
            }
        });


        // Task to update the seeker
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    if(mMediaPlayer.isPlaying()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                        int mediaPos_new = mMediaPlayer.getCurrentPosition();
                        int mediaMax_new = mMediaPlayer.getDuration();
                        // Toast.makeText(getApplicationContext(), ""+ String.valueOf(mediaPos_new) +" "+ String.valueOf(mediaMax_new), Toast.LENGTH_SHORT).show();
                        seekProgress.setMax(mediaMax_new);
                        seekProgress.setProgress(mediaPos_new);
                        timeProgression.setText(sdf.format(new Date(mediaPos_new)));
                    }
                } catch (Exception e) {
                    //e.printStackTrace(); // Or better, use next line if you have configured a logger:
                }
            }
        }, 0, 200, TimeUnit.MILLISECONDS);



    }

    public void startSong()
    {
        Song song = mSongList.get(mCurrentSongIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        title.setText(song.getName());
        artist.setText(song.getAuthor());
        if( song.getAlbumart() != null )
            img.setImageBitmap(song.getAlbumart());
        else
            img.setImageResource(R.drawable.ic_music_note_black_24dp);

        duration.setText(sdf.format(new Date(song.getDuration())));
        timeProgression.setText(sdf.format(new Date(0)));


        song.getDuration();
        try {
            Uri uri= Uri.parse("file:///"+song.getPath());
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(getApplicationContext(),uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isPaused = false;


        play_pause_btn.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    public void pauseSong(){
        mMediaPlayer.pause();
        isPaused = true;
        play_pause_btn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    public void continueSong(){
        mMediaPlayer.start();
        isPaused = false;
        play_pause_btn.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradient(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            AnimationDrawable background = (AnimationDrawable)  activity.getResources().getDrawable(R.drawable.annimation_list);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.wi30));
            window.setNavigationBarColor(activity.getResources().getColor(R.color.wi30));
            window.setBackgroundDrawable(background);

            background.setEnterFadeDuration(2000);
            background.setExitFadeDuration(4000);
            background.start();
        }
    }
}
