package com.example.ocrecognition.ui.camera;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.ocrecognition.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CameraActivity extends AppCompatActivity
{
    @BindView(R.id.camera_textureview) TextureView mCameraTextureview;
    private static final int REQUEST_CODE_PERMISION = 20;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mUnbinder = ButterKnife.bind(this);
    }

}
