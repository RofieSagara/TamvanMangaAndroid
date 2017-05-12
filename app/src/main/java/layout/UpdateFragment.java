package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tamvan.tamvanmangaindonesia.R;
import com.tamvan.tamvanmangaindonesia.activity.InfoMangaActivity;
import com.tamvan.tamvanmangaindonesia.core.InfoMangaTask;
import com.tamvan.tamvanmangaindonesia.core.UpdateMangaTask;
import com.tamvan.tamvanmangaindonesia.custom.GridViewUpdateFragmentAdapter;
import com.tamvan.tamvanmangaindonesia.holder.Manga;
import com.tamvan.tamvanmangaindonesia.holder.UpdateManga;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Handler mHadler;
    private GridView gridViewMain;
    private ProgressBar progressBarMain;
    private GridViewUpdateFragmentAdapter dataAdapter;
    private Context context;
    private boolean isDownloading = false;

    public UpdateFragment() {
        // Required empty public constructor
        mHadler = new Handler(Looper.getMainLooper()){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1==1){
                    Bundle bundle = msg.getData();
                    GridViewUpdateFragmentAdapter adapter =
                            (GridViewUpdateFragmentAdapter)bundle.get("adapter");
                    if(gridViewMain!=null) {
                        progressBarMain.setVisibility(View.GONE);
                        isDownloading = false;
                        gridViewMain.setAdapter(adapter);
                        dataAdapter = adapter;
                        adapter.notifyDataSetChanged();
                    }else {
                        dataAdapter = adapter;
                        adapter.notifyDataSetChanged();
                    }
                }else if(msg.arg1==2){
                    UpdateMangaTask updateMangaTask = new UpdateMangaTask(context,mHadler);
                    updateMangaTask.execute();
                }else if(msg.arg1==3){
                    Bundle bundle = msg.getData();
                    GridViewUpdateFragmentAdapter adapter =
                            (GridViewUpdateFragmentAdapter)bundle.get("adapter");
                    if(gridViewMain!=null) {
                        gridViewMain.setAdapter(adapter);
                        dataAdapter = adapter;
                        adapter.notifyDataSetChanged();
                        progressBarMain.setVisibility(View.VISIBLE);
                        isDownloading = true;
                    }else {
                        dataAdapter = adapter;
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        };
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(String param1, String param2) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.d("TMI","Update Fragment");
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        UpdateMangaTask updateMangaTask = new UpdateMangaTask(context,mHadler);
        updateMangaTask.execute();
        Log.d("TMI","onAttach Fragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TMI","Create Fragment");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("TMI","onCreate Fragment");
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        gridViewMain = (GridView)view.findViewById(R.id.fu_gridview_main);
        gridViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UpdateManga data = (UpdateManga)view.getTag();
                Manga dataManga = new Manga();
                dataManga.setLink(data.getLink());
                dataManga.setCover(data.getCover());
                dataManga.setNamaFolder(data.getNameFolder());
                dataManga.setTitle(data.getTitle());
                Intent intent = new Intent(context, InfoMangaActivity.class);
                intent.putExtra("Data",dataManga);
                startActivity(intent);
            }
        });
        progressBarMain = (ProgressBar)view.findViewById(R.id.fu_progressbar_main);
        if(dataAdapter!=null) {
            if(isDownloading){
                progressBarMain.setVisibility(View.VISIBLE);
            }else {
                progressBarMain.setVisibility(View.GONE);
            }
            gridViewMain.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("TMI","onDetach Fragment");
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
