package yalantis.com.sidemenu.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MapFragment extends Fragment {


    private String beaconName="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_map, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView img= (ImageView) getView().findViewById(R.id.mapImage);
        if(beaconName=="beacon001"){
            img.setImageResource(R.drawable.floor_421a);
        }else if(beaconName=="beacon002"){
            img.setImageResource(R.drawable.floorplan_p05);
        }else if(beaconName=="beacon003"){
            img.setImageResource(R.drawable.floor_409);
        }

    }
    public void setInfo(String name){
        beaconName = name;
    }
}
