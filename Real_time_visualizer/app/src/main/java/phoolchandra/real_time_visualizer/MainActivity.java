package phoolchandra.real_time_visualizer;

import android.graphics.Picture;
import android.hardware.Camera;
import android.nfc.Tag;
import android.util.Log;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.View;
import android.widget.Toast;
import android.provider.MediaStore.Files.FileColumns;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;
    private  CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

        // Create a instance of a camera


        mCamera = getCameraInstance();
        //mCamera = Camera.open();

        // create our Preview view and set it as the content of our activity

       mPreview = new CameraPreview(this, mCamera);
       FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        Button btn = (Button) findViewById(R.id.button_capture);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null,null,mPicture);

            }
        });

    }



    private boolean checkCameraHadware(Context context)
    {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            //device had a camera
            return true;
        }
        else
        {
            // no camera on the device
            return false;
        }
    }


    public static Camera getCameraInstance()
    {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a camera instance
        }catch (Exception e)
        {
            // camera is not available (in use or does not exist
        }
        return c;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //releaseMediaRecorder(); // if you are using camera recorder, release it first
        releaseCamera();
    }

    private void releaseCamera()
    {
        if(mCamera != null)
        {
            mCamera.release(); // release the camer for other application
            mCamera = null;
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera mCamera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("PhotoTest", "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                String filepath = pictureFile.getAbsolutePath();
                Bitmap myBitmap = BitmapFactory.decodeFile(filepath);
                MediaStore.Images.Media.insertImage(getContentResolver(), myBitmap, "PhotoTest", "taken with intent camera");
                MediaScannerConnection.scanFile(MainActivity.this, new String[]{filepath}, null, null);
            } catch (FileNotFoundException e) {
                Log.d("PhotoTest", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("PhotoTest", "Error accessing file: " + e.getMessage());
            }
        }
    };

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PhotoTest");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("PhotoTest", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        Log.v("PhotoTest", "storing at " + mediaFile.getAbsolutePath());
        return mediaFile;
    }
}

