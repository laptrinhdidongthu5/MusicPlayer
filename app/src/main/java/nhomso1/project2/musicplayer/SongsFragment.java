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
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public static final String Broadcast_PLAY_NEW_AUDIO = "nhomso1.project2.MusicPlayer.PlayNewAudio";
    public static final int MY_PERMISSION_REQUEST = 123;


    boolean serviceBound = false;


    private MediaPlayerService player;
    RecyclerView recyclerView;

    ArrayList<Audio> audioList;

    ImageView collapsingImageView;
    int imageIndex = 0;

    Toolbar toolbar;

    View view;

    public SongsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongsFragment newInstance(String param1, String param2) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_songs, container, false);

        //Ánh xạ image album phục vụ cho coordinate layout
        collapsingImageView = view.findViewById(R.id.collapsingImageView);
        loadCollapsingImage(imageIndex);


        //Nút floatAction để chuyển hình cho vui thôi
        FloatingActionButton fab = view.findViewById(R.id.fab);
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
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            initRecyclerView(view); //Khởi tạo danh sách và bài hát lên RecycleView
        }
        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                //Xin quyền truy cập vào SDCARD ở đây
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission Granted!", Toast.LENGTH_SHORT).show();
                        initRecyclerView(view);
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                return;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; Thêm các hành động vào menu: Quyên sẽ thêm vào chỗ này
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("serviceStatus", serviceBound);
        Log.d("it10069",outState.toString());
    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        serviceBound = savedInstanceState.getBoolean("serviceStatus");
//    }



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
        ContentResolver contentResolver = getActivity().getContentResolver();
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
    private void initRecyclerView(View view) {

        //Đầu tiên load list Audio lên
        loadAudio();


        if (audioList.size() > 0) {
            //Ánh xạ recyclerView, icon play pause của bài hát
            recyclerView = view.findViewById(R.id.recyclerview);

            //Khởi tạo Adapter để đổ vào recyclerView
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(audioList, getActivity());
            recyclerView.setAdapter(adapter);

            //recyclerView với LinearLayout
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            //Bắt sự kiện nhấp vào thành phần của danh sách
            recyclerView.addOnItemTouchListener(new CustomTouchListener(getActivity(), recyclerView, new onItemClickListener() {
                @Override
                public void playOnClick(View v, int index) {
                    //Các hiệu ứng và thành phần giao diện khác xử lý ở đây nhé
                    playAudio(index); //Khi bất kỳ item nào được nhấn thì sẽ chơi bài đó Easy!!!
                }

                @Override
                public void favoriteOnClick(View v, int index) {
                    addFavorite(index);
                }
            }));
        }
    }

    /**
     * Kiểm tra service active và Intent đến MediaPlayerService.class
     * Vì nó không có giao diện nên mình Intent tới
     * Sẽ không có Activity nào được đổ lên trên Activi hiện tại
     *
     * @param audioIndex
     */
    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Lưu trữ audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getActivity());

            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(getActivity(), MediaPlayerService.class);

            getActivity().startService(playerIntent);

            getActivity().bindService(playerIntent, serviceConnection, getActivity().BIND_AUTO_CREATE);

        } else {
            //lưu một audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getActivity());
            storage.storeAudioIndex(audioIndex);

            //Service is active and...
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            player.sendBroadcast(broadcastIntent);
        }
    }

    private void addFavorite(int audioIndex) {
        FavoriteDAO db = new FavoriteDAO(getActivity());
        Audio au = audioList.get(audioIndex);

        db.addAudio(au);
        Toast.makeText(getContext(),
                "Lưu vào yêu thích", Toast.LENGTH_LONG).show();
    }


    /**
     * Support collapsing lại để hiện thị danh sách nhạc tràn màn hình
     */
    private void loadCollapsingImage(int i) {
        TypedArray array = getResources().obtainTypedArray(R.array.images);
        collapsingImageView.setImageDrawable(array.getDrawable(i));
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (serviceBound) {
            getActivity().unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
