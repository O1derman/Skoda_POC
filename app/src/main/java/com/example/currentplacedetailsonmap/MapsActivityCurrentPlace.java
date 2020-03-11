package com.example.currentplacedetailsonmap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static com.example.currentplacedetailsonmap.FoundDestinations.setDestinationView;
import static com.example.currentplacedetailsonmap.SettingActivity.CHECK_BOX;
import static com.example.currentplacedetailsonmap.SettingActivity.SEEK_BAR1;
import static com.example.currentplacedetailsonmap.SettingActivity.SEEK_BAR2;
import static com.example.currentplacedetailsonmap.SettingActivity.SHARED_PREFS;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivityCurrentPlace extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
    private static final int AUTOCOMPLETE_REQUEST_CODE = 12345;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to the Places API.
    private PlacesClient mPlacesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(50.08804, 14.42076);
    private static final int DEFAULT_ZOOM = 8;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private List[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private List<Double> distances1 = new ArrayList<>();
    private List<Double> distances2 = new ArrayList<>();
    private List<Double> times = new ArrayList<>();
    private List<LatLng> chargersLatLng = new ArrayList<>();
    private List<Integer> IDs = new ArrayList<>();
    private List<String> addresses = new ArrayList<>();
    private List<String> reducedAddresses;
    private List<String> chargerNames = new ArrayList<>();
    private List<String> reducedChargerNames;
    private List<Integer> reducedIDs = new ArrayList<>();
    public List<Integer> tripAdvisorIDs = new ArrayList<>();
    public List<Integer> reducedTripAdvisorIDs;
    public List<Integer> restaurantTripAdvisorIDs = new ArrayList<>();
    public List<Integer> reducedRestaurantTripAdvisorIDs;
    public List<Integer> restaurantTripAdvisorIDsLink = new ArrayList<>();
    public List<Integer> reducedRestaurantTripAdvisorIDsLink;
    public HashMap<Integer, List<Integer>> hashMap = new HashMap<>();
    public HashMap<Integer, List<Integer>> reducedHashMap;
    public HashMap<Integer, List<Integer>> hashMapDistances = new HashMap<>();
    public HashMap<Integer, List<Integer>> reducedHashMapDistances;
    public HashMap<Integer, String> restaurantsHashMap = new HashMap<>();
    public HashMap<Integer, List<Integer>> restaurants = new HashMap<>();
    public static HashMap<Integer, List<Integer>> reducedRestaudants;
    public static List<Integer> points;
    public static HashMap<Integer, String> cuisineTrapvisorIDToName = new HashMap<>();

    private int counter;
    public static String destinationName;
    public static Boolean loaded;

    public static String bestMatchChergerName;
    public static String bestMatchChergerAddress;
    public static List<String> bestMatchChergerCuisinesNames;
    public static List<String> bestMatchChergerCuisinesAddresses;
    public static List<String> bestMatchChergerCuisinesDistances;
    public static String bestMatchChergerTotalDistance;
    public static String bestMatchChergerDistanceToCharger;
    public static String bestMatchChergerTime;
    public static List<Integer> bestMatchChargerKeys;

    public static String fastestChergerName;
    public static String fastestChergerAddress;
    public static List<String> fastestChergerCuisinesNames;
    public static List<String> fastestChergerCuisinesAddresses;
    public static List<String> fastestChergerCuisinesDistances;
    public static String fastestChergerTotalDistance;
    public static String fastestChergerDistanceToCharger;
    public static String fastestChergerTime;
    public static List<Integer> fastestMatchChargerKeys;

    public static String thirdBestMatchChergerName;
    public static String thirdBestMatchChergerAddress;
    public static List<String> thirdChergerCuisinesNames;
    public static List<String> thirdChergerCuisinesAddresses;
    public static List<String> thirdChergerCuisinesDistances;
    public static String thirdBestMatchChergerTotalDistance;
    public static String thirdBestMatchChergerDistanceToCharger;
    public static String thirdBestMatchChergerTime;
    public static List<Integer> thirdBestMatchChargerKeys;

    public static String fourthBestMatchChergerName;
    public static String fourthBatchChergerAddress;
    public static List<String> fourthChergerCuisinesNames;
    public static List<String> fourthChergerCuisinesAddresses;
    public static List<String> fourthChergerCuisinesDistances;
    public static String fourthBatchChergerTotalDistance;
    public static String fourthBatchChergerDistanceToCharger;
    public static String fourthBatchChergerTime;
    public static List<Integer> fourthBestMatchChargerKeys;

    public static String fifthBestMatchChergerName;
    public static String fifthBestMatchChergerAddress;
    public static List<String> fifthChergerCuisinesNames;
    public static List<String> fifthChergerCuisinesAddresses;
    public static List<String> fifthChergerCuisinesDistances;
    public static String fifthBestMatchChergerTotalDistance;
    public static String fifthBestMatchChergerDistanceToCharger;
    public static String fifthBestMatchChergerTime;
    public static List<Integer> fifthBestMatchChargerKeys;

    public static int max_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        mPlacesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Sets up the options menu.
     *
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            ActionBar actionBar = getSupportActionBar();
//            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

//            LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View v = inflator.inflate(R.layout.custom_bar, null);

//            actionBar.setCustomView(v);
        } catch (NullPointerException e) {
        }
        getMenuInflater().inflate(R.menu.current_place_menu, menu);

//        AutoCompleteTextView autocomplete = (AutoCompleteTextView) menu.findItem(R.id.autoComplete);
//
//        autocomplete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSearchCalled();
//            }
//        });

//        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
////        searchView.setIconifiedByDefault(false);
//        searchView.setQueryHint("Enter your destination");
//        searchView.setMaxWidth(1200);
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSearchCalled();
//            }
//        });

        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     *
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchCalled();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            default:
                return false;
        }
    }

    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields).setCountry("CZ").build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();
        if (!mLocationPermissionGranted) {
            Intent mStartActivity = new Intent(MapsActivityCurrentPlace.this, MapsActivityCurrentPlace.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(MapsActivityCurrentPlace.this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager) MapsActivityCurrentPlace.this.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        //------------------------------------------------------------------------------------------

        String SCVFileName = "charger.csv";

        try {
            Scanner inputStream = new Scanner(getAssets().open(SCVFileName));
            inputStream.nextLine();
            while (inputStream.hasNext()) {
                String data = inputStream.nextLine();
                String[] values = data.split(",");
                double lat = Double.valueOf(values[2]);
                double lng = Double.valueOf(values[3]);
                LatLng ll = new LatLng(lat, lng);
                chargersLatLng.add(ll);
                IDs.add(Integer.valueOf(values[0]));
                String address = values[4] + ", " + values[5];
                String addressWithoutQuotes = address.replaceAll("[\"]", "");
                String name = values[1];
                addresses.add(addressWithoutQuotes);
                chargerNames.add(name);
                mMap.addMarker(new MarkerOptions().position(ll).title(values[1]));
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }


        String SCV2FileName = "cuisine.csv";

        try {
            Scanner inputStream = new Scanner(getAssets().open(SCV2FileName));
            inputStream.nextLine();
            while (inputStream.hasNext()) {
                String data = inputStream.nextLine();
                String[] values = data.split(",");
                Integer ID = Integer.parseInt(values[2]);
                String name = values[1];
                cuisineTrapvisorIDToName.put(ID, name);
                tripAdvisorIDs.add(ID);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        String SCV3FileName = "restaurantCuisine.csv";

        try {
            Scanner inputStream = new Scanner(getAssets().open(SCV3FileName));
            inputStream.nextLine();
            int currentRestaurant = 18719427;
            List<Integer> tempList = new ArrayList<>();
            while (inputStream.hasNext()) {
                String data = inputStream.nextLine();
                String[] values = data.split(",");
                int currentCuisine = Integer.parseInt(values[2]);
                restaurantTripAdvisorIDsLink.add(currentCuisine);
                if (currentRestaurant != Integer.parseInt(values[1])) {
                    restaurants.put(currentRestaurant, new ArrayList<>(tempList));
                    restaurantTripAdvisorIDs.add(currentRestaurant);
                    currentRestaurant = Integer.parseInt(values[1]);
                    tempList.clear();
                }
                tempList.add(currentCuisine);
            }
            restaurants.put(currentRestaurant, new ArrayList<>(tempList));
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }


        String SCV4FileName = "chargerRestaurant.csv";

        try {
            Scanner inputStream = new Scanner(getAssets().open(SCV4FileName));
            inputStream.nextLine();
            List<Integer> tempList = new ArrayList<>();
            List<Integer> tempListDistances = new ArrayList<>();
            int currentID = 1;
            while (inputStream.hasNext()) {
                String data = inputStream.nextLine();
                String[] values = data.split(",");
                if (currentID != Integer.parseInt(values[1])) {
                    hashMap.put(currentID, new ArrayList<>(tempList));
                    hashMapDistances.put(currentID, new ArrayList<>(tempListDistances));
                    currentID = Integer.parseInt(values[1]);
                    tempList.clear();
                    tempListDistances.clear();
                }
                tempList.add(Integer.parseInt(values[2]));
                tempListDistances.add(Integer.parseInt(values[3]));
            }
            hashMap.put(currentID, new ArrayList<>(tempList));
            hashMapDistances.put(currentID, new ArrayList<>(tempListDistances));
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        String SCV5FileName = "restaurant.csv";

        try {
            Scanner inputStream = new Scanner(getAssets().open(SCV5FileName));
            inputStream.nextLine();
            while (inputStream.hasNext()) {
                String data = inputStream.nextLine();
                String[] values = data.split(",");
                Integer key;
                String address;
                try {
                    key = Integer.parseInt(values[4]);
                    address = values[2] + ", " + values[3];
                } catch (NumberFormatException e) {
                    key = Integer.parseInt(values[5]);
                    address = values[2] + ", " + values[3] + ", " + values[4];
                }
                String addressWithoutQuotes = address.replaceAll("[\"]", "");
                String name = values[1];
                restaurantsHashMap.put(key, name + ";" + addressWithoutQuotes);

            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                String permission = permissions[0];
                boolean showRationale = shouldShowRequestPermissionRationale(permission);
                if (!showRationale) {
                    System.exit(0);
                }
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                LatLng myLatLng = new LatLng(mDefaultLocation.latitude, mDefaultLocation.longitude);
                times.clear();
                distances1.clear();
                distances2.clear();
                counter = 0;

                destinationName = place.getName();

                Intent intent = new Intent(this, FoundDestinations.class);
                startActivity(intent);
                loaded = false;

                for (int i = 0; i < chargersLatLng.size(); i++) {
                    String url = getDirectionsUrl(myLatLng, chargersLatLng.get(i));
                    new GetDisDur().execute(url);
                }

                for (int i = 0; i < chargersLatLng.size(); i++) {
                    String url = getDirectionsUrl(chargersLatLng.get(i), place.getLatLng());
                    new GetDisDur().execute(url);
                }

                Log.i(TAG, "Place: " + destinationName + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    mPlacesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        mLikelyPlaceNames = new String[count];
                        mLikelyPlaceAddresses = new String[count];
                        mLikelyPlaceAttributions = new List[count];
                        mLikelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            mLikelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            mLikelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            mLikelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        MapsActivityCurrentPlace.this.openPlacesDialog();
                    } else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

//        String sensor = "sensor=false";

        String mode = "mode=driving";

        String language = "language=en";

        String key = "key=AIzaSyDZ7eBxdkyPJAE64ObarTBX_VQOueVTT9k";

        String parameters = str_origin + "&" + str_dest + "&" + mode + "&" + language + "&" + key;

        String output = "json";

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    private class GetDisDur extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... url) {

//            String data = "";
            HttpURLConnection mUrlConnection = null;
            StringBuilder mJsonResults = new StringBuilder();
            java.net.URL uu;

            try {
//                data = downloadUrl(url[0]);
                uu = new URL(url[0]);
                mUrlConnection = (HttpURLConnection) uu.openConnection();
                InputStreamReader in = new InputStreamReader(mUrlConnection.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    mJsonResults.append(buff, 0, read);
                }
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return mJsonResults.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONArray routes = jsonObject.getJSONArray("routes");

                JSONObject routes1 = routes.getJSONObject(0);

                JSONArray legs = routes1.getJSONArray("legs");

                JSONObject legs1 = legs.getJSONObject(0);

                JSONObject distance = legs1.getJSONObject("distance");

                JSONObject duration = legs1.getJSONObject("duration");

                String distanceText = distance.getString("text");

                String durationText = duration.getString("text");

                double hours;
                double hours_in_mins;
                double durationDouble;

                String[] currencies = durationText.split(" ");
                if (currencies[1].equals("hour") || currencies[1].equals("hours")) {
                    hours = Double.parseDouble(durationText.substring(0, durationText.indexOf(' ')));
                    hours_in_mins = hours * 60;
                    durationDouble = Double.parseDouble(currencies[2]) + hours_in_mins;
                } else {
                    durationDouble = Double.parseDouble(durationText.substring(0, durationText.indexOf(' ')));
                }

                double distanceDouble = Double.parseDouble(distanceText.substring(0, distanceText.indexOf(' ')));
                if (distances1.size() == chargersLatLng.size()) {
                    double temp;
                    temp = distances1.get(counter) + distanceDouble;
                    distances2.add(temp);
                    temp = times.get(counter) + durationDouble;
                    times.set(counter, temp);
                    counter++;
                } else {
                    distances1.add(distanceDouble);
                    times.add(durationDouble);
                }

                if (counter == chargersLatLng.size()) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    max_distance = sharedPreferences.getInt(SEEK_BAR1, 50) * 2;
                    int max_duration = sharedPreferences.getInt(SEEK_BAR2, 15);
                    reducedIDs = new ArrayList(IDs);
                    reducedTripAdvisorIDs = new ArrayList(tripAdvisorIDs);
                    reducedRestaurantTripAdvisorIDs = new ArrayList(restaurantTripAdvisorIDs);
                    reducedRestaurantTripAdvisorIDsLink = new ArrayList(restaurantTripAdvisorIDsLink);
                    reducedAddresses = new ArrayList<>(addresses);
                    reducedChargerNames = new ArrayList<>(chargerNames);
                    reducedHashMap = new HashMap<>();
                    for (int i = 0; i < IDs.size(); i++) {
                        List<Integer> temp = new ArrayList();
                        for (int j = 0; j < hashMap.get(IDs.get(i)).size(); j++) {
                            temp.add(hashMap.get(IDs.get(i)).get(j));
                        }
                        reducedHashMap.put(IDs.get(i), new ArrayList<>(temp));
                    }

                    reducedHashMapDistances = new HashMap<>();
                    for (int i = 0; i < IDs.size(); i++) {
                        List<Integer> temp = new ArrayList();
                        for (int j = 0; j < hashMapDistances.get(IDs.get(i)).size(); j++) {
                            temp.add(hashMapDistances.get(IDs.get(i)).get(j));
                        }
                        reducedHashMapDistances.put(IDs.get(i), new ArrayList<>(temp));
                    }

                    reducedRestaudants = new HashMap<>();
                    for (int i = 0; i < restaurantTripAdvisorIDs.size(); i++) {
                        List<Integer> temp = new ArrayList();
                        for (int j = 0; j < restaurants.get(restaurantTripAdvisorIDs.get(i)).size(); j++) {
                            temp.add(restaurants.get(restaurantTripAdvisorIDs.get(i)).get(j));
                        }
                        reducedRestaudants.put(restaurantTripAdvisorIDs.get(i), new ArrayList<>(temp));
                    }

                    int distances_size = distances1.size();

                    for (int i = 0; i < distances_size; i++) {
                        if (distances1.get(i) > max_distance) {
                            distances1.remove(i);
                            distances2.remove(i);
                            times.remove(i);
                            reducedIDs.remove(i);
                            reducedAddresses.remove(i);
                            reducedChargerNames.remove(i);
                            distances_size--;
                            i--;
                        }
                    }

                    sort2(times, distances2, distances1, reducedIDs, reducedAddresses, reducedChargerNames);


                    int times_size = times.size();

                    for (int i = 0; i < times_size; i++) {
                        if (times.get(i) > times.get(0) + max_duration) {
                            distances1.remove(i);
                            distances2.remove(i);
                            times.remove(i);
                            reducedIDs.remove(i);
                            reducedAddresses.remove(i);
                            reducedChargerNames.remove(i);
                            times_size--;
                            i--;
                        }
                    }

                    int fastestID = 0;
                    Long time;
                    Long distanceToCharger;
                    Long totlaDistance;
                    if (reducedIDs.size() > 0) {
                        fastestChergerCuisinesNames = new ArrayList<>();
                        fastestChergerCuisinesAddresses = new ArrayList<>();
                        fastestChergerCuisinesDistances = new ArrayList<>();
                        fastestMatchChargerKeys = new ArrayList<>();
                        fastestID = reducedIDs.get(0);
                        time = Math.round(times.get(0));
                        distanceToCharger = Math.round(distances1.get(0));
                        totlaDistance = Math.round(distances2.get(0));
                        fastestChergerTime = time.toString();
                        fastestChergerDistanceToCharger = distanceToCharger.toString();
                        fastestChergerTotalDistance = totlaDistance.toString();
                        fastestChergerAddress = reducedAddresses.get(0);
                        fastestChergerName = reducedChargerNames.get(0);
                    }

                    int count = 0;

                    for (int i = 0; i < tripAdvisorIDs.size(); i++) {
                        if (!sharedPreferences.getBoolean(CHECK_BOX + count, false)) {
                            reducedTripAdvisorIDs.remove(count);
                        } else {
                            count++;
                        }
                    }

                    int reducedRestaurantTripAdvisorIDs_size = reducedRestaurantTripAdvisorIDs.size();

                    for (int i = 0; i < reducedRestaurantTripAdvisorIDs_size; i++) {
                        int size = reducedRestaudants.get(reducedRestaurantTripAdvisorIDs.get(i)).size();
                        for (int k = 0; k < size; k++) {
                            boolean found = false;
                            for (int j = 0; j < reducedTripAdvisorIDs.size(); j++) {
                                if (reducedRestaudants.get(reducedRestaurantTripAdvisorIDs.get(i)).get(k).equals(reducedTripAdvisorIDs.get(j))) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                reducedRestaudants.get(reducedRestaurantTripAdvisorIDs.get(i)).remove(k);
                                k--;
                                size--;
                            }
                        }
                        if (reducedRestaudants.get(reducedRestaurantTripAdvisorIDs.get(i)).size() == 0) {
                            reducedRestaurantTripAdvisorIDs.remove(i);
                            i--;
                            reducedRestaurantTripAdvisorIDs_size--;
                        }
                    }

                    points = new ArrayList<>();

                    for (int i = 0; i < reducedIDs.size(); i++) {
                        int points_for_time = reducedIDs.size() - i;
                        int points_for_cuisines = 0;
                        int ID = reducedIDs.get(i);
                        int hashMapSize = reducedHashMap.get(ID).size();
                        for (int j = 0; j < hashMapSize; j++) {
                            boolean found = false;
                            for (int k = 0; k < reducedRestaurantTripAdvisorIDs.size(); k++) {
                                if (hashMap.get(ID).get(j).equals(reducedRestaurantTripAdvisorIDs.get(k))) {
                                    points_for_cuisines++;
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                reducedHashMap.get(ID).remove(j);
                                reducedHashMapDistances.get(ID).remove(j);
                                j--;
                                hashMapSize--;
                            }
                        }
                        int points_val = points_for_cuisines + points_for_time;
                        points.add(points_val);
                    }

                    sort3(points, reducedIDs, times, distances2, distances1, reducedAddresses, reducedChargerNames);

                    int points_size = points.size();
                    int p = 0;
                    int ID;
                    if (points_size > 0) {
                        p++;
                        bestMatchChergerCuisinesNames = new ArrayList<>();
                        bestMatchChergerCuisinesAddresses = new ArrayList<>();
                        bestMatchChergerCuisinesDistances = new ArrayList<>();
                        bestMatchChargerKeys = new ArrayList<>();
                        ID = reducedIDs.get(reducedIDs.size() - p);
                        for (int i = 0; i < reducedHashMap.get(ID).size(); i++) {
                            Integer key = reducedHashMap.get(ID).get(i);
                            bestMatchChargerKeys.add(key);
                            String[] values = restaurantsHashMap.get(key).split(";");
                            String name = values[0];
                            String address = values[1];
                            String distanceToRestaurant = reducedHashMapDistances.get(ID).get(i).toString();
                            bestMatchChergerCuisinesNames.add(name);
                            bestMatchChergerCuisinesAddresses.add(address);
                            bestMatchChergerCuisinesDistances.add(distanceToRestaurant);
                        }
                        time = Math.round(times.get(times.size() - p));
                        distanceToCharger = Math.round(distances1.get(distances1.size() - p));
                        totlaDistance = Math.round(distances2.get(distances2.size() - p));
                        bestMatchChergerTime = time.toString();
                        bestMatchChergerDistanceToCharger = distanceToCharger.toString();
                        bestMatchChergerTotalDistance = totlaDistance.toString();
                        bestMatchChergerAddress = reducedAddresses.get(reducedAddresses.size() - p);
                        bestMatchChergerName = reducedChargerNames.get(reducedChargerNames.size() - p);


                        for (int i = 0; i < reducedHashMap.get(fastestID).size(); i++) {
                            Integer key = reducedHashMap.get(fastestID).get(i);
                            fastestMatchChargerKeys.add(key);
                            String[] values = restaurantsHashMap.get(key).split(";");
                            String name = values[0];
                            String address = values[1];
                            String distanceToRestaurant = reducedHashMapDistances.get(fastestID).get(i).toString();
                            fastestChergerCuisinesNames.add(name);
                            fastestChergerCuisinesAddresses.add(address);
                            fastestChergerCuisinesDistances.add(distanceToRestaurant);
                        }
                    }
                    if (points_size > 1) {
                        p++;
                        thirdChergerCuisinesNames = new ArrayList<>();
                        thirdChergerCuisinesAddresses = new ArrayList<>();
                        thirdChergerCuisinesDistances = new ArrayList<>();
                        thirdBestMatchChargerKeys = new ArrayList<>();
                        ID = reducedIDs.get(reducedIDs.size() - p);
                        for (int i = 0; i < reducedHashMap.get(ID).size(); i++) {
                            Integer key = reducedHashMap.get(ID).get(i);
                            thirdBestMatchChargerKeys.add(key);
                            String[] values = restaurantsHashMap.get(key).split(";");
                            String name = values[0];
                            String address = values[1];
                            String distanceToRestaurant = reducedHashMapDistances.get(ID).get(i).toString();
                            thirdChergerCuisinesNames.add(name);
                            thirdChergerCuisinesAddresses.add(address);
                            thirdChergerCuisinesDistances.add(distanceToRestaurant);
                        }
                        time = Math.round(times.get(times.size() - p));
                        distanceToCharger = Math.round(distances1.get(distances1.size() - p));
                        totlaDistance = Math.round(distances2.get(distances2.size() - p));
                        thirdBestMatchChergerTime = time.toString();
                        thirdBestMatchChergerDistanceToCharger = distanceToCharger.toString();
                        thirdBestMatchChergerTotalDistance = totlaDistance.toString();
                        thirdBestMatchChergerAddress = reducedAddresses.get(reducedAddresses.size() - p);
                        thirdBestMatchChergerName = reducedChargerNames.get(reducedChargerNames.size() - p);
                    }
                    if (points_size > 2) {
                        p++;
                        fourthChergerCuisinesNames = new ArrayList<>();
                        fourthChergerCuisinesAddresses = new ArrayList<>();
                        fourthChergerCuisinesDistances = new ArrayList<>();
                        fourthBestMatchChargerKeys = new ArrayList<>();
                        ID = reducedIDs.get(reducedIDs.size() - p);
                        for (int i = 0; i < reducedHashMap.get(ID).size(); i++) {
                            Integer key = reducedHashMap.get(ID).get(i);
                            fourthBestMatchChargerKeys.add(key);
                            String[] values = restaurantsHashMap.get(key).split(";");
                            String name = values[0];
                            String address = values[1];
                            String distanceToRestaurant = reducedHashMapDistances.get(ID).get(i).toString();
                            fourthChergerCuisinesNames.add(name);
                            fourthChergerCuisinesAddresses.add(address);
                            fourthChergerCuisinesDistances.add(distanceToRestaurant);
                        }
                        time = Math.round(times.get(times.size() - p));
                        distanceToCharger = Math.round(distances1.get(distances1.size() - p));
                        totlaDistance = Math.round(distances2.get(distances2.size() - p));
                        fourthBatchChergerTime = time.toString();
                        fourthBatchChergerDistanceToCharger = distanceToCharger.toString();
                        fourthBatchChergerTotalDistance = totlaDistance.toString();
                        fourthBatchChergerAddress = reducedAddresses.get(reducedAddresses.size() - p);
                        fourthBestMatchChergerName = reducedChargerNames.get(reducedChargerNames.size() - p);
                    }
                    if (points_size > 3) {
                        p++;
                        fifthChergerCuisinesNames = new ArrayList<>();
                        fifthChergerCuisinesAddresses = new ArrayList<>();
                        fifthChergerCuisinesDistances = new ArrayList<>();
                        fifthBestMatchChargerKeys = new ArrayList<>();
                        ID = reducedIDs.get(reducedIDs.size() - p);
                        for (int i = 0; i < reducedHashMap.get(ID).size(); i++) {
                            Integer key = reducedHashMap.get(ID).get(i);
                            fifthBestMatchChargerKeys.add(key);
                            String[] values = restaurantsHashMap.get(key).split(";");
                            String name = values[0];
                            String address = values[1];
                            String distanceToRestaurant = reducedHashMapDistances.get(ID).get(i).toString();
                            fifthChergerCuisinesNames.add(name);
                            fifthChergerCuisinesAddresses.add(address);
                            fifthChergerCuisinesDistances.add(distanceToRestaurant);
                        }
                        time = Math.round(times.get(times.size() - p));
                        distanceToCharger = Math.round(distances1.get(distances1.size() - p));
                        totlaDistance = Math.round(distances2.get(distances2.size() - p));
                        fifthBestMatchChergerTime = time.toString();
                        fifthBestMatchChergerDistanceToCharger = distanceToCharger.toString();
                        fifthBestMatchChergerTotalDistance = totlaDistance.toString();
                        fifthBestMatchChergerAddress = reducedAddresses.get(reducedAddresses.size() - p);
                        fifthBestMatchChergerName = reducedChargerNames.get(reducedChargerNames.size() - p);
                    }
                    setDestinationView();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static void sort(List<Double> root_list, List<Double> list2) {
        sort(root_list, list2, 0, root_list.size() - 1);
    }

    public static void sort(List<Double> root_list, List<Double> list2, int from, int to) {
        if (from < to) {
            int pivot = from;
            int left = from + 1;
            int right = to;
            double pivotValue = root_list.get(pivot);
            while (left <= right) {
                // left <= to -> limit protection
                while (left <= to && pivotValue >= root_list.get(left)) {
                    left++;
                }
                // right > from -> limit protection
                while (right > from && pivotValue < root_list.get(right)) {
                    right--;
                }
                if (left < right) {
                    Collections.swap(root_list, left, right);
                    Collections.swap(list2, left, right);
                }
            }
            Collections.swap(root_list, pivot, left - 1);
            Collections.swap(list2, pivot, left - 1);
            sort(root_list, list2, from, right - 1); // <-- pivot was wrong!
            sort(root_list, list2, right + 1, to);   // <-- pivot was wrong!
        }
    }

    public static void sort2(List<Double> root_list, List<Double> list2, List<Double> ranking, List<Integer> IDs, List<String> addresses, List<String> a) {
        sort2(root_list, list2, ranking, IDs, addresses, a, 0, root_list.size() - 1);
    }

    public static void sort2(List<Double> root_list, List<Double> list2, List<Double> ranking, List<Integer> IDs, List<String> addresses, List<String> a, int from, int to) {
        if (from < to) {
            int pivot = from;
            int left = from + 1;
            int right = to;
            double pivotValue = root_list.get(pivot);
            while (left <= right) {
                // left <= to -> limit protection
                while (left <= to && pivotValue >= root_list.get(left)) {
                    left++;
                }
                // right > from -> limit protection
                while (right > from && pivotValue < root_list.get(right)) {
                    right--;
                }
                if (left < right) {
                    Collections.swap(root_list, left, right);
                    Collections.swap(list2, left, right);
                    Collections.swap(ranking, left, right);
                    Collections.swap(IDs, left, right);
                    Collections.swap(addresses, left, right);
                    Collections.swap(a, left, right);
                }
            }
            Collections.swap(root_list, pivot, left - 1);
            Collections.swap(list2, pivot, left - 1);
            Collections.swap(ranking, pivot, left - 1);
            Collections.swap(IDs, pivot, left - 1);
            Collections.swap(addresses, pivot, left - 1);
            Collections.swap(a, pivot, left - 1);
            sort2(root_list, list2, ranking, IDs, addresses, a, from, right - 1); // <-- pivot was wrong!
            sort2(root_list, list2, ranking, IDs, addresses, a, right + 1, to);   // <-- pivot was wrong!
        }
    }

    public static void sort3(List<Integer> root_list, List<Integer> a, List<Double> b, List<Double> c, List<Double> d, List<String> e, List<String> f) {
        sort3(root_list, a, b, c, d, e, f, 0, root_list.size() - 1);
    }

    public static void sort3(List<Integer> root_list, List<Integer> a, List<Double> b, List<Double> c, List<Double> d, List<String> e, List<String> f, int from, int to) {
        if (from < to) {
            int pivot = from;
            int left = from + 1;
            int right = to;
            double pivotValue = root_list.get(pivot);
            while (left <= right) {
                // left <= to -> limit protection
                while (left <= to && pivotValue >= root_list.get(left)) {
                    left++;
                }
                // right > from -> limit protection
                while (right > from && pivotValue < root_list.get(right)) {
                    right--;
                }
                if (left < right) {
                    Collections.swap(root_list, left, right);
                    Collections.swap(a, left, right);
                    Collections.swap(b, left, right);
                    Collections.swap(c, left, right);
                    Collections.swap(d, left, right);
                    Collections.swap(e, left, right);
                    Collections.swap(f, left, right);
                }
            }
            Collections.swap(root_list, pivot, left - 1);
            Collections.swap(a, pivot, left - 1);
            Collections.swap(b, pivot, left - 1);
            Collections.swap(c, pivot, left - 1);
            Collections.swap(d, pivot, left - 1);
            Collections.swap(e, pivot, left - 1);
            Collections.swap(f, pivot, left - 1);
            sort3(root_list, a, b, c, d, e, f, from, right - 1); // <-- pivot was wrong!
            sort3(root_list, a, b, c, d, e, f, right + 1, to);   // <-- pivot was wrong!
        }
    }
}
