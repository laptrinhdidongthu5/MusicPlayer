package nhomso1.project2.musicplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Đây là lớp củ thành phần giao dieenjnos nhận List<Audio> và trả về một adapter hoàn chỉnh
 * ViewwHolder giúp cho ứng dụng chỉ load các thành phần giao diện (có thể gọi là
 * xml một lần) lần sau sẽ k load lại giúp tối ưu tốc độ của list
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<Audio> list = Collections.emptyList();

    Context context;

    public RecyclerViewAdapter(List<Audio> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(list.get(position).getTitle());
        holder.artist.setText(list.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView title;
    TextView artist;
    ImageView play_pause;
    private onItemClickListener ClickHandler;

    ViewHolder(final View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        artist = (TextView) itemView.findViewById(R.id.artist);
        play_pause = (ImageView) itemView.findViewById(R.id.play_pause);
        play_pause.setOnClickListener(this);
    }
    @Override
    public void onClick(final View view) {
        if (view.getId() == play_pause.getId()) {
            if (ClickHandler != null) {
                ClickHandler.playOnClick(view,getAdapterPosition());
            }
        }
        else {
            if (ClickHandler != null) {
                ClickHandler.favoriteOnClick(view,getAdapterPosition());
            }
        }
    }


}
