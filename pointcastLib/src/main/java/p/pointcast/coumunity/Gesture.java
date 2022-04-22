package p.pointcast.coumunity;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class Gesture implements SensorEventListener {
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    final  Context context;

    private SensorManager mSensorManager,mSensorManager1;
    private Sensor mOrientationSensor ,mAccelerometer;

 public static String gesturev  = "";
    public Gesture(Context context) {
        // sensor manager
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mSensorManager1 = (SensorManager)context.getSystemService(SENSOR_SERVICE);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // If this phone has an accelerometer, listen to it.
        if (mAccelerometer != null) {
            mSensorManager1.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        }
       start(context);
        this.context = context;
    }

    private void start(Context gesture) {
            mSensorManager1 = (SensorManager)gesture.getSystemService(SENSOR_SERVICE);

            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            // If this phone has an accelerometer, listen to it.
            if (mAccelerometer != null) {
                this.mSensorManager1 = mSensorManager;
                mSensorManager1.registerListener(this, mAccelerometer,
                        SensorManager.SENSOR_DELAY_UI);
            }else {
                noSensorsAlert();

            }
            mAccel = 0.00f;
            mAccelCurrent = SensorManager.GRAVITY_EARTH;
            mAccelLast = SensorManager.GRAVITY_EARTH;
        }

    private void noSensorsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("Your device doesn't support the compasss.")
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        alertDialog.show();
    }

    public void stop() {
            mSensorManager1.unregisterListener(this, mAccelerometer);
        }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // shake code start
        System.out.println("event======" + Sensor.TYPE_ACCELEROMETER);
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.out.println("event======" + Sensor.TYPE_ACCELEROMETER);
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            if (mAccel > 8) {
                // do something
//                System.out.println("pointcast===="+ mAccel);
                System.out.println("pointcast=====x" + x + "y===" + y + "z====" + z);

                System.out.println("zkkkkkkkkkkkk" + z);

                if (z > 0) {
                    if (Round(x,4) < -10.0000 || Round(x,4) > 10.0000) {
                        //Toast.makeText(context, "sweep Event ", Toast.LENGTH_SHORT).show();
                        System.out.println("z" + z);
                        gesturev = "sweep";

                    } else {
                       // Toast.makeText(context, "point cast ", Toast.LENGTH_SHORT).show();
                        System.out.println("z" + z);
                        gesturev = "cast";
                    }
                } else {
                    //Toast.makeText(context, "point cast1", Toast.LENGTH_SHORT).show();
                    System.out.println("z" + z);
                    gesturev = "cast";

                }
                stop();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        start(context);
                    }
                }, 3000);

            }
        }
        // shake code end
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public static float Round(float Rval, int Rpl){
        float p = (float) Math.pow(10, Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (float) tmp / p;
    }

}
