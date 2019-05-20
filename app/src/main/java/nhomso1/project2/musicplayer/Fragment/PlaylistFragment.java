package nhomso1.project2.musicplayer.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import nhomso1.project2.musicplayer.Adapter.RecyclerViewAdapter;
import nhomso1.project2.musicplayer.Interface.onItemClickListener;
import nhomso1.project2.musicplayer.MusicActivity;
import nhomso1.project2.musicplayer.Object.Audio;
import nhomso1.project2.musicplayer.R;
import nhomso1.project2.musicplayer.RecyclerView.CustomTouchListener;
import nhomso1.project2.musicplayer.Service.MediaPlayerService;
import nhomso1.project2.musicplayer.SqLite.FavoriteDAO;
import nhomso1.project2.musicplayer.Storage.StorageUtil;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaylistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public static final String Broadcast_PLAY_NEW_AUDIO = "it1006.learn.musicwidget.PlayNewAudio";

    RecyclerView recyclerView;
    boolean serviceBound = false;
    private MediaPlayerService player;
    ArrayList<Audio> audioList;


    public PlaylistFragment() {
        // Required empty public constructor
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
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
        View view =  inflater.inflate(R.layout.fragment_playlist, container, false);
        initRecyclerView(view);
        return  view;
    }
    private void loadAudio() {

        audioList = new ArrayList<>();
        FavoriteDAO db = new FavoriteDAO(getActivity());
        for (Audio au:db.getAllFavorite()) {
            audioList.add(new Audio(au.getData(), au.getTitle(), au.getAlbum(), au.getArtist()));
        }
    }
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
//                    playAudio(index); //Khi bất kỳ item nào được nhấn thì sẽ chơi bài đó Easy!!!
                    Intent intent = new Intent(getActivity(), MusicActivity.class);
                    intent.putExtra("index",index);
                    loadAudio();
                    new StorageUtil(getActivity().getApplicationContext()).storeAudio(audioList);
                    new StorageUtil(getActivity().getApplicationContext()).storeAudioIndex(index);
                    startActivity(intent);
                }

                @Override
                public void favoriteOnClick(View v, int index) {
                    deleteAudio(index);
                }
            }));
        }
    }

    private void deleteAudio(int audioIndex) {
        FavoriteDAO db = new FavoriteDAO(getActivity());
        Audio au = audioList.get(audioIndex);

        db.deleteFavorite(au);
        audioList.remove(audioIndex);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(audioList, getActivity().getApplication());
        recyclerView.setAdapter(adapter);
        Toast.makeText(getActivity().getApplicationContext(),
                "Xóa khỏi yêu thích", Toast.LENGTH_LONG).show();


    }

    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Lưu trữ audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getActivity().getApplicationContext());

            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(getActivity(), MediaPlayerService.class);

            getActivity().startService(playerIntent);
            getActivity().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        } else {
            //lưu một audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getActivity().getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active and...
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            getActivity().sendBroadcast(broadcastIntent);
        }
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
