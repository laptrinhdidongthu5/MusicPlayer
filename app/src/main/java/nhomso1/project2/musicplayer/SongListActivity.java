package nhomso1.project2.musicplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;



public class SongListActivity extends AppCompatActivity {
    public static final String Broadcast_PLAY_NEW_AUDIO = "nhomso1.project2.MusicPlayer.PlayNewAudio";
    public static final int MY_PERMISSION_REQUEST = 123;


    boolean serviceBound = false;


    private MediaPlayerService player;
    RecyclerView recyclerView;

    ArrayList<Audio> audioList;

    ImageView collapsingImageView;
    int imageIndex = 0;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        //Ánh xạ toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Ánh xạ image album phục vụ cho coordinate layout
        collapsingImageView = (ImageView) findViewById(R.id.collapsingImageView);
        loadCollapsingImage(imageIndex);


        //Nút floatAction để chuyển hình cho vui thôi
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //       playAudio("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg");
                //play the first audio in the ArrayList
                //       playAudio(2);
                if (imageIndex == 4) {
                    imageIndex = 0;
                    loadCollapsingImage(imageIndex);
                } else {
                    loadCollapsingImage(++imageIndex);
                }
            }
        });


        //Xin quyền trực tiếp, nếu không xin được thì sẽ chuyển đến
        //hàm onRequestPermissionsResult(int requestCode,...) để xin
        if (ContextCompat.checkSelfPermission(SongListActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SongListActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(SongListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(SongListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            initRecyclerView(); //Khởi tạo danh sách và bài hát lên RecycleView
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                //Xin quyền truy cập vào SDCARD ở đây
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(SongListActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SongListActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                        initRecyclerView();
                    }
                } else {
                    Toast.makeText(SongListActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; Thêm các hành động vào menu: Quyên sẽ thêm vào chỗ này
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item xử lý ở đây
        // miễn là có activity khai báo trong AndroidManifest.xml.
        int id = item.getItemId();

        //Không làm gì cả
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean("serviceStatus", serviceBound);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        serviceBound = savedInstanceState.getBoolean("serviceStatus");
    }

    /**
     * Binding tới to the AudioPlayer Service
     */
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
            //Đơn giản là nếu service bị disconnect thì serviceBound phải bằng false đảm bảo cho xử lý chơi nhạc
        }

        @Override
        public void onBindingDied(ComponentName name) {

        }

        @Override
        public void onNullBinding(ComponentName name) {

        }
    };

    /**
     * Kiểm tra toàn bộ EXTERNAL_CONTENT
     * Load audio lên đổ vào danh sách audioList
     */
    private void loadAudio() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            audioList = new ArrayList<>();
            do {

                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                // Save to audioList
                audioList.add(new Audio(data, title, album, artist));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    /**
     * Đây là hàm chính trong xử lý ở MainActivity
     * Các thành phần khai báo và hàm @OVerride ở trên đã tạo xong môi trường
     * Service và các nhân tố cần thiết: quyền truy cập
     * Giờ ta sẽ load nhạc và khởi tạo
     * Mọi xử lý liên quan đến chơi nhạc đều nằm ở Class MediaPlayerService chứ không nằm ở đây
     */
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
            recyclerView.addOnItemTouchListener(new CustomTouchListener(this,recyclerView , new onItemClickListener() {
                @Override
                public void playOnClick(View v,int index) {
                    //Các hiệu ứng và thành phần giao diện khác xử lý ở đây nhé
                    playAudio(index); //Khi bất kỳ item nào được nhấn thì sẽ chơi bài đó Easy!!!
                }
                @Override
                public void favoriteOnClick(View v,int index) {
                    addFavorite(index);
                }
            }));
        }
    }

    /**
     *  Kiểm tra service active và Intent đến MediaPlayerService.class
     *  Vì nó không có giao diện nên mình Intent tới
     *  Sẽ không có Activity nào được đổ lên trên Activi hiện tại
     * @param audioIndex
     */
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

    private void addFavorite(int audioIndex){
        FavoriteDAO db = new FavoriteDAO(this);
        Audio au = audioList.get(audioIndex);

        db.addAudio(au);
        Toast.makeText(getApplicationContext(),
                "Lưu vào yêu thích", Toast.LENGTH_LONG).show();
    }


    /**
     * Support collapsing lại để hiện thị danh sách nhạc tràn màn hình
     */
    private void loadCollapsingImage(int i) {
        TypedArray array = getResources().obtainTypedArray(R.array.images);
        collapsingImageView.setImageDrawable(array.getDrawable(i));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }
}
