package yalantis.com.sidemenu.sample;

import android.app.Activity;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.*;

import java.util.Collection;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;

    @Override
    public void onBeaconServiceConnect() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.i(TAG,"Beacon size:"+beacons.size());
                String FILENAME = "hello_file";
                String string = "hello world!";

                boolean a = true;


                try {
                    /*
                    FileOutputStream fos = openFileOutput(FILENAME,MODE_PRIVATE);
                    fos.write(string.getBytes());
                    fos.close();
                    FileInputStream abc = openFileInput(FILENAME);
                    InputStreamReader inputStreamReader = new InputStreamReader(abc);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    */
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (beacons.size() > 0) {

                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                    Log.i(TAG," id1= " +beacons.iterator().next().getId1()+"  id2= "+beacons.iterator().next().getId2()+"  id3= "+beacons.iterator().next().getId3());
                    Log.i(TAG,"Rssi: "+beacons.iterator().next().getRssi());

                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }
}

