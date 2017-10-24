package com.example.seg.mediaplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.seg.mediaplayer.Listener.RecyclerSongClickListener;
import com.example.seg.mediaplayer.Singletons.SongListSingleton;
import com.example.seg.mediaplayer.adapter.SongListRecyclerViewAdapter;
import com.example.seg.mediaplayer.objects.Song;


import java.util.List;

public class PlayListActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private RecyclerView  mRecyclerView;
    private LayoutManager mLayoutManager;
    private SongListRecyclerViewAdapter mSongListAdapter;
    private DividerItemDecoration mDividerItemDecoration;
    private List<Song> mSongList = SongListSingleton.getInstance();
    public LinearLayout rootView;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        // get the view
        rootView = (LinearLayout) findViewById(R.id.root_layout2);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // Design
        // Note that root view should be got back
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.wi30)));
        setStatusBarGradient(this);


        // Instantiate the adapter
        mSongListAdapter = new SongListRecyclerViewAdapter(mSongList);
        mLayoutManager = new LinearLayoutManager(this);

        // Create a divider
        mDividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                LinearLayout.VERTICAL
        );
        mDividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.sk_line_divider));

        // build the recycler
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSongListAdapter);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set when a item is selected
        mRecyclerView.addOnItemTouchListener(
                new RecyclerSongClickListener(this, new RecyclerSongClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent playerActivityIntent = new Intent(getApplicationContext(), PlayerActivity.class);
                        playerActivityIntent.putExtra(PlayerActivity.SONG_INDEX, position);
                        startActivity( playerActivityIntent);
                    }
                })
        );

        // Permission
        // Note that this function need the recycler view to be instantiate
        requestInternalPermission();
    }


    /**
     * This function will be we call when the user granted access to the internal storage
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestInternalPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            onStoragePermissionGranted();
        }
    }

    /**
     * This function should be called only when the storage permission is granted
     */
    private void onStoragePermissionGranted() {
        // Get all song
        SongListSingleton.populate(this);
        mSongListAdapter.notifyDataSetChanged();
    }

    /**
     * This function will be we call when the user granted access to the internal storage
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {

            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onStoragePermissionGranted();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

}
