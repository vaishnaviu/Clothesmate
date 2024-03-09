package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StylingFragment extends Fragment {

    private ListView listView;
    private TextView suggestionsTextView;
    private AutoCompleteTextView weatherAutoCompleteTextView, occasionAutoCompleteTextView;
    private DatabaseReference databaseReference;
    private Map<String, Map<String, Object>> inventory;
    private Map<String, Map<String, Object>> usageData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_styling, container, false);
        Toolbar toolbar = view.findViewById(R.id.new_toolbar);
        toolbar.setTitle("Styling Suggestion");

        listView = view.findViewById(R.id.listview);
        weatherAutoCompleteTextView = view.findViewById(R.id.auto_complete_weathertxt);
        occasionAutoCompleteTextView = view.findViewById(R.id.auto_complete_occasiontxt);
        suggestionsTextView = view.findViewById(R.id.suggestions);

        Button btnSuggestStyle = view.findViewById(R.id.btnSuggestStyle);
        btnSuggestStyle.setOnClickListener(v -> {
            String selectedWeather = weatherAutoCompleteTextView.getText().toString();
            String selectedOccasion = occasionAutoCompleteTextView.getText().toString();

            System.out.println("Selected weather and occasion: " + selectedWeather + " " + selectedOccasion);

            if (!selectedWeather.isEmpty() && !selectedOccasion.isEmpty()) {
                suggestionsTextView.setVisibility(View.VISIBLE);
                generateStylingSuggestions(selectedWeather, selectedOccasion);
            } else {
                showToast("Please select both weather and occasion.");
            }
        });

        // Set up AutoCompleteTextView adapters for weather and occasion
        ArrayAdapter<String> weatherAdapter = new ArrayAdapter<>(requireContext(), R.layout.styling_dropdown, getWeatherOptions());
        weatherAutoCompleteTextView.setAdapter(weatherAdapter);

        ArrayAdapter<String> occasionAdapter = new ArrayAdapter<>(requireContext(), R.layout.styling_dropdown, getOccasionOptions());
        occasionAutoCompleteTextView.setAdapter(occasionAdapter);

        // Get a reference to the Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Load inventory and usage data from Firebase
        loadDataFromFirebase();
        return view;
    }

    private void generateStylingSuggestions(String selectedWeather, String selectedOccasion) {
        List<Map<String, Object>> filteredItems = filterInventoryByWeatherOccasion(selectedWeather, selectedOccasion);

        System.out.println("generateStylingSuggestions filteredItems: " + filteredItems);

        List<Map<String, Object>> uniqueItemsList = createUniqueItemsList(filteredItems);

        if (!uniqueItemsList.isEmpty()) {
            StylingAdapter adapter = new StylingAdapter(requireContext(), R.layout.item_styling, uniqueItemsList);
            listView.setAdapter(adapter);
        } else {
            showToast("No styling suggestions found for the selected weather and occasion.");
        }
    }

    private List<Map<String, Object>> createUniqueItemsList(List<Map<String, Object>> items) {
        List<Map<String, Object>> uniqueItemsList = new ArrayList<>();
        Set<String> uniqueItemSet = new HashSet<>();

        for (Map<String, Object> item : items) {
            String itemId = item.get("Id") != null ? item.get("Id").toString() : "";
            String type = item.get("Type") != null ? item.get("Type").toString() : "";
            String color = item.get("Color") != null ? item.get("Color").toString() : "";

            String uniqueKey = itemId + type + color;

            if (!uniqueItemSet.contains(uniqueKey)) {
                uniqueItemSet.add(uniqueKey);
                uniqueItemsList.add(item);
            }
        }

        return uniqueItemsList;
    }

    private Map<String, Integer> calculateTypeFrequency(List<Map<String, Object>> items) {
        Map<String, Integer> typeFrequency = new HashMap<>();
        for (Map<String, Object> item : items) {
            String type = item.get("Type").toString();
            typeFrequency.put(type, typeFrequency.getOrDefault(type, 0) + 1);
        }
        return typeFrequency;
    }

    private String getMostFrequentType(Map<String, Integer> typeFrequency) {
        int maxFrequency = 0;
        String mostFrequentType = null;
        for (Map.Entry<String, Integer> entry : typeFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequentType = entry.getKey();
            }
        }
        return mostFrequentType;
    }

    private List<Map<String, Object>> getSuggestedItemsForType(String type, List<Map<String, Object>> items) {
        List<Map<String, Object>> suggestedItems = new ArrayList<>();

        // Filter items based on the specified type
        for (Map<String, Object> item : items) {
            String itemTypeId = item.get("Type").toString();
            if (itemTypeId.equals(type)) {
                suggestedItems.add(item);
            }
        }

        // Sort items by frequency (descending order)
        suggestedItems.sort((item1, item2) -> {
            int frequency1 = calculateItemFrequency(type, items);
            int frequency2 = calculateItemFrequency(type, items);
            return Integer.compare(frequency2, frequency1);
        });

        return suggestedItems;
    }

    private int calculateItemFrequency(String type, List<Map<String, Object>> items) {
        int frequency = 0;
        for (Map<String, Object> item : items) {
            String itemTypeId = item.get("Type").toString();
            if (itemTypeId.equals(type)) {
                frequency++;
            }
        }
        return frequency;
    }

    private ArrayList<Map<String, Object>> createItemListFromItems(List<Map<String, Object>> items) {
        ArrayList<Map<String, Object>> itemList = new ArrayList<>();
        for (Map<String, Object> item : items) {
            Map<String, Object> itemDetails = new HashMap<>();
            String itemId = item.get("Id").toString();
            String type = item.get("Type").toString();
            String color = item.get("Color").toString();

            itemDetails.put("Id", itemId);
            itemDetails.put("Type", type);
            itemDetails.put("Color", color);
            itemList.add(itemDetails);
        }
        return itemList;
    }

    private List<Map<String, Object>> filterInventoryByWeatherOccasion(String weather, String occasion) {
        List<Map<String, Object>> filteredItems = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> timestampEntry : usageData.entrySet()) {
            Map<String, Object> timestampData = timestampEntry.getValue();
            String itemWeather = timestampData.get("weather") != null ? timestampData.get("weather").toString() : "";
            String itemOccasion = timestampData.get("occasion") != null ? timestampData.get("occasion").toString() : "";
            String itemId = timestampData.get("id").toString();
            int itemStatus = getIntegerValue(timestampData, "status");

            if (itemWeather.equalsIgnoreCase(weather) &&
                    itemOccasion.equalsIgnoreCase(occasion) &&
                    !itemId.isEmpty() &&
                    itemStatus == 1) {

                // Fetch type and color from the Inventory collection using the ID
                Map<String, Object> inventoryItem = inventory.get(itemId);

                // Add Id, type, and color to the filtered items
                if (inventoryItem != null) {
                    Map<String, Object> filteredItem = new HashMap<>();
                    filteredItem.put("Id", itemId);
                    filteredItem.put("Type", inventoryItem.get("Type"));
                    filteredItem.put("Color", inventoryItem.get("Color"));
                    filteredItems.add(filteredItem);
                }
            }
        }
        return filteredItems;
    }

    private int getIntegerValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Integer) {
            return (int) value;
        } else if (value instanceof Long) {
            return ((Long) value).intValue();
        } else {
            return 0;
        }
    }

    private void loadDataFromFirebase() {
        DatabaseReference inventoryRef = databaseReference.child("Inventory");
        DatabaseReference usageDataRef = databaseReference.child("ourtest");

        inventoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inventory = new HashMap<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String itemId = childSnapshot.getKey();
                    Map<String, Object> itemData = (Map<String, Object>) childSnapshot.getValue();
                    inventory.put(itemId, itemData);
                }
                System.out.println("Inventory data loaded: " + inventory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Failed to load Inventory data from Firebase: " + databaseError.getMessage());
            }
        });

        usageDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usageData = new HashMap<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String timestamp = childSnapshot.getKey();
                    Map<String, Object> userData = (Map<String, Object>) childSnapshot.getValue();
                    usageData.put(timestamp, userData);
                }
                System.out.println("Usage data loaded: " + usageData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Failed to load Usage data from Firebase: " + databaseError.getMessage());
            }
        });
    }

    private ArrayList<String> getWeatherOptions() {
        // Return a list of weather options (Sunny, Rainy, Cloudy, Snowy, Windy)
        ArrayList<String> options = new ArrayList<>();
        options.add("Sunny");
        options.add("Rainy");
        options.add("Cloudy");
        options.add("Snowy");
        options.add("Windy");
        return options;
    }

    private ArrayList<String> getOccasionOptions() {
        // Return a list of occasion options (Formal, Semi-formal, Casual)
        ArrayList<String> options = new ArrayList<>();
        options.add("Formal");
        options.add("Semi-formal");
        options.add("Casual");
        return options;
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}