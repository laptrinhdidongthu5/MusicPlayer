package nhomso1.project2.musicplayer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import nhomso1.project2.musicplayer.Fragment.SongsFragment;
import nhomso1.project2.musicplayer.Object.Audio;
import nhomso1.project2.musicplayer.Service.MediaPlayerService;
import nhomso1.project2.musicplayer.Service.PlaySongService;
import nhomso1.project2.musicplayer.Storage.StorageUtil;

public class MusicActivity extends AppCompatActivity {

    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageView imgHinh;
    ImageButton btnPrev, btnPlay, btnStop, btnNext;

    private PlaySongService playSongService;

    int position = 0;
    Animation animation;

    ArrayList<Audio> audioList;

    boolean serviceBound = false;

    public MediaPlayerService player;
    public static final String Broadcast_PLAY_NEW_AUDIO = "nhomso1.project2.MusicPlayer.PlayNewAudio";
    public static final int MY_PERMISSION_REQUEST = 123;

    /**
     * Binding tới to the AudioPlayer Service
     */
    public ServiceConnection serviceConnection = new ServiceConnection() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        AnhXa();

        audioList = new StorageUtil(getApplicationContext()).loadAudio();

        position = getIntent().getIntExtra("index", 0);

        playAudio(position);

        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                playSongService.LayGiaTri(position);
                if (position > playSongService.arraySong.size() - 1) {
                    position = 0;
                    playSongService.LayGiaTri(position);
                }
                if (playSongService.Playing()) {
                    playSongService.Stop();
                }
                playSongService.KhoiTaoMediaPlayer();
                txtTitle.setText(playSongService.TenBaiHat());
                playSongService.Play();
                btnPlay.setImageResource(R.drawable.pause1);
                imgHinh.startAnimation(animation);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playSongService.ThayDoiThanhBar(skSong.getProgress());
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                playSongService.LayGiaTri(position);
                if (position < 0) {
                    position = playSongService.arraySong.size() - 1;
                    playSongService.LayGiaTri(position);
                }
                if (playSongService.Playing()) {
                    playSongService.Stop();
                }
                playSongService.KhoiTaoMediaPlayer();
                txtTitle.setText(playSongService.TenBaiHat());
                playSongService.Play();
                btnPlay.setImageResource(R.drawable.pause1);
                imgHinh.startAnimation(animation);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSongService.Stop();
                playSongService.Relaxse();
                btnPlay.setImageResource(R.drawable.play1);
                playSongService.KhoiTaoMediaPlayer();
                SetTimeTotal();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SONG1", playSongService.TenBaiHat());
                if (playSongService.Playing() == true) {
                    //Nếu đang hát--->Pause-->Play
                    playSongService.TamDung();
                    btnPlay.setImageResource(R.drawable.play1);
                    animation.cancel();
                } else {
                    // Đang ngừng ---> Phát ---> đổi hình pause
                    playSongService.Play();
                    btnPlay.setImageResource(R.drawable.pause1);
                    imgHinh.startAnimation(animation);
                }

                txtTitle.setText(playSongService.TenBaiHat());
                SetTimeTotal();
                UpdateTimeSong();
                playSongService.Play();


            }
        });
    }

    private void UpdateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(dinhDangGio.format(playSongService.LayMedia().getCurrentPosition()));
                skSong.setProgress(playSongService.LayMedia().getCurrentPosition());

                // Kiểm tra thời gian bài hát---> nếu kết thúc ----> next
                playSongService.LayMedia().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if (position > playSongService.arraySong.size() - 1) {
                            position = 0;
                        }
                        if (playSongService.Playing()) {
                            playSongService.Stop();
                        }
                        playSongService.LayGiaTri(position);
                        playSongService.Play();
                        btnPlay.setImageResource(R.drawable.pause1);
                        SetTimeTotal();
                    }
                });
                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    private void SetTimeTotal() {
        SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dinhDangGio.format(playSongService.MaxSize()));
        skSong.setMax(playSongService.MaxSize());
    }


    private void AnhXa() {
        txtTitle = findViewById(R.id.textviewTitle);
        txtTimeSong = findViewById(R.id.TextViewTimeSong);
        txtTimeTotal = findViewById(R.id.TextViewTimeTotal);
        skSong = findViewById(R.id.seekBarSong);
        btnPrev = findViewById(R.id.imageButtonPrev);
        btnNext = findViewById(R.id.imageButtonNext);
        btnPlay = findViewById(R.id.imageButtonPlay);
        btnStop = findViewById(R.id.imageButtonStop);
        imgHinh = findViewById(R.id.imageViewDisc);
    }

    protected void onStart() {
        super.onStart();

        // Tạo đối tượng Intent cho WeatherService.
//        Intent intent = new Intent(this, PlaySongService.class);

        // Gọi method bindService(..) để giàng buộc dịch vụ với giao diện.
//        this.bindService(intent, playSongServiceConnection, Context.BIND_AUTO_CREATE);
//        startService(intent);
    }


    /**
     * Kiểm tra service active và Intent đến MediaPlayerService.class
     * Vì nó không có giao diện nên mình Intent tới
     * Sẽ không có Activity nào được đổ lên trên Activi hiện tại
     *
     * @param audioIndex
     */
    public void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Lưu trữ audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());

            storage.storeAudio(audioList);

            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(MusicActivity.this, MediaPlayerService.class);

            startService(playerIntent);

            bindService(playerIntent, serviceConnection, this.BIND_AUTO_CREATE);

        } else {
            //lưu một audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active and...
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            player.sendBroadcast(broadcastIntent);
        }
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

