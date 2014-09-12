package com.utr.postscripter8.utr;


import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;


public class MapsActivity extends FragmentActivity implements LocationListener {

    private Boolean isRecording;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    FrameLayout preview;


    private Camera mCamera;
    private CameraPreview mPreview;

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(this, BackgroundVideoRecorder.class);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void startRecord(View v) {



        Button b = (Button) findViewById(R.id.button);

        if(!isRecording) {
            ComponentName s = startService(new Intent(this, BackgroundVideoRecorder.class));
            b.setText("Zakoncz");
            isRecording = true;
            stopService(new Intent(this, CameraPreview.class));
            preview.removeAllViews();
            if(!isRecording) {
                mCamera = getCameraInstance();
                // Create our Preview view and set it as the content of our activity.
                if (mPreview == null) {
                    mPreview =  new CameraPreview(this, mCamera);
                    this.preview = (FrameLayout) findViewById(R.id.camera_preview);
                }
                preview.addView(mPreview);
            }
        } else {
            stopService(new Intent(this, BackgroundVideoRecorder.class));
            b.setText("Zacznij");
            isRecording = false;
            // Create our Preview view and set it as the content of our activity.
            this.preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);




        if(savedInstanceState != null) {
            isRecording = savedInstanceState.getBoolean("isRecording");
            if(isRecording) {
                Button b = (Button) findViewById(R.id.button);
                b.setText("Zakoncz");
            }
        } else {
            isRecording = false;
        }


    }



    @Override
    protected void onResume() {
        super.onResume();

        if(isRecording) {
            Button b = (Button) findViewById(R.id.button);
            b.setText("Zakoncz");
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        if(location!=null){
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
    }



    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, CameraPreview.class));
        if(preview!=null)
            preview.removeAllViews();
    }
    @Override
    protected void onStart() {
        super.onStop();
        if(!isRecording) {
            mCamera = getCameraInstance();
            // Create our Preview view and set it as the content of our activity.
            if (mPreview == null) {
                mPreview = new CameraPreview(this, mCamera);
                this.preview = (FrameLayout) findViewById(R.id.camera_preview);
            }
            preview.addView(mPreview);
        }
    }


    @Override
    protected  void onRestart() {
        super.onRestart();

        if(isRecording) {
            Button b = (Button) findViewById(R.id.button);
            b.setText("Zakoncz");
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        outstate.putBoolean("isRecording",isRecording);

    }
    @Override

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean("isRecording")) isRecording = true;
        else isRecording = false;
    }







    /*
 * Handle results returned to the FragmentActivity
 * by Google Play services
 */
//    @Override
//    protected void onActivityResult(
//            int requestCode, int resultCode, Intent data) {
//        // Decide what to do based on the original request code
//        switch (requestCode) {
//
//            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
//            /*
//             * If the result code is Activity.RESULT_OK, try
//             * to connect again
//             */
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        mLocationClient.connect();
//                        break;
//                }
//
//        }
//    }
//    private boolean isGooglePlayServicesAvailable() {
//        // Check that Google Play services is available
//        int resultCode =  GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        // If Google Play services is available
//        if (ConnectionResult.SUCCESS == resultCode) {
//            // In debug mode, log the status
//            Log.d("Location Updates", "Google Play services is available.");
//            return true;
//        } else {
//            // Get the error dialog from Google Play services
//            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog( resultCode,
//                    this,
//                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
//
//            // If Google Play services can provide an error dialog
//            if (errorDialog != null) {
//                // Create a new DialogFragment for the error dialog
//                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
//                errorFragment.setDialog(errorDialog);
//                errorFragment.show(getSupportFragmentManager(), "Location Updates");
//            }
//
//            return false;
//        }
//    }

//    /*
//     * Called by Location Services when the request to connect the
//     * client finishes successfully. At this point, you can
//     * request the current location or start periodic updates
//     */
//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
    @Override
    public void onLocationChanged(Location location) {

    // Getting latitude of the current location
    // double latitude = location.getLatitude();
    // Getting longitude of the current location
    //double longitude = location.getLongitude();
    // Creating a LatLng object for the current location
    // LatLng latLng = new LatLng(latitude, longitude);
    // Showing the current location in Google Map

}

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
//        setUpMapIfNeeded();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

//    }
}
