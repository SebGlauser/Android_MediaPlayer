package com.example.seg.mediaplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seg.mediaplayer.R;
import com.example.seg.mediaplayer.Singletons.SongListSingleton;
import com.example.seg.mediaplayer.objects.Song;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Sebastien Glauser
 * @version 1.0.0
 * @since 18.10.2017
 * @brief The recycler adapter of a list of song
 */

public class SongListRecyclerViewAdapter extends RecyclerView.Adapter<SongListRecyclerViewAdapter.ViewHolder>{
    private List<Song> songsList;

    public SongListRecyclerViewAdapter(List<Song> songsList) {
        this.songsList = songsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get back the song on fx of the position
        Song song = songsList.get(position);

        // Create a format for the duration
        // @// TODO: 20.10.17 Manage more than 1h durations
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

        // Fill the layout
        holder.title.setText(song.getName());
        holder.artist.setText(song.getAuthor());
        if( song.getAlbumart() != null )
            holder.img.setImageBitmap(song.getAlbumart());
        else
            holder.img.setImageResource(R.drawable.ic_music_note_black_24dp);

        holder.duration.setText(sdf.format(new Date(song.getDuration())));
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, artist, duration;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            // Get back items on the layout
            title = itemView.findViewById(R.id.rv_song_title);
            artist = itemView.findViewById(R.id.rv_song_artist);
            duration = itemView.findViewById(R.id.rv_duration);
            img = itemView.findViewById(R.id.rv_cover);
        }
    }
}
