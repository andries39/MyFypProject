package yalantis.com.sidemenu.sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;


public class SignInFragment extends Fragment {
    private double distance =0;
    private TextView rssiText,beacon001Name,beacon002Name,beacon003Name;
    private CheckBox beacon001CheckBox,beacon002CheckBox,beacon003CheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        beacon001Name = (TextView)getView().findViewById(R.id.beacon001Name);
        beacon001CheckBox = getView().findViewById(R.id.beacon001CheckBox);
        beacon002Name = (TextView)getView().findViewById(R.id.beacon002Name);
        beacon002CheckBox = getView().findViewById(R.id.beacon002CheckBox);
        beacon003Name = (TextView)getView().findViewById(R.id.beacon003Name);
        beacon003CheckBox = getView().findViewById(R.id.beacon003CheckBox);

        SharedPreferences mPrefs = this.getActivity().getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json001 = mPrefs.getString("courseInfo_beacon001", "");
        String json002 = mPrefs.getString("courseInfo_beacon002", "");
        String json003 = mPrefs.getString("courseInfo_beacon003", "");

        if(json001!=""){
            CourseInfo courseInfo001 = gson.fromJson(json001, CourseInfo.class);
            beacon001Name.setText(courseInfo001.getName()+"-Room:"+courseInfo001.getRoom());
            beacon001CheckBox.setChecked(true);
        }
        if(json002!=""){
            CourseInfo courseInfo002 = gson.fromJson(json002, CourseInfo.class);
            beacon002Name.setText(courseInfo002.getName()+"-Room:"+courseInfo002.getRoom());
            beacon002CheckBox.setChecked(true);
        }
        if(json003!=""){
            CourseInfo courseInfo003 = gson.fromJson(json003, CourseInfo.class);
            beacon003Name.setText(courseInfo003.getName()+"-Room:"+courseInfo003.getRoom());
            beacon003CheckBox.setChecked(true);
        }


       // rssiText.setText(String.valueOf(distance));
    }
    public void setDistance(double distance){
        this.distance = distance;
    }

}
