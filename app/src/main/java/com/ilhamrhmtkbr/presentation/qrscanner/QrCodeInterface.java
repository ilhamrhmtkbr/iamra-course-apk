package com.ilhamrhmtkbr.presentation.qrscanner;

import android.os.Bundle;
import android.util.Size;
import android.widget.Toast;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

import java.util.concurrent.ExecutorService;

public interface QrCodeInterface extends CertificateInterface{
    default void handleQRCodeResult(String qrValue, AppCompatActivity activity) {
        // Stop scanning to prevent multiple triggers
        setScanning(false);

        // Extract certificate ID from URL
        // Expected format: "https://student.course.iamra.site/certificates/123kdkkdkdk"
        String certificateId = extractCertificateId(qrValue);

        if (certificateId != null) {
            Bundle bundle = new Bundle();
            bundle.putString("navigate_to", "certificates");
            bundle.putString("certificate_id", certificateId);
            RedirectUtil.redirectToActivityWithBundle(activity, GuestActivity.class, bundle);
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Invalid certificate QR code", Toast.LENGTH_SHORT).show();
                    setScanning(true);
                }
            });
        }
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    default void bindPreview(
            ProcessCameraProvider cameraProvider,
            AppCompatActivity activity,
            ExecutorService cameraExecutor,
            BarcodeScanner barcodeScanner,
            PreviewView previewView) {

        // Preview
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Image Analysis untuk QR scanning
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(cameraExecutor, imageProxy -> {
            if (!getScanning()) {
                imageProxy.close();
                return;
            }

            @androidx.camera.core.ExperimentalGetImage
            android.media.Image mediaImage = imageProxy.getImage();

            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(mediaImage,
                        imageProxy.getImageInfo().getRotationDegrees());

                barcodeScanner.process(image)
                        .addOnSuccessListener(barcodes -> {
                            for (Barcode barcode : barcodes) {
                                String rawValue = barcode.getRawValue();
                                if (rawValue != null) {
                                    handleQRCodeResult(rawValue, activity);
                                    break;
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                        })
                        .addOnCompleteListener(task -> imageProxy.close());
            } else {
                imageProxy.close();
            }
        });

        // TAMBAHKAN INI - Pilih kamera belakang
        androidx.camera.core.CameraSelector cameraSelector =
                new androidx.camera.core.CameraSelector.Builder()
                        .requireLensFacing(androidx.camera.core.CameraSelector.LENS_FACING_BACK)
                        .build();

        try {
            // Unbind semua use case sebelumnya
            cameraProvider.unbindAll();

            // TAMBAHKAN INI - Bind camera ke lifecycle
            cameraProvider.bindToLifecycle(
                    activity,           // LifecycleOwner
                    cameraSelector,     // CameraSelector
                    preview,           // Preview use case
                    imageAnalysis      // ImageAnalysis use case
            );

        } catch (Exception e) {
            Toast.makeText(activity, "Camera binding failed: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    void setScanning(boolean value);
    boolean getScanning();
}
