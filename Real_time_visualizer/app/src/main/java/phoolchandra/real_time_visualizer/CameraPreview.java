package phoolchandra.real_time_visualizer;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import android.app.Activity;
import android.view.Surface;

/**
 * Created by ph051 on 22/3/18.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    public static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;


    public CameraPreview(Context context, Camera camera)
    {
        super(context);
        mCamera = camera;

        // install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.

        mHolder = getHolder();
        mHolder.addCallback(this);

        //  deprecated setting but required on android prior to 3.0

        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // the surface has been creared , now tell the camera where to draw the previiew.

       Camera.Parameters params = mCamera.getParameters();

       if(this.getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE)
       {
           params.set("orientation","portrait");
           mCamera.setDisplayOrientation(90);
           params.setRotation(90);
       }
       else
       {
           params.set("orientation","landscape");
           mCamera.setDisplayOrientation(0);
           params.setRotation(0);
       }

        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }catch (IOException e){
            Log.d(TAG, "Error setting camera preview :" + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty . take care of releasing the camera where to draw the priew.
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


        if(mHolder.getSurface() == null){
            // preview surface does not exist
            return;

        }

        try{
            mCamera.stopPreview();
        }catch (Exception e){
            // ignore: tried to stop a non-existed preview
        }


        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }catch (Exception e){
            Log.d(TAG, "Error starting camera preview" + e.getMessage());
        }
    }

    



}
