package com.ddc.UI.FriendsParcels;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ddc.Model.Parcel.Parcel;
import com.ddc.R;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class QRCodeScanner extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private BarcodeReader barcodeReader;
    private FriendsParcelsViewModel viewModel;
    private String parcelID;

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

        Bundle bundle = getIntent().getExtras();
        parcelID = bundle.getString("ParcelID");

        TextView tv = findViewById(R.id.qr_code_no_tv);
        tv.setText(tv.getText() + " " + parcelID);
    }

    // single barcode scanned
    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.pauseScanning();
        barcodeReader.playBeep();
        try {
            Parcel parcel = viewModel.searchParcelByID(parcelID);
            if (parcel.getParcelID().equals(barcode.displayValue)) {
                viewModel.deliverHaveTheParcel(parcel);
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "שגיאה. נסה שוב מאוחר יותר", Toast.LENGTH_LONG).show();
            finish();
        }
        Toast.makeText(getBaseContext(), "אופס... זה לא החבילה הנכונה", Toast.LENGTH_LONG).show();
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
