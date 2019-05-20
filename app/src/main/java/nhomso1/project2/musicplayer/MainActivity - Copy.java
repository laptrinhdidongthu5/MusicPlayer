//package nhomso1.project2.musicplayer;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.design.widget.BottomNavigationView;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.annotation.NonNull;
//import android.view.MenuItem;
//
//import nhomso1.project2.musicplayer.Fragment.HomeFragment;
//import nhomso1.project2.musicplayer.Fragment.PlaylistFragment;
//import nhomso1.project2.musicplayer.Fragment.SongsFragment;
//import nhomso1.project2.musicplayer.SupportMore.BottomNavigationBehavior;
//
//
//public class MainActivity2 extends AppCompatActivity implements
//        HomeFragment.OnFragmentInteractionListener,
//        SongsFragment.OnFragmentInteractionListener,
//        PlaylistFragment.OnFragmentInteractionListener
//
//{
//
//
//    private ActionBar toolbar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        toolbar = getSupportActionBar(); //Get Action bar
//
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navView.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());
//
//        toolbar.setTitle("Home"); //Change title first
//        loadFragment(new HomeFragment());//load fragment
//
//
//    }
//
//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.navigation_home:
//                    toolbar.setTitle("Home");
//                    loadFragment(new HomeFragment());//load fragment
//                    return true;
//                case R.id.navigation_song:
//                    toolbar.setTitle("Songs");
//                    loadFragment(new SongsFragment());//load fragment
//                    return true;
//                case R.id.navigation_playlist:
//                    toolbar.setTitle("Playlists");
//                    loadFragment(new PlaylistFragment());//load fragment
//                    return true;
//            }
//            return false;
//        }
//    };
//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
//}
