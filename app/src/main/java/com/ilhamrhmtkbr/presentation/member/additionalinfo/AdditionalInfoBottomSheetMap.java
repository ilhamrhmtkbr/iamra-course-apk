package com.ilhamrhmtkbr.presentation.member.additionalinfo;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ilhamrhmtkbr.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AdditionalInfoBottomSheetMap extends BottomSheetDialogFragment implements LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private static final String TAG = "MapBottomSheet";

    private MapView map;
    private SearchView searchAddress;
    private FloatingActionButton fabMyLocation, fabConfirm;

    private MyLocationNewOverlay myLocationOverlay;
    private Marker selectedMarker;
    private LocationManager locationManager;
    private Geocoder geocoder;

    private String selectedAddress = "";
    private double selectedLat = 0;
    private double selectedLng = 0;

    // Callback interface untuk kirim data balik ke fragment
    public interface OnLocationSelectedListener {
        void onLocationSelected(String address, double lat, double lng);
    }

    private OnLocationSelectedListener locationSelectedListener;

    public static AdditionalInfoBottomSheetMap newInstance() {
        return new AdditionalInfoBottomSheetMap();
    }

    public static AdditionalInfoBottomSheetMap newInstance(double lat, double lng) {
        AdditionalInfoBottomSheetMap fragment = new AdditionalInfoBottomSheetMap();
        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        fragment.setArguments(args);
        return fragment;
    }

    // Method untuk set listener dari fragment parent
    public void setOnLocationSelectedListener(OnLocationSelectedListener listener) {
        this.locationSelectedListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());
        geocoder = new Geocoder(requireContext(), Locale.getDefault());
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_member_additional_info_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        map = view.findViewById(R.id.map);
        searchAddress = view.findViewById(R.id.search_address);
        fabMyLocation = view.findViewById(R.id.fab_my_location);
        fabConfirm = view.findViewById(R.id.fab_confirm);

        setupMap();
        setupSearch();
        setupFabButtons();
        checkLocationPermission();
    }

    private void setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);

        // Get lat/lng dari arguments (kalau ada)
        double lat = -6.2088; // Default Jakarta
        double lng = 106.8456;

        if (getArguments() != null) {
            lat = getArguments().getDouble("lat", -6.2088);
            lng = getArguments().getDouble("lng", 106.8456);
        }

        // Set initial location
        GeoPoint startPoint = new GeoPoint(lat, lng);
        map.getController().setZoom(15.0);
        map.getController().setCenter(startPoint);

        // Setup my location overlay
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), map);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableFollowLocation();
        map.getOverlays().add(myLocationOverlay);

        // Setup map click listener
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                onMapClicked(geoPoint);
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                return false;
            }
        };

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        map.getOverlays().add(0, mapEventsOverlay);
    }

    private void setupSearch() {
        searchAddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                searchAddress.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setupFabButtons() {
        // FAB My Location
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyLocation();
            }
        });

        // FAB Confirm
        fabConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLocation();
            }
        });
    }

    private void onMapClicked(GeoPoint geoPoint) {
        selectedLat = geoPoint.getLatitude();
        selectedLng = geoPoint.getLongitude();

        // Add/update marker
        addMarker(geoPoint);

        // Get address from coordinates
        getAddressFromCoordinates(selectedLat, selectedLng);
    }

    private void addMarker(GeoPoint geoPoint) {
        // Remove old marker if exists
        if (selectedMarker != null) {
            map.getOverlays().remove(selectedMarker);
        }

        // Create new marker
        selectedMarker = new Marker(map);
        selectedMarker.setPosition(geoPoint);
        selectedMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        selectedMarker.setTitle("Lokasi Terpilih");

        // Custom icon (optional - uncomment kalau mau pakai icon custom)
        // Drawable customIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_custom_marker);
        // selectedMarker.setIcon(customIcon);

        map.getOverlays().add(selectedMarker);
        map.invalidate();
    }

    private void searchLocation(String query) {
        if (query.isEmpty()) {
            Toast.makeText(requireContext(), "Masukkan alamat yang ingin dicari", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double lat = address.getLatitude();
                double lng = address.getLongitude();

                GeoPoint geoPoint = new GeoPoint(lat, lng);
                map.getController().animateTo(geoPoint);
                map.getController().setZoom(17.0);

                // Set as selected location
                selectedLat = lat;
                selectedLng = lng;
                selectedAddress = getFullAddress(address);

                addMarker(geoPoint);

                Toast.makeText(requireContext(), "Lokasi ditemukan!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Geocoding error: " + e.getMessage());
            Toast.makeText(requireContext(), "Error mencari lokasi", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMyLocation() {
        if (checkLocationPermission()) {
            Location location = myLocationOverlay.getLastFix();

            if (location != null) {
                GeoPoint myLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                map.getController().animateTo(myLocation);
                map.getController().setZoom(17.0);

                // Set as selected location
                selectedLat = location.getLatitude();
                selectedLng = location.getLongitude();

                addMarker(myLocation);
                getAddressFromCoordinates(selectedLat, selectedLng);

                Toast.makeText(requireContext(), "Lokasi saat ini", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Menunggu GPS...", Toast.LENGTH_SHORT).show();
                requestLocationUpdates();
            }
        }
    }

    private void getAddressFromCoordinates(double lat, double lng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            if (addresses != null && !addresses.isEmpty()) {
                selectedAddress = getFullAddress(addresses.get(0));

                if (selectedMarker != null) {
                    selectedMarker.setSnippet(selectedAddress);
                    selectedMarker.showInfoWindow();
                }

                Log.d(TAG, "Address: " + selectedAddress);
            }
        } catch (IOException e) {
            Log.e(TAG, "Reverse geocoding error: " + e.getMessage());
            selectedAddress = "Lat: " + lat + ", Lng: " + lng;
        }
    }

    private String getFullAddress(Address address) {
        StringBuilder fullAddress = new StringBuilder();

        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            fullAddress.append(address.getAddressLine(i));
            if (i < address.getMaxAddressLineIndex()) {
                fullAddress.append(", ");
            }
        }

        return fullAddress.toString();
    }

    private void confirmLocation() {
        if (selectedAddress.isEmpty()) {
            Toast.makeText(requireContext(), "Pilih lokasi terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kirim data balik ke fragment parent
        if (locationSelectedListener != null) {
            locationSelectedListener.onLocationSelected(selectedAddress, selectedLat, selectedLng);
        }

        Toast.makeText(requireContext(), "Lokasi dipilih: " + selectedAddress, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST
            );
            return false;
        }
        return true;
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myLocationOverlay.enableMyLocation();
                myLocationOverlay.enableFollowLocation();
                goToMyLocation();
            } else {
                Toast.makeText(requireContext(), "Permission lokasi ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        GeoPoint myLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
        map.getController().animateTo(myLocation);

        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                View bottomSheet = bottomSheetDialog.findViewById(
                        com.google.android.material.R.id.design_bottom_sheet
                );

                if (bottomSheet != null) {
                    bottomSheet.setBackgroundResource(R.drawable.bg_bottom_sheet);

                    BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    behavior.setSkipCollapsed(true);
                }
            }
        });

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause();
        }
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (myLocationOverlay != null) {
            myLocationOverlay.disableMyLocation();
        }
    }
}