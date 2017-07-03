package com.procus.simpleuserregistration.fragments;


import android.Manifest;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.procus.simpleuserregistration.R;
import com.procus.simpleuserregistration.models.DatabaseHandler;
import com.procus.simpleuserregistration.models.User;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Peter on 2.7.17.
 */

public class RegistrationFragment extends Fragment implements View.OnClickListener, LocationListener {

    private Button addPhotoButton;
    private Button registerPhotoButton;
    private EditText nameText;
    private EditText surnameText;
    private EditText dateText;
    private static int RESULT_LOAD_IMAGE = 1;
    private String bitmapToSave;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private LocationManager locationManager;
    private DatabaseHandler db;
    private ImageView imageView;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_registration, container, false);
        return rootView;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = DatabaseHandler.getInstance(getContext());
        db.allUsers();
        locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startLocationUpdate();
            }
        }
        imageView = (ImageView) getView().findViewById(R.id.photoImageView);
        addPhotoButton = (Button) getView().findViewById(R.id.photoButton);
        addPhotoButton.setOnClickListener(this);
        registerPhotoButton = (Button) getView().findViewById(R.id.registerButton);
        registerPhotoButton.setOnClickListener(this);
        dateText = (EditText) getView().findViewById(R.id.birthdayText);
        dateText.setOnClickListener(this);
        nameText = (EditText) getView().findViewById(R.id.nameText);
        surnameText = (EditText) getView().findViewById(R.id.surnameText);
    }


    private void addPhoto() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            loadPhoto();
        }

    }

    private void loadPhoto(){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoButton:
                addPhoto();
                break;
            case R.id.birthdayText:
                datePicker();
                break;
            case R.id.registerButton:
                register();
                break;
        }
    }

    private void register() {
        Boolean valid = false;
        if (TextUtils.isEmpty(nameText.getText())) {
            showAlert("No name", "Insert your name");
        } else if (TextUtils.isEmpty(surnameText.getText())) {
            showAlert("No surname", "Insert your surname");
        } else {
            valid = true;
        }
        if (valid) {
            final User user = new User();
            user.setBirthday(dateText.getText().toString());
            user.setName(nameText.getText().toString());
            user.setSurname(surnameText.getText().toString());
            user.setPhoto(bitmapToSave);

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
            user.setRegisterTime(format.format(new Date().getTime()));
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            String android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            user.setDevId(android_id);

            dateText.setText("");
            nameText.setText("");
            surnameText.setText("");
            bitmapToSave = null;
            imageView.setImageResource(R.drawable.no_image);

            db.addUser(user);


        }
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void datePicker() {

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getActivity().getSupportFragmentManager(), "date");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);

            bitmapToSave = picturePath;
            imageView.setImageBitmap(bitmap);

        }


    }


    private void startLocationUpdate() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 1, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    startLocationUpdate();

                }
                return;
            }
            case 1:{
                loadPhoto();
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}


