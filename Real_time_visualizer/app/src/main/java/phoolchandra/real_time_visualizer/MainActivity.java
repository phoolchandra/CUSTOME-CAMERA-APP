package phoolchandra.real_time_visualizer;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;
    private  CameraPreview mPriview;
    FrameLayout camera_preview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

        // Create a instance of a camera

        //mCamera = getCameraInstance();
        mCamera = Camera.open();

        // create our Preview view and set it as the content of our activity

        mPriview = new CameraPreview(this, mCamera);

        preview.addView(mPriview);



}


}
