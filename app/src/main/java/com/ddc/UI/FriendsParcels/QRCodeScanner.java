package com.ddc.UI.FriendsParcels;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ddc.R;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class QRCodeScanner extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private BarcodeReader barcodeReader;
    private FriendsParcelsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);

        // hide the Action Bar and the Status bar
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            getActionBar().hide();
        }
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        viewModel = ViewModelProviders.of(this).get(FriendsParcelsViewModel.class);
    }

    // single barcode scanned
    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.pauseScanning();
        barcodeReader.playBeep();
        if (barcode.displayValue.equals("123"))

            barcodeReader.resumeScanning();
    }

    // multiple barcodes scanned
    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    // barcode scanned from bitmap image
    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    // scan error
    @Override
    public void onScanError(String s) {

    }

    // camera permission denied
    @Override
    public void onCameraPermissionDenied() {

    }
}
