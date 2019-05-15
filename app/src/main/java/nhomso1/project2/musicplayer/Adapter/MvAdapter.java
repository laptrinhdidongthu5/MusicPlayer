package nhomso1.project2.musicplayer.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nhomso1.project2.musicplayer.Object.Song;
import nhomso1.project2.musicplayer.R;

public class MvAdapter extends RecyclerView.Adapter<MvAdapter.ViewHolder> {
    private List<Song> mListSong ;

    public MvAdapter(List<Song> listSong) {
        this.mListSong = listSong ;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mv,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        Song song = mListSong.get(i);
        holder.image.setImageResource(song.getImageSong());
        holder.nameSong.setText(song.getNameSong());
        holder.countSong.setText(song.getCountSong());
    }

    @Override
    public int getItemCount() {
        return mListSong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView nameSong ;
        TextView countSong ;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imgMv);
            nameSong = itemView.findViewById(R.id.info_text);
            countSong = itemView.findViewById(R.id.count_text);

        }
    }
}
