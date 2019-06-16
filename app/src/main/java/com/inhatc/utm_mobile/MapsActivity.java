package com.inhatc.utm_mobile;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MapsActivity extends FragmentActivity
        implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private ImageButton btnOpt;
    private ImageButton btnMak;
    private KmlLayer layer1, layer2, layer2_2, layer3, layer4, layer5, layer6, layer6_2;
    private int chkInt = 63;
    private String makertitle;
    private String makermessage;
    //db
    SQLiteDatabase DB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //DB;
        DB = this.openOrCreateDatabase("mobileDB",MODE_PRIVATE,null);

        try {
            DB.execSQL("Create table dataf (" + "_id integer primary key autoincrement,"
                    + "Title text not null," + "Message text not null," + "latitude text not null , longitude text not null,"
                    + "thatdate DATETIME DEFAULT CURRENT_TIMESTAMP);");
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "UTM Start ", Toast.LENGTH_LONG).show();

        }


        btnOpt = (ImageButton)findViewById(R.id.btnOpt1);
        btnOpt.setOnClickListener(this);
        btnMak = (ImageButton)findViewById(R.id.btnMak1);
        btnMak.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setPadding(0, 150, 0, 0);

        // Add a marker in Sydney and move the camera
        LatLng inhatc = new LatLng(37.448344, 126.657474);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(inhatc));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(7));

        setObject(mMap);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.getUiSettings().setCompassEnabled(true);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        MarkerOptions mOptions = new MarkerOptions();
/*        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

       mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        Cursor cursor;
        cursor = DB.rawQuery("SELECT * FROM dataf", null);
        //Double latitude = point.latitude; // 위도
        //Double longitude = point.longitude; // 경도
        if(cursor !=null) {
            while (cursor.moveToNext()) {

                String title = cursor.getString(1);
                String message = cursor.getString(2);
                Double latitude = Double.parseDouble(cursor.getString(3));
                Double longitude = Double.parseDouble(cursor.getString(4));
                mOptions.title("This is ");
                mOptions.snippet(" Title : " + title + " / Text : " + message);
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                mOptions.draggable(true);
                mOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow));
                // 마커(핀) 추가
                //markeron=
                mMap.addMarker(mOptions);
                //Toast.makeText(getApplicationContext(), "MarKer Done", Toast.LENGTH_LONG).show();
            }
            cursor.close();
        }


/*        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Marker marker = mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        return false;
                    }
                });
                    if(Math.abs(marker.getPosition().latitude - latLng.latitude) < 0.05 && Math.abs(marker.getPosition().longitude - latLng.longitude) < 0.05)
                    {
                        Toast.makeText(MapActivity.this, "got clicked", Toast.LENGTH_SHORT).show(); //do some stuff
                    }
           }
        });*/


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(final Marker marker) {
                //mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                //   @Override
                //   public void onMapLongClick(LatLng latLng) {
                String adios = marker.getSnippet().trim();
                int idx = adios.indexOf("/");
                String adios_c = adios.substring(0,idx);
                int idx_c = adios.indexOf(":");
                String adios_result = adios_c.substring(idx_c+1);

                marker.remove();
                String table = "dataf";
                String whereClause = "Title=?";
                String[] whereArgs = new String[] { adios_result.trim() };
                DB.delete(table,whereClause,whereArgs);
                Toast.makeText(getApplicationContext(), ""+adios_result+" is Drop", Toast.LENGTH_LONG).show();
                //  }
                // });
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });


    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Toast.makeText(this, "Current location: (" + latitude + ", " + longitude + ")", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public void setObject(GoogleMap googleMap){
        mMap = googleMap;
        try {
            layer1 = new KmlLayer(mMap, R.raw.layer1, getApplicationContext());
            layer1.addLayerToMap();
            layer2 = new KmlLayer(mMap, R.raw.layer2, getApplicationContext());
            layer2.addLayerToMap();
            layer2_2 = new KmlLayer(mMap, R.raw.layer2_2, getApplicationContext());
            layer2_2.addLayerToMap();
            layer3 = new KmlLayer(mMap, R.raw.layer3, getApplicationContext());
            layer3.addLayerToMap();
            layer4 = new KmlLayer(mMap, R.raw.layer4, getApplicationContext());
            layer4.addLayerToMap();
            layer5 = new KmlLayer(mMap, R.raw.layer5, getApplicationContext());
            layer5.addLayerToMap();
            layer6 = new KmlLayer(mMap, R.raw.layer6, getApplicationContext());
            layer6.addLayerToMap();
            layer6_2 = new KmlLayer(mMap, R.raw.layer6_2, getApplicationContext());
            layer6_2.addLayerToMap();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnOpt) {
            Intent menuIntent = new Intent(MapsActivity.this, MenuActivity.class);
            menuIntent.putExtra("chkVar", Integer.toString(chkInt));
            setResult(RESULT_OK, menuIntent);
            startActivityForResult(menuIntent, 1);
        } else if (view == btnMak) {
            Toast.makeText(this, "Marker button clicked", Toast.LENGTH_SHORT).show();
        }

        // if(chk) {
        //map
        Toast.makeText(getApplicationContext(), "MarKer Ready", Toast.LENGTH_LONG).show();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(final LatLng point) {

                final CustomDialog Dialog = new CustomDialog(MapsActivity.this);
                Dialog.setCancelable(false);
                Dialog.setDialogListener(new CustomDialog.CustomDialogListener() {
                    @Override
                    public void onPositiveClicked(String t, String m) {
                        makertitle = t;
                        makermessage = m;

                        //db
                        Cursor cursor = DB.rawQuery("SELECT * FROM dataf", null);
                        //Double latitude = point.latitude; // 위도
                        //Double longitude = point.longitude; // 경도
                        if(cursor != null && cursor.getCount() != 0) {
                            while (cursor.moveToNext()) {

                                String title = cursor.getString(1);
                                if(title.equals(t)){
                                    Toast.makeText(getApplicationContext(), " Aleady exist : "+title+"", Toast.LENGTH_LONG).show();
                                    cursor.close();
                                    break;
                                }
                                else if(!title.equals(t)){
                                    MarkerOptions mOptions = new MarkerOptions();
                                    // 마커 타이틀
                                    mOptions.title("This is ");
                                    Double latitude = point.latitude; // 위도
                                    Double longitude = point.longitude; // 경도
                                    // 마커의 스니펫(간단한 텍스트) 설정
                                    //mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                                    mOptions.snippet(" Title : "+makertitle+" / Text : "+makermessage);
                                    // LatLng: 위도 경도 쌍을 나타냄
                                    mOptions.position(new LatLng(latitude, longitude));
                                    mOptions.draggable(true);
                                    mOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow));
                                    // 마커(핀) 추가
                                    mMap.addMarker(mOptions);
                                    Toast.makeText(getApplicationContext(), "MarKer Done", Toast.LENGTH_LONG).show();
                                    mMap.setOnMapClickListener(null);

                                    ContentValues insertValue = new ContentValues();
                                    insertValue.put("Title", t);
                                    insertValue.put("Message", m);
                                    insertValue.put("latitude", latitude);
                                    insertValue.put("longitude", longitude);
                                    DB.insert("dataf", null, insertValue);
                                    cursor.close();
                                    break;
                                }
                            }
                        }
                        else {
                            MarkerOptions mOptions = new MarkerOptions();
                            // 마커 타이틀
                            mOptions.title("This is ");
                            Double latitude = point.latitude; // 위도
                            Double longitude = point.longitude; // 경도
                            // 마커의 스니펫(간단한 텍스트) 설정
                            //mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                            mOptions.snippet(" Title : "+makertitle+" / Text : "+makermessage);
                            // LatLng: 위도 경도 쌍을 나타냄
                            mOptions.position(new LatLng(latitude, longitude));
                            mOptions.draggable(true);
                            mOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow));
                            // 마커(핀) 추가
                            mMap.addMarker(mOptions);
                            Toast.makeText(getApplicationContext(), "MarKer Done", Toast.LENGTH_LONG).show();
                            mMap.setOnMapClickListener(null);

                            ContentValues insertValue = new ContentValues();
                            insertValue.put("Title", t);
                            insertValue.put("Message", m);
                            insertValue.put("latitude", latitude);
                            insertValue.put("longitude", longitude);
                            DB.insert("dataf", null, insertValue);
                            cursor.close();

                        }
                        cursor.close();
                    }
                    @Override
                    public void onNegativeClicked() {
                    }

                });
                Dialog.show();
            }});
        // }
        //else{
        //  chk = true;
        // }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            String strData = data.getStringExtra("chkVar");
            try {
                setLayer(strData);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Set kml Layer", Toast.LENGTH_SHORT).show();
        }
    }

    public void setLayer(String chkVar) throws IOException, XmlPullParserException {
        chkInt = Integer.parseInt(chkVar);

        layer1.removeLayerFromMap();
        layer2.removeLayerFromMap();
        layer2_2.removeLayerFromMap();
        layer3.removeLayerFromMap();
        layer4.removeLayerFromMap();
        layer5.removeLayerFromMap();
        layer6.removeLayerFromMap();
        layer6_2.removeLayerFromMap();

        if ((chkInt & 1) == 1) layer1.addLayerToMap();
        if ((chkInt & 2) == 2) {layer2.addLayerToMap();layer2_2.addLayerToMap();}
        if ((chkInt & 4) == 4) layer3.addLayerToMap();
        if ((chkInt & 8) == 8) layer4.addLayerToMap();
        if ((chkInt & 16) == 16) layer5.addLayerToMap();
        if ((chkInt & 32) == 32) {layer6.addLayerToMap();layer6_2.addLayerToMap();}
    }
}
