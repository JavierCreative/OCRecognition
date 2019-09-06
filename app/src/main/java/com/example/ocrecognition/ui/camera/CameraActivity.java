package com.example.ocrecognition.ui.camera;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

import com.example.ocrecognition.R;
import com.example.ocrecognition.data.TextRecognizerResult;
import com.example.ocrecognition.ui.resulttext.ResultTextActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.gson.Gson;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CameraActivity extends AppCompatActivity
{
    @BindView(R.id.camera_textureview) TextureView mCameraTextureview;
    @BindView(R.id.back_imagebutton) ImageButton mBack;
    @BindView(R.id.analyze_floating_action_button) FloatingActionButton mAnalyze;

    private static final int REQUEST_CODE_PERMISION = 20;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mUnbinder = ButterKnife.bind(this);
        mCameraTextureview.post(() -> {
            startCamera();
        });
    }

    private void startCamera() {
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

        Preview preview = new Preview(previewConfig);

        preview.setOnPreviewOutputUpdateListener(output ->
        {
            ViewGroup viewGroup = (ViewGroup) mCameraTextureview.getParent();
            viewGroup.removeView(mCameraTextureview);
            viewGroup.addView(mCameraTextureview, 0);
            mCameraTextureview.setSurfaceTexture(output.getSurfaceTexture());
            updateTransform();
        });

        ImageCaptureConfig config =
                new ImageCaptureConfig.Builder()
                        .setTargetAspectRatio(new Rational(1,1))
                        .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                        .build();

        ImageCapture capture = new ImageCapture(config);

        mAnalyze.setOnClickListener(view ->
        {
            File file  = new File(getExternalMediaDirs()[0], System.currentTimeMillis() +".jpg");
            capture.takePicture(file, new ImageCapture.OnImageSavedListener() {
                @Override
                public void onImageSaved(@NonNull File file) {
                    startTextRecognition(file);
                }

                @Override
                public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {

                }
            });
        });

        CameraX.bindToLifecycle(this, preview,capture);
    }

    private void startTextRecognition(File file)
    {
        SweetAlertDialog alert = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        alert.setContentText("Analyzing image...");
        alert.setCancelable(false);
        alert.create();
        alert.show();

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bitmapOptions);
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> result = recognizer.processImage(image)
                .addOnSuccessListener(firebaseVisionText -> {
                    alert.dismissWithAnimation();
                    TextRecognizerResult model = new TextRecognizerResult();
                    model.setBloques(firebaseVisionText.getTextBlocks());
                    String blocks = new Gson().toJson(model);
                    SweetAlertDialog success = new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
                    success.setContentText("Your image was Analyzed with success!");
                    success.setNeutralButton("Accept", sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                        Intent results = ResultTextActivity.createIntent(CameraActivity.this,blocks);
                        startActivity(results);
                        CameraActivity.this.finish();
                    });
                    success.show();
                })
                .addOnFailureListener(e -> {
                    alert.dismissWithAnimation();
                    SweetAlertDialog fail = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                    fail.setContentText("Something went wrong...");
                    fail.setNeutralButton("Accept", sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                        CameraActivity.this.finish();
                    });
                });
    }

    private void updateTransform() {
        Matrix matrix = new Matrix();

        float centerX = mCameraTextureview.getWidth() / 2f;
        float centerY = mCameraTextureview.getHeight() / 2f;

        int rotationDegrees = 0;

        switch (mCameraTextureview.getDisplay().getRotation()) {
            case Surface.ROTATION_0:
                rotationDegrees = 0;
                break;
            case Surface.ROTATION_180:
                rotationDegrees = 180;
                break;
            case Surface.ROTATION_270:
                rotationDegrees = 270;
                break;
            case Surface.ROTATION_90:
                rotationDegrees = 90;
                break;
        }

        matrix.postRotate(-(float) rotationDegrees, centerX, centerY);
        mCameraTextureview.setTransform(matrix);
        changeRotation(mBack, rotationDegrees);
    }

    private void changeRotation(View view, int degrees)
    {
        Log.wtf("ROTATE","TRUE");
        RotateAnimation rotateAnimation = new RotateAnimation(0, degrees);
        rotateAnimation.setDuration(1000);
        view.startAnimation(rotateAnimation);
    }

    @OnClick(R.id.back_imagebutton)
    public void onViewClicked()
    {
        this.finish();
    }
}
