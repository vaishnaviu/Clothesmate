package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;

public class StylingFragment extends Fragment {

    private ListView listView;
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

        // Set up AutoCompleteTextView adapters for weather and occasion
        ArrayAdapter<String> weatherAdapter = new ArrayAdapter<>(requireContext(), R.layout.styling_dropdown, getWeatherOptions());
        weatherAutoCompleteTextView.setAdapter(weatherAdapter);

        ArrayAdapter<String> occasionAdapter = new ArrayAdapter<>(requireContext(), R.layout.styling_dropdown, getOccasionOptions());
        occasionAutoCompleteTextView.setAdapter(occasionAdapter);

        // Listen for changes in both weather and occasion AutoCompleteTextViews
        weatherAutoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> onWeatherOccasionSelected());
        occasionAutoCompleteTextView.setOnItemClickListener((parent, view12, position, id) -> onWeatherOccasionSelected());

        // Get a reference to the Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Load inventory and usage data from Firebase
        loadDataFromFirebase();

        return view;
    }

    private void onWeatherOccasionSelected() {
        String selectedWeather = weatherAutoCompleteTextView.getText().toString();
        String selectedOccasion = occasionAutoCompleteTextView.getText().toString();

        if (!selectedWeather.isEmpty() && !selectedOccasion.isEmpty()) {
            generateStylingSuggestions(selectedWeather, selectedOccasion);
        }
    }

    private void generateStylingSuggestions(String selectedWeather, String selectedOccasion) {
        List<Map<String, Object>> filteredItems = filterInventoryByWeatherOccasion(selectedWeather, selectedOccasion);
        String mostFrequentType = findMostFrequentType(filteredItems);

        if (mostFrequentType != null) {
            Map<String, Object> suggestedItem = findSuggestedItem(mostFrequentType);
            if (suggestedItem != null) {
                String type = suggestedItem.get("Type").toString();
                String color = suggestedItem.get("Color").toString();

                ArrayList<Event> suggestions = new ArrayList<>();
                suggestions.add(new Event(type));

                StylingAdapter adapter = new StylingAdapter(requireContext(), R.layout.item_styling, suggestions);
                listView.setAdapter(adapter);
            } else {
                showToast("No suitable outfit found for the selected weather and occasion.");
            }
        } else {
            showToast("No matching outfits found for the selected weather and occasion.");
        }
    }

    private List<Map<String, Object>> filterInventoryByWeatherOccasion(String weather, String occasion) {
        List<Map<String, Object>> filteredItems = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry : usageData.entrySet()) {
            Map<String, Object> itemData = entry.getValue();
            String itemWeather = itemData.get("weather") != null ? itemData.get("weather").toString() : "";
            String itemOccasion = itemData.get("occasion") != null ? itemData.get("occasion").toString() : "";
            String itemId = itemData.get("id") != null ? itemData.get("id").toString() : "";
            int itemStatus = getIntegerValue(itemData, "status");

            if (itemWeather.equalsIgnoreCase(weather) &&
                    itemOccasion.equalsIgnoreCase(occasion) &&
                    !itemId.isEmpty()) {
                Map<String, Object> inventoryItem = inventory.get(itemId);
                if (inventoryItem != null &&
                        getIntegerValue(inventoryItem, "status") == 1 &&
                        itemStatus == 1) {
                    filteredItems.add(inventoryItem);
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

    private String findMostFrequentType(List<Map<String, Object>> items) {
        Map<String, Long> typeFrequency = new HashMap<>();
        for (Map<String, Object> item : items) {
            String type = item.get("Type").toString();
            typeFrequency.put(type, typeFrequency.getOrDefault( type, 0L) + 1);
        }

        int maxFrequency = 0;
        String mostFrequentType = null;
        for (Map.Entry<String, Long> entry : typeFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = Math.toIntExact(entry.getValue());
                mostFrequentType = entry.getKey();
            }
        }
        return mostFrequentType;
    }


    private Map<String, Object> findSuggestedItem(String type) {
        for (Map.Entry<String, Map<String, Object>> entry : inventory.entrySet()) {
            Map<String, Object> item = entry.getValue();
            if (item.get("Type").toString().equalsIgnoreCase(type) &&
                    (int) item.get("status") == 1 &&
                    !usageData.containsKey(entry.getKey())) {
                return item;
            }
        }
        return null;
    }

    private void loadDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inventory = (Map<String, Map<String, Object>>) dataSnapshot.child("Inventory").getValue();
                usageData = (Map<String, Map<String, Object>>) dataSnapshot.child("ourtest").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Failed to load data from Firebase: " + databaseError.getMessage());
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