package com.procus.simpleuserregistration.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.procus.simpleuserregistration.R;
import com.procus.simpleuserregistration.models.DatabaseHandler;
import com.procus.simpleuserregistration.models.User;

/**
 * Created by Peter on 3.7.17.
 */

public class UserProfileActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String USER_DETAIL_ID = "id";
    private Integer userDetailId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_user_profile);
        Intent intent = getIntent ();
        String id = intent.getStringExtra(USER_DETAIL_ID);
        try {
            userDetailId = Integer.parseInt(id);
        } catch(NumberFormatException e) {
            System.out.println("Could not parse " + e);
        }
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(userDetailId != null) {
            User user = DatabaseHandler.getUserById(userDetailId);
            if(user != null) {
                fillUserInfo(user);
            }
        }
    }

    private void fillUserInfo(User user){
        if(user.getPhoto() != null && !user.getPhoto().isEmpty()) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(user.getPhoto(), options);
            imageView.setImageBitmap(bitmap);
        }
        if(user.getName() != null && user.getSurname() != null){
            TextView username = (TextView) findViewById(R.id.profileName);
            username.setText(user.getName() + " " + user.getSurname());
        }
        if(user.getBirthday() != null && !user.getBirthday().isEmpty()){
            TextView birthday = (TextView) findViewById(R.id.profileBirthday);
            birthday.setText(user.getBirthday());
        }
        if(user.getRegisterTime() != null && !user.getRegisterTime().isEmpty()){
            TextView regtime = (TextView) findViewById(R.id.profileRegTime);
            regtime.setText(user.getRegisterTime());
        }
        if(user.getDevId() != null && !user.getDevId().isEmpty()){
            TextView devID = (TextView) findViewById(R.id.profileDevID);
            devID.setText(user.getDevId());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(userDetailId != null) {
            User user = DatabaseHandler.getUserById(userDetailId);
            if(user != null && user.getLatitude() != null && user.getLongitude() != null) {
                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(user.getLatitude(), user.getLongitude()))
                        .zoom(16)
                        .bearing(0)
                        .tilt(45)
                        .build();

                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(user.getLatitude(), user.getLongitude())));
            }
        }
    }
}
