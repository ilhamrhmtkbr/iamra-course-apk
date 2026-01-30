package com.ilhamrhmtkbr.presentation.qrscanner;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.databinding.ActivityQrScannerBinding;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QrScannerActivity extends AppCompatActivity
        implements CameraInterface, QrCodeInterface {

    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private BarcodeScanner barcodeScanner;
    private boolean isScanning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityQrScannerBinding binding = ActivityQrScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RedirectUtil.afterOnBackPressed(this, GuestActivity.class);
        previewView = findViewById(R.id.preview_view);
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Setup ML Kit Barcode Scanner (QR Code only)
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        barcodeScanner = BarcodeScanning.getClient(options);

        // Check camera permission
        if (checkCameraPermission(this)) {
            startCamera(this, cameraExecutor, barcodeScanner, previewView);
        } else {
            requestCameraPermission(this);
        }

        binding.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectUtil.redirectToActivity(view.getContext(), GuestActivity.class);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera(this, cameraExecutor, barcodeScanner, previewView);
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        if (barcodeScanner != null) {
            barcodeScanner.close();
        }
    }

    @Override
    public void setScanning(boolean value) {
        this.isScanning = value;
    }

    @Override
    public boolean getScanning() {
        return this.isScanning;
    }
}