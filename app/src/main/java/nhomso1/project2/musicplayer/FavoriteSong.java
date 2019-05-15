package nhomso1.project2.musicplayer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteSong extends AppCompatActivity {
    public static final String Broadcast_PLAY_NEW_AUDIO = "it1006.learn.musicwidget.PlayNewAudio";

    RecyclerView recyclerView;
    boolean serviceBound = false;
    private MediaPlayerService player;
    ArrayList<Audio> audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_song);
        initRecyclerView();
    }
    private void initRecyclerView() {

        //Đầu tiên load list Audio lên
        loadAudio();


        if (audioList.size() > 0) {
            //Ánh xạ recyclerView, icon play pause của bài hát
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

            //Khởi tạo Adapter để đổ vào recyclerView
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(audioList, getApplication());
            recyclerView.setAdapter(adapter);

            //recyclerView với LinearLayout
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            //Bắt sự kiện nhấp vào thành phần của danh sách
            recyclerView.addOnItemTouchListener(new CustomTouchListener(this, recyclerView, new onItemClickListener() {
                @Override
                public void playOnClick(View v, int index) {
                    //Các hiệu ứng và thành phần giao diện khác xử lý ở đây nhé
                    playAudio(index); //Khi bất kỳ item nào được nhấn thì sẽ chơi bài đó Easy!!!
                }

                @Override
                public void favoriteOnClick(View v, int index) {
                    deleteAudio(index);
                }
            }));
        }
    }
    private void loadAudio() {

        audioList = new ArrayList<>();
        FavoriteDAO db = new FavoriteDAO(this);
        for (Audio au:db.getAllFavorite()) {
            audioList.add(new Audio(au.getData(), au.getTitle(), au.getAlbum(), au.getArtist()));
        }
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Ràng buộc với LocalService, bỏ IBinder và lấy phiên bản LocalService
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;

            player = binder.getService();

            serviceBound = true;//Set biến service Bound đang được chạy
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
        //Đơn giản là nếu service bị disconnect thì serviceBound phải bằng false đảm bảo cho xử lý chơi nhạc
    };
    private void deleteAudio(int audioIndex) {
        FavoriteDAO db = new FavoriteDAO(this);
        Audio au = audioList.get(audioIndex);

        db.deleteFavorite(au);
        audioList.remove(audioIndex);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(audioList, getApplication());
        recyclerView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(),
                "Xóa khỏi yêu thích", Toast.LENGTH_LONG).show();


    }

    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Lưu trữ audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());

            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(this, MediaPlayerService.class);

            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        } else {
            //lưu một audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active and...
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }
}
