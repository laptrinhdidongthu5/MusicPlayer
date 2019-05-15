package nhomso1.project2.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import nhomso1.project2.musicplayer.Service.PlaySongService;

public class MusicActivity extends AppCompatActivity {

    TextView txtTitle,txtTimeSong,txtTimeTotal;
    SeekBar skSong;
    ImageView imgHinh;
    ImageButton btnPrev,btnPlay,btnStop,btnNext;

    private PlaySongService playSongService;


    int position=0;
    Animation   animation;

    ServiceConnection playSongServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            PlaySongService.PlaySongBinder binDer=(PlaySongService.PlaySongBinder) service;
            playSongService=binDer.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        AnhXa();
        Log.d("PLAY",btnPlay.toString());


        animation = AnimationUtils.loadAnimation(this,R.anim.disc_rotate);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                playSongService.LayGiaTri(position);
                if(position>playSongService.arraySong.size()-1)
                {
                    position=0;
                    playSongService.LayGiaTri(position);
                }
                if(playSongService.Playing())
                {
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
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                playSongService.ThayDoiThanhBar(skSong.getProgress());
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                playSongService.LayGiaTri(position);
                if(position<0)
                {
                    position=playSongService.arraySong.size()-1;
                    playSongService.LayGiaTri(position);
                }
                if(playSongService.Playing())
                {
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
            public void onClick(View v)
            {
                Log.d("SONG1", playSongService.TenBaiHat());
                if(playSongService.Playing()==true)
                {
                    //Nếu đang hát--->Pause-->Play
                    playSongService.TamDung();
                    btnPlay.setImageResource(R.drawable.play1);
                    animation.cancel();
                }
                else
                {
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

    private void UpdateTimeSong()
    {
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangGio= new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(dinhDangGio.format( playSongService.LayMedia().getCurrentPosition()));
                skSong.setProgress( playSongService.LayMedia().getCurrentPosition());

                // Kiểm tra thời gian bài hát---> nếu kết thúc ----> next
                playSongService.LayMedia().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        position++;
                        if(position>playSongService.arraySong.size()-1)
                        {
                            position=0;
                        }
                        if(playSongService.Playing())
                        {
                            playSongService.Stop();
                        }
                        playSongService.LayGiaTri(position);
                        playSongService.Play();
                        btnPlay.setImageResource(R.drawable.pause1);
                        SetTimeTotal();
                    }
                });
                handler.postDelayed(this,500);
            }
        },100);
    }

    private void SetTimeTotal()
    {
        SimpleDateFormat dinhDangGio=new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dinhDangGio.format(playSongService.MaxSize()));
        skSong.setMax(playSongService.MaxSize());
    }




    private void AnhXa()
    {
        txtTitle=findViewById(R.id.textviewTitle);
        txtTimeSong=findViewById(R.id.TextViewTimeSong);
        txtTimeTotal=findViewById(R.id.TextViewTimeTotal);
        skSong=findViewById(R.id.seekBarSong);
        btnPrev=findViewById(R.id.imageButtonPrev);
        btnNext=findViewById(R.id.imageButtonNext);
        btnPlay=findViewById(R.id.imageButtonPlay);
        btnStop=findViewById(R.id.imageButtonStop);
        imgHinh=findViewById(R.id.imageViewDisc);
    }

    protected void onStart() {
        super.onStart();

        // Tạo đối tượng Intent cho WeatherService.
        Intent intent = new Intent(this, PlaySongService.class);

        // Gọi method bindService(..) để giàng buộc dịch vụ với giao diện.
        this.bindService(intent, playSongServiceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }
}

