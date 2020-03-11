package com.example.currentplacedetailsonmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static List<String> expandableListName = new ArrayList<>();
    public static HashMap<Integer, List<Integer>> expandableListCuisines = new HashMap<>();
    public static List<String> expandableListDistance = new ArrayList<>();
    public static List<String> expandableListAddress = new ArrayList<>();
    public static List<Integer> expandableListKeys = new ArrayList<>();

    public static void setExpandableList(List<String> name, HashMap<Integer, List<Integer>> cuisines, List<String> distance, List<String> address, List<Integer> keys) {
        expandableListName = name;
        expandableListDistance = distance;
        expandableListAddress = address;
        expandableListCuisines = cuisines;
        expandableListKeys = keys;
    }
}
