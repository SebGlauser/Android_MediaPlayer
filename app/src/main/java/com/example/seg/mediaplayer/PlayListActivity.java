package com.example.seg.mediaplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
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



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.wi30)));

        setStatusBarGradient(this);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        }

        mSongListAdapter = new SongListRecyclerViewAdapter(mSongList);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSongListAdapter);

        mLayoutManager = new LinearLayoutManager(this);

        mDividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                LinearLayout.VERTICAL
        );
        mDividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.sk_line_divider));
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        mRecyclerView.setLayoutManager(mLayoutManager);
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


        SongListSingleton.populate(this);
        mSongListAdapter.notifyDataSetChanged();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                }
                return;
            }
        }
    }
}
