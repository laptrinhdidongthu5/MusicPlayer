package nhomso1.project2.musicplayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private List<PlayList> PlayList ;

    public PlayListAdapter(List<PlayList> listPlayList) {
        this.PlayList = listPlayList ;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist,parent,false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        PlayList playlist = PlayList.get(i);
        holder.image.setImageResource(playlist.getImageSong());
        holder.nameList.setText(playlist.getNamePlayList());
        holder.singer.setText(playlist.getSinger());
    }

    @Override
    public int getItemCount() {
        return PlayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView nameList ;
        TextView singer ;
        public ViewHolder(View itemView) {
           super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imgPlay);
            nameList = itemView.findViewById(R.id.nameList);
            singer = itemView.findViewById(R.id.textView_sing);

        }
    }
}
