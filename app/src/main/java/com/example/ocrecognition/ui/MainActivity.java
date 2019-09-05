package com.example.ocrecognition.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ocrecognition.R;
import com.example.ocrecognition.ui.camera.CameraActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.camera_cardview) MaterialCardView mCameraCardview;
    @BindView(R.id.gallery_cardview) MaterialCardView mGalleryCardview;

    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };
    private static final int REQUEST_CODE_PERMISSIONS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (hasPermissions())
        {

        }
        else
        {
            finish();
        }
    }

    private boolean hasPermissions()
    {
        List<String> listPermissionNeeded = new ArrayList<>();
        for (String permission : PERMISSIONS)
        {
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED)
                listPermissionNeeded.add(permission);
        }

        if (!listPermissionNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),REQUEST_CODE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_CODE_PERMISSIONS)
        {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;

            for (int i = 0; i < grantResults.length; i++)
            {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                {
                    permissionResults.put(permissions[i],grantResults[i]);
                    deniedCount++;
                }
            }

            if (deniedCount != 0)
            {
                for (Map.Entry<String, Integer> entry: permissionResults.entrySet())
                {
                    if  (ActivityCompat.shouldShowRequestPermissionRationale(this,entry.getKey()))
                    {
                        showDialog();
                    }
                }
            }

        }
    }

    private void showDialog(String title, String message, DialogInterface.OnCancelListener listener)
    {

    }

    @OnClick(R.id.camera_cardview)
    public void onCameraCardviewClicked()
    {
        Intent camera = new Intent(this, CameraActivity.class);
        startActivity(camera);
    }

    @OnClick(R.id.gallery_cardview)
    public void onGalleryCardviewClicked()
    {
    }
}
