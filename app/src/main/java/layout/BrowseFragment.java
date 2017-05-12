package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tamvan.tamvanmangaindonesia.R;
import com.tamvan.tamvanmangaindonesia.activity.InfoMangaActivity;
import com.tamvan.tamvanmangaindonesia.core.BrowseMangaTask;
import com.tamvan.tamvanmangaindonesia.core.InfoMangaTask;
import com.tamvan.tamvanmangaindonesia.custom.GridViewBrowseManga;
import com.tamvan.tamvanmangaindonesia.holder.Manga;
import com.tamvan.tamvanmangaindonesia.tools.BasicInputOuput;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrowseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Handler handler;
    private GridView gridViewMain;
    private ProgressBar progressBarMain;
    private EditText editTextSearch;
    private GridViewBrowseManga dataAdapter;
    private Context context;
    private GridViewBrowseManga filterAdapter;
    private boolean isDownloading = false;

    public BrowseFragment() {
        // Required empty public constructor
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1==1){ //sukses
                    Bundle bundle = msg.getData();
                    GridViewBrowseManga adapter = (GridViewBrowseManga)bundle.get("adapter");
                    if(gridViewMain!=null) {
                        gridViewMain.setAdapter(adapter);
                        dataAdapter = adapter;
                        adapter.notifyDataSetChanged();
                        editTextSearch.addTextChangedListener(editTextTextChange);
                        progressBarMain.setVisibility(View.GONE);
                        isDownloading = false;
                    }else {
                        dataAdapter = adapter;
                        adapter.notifyDataSetChanged();
                    }
                }else if(msg.arg1==2){ //gagal
                    Toast.makeText(context,"Gagal mendapatkan data!",Toast.LENGTH_SHORT)
                            .show();
                    progressBarMain.setVisibility(View.GONE);
                    isDownloading = false;
                }else if(msg.arg1==3){ // offline reader
                    Bundle bundle = msg.getData();
                    GridViewBrowseManga adapter =
                            (GridViewBrowseManga)bundle.get("adapter");
                    if(gridViewMain!=null) {
                        gridViewMain.setAdapter(adapter);
                        dataAdapter = adapter;
                        adapter.notifyDataSetChanged();
                        progressBarMain.setVisibility(View.VISIBLE);
                        isDownloading = true;
                        editTextSearch.addTextChangedListener(editTextTextChange);
                    }else {
                        dataAdapter = adapter;
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        };
    }

    TextWatcher editTextTextChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            ArrayList<Manga> dataFilter = new ArrayList<>();
            if(charSequence.equals("")){
                gridViewMain.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            }else {
                for(int z = 0;z<dataAdapter.getCount();z++){
                    Manga m = dataAdapter.getItem(z);
                    if(m.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        dataFilter.add(m);
                    }
                }
                filterAdapter = new GridViewBrowseManga(
                        context,dataFilter.toArray(new Manga[dataFilter.size()]));
                gridViewMain.setAdapter(filterAdapter);
                filterAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseFragment newInstance(String param1, String param2) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        BrowseMangaTask browseMangaTask = new BrowseMangaTask(context,handler);
        browseMangaTask.execute();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        gridViewMain = (GridView)view.findViewById(R.id.fb_gridview_main);
        gridViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Manga dataManga = (Manga)view.getTag();
                Intent intent = new Intent(context, InfoMangaActivity.class);
                intent.putExtra("Data",dataManga);
                startActivity(intent);
            }
        });
        try {
            editTextSearch.addTextChangedListener(editTextTextChange);
        }catch (Exception e){
            e.printStackTrace();
        }
        progressBarMain = (ProgressBar)view.findViewById(R.id.fb_progressbar_main);
        editTextSearch = (EditText)view.findViewById(R.id.fb_edittext_search);
        if(dataAdapter!=null){
            if(isDownloading) {
                progressBarMain.setVisibility(View.GONE);
            }else {
                progressBarMain.setVisibility(View.VISIBLE);
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
