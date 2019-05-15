package nhomso1.project2.musicplayer.Interface;

import android.view.View;


/**
 * Tạo interface mới để lấy sự kiện truyền vào index
 * Not now
 */
public interface onItemClickListener {

    public void playOnClick(View view, final int index);
    public void favoriteOnClick(View view, final int position);
}
