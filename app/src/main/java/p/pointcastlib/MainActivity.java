package p.pointcastlib;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import java.util.ArrayList;

import p.pointcast.coumunity.Compass;
import p.pointcast.coumunity.Gesture;
import p.pointcast.coumunity.LocationTrack;
public class MainActivity extends AppCompatActivity {
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    TextView  latt, comvalue,costvalue;
    Gesture gesture;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    Compass compass;
    String display;
    String freezall= "";
    private float currentAzimuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latt = (TextView)findViewById(R.id.latt);
        comvalue= (TextView)findViewById(R.id.comvalue);
        costvalue= (TextView)findViewById(R.id.costvalue);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

//        sensor tamal = new sensor(MainActivity.this);
//        tamal.Creator(MainActivity.this, "tyagi ji");

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        setupCompass();

        NEWGESTure();
        gesture = new Gesture(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

                locationTrack = new LocationTrack(MainActivity.this);


                if (locationTrack.canGetLocation()) {
                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();

                    latt.setText("Latitude:  " + Double.toString(latitude) + "\nLongitude:  " + Double.toString(longitude));
                   // Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                } else {
                    locationTrack.showSettingsAlert();
                }

    }

    private void NEWGESTure() {

        if (gesture.gesturev.isEmpty()){
            costvalue.setText("None");
        }else {
               String kama=   gesture.gesturev;
                costvalue.setText(kama);
               freezall ="pointLib";

               // you call your Api here

        }

    }


    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @SuppressLint("MissingSuperCall")
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

    private void setupCompass() {
        compass = new Compass(this);
        Compass.CompassListener cl = new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(float azimuth) {
                adjustArrow(azimuth);
            }
        };
        compass.setListener(cl);

    }

    @SuppressLint("NewApi")
    private void adjustArrow(float azimuth) {
        Log.d("TAG", "will set rotation from " + currentAzimuth + " to "
                + azimuth);

        NEWGESTure();
        if (freezall.isEmpty()){
        }

        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;

        display = (int) currentAzimuth + "";
        System.out.println("currentAzimuth=====1"+ " " +currentAzimuth +"°");
        String cardDirect;

        if (currentAzimuth == 0 || currentAzimuth == 360)
            cardDirect = "N";
        else if (currentAzimuth > 0 && currentAzimuth < 90)
            cardDirect = "NE";
        else if (currentAzimuth == 90)
            cardDirect = "E";
        else if (currentAzimuth > 90 && currentAzimuth < 180)
            cardDirect = "SE";
        else if (currentAzimuth == 180)
            cardDirect = "S";
        else if (currentAzimuth > 180 && currentAzimuth < 270)
            cardDirect = "SW";
        else if (currentAzimuth == 270)
            cardDirect = "W";
        else if (currentAzimuth > 270 && currentAzimuth < 360)
            cardDirect = "NW";
        else
            cardDirect = "Unknown";

        System.out.println("currentAzimuth2====="+display +"°");
        comvalue.setText(display + "°");
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG", "start compass");
        compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        compass.start();

        String kama=   gesture.gesturev;
        costvalue.setText(kama);

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG", "stop compass");
        compass.stop();
//        gesture.stop();
    }

}
