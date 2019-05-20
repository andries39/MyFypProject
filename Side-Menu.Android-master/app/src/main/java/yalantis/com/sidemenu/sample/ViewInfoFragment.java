package yalantis.com.sidemenu.sample;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import yalantis.com.sidemenu.sample.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewInfoFragment extends Fragment {
    private String[] json = new String[3];
    private ArrayList<String> name=new ArrayList<String>();


    public ViewInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_info, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        name.clear();
        ListView listview = (ListView) getView().findViewById(R.id.listview);
        SharedPreferences mPrefs = this.getActivity().getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        json[0] = mPrefs.getString("courseInfo_beacon001", "");
        json[1] = mPrefs.getString("courseInfo_beacon002", "");
        json[2] = mPrefs.getString("courseInfo_beacon003", "");
        for(int a = 0;a<json.length;a++){
            if(json[a]!=""){
                CourseInfo courseInfo = gson.fromJson(json[a], CourseInfo.class);
                Log.i("CourseInfo Get:","courseInfo.getName()-"+courseInfo.getName());
                name.add(courseInfo.getName());
            }
        }
      ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                name);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onClickListView);
    }
    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Toast.makeText(getActivity(),"點選第 "+(position +1) +" 個 \n內容："+name.get(position), Toast.LENGTH_SHORT).show();
            SharedPreferences mPrefs = getActivity().getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            json[0] = mPrefs.getString("courseInfo_beacon001", "");
            json[1] = mPrefs.getString("courseInfo_beacon002", "");
            json[2] = mPrefs.getString("courseInfo_beacon003", "");
            for(int a = 0;a<json.length;a++){
                if(json[a]!="") {

                    CourseInfo courseInfo = gson.fromJson(json[a], CourseInfo.class);
                    if(courseInfo.getName().equals(name.get(position))){
                        String json = gson.toJson(courseInfo);
                        prefsEditor.putString("courseInfoView", json);
                        prefsEditor.commit();
                        Log.i("onItemClick:","courseInfoView Hello");
                    }

                }
            }

            InfoFragment infoFragment = new InfoFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLay, infoFragment);
            fragmentTransaction.commit();
        }
    };

}
