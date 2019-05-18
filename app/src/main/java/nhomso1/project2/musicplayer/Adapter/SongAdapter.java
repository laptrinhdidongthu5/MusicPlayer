package nhomso1.project2.musicplayer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nhomso1.project2.musicplayer.MainActivity;
import nhomso1.project2.musicplayer.MusicActivity;
import nhomso1.project2.musicplayer.Object.Song;
import nhomso1.project2.musicplayer.R;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private List<Song> mListSong ;
    private Context mContext ;
    private onNoteListener monNoteListener ;
    private static final String img_url = "img_url";
    private static final String descript = "descript";

    public SongAdapter(List<Song> listSong,onNoteListener onNoteListener) {
        this.mListSong = listSong ;
         this.monNoteListener = onNoteListener ;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content,parent,false);

        return new ViewHolder(itemView,monNoteListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        Song song = mListSong.get(i);
        holder.image.setImageResource(song.getImageSong());
        holder.nameSong.setText(song.getNameSong());
        holder.countSong.setText(song.getCountSong());



    }

    @Override
    public int getItemCount() {
        return mListSong.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        ImageView image;
        TextView nameSong ;
        TextView countSong ;
        onNoteListener onNoteListener ;



        public ViewHolder(final View itemView, onNoteListener onNoteListener) {
            super(itemView);
            image =  itemView.findViewById(R.id.imageView_img);
            nameSong = itemView.findViewById(R.id.textView_songName);
            countSong = itemView.findViewById(R.id.textView_count);

            this.onNoteListener = onNoteListener ;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick (getAdapterPosition());

        }
    }
    public interface onNoteListener{
        void onNoteClick(int position);
    }
}
