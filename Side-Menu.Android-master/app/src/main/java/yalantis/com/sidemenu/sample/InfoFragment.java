package yalantis.com.sidemenu.sample;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {
    private TextView courseName,proCode,description,duration,mode,average_Result,link;


    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        courseName = (TextView)getView().findViewById(R.id.courseNameInfo);
        proCode = (TextView)getView().findViewById(R.id.proCodeInfo);
        description = (TextView)getView().findViewById(R.id.descriptionInfo);
        duration = (TextView)getView().findViewById(R.id.durationInfo);
        mode = (TextView)getView().findViewById(R.id.modeInfo);
        average_Result = (TextView)getView().findViewById(R.id.average_ResultInfo);
        link = (TextView)getView().findViewById(R.id.linkInfo);




        SharedPreferences mPrefs = this.getActivity().getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("courseInfoView", "");
        CourseInfo courseInfo = gson.fromJson(json, CourseInfo.class);
        courseName.setText("Course Name: "+courseInfo.getName());
        proCode.setText("Programme Code: "+courseInfo.getProgrammeCode());
        description.setText("Description: "+courseInfo.getDesctiption());
        duration.setText("Duration: "+courseInfo.getDuration());
        mode.setText("Mode of Study: "+courseInfo.getMode());
        average_Result.setText("Average Result: "+courseInfo.getAverage_Result());
        link.setText("Link: "+courseInfo.getLink());
        Linkify.addLinks(link,Linkify.WEB_URLS);
        link.setLinkTextColor(Color.parseColor("#1E1D1D"));

    }
}
