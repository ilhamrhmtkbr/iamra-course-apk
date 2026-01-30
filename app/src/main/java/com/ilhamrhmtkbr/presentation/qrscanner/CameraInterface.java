package com.ilhamrhmtkbr.presentation.qrscanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public interface CameraInterface extends QrCodeInterface{
    int CAMERA_PERMISSION_REQUEST_CODE = 100;

    default boolean checkCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    default void requestCameraPermission(AppCompatActivity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
    }

    default void startCamera(
            AppCompatActivity activity,
            ExecutorService cameraExecutor,
            BarcodeScanner barcodeScanner,
            PreviewView previewView) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(activity);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider, activity, cameraExecutor, barcodeScanner, previewView);
                } catch (ExecutionException | InterruptedException e) {
                    Toast.makeText(activity, "Camera initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, ContextCompat.getMainExecutor(activity));
    }
}
