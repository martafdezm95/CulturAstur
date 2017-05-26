package es.uniovi.imovil.user.CulturAstur.Activities;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.uniovi.imovil.user.CulturAstur.Fragments.ArticleListFragment;
import es.uniovi.imovil.user.CulturAstur.Fragments.ArticleShowFragment;
import es.uniovi.imovil.user.CulturAstur.Models.Article;
import es.uniovi.imovil.user.CulturAstur.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener , ArticleListFragment.Callbacks{


    private DrawerLayout mDrawerLayout = null;
    private NavigationView navigation  = null;
    private ActionBarDrawerToggle mDrawerToggle = null;
    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    LatLng loc;
    static final int PICK_CONTACT_REQUEST = 0;
    public static boolean mTwoPanes = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_maps);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Inicializamos toolbar y drawer
        initInstancesDrawer();

        if (findViewById(R.id.article_details_container) != null) {
            mTwoPanes = true;
        }

        //Inicializamos el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient= new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
    }
    private void initInstancesDrawer() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_design_support_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, mToolbar,
                R.string.app_name, R.string.app_name
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        navigation = (NavigationView) findViewById(R.id.navigation_drawer);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_drawer_home:
                        finish(); //exit y volvemos a mainactivity
                        break;
                    case R.id.navigation_drawer_maps:
                        Toast.makeText(getApplicationContext(),R.string.maps_already_maps,Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_favourite:
                        setResult(2, null); //se procesa en onActivityResult de Mainactivity
                        finish();
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addMarkers(ArticleListFragment.articlesModelList);
    }

    //Añadimos marcadores de todos y cada uno de los elementos de la lista ue se pasa por parámetro
    private void addMarkers(List<Article> articlesList){
        for (int i = 0; i< articlesList.size(); i++ ){
            Double lat = articlesList.get(i).getLat();
            Double lng = articlesList.get(i).getLng();
            if(lng != null && lng != null){
                LatLng coordinates = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(coordinates).title(articlesList.get(i).getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.museum_marker)));
            }
        }
    }

    @Override
    public void onStart(){
        googleApiClient.connect();
        super.onStart();
    }
    @Override
    public void onStop(){
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    //Controlamos que el dispositivo tenga conectado el GPS

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,getApplicationContext(),this)) {
            fetchLocationData();
        }
        else {
            requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), this);
        }
    }

    public void requestPermission(String strPermission,int perCode,Context _c,Activity _a){
        Toast.makeText(getApplicationContext(),R.string.toast_maps_gps,Toast.LENGTH_LONG).show();
        ActivityCompat.requestPermissions(_a,new String[]{strPermission},perCode);

    }

    public static boolean checkPermission(String strPermission, Context _c, Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocationData();
                } else {
                    Toast.makeText(getApplicationContext(),R.string.toast_maps_permission,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    private void fetchLocationData()
    {
        //Una vez se han concedido los permisos
        try{
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            loc = new LatLng(0, 0);
            if (location == null) {
                Toast.makeText(getApplicationContext(),R.string.toast_maps_gps2,Toast.LENGTH_LONG).show();
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }
            else {
                loc = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(loc)
                        .title(getResources().getString(R.string.you_are_here))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15f));
            }
        }
        catch (Exception e) {}
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onCourseSelected(int pos) {
        Article article = ArticleListFragment.articlesModelList.get(pos);
        Double lat = article.getLat();
        Double lng = article.getLng();
        if(lng != null && lng != null){
            //Almamacenamos las coordenadas
            LatLng coordinate = new LatLng(lat, lng);
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    coordinate, 15);
            mMap.animateCamera(location);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search_toolbar:
                Intent i = new Intent(MapsActivity.this, FilterActivity.class);
                i.putExtra("parentId", 2);
                startActivityForResult(i, PICK_CONTACT_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //En caso de etar en un dispositivo tipo tablet contamos con la lista de elementos,
        //tras filtrar la lista obtenemos dichos elementos y los mostramos el resutado tanto en el listview
        // como en el mapa
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK && data != null) {
            ArrayList<Article> myList = (ArrayList<Article>) data.getSerializableExtra("myArticle");
            int screenSize = getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;
            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                ArticleListFragment fragment = (ArticleListFragment) fragmentManager.findFragmentById(R.id.article_list_map);
                fragment.setArticleList(myList);
            }
            mMap.clear(); //Borramos marcadores previos
            addMarkers(myList); //Añadimos los nuevos
        }
    }
}
