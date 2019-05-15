package nhomso1.project2.musicplayer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nhomso1.project2.musicplayer.Object.Song;
import nhomso1.project2.musicplayer.R;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private List<Song> mListSong ;
    private Context mContext ;
    private static final String img_url = "img_url";
    private static final String descript = "descript";

    public SongAdapter(List<Song> listSong) {
        this.mListSong = listSong ;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        Song song = mListSong.get(i);
        holder.image.setImageResource(song.getImageSong());
        holder.nameSong.setText(song.getNameSong());
        holder.countSong.setText(song.getCountSong());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", String.valueOf(holder.nameSong.getText()));
//                Intent intent = new Intent(mContext,Music_activity_main.class);
//                mContext.startActivity(intent);

            }
        });

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
            image =  itemView.findViewById(R.id.imageView_img);
            nameSong = itemView.findViewById(R.id.textView_songName);
            countSong = itemView.findViewById(R.id.textView_count);



        }


    }
}
