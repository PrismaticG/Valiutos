package com.example.uni0;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText filterEditText;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> originalCurrencyList; // Originalus sąrašas
    private ArrayList<String> filteredCurrencyList; // Filtruotas sąrašas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        filterEditText = findViewById(R.id.filterEditText);

        originalCurrencyList = new ArrayList<>();
        filteredCurrencyList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredCurrencyList);
        listView.setAdapter(adapter);

        // Įkeliam duomenis iš API
        loadCurrencyData();

        // Filtravimo funkcija
        filterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCurrencyList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadCurrencyData() {
        Log.d("MainActivity", "Loading currency data...");
        DataLoader dataLoader = new DataLoader(new DataLoader.DataCallback() {
            @Override
            public void onDataLoaded(ArrayList<String> data) {
                originalCurrencyList.clear();
                originalCurrencyList.addAll(data);
                filteredCurrencyList.clear();
                filteredCurrencyList.addAll(originalCurrencyList);

                adapter.notifyDataSetChanged();
                Log.d("MainActivity", "Currency data loaded successfully.");
            }
        });
        dataLoader.execute("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
    }

    private void filterCurrencyList(String query) {
        filteredCurrencyList.clear();

        if (query.isEmpty()) {
            // Jei filtravimo laukas tuščias, rodomas visas sąrašas
            filteredCurrencyList.addAll(originalCurrencyList);
        } else {
            // Jei yra įvestas tekstas, filtruojame
            for (String item : originalCurrencyList) {
                if (item.toLowerCase().contains(query.toLowerCase())) {
                    filteredCurrencyList.add(item);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}
