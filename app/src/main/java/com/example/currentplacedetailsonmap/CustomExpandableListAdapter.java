package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.currentplacedetailsonmap.ExpandableListDataPump.expandableListAddress;
import static com.example.currentplacedetailsonmap.ExpandableListDataPump.expandableListCuisines;
import static com.example.currentplacedetailsonmap.ExpandableListDataPump.expandableListDistance;
import static com.example.currentplacedetailsonmap.ExpandableListDataPump.expandableListKeys;
import static com.example.currentplacedetailsonmap.ExpandableListDataPump.expandableListName;
import static com.example.currentplacedetailsonmap.MapsActivityCurrentPlace.cuisineTrapvisorIDToName;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> name;
    private HashMap<Integer, List<Integer>> cuisines;
    private List<String> distance;
    private List<String> address;
    private List<Integer> keys;

    public CustomExpandableListAdapter(Context context) {
        this.context = context;
        this.name = new ArrayList<>(expandableListName);
        this.cuisines = new HashMap<>(expandableListCuisines);
        this.distance = new ArrayList<>(expandableListDistance);
        this.address = new ArrayList<>(expandableListAddress);
        this.keys = new ArrayList<>(expandableListKeys);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.name.get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListName = (String) getChild(listPosition, expandedListPosition);
        final String expandedListDistance = distance.get(expandedListPosition);
        final String expandedListAddress = address.get(expandedListPosition);

        final List<String> namesOfMatchingCuisines_list = new ArrayList<>();
        for (int i = 0; i < cuisines.get(keys.get(expandedListPosition)).size(); i++) {
            namesOfMatchingCuisines_list.add(cuisineTrapvisorIDToName.get(cuisines.get(keys.get(expandedListPosition)).get(i)));
        }

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextViewName = convertView.findViewById(R.id.name);
        expandedListTextViewName.setText(expandedListName);
        TextView expandedListTextViewDistance = convertView.findViewById(R.id.distance);
        expandedListTextViewDistance.setText(expandedListDistance + "m");
        TextView expandedListTextViewAddress = convertView.findViewById(R.id.address);
        expandedListTextViewAddress.setText(expandedListAddress);

        ListView listview = convertView.findViewById(R.id.cuisineNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_list_item_1, namesOfMatchingCuisines_list);

        listview.setAdapter(adapter);

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight + (listview.getDividerHeight() * (adapter.getCount() - 1));
        listview.setLayoutParams(params);
        listview.requestLayout();


        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.name.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.name;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
//        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setGravity(Gravity.CENTER);
        if (isExpanded) {
            listTitleTextView.setText("Collapse");
        } else {
            listTitleTextView.setText("See restaurants...");
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
