package nhomso1.project2.musicplayer.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Binder;
import android.util.Log;

import java.util.ArrayList;

import nhomso1.project2.musicplayer.R;
import nhomso1.project2.musicplayer.Object.SongList;


public class PlaySongService extends Service {
    private MediaPlayer mediaPlayer;

    public ArrayList<SongList> arraySong;

    int position=0;

    private final IBinder binder = new PlaySongBinder();

    public class PlaySongBinder extends Binder {

        public PlaySongService getService()  {
            return PlaySongService.this;
        }
    }

    public PlaySongService() {
    }


    @Override
    public void onCreate(){
        super.onCreate();
        // Tạo đối tượng MediaPlayer, chơi file nhạc của bạn.
        AddSong();
        KhoiTaoMediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return this.binder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // Chơi nhạc.
        //mediaPlayer.start();

        return START_STICKY;
    }

    // Hủy bỏ dịch vụ.
    @Override
    public void onDestroy() {
        // Giải phóng nguồn dữ nguồn phát nhạc.
        mediaPlayer.stop();
        mediaPlayer.release();
        // super.onDestroy();
    }

    public void TamDung()
    {
        mediaPlayer.pause();
    }

    public  void Play()
    {
        mediaPlayer.start();
    }

    public boolean Playing()
    {
        return mediaPlayer.isPlaying();
    }

    public void Stop()
    {
        mediaPlayer.stop();
    }

    public void Relaxse()
    {
        mediaPlayer.release();
    }

    private void AddSong()
    {
        arraySong = new ArrayList<>();
        arraySong.add(new SongList("Anh ơi ở lại", R.raw.baihat1));
        arraySong.add(new SongList("Bạc Phận",R.raw.baihat2));
        Log.d("BHHH", String.valueOf(arraySong.size()));
    }

    public void KhoiTaoMediaPlayer()
    {
        mediaPlayer=MediaPlayer.create(getApplicationContext(),arraySong.get(position).getFile());
    }

    public int MaxSize()
    {
        return mediaPlayer.getDuration();
    }

    public void LayGiaTri(int position)
    {
        this.position=position;
    }

    public  String TenBaiHat()
    {
        return arraySong.get(position).getTitle();
    }

    public void ThayDoiThanhBar(int time)
    {
        mediaPlayer.seekTo(time);
    }

    public  MediaPlayer LayMedia()
    {
        return this.mediaPlayer;
    }
}

