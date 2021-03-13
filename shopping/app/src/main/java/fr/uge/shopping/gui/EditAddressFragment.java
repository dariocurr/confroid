package fr.uge.shopping.gui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.Task;
import fr.uge.shopping.*;
import fr.uge.shopping.R;
import fr.uge.shopping.model.ShippingAddress;

import java.io.IOException;
import java.util.Locale;

public class EditAddressFragment extends Fragment {
    private String infoName;
    private PreferencesManager preferencesManager;
    private ShippingAddress address;


    private EditText name;
    private EditText street;
    private EditText city;
    private EditText country;
    private Switch geolocation;
    private ProgressBar progressBar;
    private Button back;
    private Button save;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private GpsUtils gpsUtils;
    private boolean gpsAvalaible;
    private boolean firstGpsAsk;
    private double latitude = 0.0;
    private double longitude = 0.0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.infoName = getArguments().getString("name");
        this.preferencesManager = PreferencesManager.getPreferencesManager(getContext());
        this.address = this.preferencesManager.getShoppingInfo(this.infoName).address;

        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        this.locationRequest = LocationRequest.create();
        this.gpsUtils = new GpsUtils(this.getActivity());


        return inflater.inflate(R.layout.edit_address_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationRequest.setInterval(20 * 1000);
        this.locationRequest.setFastestInterval(5 * 1000);

        this.gpsUtils.turnGPSOn(isGPSEnable -> this.gpsAvalaible = isGPSEnable);
        this.firstGpsAsk = true;

        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setIndeterminate(true);
                        parseCoordinates(latitude, longitude);
                        if (fusedLocationClient != null) {
                            fusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        this.name = getView().findViewById(R.id.editAddressFragmentName);
        this.name.setText(this.address.name);

        this.street = getView().findViewById(R.id.editAddressFragmentStreet);
        this.street.setText(this.address.street);

        this.city = getView().findViewById(R.id.editAddressFragmentCity);
        this.city.setText(this.address.city);

        this.country = getView().findViewById(R.id.editAddressFragmentCountry);
        this.country.setText(this.address.country);

        this.back = getView().findViewById(R.id.editAddressFragmentBack);
        this.back.setOnClickListener( ev -> {
            close();
        });
        this.save = getView().findViewById(R.id.editAddressFragmentSave);
        this.save.setOnClickListener( ev -> {
            ((EditActivity) getActivity()).updateAddress(
                    new ShippingAddress(
                            name.getText().toString(),
                            street.getText().toString(),
                            city.getText().toString(),
                            country.getText().toString())
            );
            close();
        });

        this.geolocation = getView().findViewById(R.id.geolocationSwitch);
        this.geolocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                enableTexts(false);
                askGeolocation();
            } else {
                enableTexts(true);
            }
        });

        this.progressBar = getView().findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
    }


    private void close() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();
    }

    private void enableTexts(boolean enabled) {
        this.street.setEnabled(enabled);
        this.city.setEnabled(enabled);
        this.country.setEnabled(enabled);
    }

    private void askGeolocation() {
        // check permission
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request for permission
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GpsUtils.GPS_LOCATION_CODE);
            this.geolocation.setChecked(false);
        } else {
            // already permission granted
            this.gpsUtils.turnGPSOn(isGPSEnable -> {
                this.gpsAvalaible = isGPSEnable;
            });
            getGeolocationPosition();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case GpsUtils.GPS_LOCATION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getGeolocationPosition();
                } else {
                    Toast.makeText(this.getActivity(), getString(R.string.permissionDenied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GpsUtils.GPS_REQUEST_CODE) {
                this.gpsAvalaible = true; // flag maintain before get location
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(this.firstGpsAsk) {
            this.firstGpsAsk = false;
        } else {
            new GpsUtils(this.getActivity()).turnGPSOn(isGPSEnable -> {
                this.gpsAvalaible = isGPSEnable;
            });
        }

    }

    private void getGeolocationPosition() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.progressBar.setIndeterminate(true);
        Task<Location> lastLocation =  this.fusedLocationClient.getLastLocation();
        lastLocation.addOnSuccessListener(this.getActivity(), location -> {
            if (location != null) {
                this.latitude = location.getLatitude();
                this.longitude = location.getLongitude();
                //Log.i("geo123", String.format(Locale.US, "%s -- %s", latitude, longitude));
                parseCoordinates(latitude, longitude);
            } else {
                this.fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        });
    }

    private void parseCoordinates(double latitude, double longitude) {
        Geocoder coder = new Geocoder(this.getActivity(), Locale.US);
        try {
            Address address = coder.getFromLocation(latitude, longitude, 1).stream().findAny().orElse(null);
            if(address != null) {
                this.street.setText(address.getThoroughfare()+", "+address.getFeatureName());
                this.city.setText(address.getLocality());
                this.country.setText(address.getCountryName());

            } else {
                Toast.makeText(this.getActivity(), getString(R.string.geoError), Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            Toast.makeText(this.getActivity(), getString(R.string.geoError), Toast.LENGTH_SHORT).show();
        }
        this.progressBar.setIndeterminate(false);
        this.progressBar.setVisibility(View.INVISIBLE);
    }
}
