package yalantis.com.sidemenu.sample;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;


public class SearchFragment extends Fragment  {
    private TextView courseName,proCode,description,duration,mode,average_Result,link;
    private int testrssi=0;
    private String beaconName="";
    private int testCount=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        courseName = (TextView)getView().findViewById(R.id.courseName);
        proCode = (TextView)getView().findViewById(R.id.proCode);
        description = (TextView)getView().findViewById(R.id.description);
        duration = (TextView)getView().findViewById(R.id.duration);
        mode = (TextView)getView().findViewById(R.id.mode);
        average_Result = (TextView)getView().findViewById(R.id.average_Result);
        link = (TextView)getView().findViewById(R.id.link);



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.i("BeaconNo","beaconNo.~~~~~~~~~~~~~~~~~:"+beaconName);
        String a="beacon002";//need to change!!!!
        if(a.equals(beaconName)){
            Log.i("BeaconNo","Sucess!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }else{
            Log.i("BeaconNo","No!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        if(beaconName!=""){
            DocumentReference docRef = db.collection("ibeacon").document(beaconName);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot ds) {

                    CourseInfo courseInfo = ds.toObject(CourseInfo.class);
                    //courseInfo = ds.toObject(CourseInfo.class);
                    Log.i("courseInfo:","Get Data:"+courseInfo);
                    courseName.setText("Course Name: "+courseInfo.getName());
                    proCode.setText("Programme Code: "+courseInfo.getProgrammeCode());
                    description.setText("Description: "+courseInfo.getDesctiption());
                    duration.setText("Duration: "+courseInfo.getDuration());
                    mode.setText("Mode of Study: "+courseInfo.getMode());
                    average_Result.setText("Average Result: "+courseInfo.getAverage_Result());
                    link.setText("Link: "+courseInfo.getLink());
                    Linkify.addLinks(link,Linkify.WEB_URLS);
                    link.setLinkTextColor(Color.parseColor("#1E1D1D"));

                    SharedPreferences mPrefs = getActivity().getPreferences(MODE_PRIVATE);


                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(courseInfo);
                    prefsEditor.putString("courseInfo_"+beaconName, json);
                    prefsEditor.putString("courseSignIn_"+beaconName, "1");
                    prefsEditor.commit();

                    if(testCount==0){
                        testCount++;
                        int count =courseInfo.getCount() ;
                        updateCount(count);
                    }
                    // Campus.setText("description: "+ds.getString("Campus"));
                }
            });
        }



    }
    public void setInfo(String name){
        beaconName = name;
    }

    public void updateCount(int count){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("ibeacon").document(beaconName);
        ref.update(
                "count",count+1
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("updateCount","Successful!");
                }else{
                    Log.i("updateCount","Not Successful");
                }
            }
        });
    }

}
