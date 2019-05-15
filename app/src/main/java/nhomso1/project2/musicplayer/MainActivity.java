package nhomso1.project2.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nhomso1.project2.musicplayer.HomeFragment;
import nhomso1.project2.musicplayer.SongsFragment;
import nhomso1.project2.musicplayer.PlaylistFragment;


public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        SongsFragment.OnFragmentInteractionListener,
        PlaylistFragment.OnFragmentInteractionListener {


    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar(); //Get Action bar
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Home"); //Change title first
        loadFragment(new HomeFragment());//load fragment
//        intentView();
//        ViewMv();
//        ViewPlay();
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    loadFragment(new HomeFragment());//load fragment
                    return true;
                case R.id.navigation_song:
                    toolbar.setTitle("Songs");
                    loadFragment(new SongsFragment());//load fragment
                    return true;
                case R.id.navigation_playlist:
                    toolbar.setTitle("Playlists");
                    loadFragment(new PlaylistFragment());//load fragment
                    return true;
            }
            return false;
        }
    };




    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
