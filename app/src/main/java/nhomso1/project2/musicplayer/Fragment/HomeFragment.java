package nhomso1.project2.musicplayer.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nhomso1.project2.musicplayer.Adapter.MvAdapter;
import nhomso1.project2.musicplayer.Adapter.PlayListAdapter;
import nhomso1.project2.musicplayer.MusicActivity;
import nhomso1.project2.musicplayer.Object.MV;
import nhomso1.project2.musicplayer.Object.PlayList;
import nhomso1.project2.musicplayer.Object.Song;
import nhomso1.project2.musicplayer.R;
import nhomso1.project2.musicplayer.Adapter.SongAdapter;

import static android.widget.Toast.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SongAdapter.onNoteListener, MvAdapter.OnMVListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;



    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private MvAdapter mvAdapter;
    private List<Song> mSong;
    private List<MV> mMV ;
    private PlayListAdapter playAdapter;
    private List<PlayList> playLists;
    private Context context;
    private Fragment fragment ;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ViewPlay(view);
        ViewMv(view);
        intentView(view);

        return view;
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

    @Override
    public void onNoteClick(int position) {
      //  Log.d("TEST", "onNoteClick: "+mSong.get(position).getNameSong());

        if(mSong.get(position).getNameSong() == "Bài hát" )
        {
            loadFragment(new SongsFragment());
        }
        else{
            if(mSong.get(position).getNameSong() == "PlayList"){
                loadFragment(new PlaylistFragment());
            }
            else{
                Toast.makeText(getContext(),mSong.get(position).getNameSong(),Toast.LENGTH_LONG).show();

            }
        }

    }
    public void loadFragment(Fragment fragment){
        Log.d("asds", "loadFragment: F");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onMVClick(int position) {
        Toast.makeText(getContext(),mMV.get(position).getCountSong(),Toast.LENGTH_LONG).show();
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



    private void ViewPlay(View view) {
        playLists = new ArrayList<>();

        playLists.add(new PlayList(R.drawable.h8, "ZINGCHART", "Various"));
        playLists.add(new PlayList(R.drawable.h11, "Nhạc Khắc Việt", "Khắc Việt"));
        playLists.add(new PlayList(R.drawable.h12, "Sơn Tùng", "Sơn Tùng"));
        playLists.add(new PlayList(R.drawable.h13, "No name", "No name"));


        // mSong.add(new Song("PlayList",R.drawable.ic_dashboard_black_24dp,"10"));
        recyclerView = view.findViewById(R.id.recycler_view3);
        recyclerView.setHasFixedSize(true);

        //  recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        playAdapter = new PlayListAdapter(playLists);
        recyclerView.setAdapter(playAdapter);


    }

    private void ViewMv(View view) {
        mMV = new ArrayList<>();

        mMV.add(new MV("SEND IT", R.drawable.mv, "Austin Mahone"));
        mMV.add(new MV("KILL ME", R.drawable.mv, "JayKii"));
        mMV.add(new MV("WITHOUT ME", R.drawable.mv, "The Sheep"));
        mMV.add(new MV("LAVARO", R.drawable.mv, "JustaTee"));
        Log.d("ABCD", mMV.get(1).getNameSong());

        recyclerView = view.findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        mvAdapter = new MvAdapter(mMV,this);
        recyclerView.setAdapter(mvAdapter);

    }

    private void intentView(View view) {
        mSong = new ArrayList<>();

        mSong.add(new Song("Bài hát", R.drawable.h4, "73"));
        mSong.add(new Song("PlayList", R.drawable.songs, "2"));
        mSong.add(new Song("Tải về", R.drawable.h5, "73"));
        mSong.add(new Song("Nhạc yêu thích", R.drawable.h9, "10"));

        // mSong.add(new Song("PlayList",R.drawable.ic_dashboard_black_24dp,"10"));
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //  recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SongAdapter(mSong,this);
        recyclerView.setAdapter(adapter);
    }
}
