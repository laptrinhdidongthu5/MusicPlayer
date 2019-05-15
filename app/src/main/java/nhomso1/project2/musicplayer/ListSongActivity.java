package nhomso1.project2.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.Instant;

public class ListSongActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static final String TAG="Gallaery";
    private Instant Glide;
    private Context context ;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_song:
                    mTextMessage.setText(R.string.title_songs);
                    return true;
                case R.id.navigation_playlist:
                    mTextMessage.setText(R.string.title_playlist);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //  getIntent();
    }

    public Intent getIntent() {
        if (getIntent().hasExtra("img_url") && getIntent().hasExtra("descript")) {
//            Hình ảnh
            String img_url = getIntent().getStringExtra("img_url");
            String descript = getIntent().getStringExtra("descript");

            setImage(img_url, descript);
        }


        return null;
    }

    private void setImage(String img, String name) {
        TextView desc = findViewById(R.id.descript);
        desc.setText(name);

        ImageView image = findViewById(R.id.image_song);
        image.setImageResource(R.drawable.banner2);
//        Glide.with(this).asBitmap().load(img).into(image);


    }

}
