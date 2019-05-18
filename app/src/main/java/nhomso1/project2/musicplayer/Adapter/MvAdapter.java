package nhomso1.project2.musicplayer.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nhomso1.project2.musicplayer.Object.MV;
import nhomso1.project2.musicplayer.Object.Song;
import nhomso1.project2.musicplayer.R;

public class MvAdapter extends RecyclerView.Adapter<MvAdapter.ViewHolder> {
    private List<MV> mListMV ;
    private OnMVListener mOnMVListener ;


    public MvAdapter(List<MV> listSong,OnMVListener mOnMVListener) {
        this.mListMV = listSong ;
        this.mOnMVListener = mOnMVListener ;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mv,parent,false);
        return new ViewHolder(itemView,mOnMVListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        MV song = mListMV.get(i);
        holder.image.setImageResource(song.getImageSong());
        holder.nameSong.setText(song.getNameSong());
        holder.countSong.setText(song.getCountSong());
    }

    @Override
    public int getItemCount() {
        return mListMV.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        ImageView image;
        TextView nameSong ;
        TextView countSong ;
        OnMVListener onMVListener ;
        public ViewHolder(View itemView, OnMVListener onMVListener) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imgMv);
            nameSong = itemView.findViewById(R.id.info_text);
            countSong = itemView.findViewById(R.id.count_text);
            this.onMVListener = onMVListener ;

           itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMVListener.onMVClick(getAdapterPosition());

        }
    }
    public interface OnMVListener{
        void onMVClick(int position);
    }
}
