package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.currentplacedetailsonmap.ExpandableListDataPump.setExpandableList;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.bestMatchChargerKeys;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.bestMatchChergerAddress;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.bestMatchChergerCuisinesAddresses;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.bestMatchChergerCuisinesDistances;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.bestMatchChergerCuisinesNames;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.bestMatchChergerDistanceToCharger;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.bestMatchChergerName;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.bestMatchChergerTime;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.bestMatchChergerTotalDistance;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.destinationName;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fastestChergerAddress;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fastestChergerCuisinesAddresses;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fastestChergerCuisinesDistances;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fastestChergerCuisinesNames;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fastestChergerDistanceToCharger;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fastestChergerName;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fastestChergerTime;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fastestChergerTotalDistance;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fastestMatchChargerKeys;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fifthBestMatchChargerKeys;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fifthBestMatchChergerAddress;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fifthBestMatchChergerDistanceToCharger;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fifthBestMatchChergerName;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fifthBestMatchChergerTime;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fifthBestMatchChergerTotalDistance;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fifthChergerCuisinesAddresses;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fifthChergerCuisinesDistances;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fifthChergerCuisinesNames;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fourthBatchChergerAddress;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fourthBatchChergerDistanceToCharger;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fourthBatchChergerTime;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fourthBatchChergerTotalDistance;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fourthBestMatchChargerKeys;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fourthBestMatchChergerName;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fourthChergerCuisinesAddresses;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fourthChergerCuisinesDistances;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.fourthChergerCuisinesNames;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.max_distance;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.points;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.reducedRestaudants;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.thirdBestMatchChargerKeys;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.thirdBestMatchChergerDistanceToCharger;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.thirdBestMatchChergerName;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.thirdBestMatchChergerTime;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.thirdBestMatchChergerTotalDistance;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.thirdChergerCuisinesAddresses;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.thirdChergerCuisinesDistances;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.thirdChergerCuisinesNames;

public class FoundDestinations extends AppCompatActivity {
    private static ViewSwitcher switcher;
    private static TextView noRoutesFound;
    private static ScrollView routesFound;


    private static LinearLayout bestRoute;
    private static TextView bestAddress;
    private static TextView bestChargerName;
    private static TextView bestDaT;
    private static TextView bestDToChargeraRange;
    private static TextView bestNofCuisines;


    private static LinearLayout fastestRoute;
    private static TextView fastestAddress;
    private static TextView fastestChargerName;
    private static TextView fastestDaT;
    private static TextView fastestDtoChargerRange;
    private static TextView fastestNofCuisines;


    private static LinearLayout thirdRoute;
    private static TextView thirdAddress;
    private static TextView thirdChargerName;
    private static TextView thirdDaT;
    private static TextView thirdDToChargeraRange;
    private static TextView thirdNofCuisines;


    private static LinearLayout fourthRoute;
    private static TextView fourthAddress;
    private static TextView fourthChargerName;
    private static TextView fourthDaT;
    private static TextView fourthDToChargeraRange;
    private static TextView fourthNofCuisines;


    private static LinearLayout fifthRoute;
    private static TextView fifthAddress;
    private static TextView fifthChargerName;
    private static TextView fifthDaT;
    private static TextView fifthDToChargeraRange;
    private static TextView fifthNofCuisines;


    static ExpandableListView expandableListView;
    static ExpandableListAdapter expandableListAdapter;
    static List<ExpandableListView> expandableListViews = new ArrayList();

    private static Context context;

    static boolean expanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FoundDestinations.context = getApplicationContext();
        setContentView(R.layout.activity_found_destinations);
        setTitle(destinationName);
        switcher = findViewById(R.id.profileSwitcher);
        noRoutesFound = findViewById(R.id.noRoutesFound);
        routesFound = findViewById(R.id.routesFound);

        expandableListViews.clear();
        expandableListViews.add((ExpandableListView) findViewById(R.id.expandableListView1));
        expandableListViews.add((ExpandableListView) findViewById(R.id.expandableListView2));
        expandableListViews.add((ExpandableListView) findViewById(R.id.expandableListView3));
        expandableListViews.add((ExpandableListView) findViewById(R.id.expandableListView4));
        expandableListViews.add((ExpandableListView) findViewById(R.id.expandableListView5));


        bestRoute = findViewById(R.id.bestRoute);
        bestAddress = findViewById(R.id.bestAddress);
        bestChargerName = findViewById(R.id.bestChargerName);
        bestDaT = findViewById(R.id.bestDaT);
        bestDToChargeraRange = findViewById(R.id.bestDtoChargeraRange);
        bestNofCuisines = findViewById(R.id.bestNofCuisines);


        fastestRoute = findViewById(R.id.fastestRoute);
        fastestAddress = findViewById(R.id.fastestAddress);
        fastestChargerName = findViewById(R.id.fastestChargerName);
        fastestDaT = findViewById(R.id.fastestDaT);
        fastestDtoChargerRange = findViewById(R.id.fastestDtoChargeraRange);
        fastestNofCuisines = findViewById(R.id.fastestNofCuisines);


        thirdRoute = findViewById(R.id.thirdRoute);
        thirdAddress = findViewById(R.id.thirdAddress);
        thirdChargerName = findViewById(R.id.thirdChargerName);
        thirdDaT = findViewById(R.id.thirdDaT);
        thirdDToChargeraRange = findViewById(R.id.thirdDtoChargeraRange);
        thirdNofCuisines = findViewById(R.id.thirdNofCuisines);


        fourthRoute = findViewById(R.id.fourthRoute);
        fourthAddress = findViewById(R.id.fourthAddress);
        fourthChargerName = findViewById(R.id.fourthChargerName);
        fourthDaT = findViewById(R.id.fourthDaT);
        fourthDToChargeraRange = findViewById(R.id.fourthDtoChargeraRange);
        fourthNofCuisines = findViewById(R.id.fourthNofCuisines);


        fifthRoute = findViewById(R.id.fifthRoute);
        fifthAddress = findViewById(R.id.fifthAddress);
        fifthChargerName = findViewById(R.id.fifthChargerName);
        fifthDaT = findViewById(R.id.fifthDaT);
        fifthDToChargeraRange = findViewById(R.id.fifthDtoChargeraRange);
        fifthNofCuisines = findViewById(R.id.fifthNofCuisines);
    }

    public static void setDestinationView() {
        int points_size = points.size();
        if (points_size == 0) {
            routesFound.setVisibility(View.GONE);
        } else {
            noRoutesFound.setVisibility(View.GONE);
            if (thirdBestMatchChergerName == null || thirdBestMatchChergerName.length() == 0) {
                thirdRoute.setVisibility(View.GONE);
            } else {
                createThirdRouteView();
            }
            if (fourthBestMatchChergerName == null || fourthBestMatchChergerName.length() == 0) {
                fourthRoute.setVisibility(View.GONE);
            } else {
                createFourthRouteView();
            }
            if (fifthBestMatchChergerName == null || fifthBestMatchChergerName.length() == 0) {
                fifthRoute.setVisibility(View.GONE);
            } else {
                createFifthRouteView();
            }
            createBestRouteView();
            createFastestRouteView();
        }

        switcher.showNext();
    }

    private static void createExpandableListView(int ID, int num) {
        expandableListView = expandableListViews.get(ID - 1);
        expandableListAdapter = new CustomExpandableListAdapter(getAppContext());
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.requestLayout();
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int position, long id) {
                setExpandableListViewHeight(parent, position);
                return false;
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();
                return false;
            }
        });
    }

    private static void setExpandableListViewHeight(ExpandableListView listView,
                                                    int group) {
        CustomExpandableListAdapter listAdapter = (CustomExpandableListAdapter) listView.getExpandableListAdapter();
        if (!expanded) {
            expanded = true;
        } else {
            expanded = false;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, expanded, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }
        if (!expanded) {
            expanded = true;
        } else {
            expanded = false;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private static void createBestRouteView() {
        setExpandableList(bestMatchChergerCuisinesNames, reducedRestaudants, bestMatchChergerCuisinesDistances, bestMatchChergerCuisinesAddresses, bestMatchChargerKeys);
        createExpandableListView(1, 1);
        bestAddress.setText(bestMatchChergerAddress);
        bestChargerName.setText("Charging at " + bestMatchChergerName);
        bestDaT.setText(bestMatchChergerTotalDistance + "km, " + bestMatchChergerTime + "min");
        bestDToChargeraRange.setText(bestMatchChergerDistanceToCharger + "km to charger, " + max_distance + "km range");
        bestNofCuisines.setText(bestMatchChergerCuisinesAddresses.size() + " matching cuisines");

    }

    private static void createFastestRouteView() {
        setExpandableList(fastestChergerCuisinesNames, reducedRestaudants, fastestChergerCuisinesDistances, fastestChergerCuisinesAddresses, fastestMatchChargerKeys);
        createExpandableListView(2, 2);
        fastestAddress.setText(fastestChergerAddress);
        fastestChargerName.setText("Charging at " + fastestChergerName);
        fastestDaT.setText(fastestChergerTotalDistance + "km, " + fastestChergerTime + "min");
        fastestDtoChargerRange.setText(fastestChergerDistanceToCharger + "km to charger, " + max_distance + "km range");
        fastestNofCuisines.setText(fastestChergerCuisinesAddresses.size() + " matching cuisines");
    }

    private static void createThirdRouteView() {
        setExpandableList(thirdChergerCuisinesNames, reducedRestaudants, thirdChergerCuisinesDistances, thirdChergerCuisinesAddresses, thirdBestMatchChargerKeys);
        createExpandableListView(3, 3);
        thirdAddress.setText(bestMatchChergerAddress);
        thirdChargerName.setText("Charging at " + thirdBestMatchChergerName);
        thirdDaT.setText(thirdBestMatchChergerTotalDistance + "km, " + thirdBestMatchChergerTime + "min");
        thirdDToChargeraRange.setText(thirdBestMatchChergerDistanceToCharger + "km to charger, " + max_distance + "km range");
        thirdNofCuisines.setText(thirdChergerCuisinesAddresses.size() + " matching cuisines");
    }

    private static void createFourthRouteView() {
        setExpandableList(fourthChergerCuisinesNames, reducedRestaudants, fourthChergerCuisinesDistances, fourthChergerCuisinesAddresses, fourthBestMatchChargerKeys);
        createExpandableListView(4, 4);
        fourthAddress.setText(fourthBatchChergerAddress);
        fourthChargerName.setText("Charging at " + fourthBestMatchChergerName);
        fourthDaT.setText(fourthBatchChergerTotalDistance + "km, " + fourthBatchChergerTime + "min");
        thirdDToChargeraRange.setText(fourthBatchChergerDistanceToCharger + "km to charger, " + max_distance + "km range");
        fourthNofCuisines.setText(fourthChergerCuisinesAddresses.size() + " matching cuisines");
    }

    private static void createFifthRouteView() {
        setExpandableList(fifthChergerCuisinesNames, reducedRestaudants, fifthChergerCuisinesDistances, fifthChergerCuisinesAddresses, fifthBestMatchChargerKeys);
        createExpandableListView(5, 5);
        fifthAddress.setText(fifthBestMatchChergerAddress);
        fifthChargerName.setText("Charging at " + fifthBestMatchChergerName);
        fifthDaT.setText(fifthBestMatchChergerTotalDistance + "km, " + fifthBestMatchChergerTime + "min");
        fifthDToChargeraRange.setText(fifthBestMatchChergerDistanceToCharger + "km to charger, " + max_distance + "km range");
        fifthNofCuisines.setText(fifthChergerCuisinesAddresses.size() + " matching cuisines");
    }

    public static Context getAppContext() {
        return FoundDestinations.context;
    }
}
