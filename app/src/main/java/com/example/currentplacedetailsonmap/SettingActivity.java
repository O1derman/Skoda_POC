package com.example.currentplacedetailsonmap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SettingActivity extends AppCompatActivity {

    public static ArrayList<DataModel> dataModels;
    ListView listView;
    private CustomAdapter adapter;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SEEK_BAR1 = "seekBar1";
    public static final String SEEK_BAR2 = "seekBar2";
    public static final String SEEK_BAR1_TEXT = "seekBar1Text";
    public static final String SEEK_BAR2_TEXT = "seekBar2Text";
    public static final String CHECK_BOX = "checkBox";

    private SeekBar seekBar1;
    private SeekBar seekBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        listView = findViewById(R.id.listView);
        LinearLayout linearLayout = findViewById(R.id.settingsLayout);

        dataModels = new ArrayList<>();

        String SCVFileName = "cuisine.csv";

        try {
            Scanner inputStream = new Scanner(getAssets().open(SCVFileName));
            inputStream.nextLine();
            int count = 0;
            while (inputStream.hasNext()) {
                String data = inputStream.nextLine();
                String[] values = data.split(",");
                String name = values[1];
                boolean checked = sharedPreferences.getBoolean(CHECK_BOX + count, false);
                dataModels.add(new DataModel(name, checked, count));
                count++;
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        adapter = new CustomAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel = dataModels.get(position);
                dataModel.checked = !dataModel.checked;
                adapter.notifyDataSetChanged();
                saveCheckBoxStatus(dataModel.id, dataModel.checked);


            }
        });

        Button button = new Button(this);
        button.setText("Remove All");
        linearLayout.addView(button);

        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) button.getLayoutParams();
        ll.gravity = Gravity.CENTER;
        ll.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        button.setLayoutParams(ll);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < dataModels.size(); i++) {
                    CheckBox checkBox = findViewById(i);
                    checkBox.setChecked(false);
                    dataModels.get(i).checked = false;

                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(CHECK_BOX + i, false);
                    editor.apply();
                }

            }
        });

        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        final TextView textOfSeekBar1 = findViewById(R.id.textOfSeekBar1);
        final TextView textOfSeekBar2 = findViewById(R.id.textOfSeekBar2);

        seekBar2.setMax(30);
        loadData();
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(SEEK_BAR1, seekBar.getProgress());
                editor.putString(SEEK_BAR1_TEXT, textOfSeekBar1.getText().toString());
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textOfSeekBar1.setText(progress * 2 + " km");

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(SEEK_BAR2, seekBar.getProgress());
                editor.putString(SEEK_BAR2_TEXT, textOfSeekBar2.getText().toString());
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textOfSeekBar2.setText(progress + " min");

            }
        });
    }

    public void saveCheckBoxStatus(int id, boolean checked) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHECK_BOX + id, checked);
        editor.apply();
    }

    public void loadData() {
        TextView textOfSeekBar1 = findViewById(R.id.textOfSeekBar1);
        TextView textOfSeekBar2 = findViewById(R.id.textOfSeekBar2);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int value1 = sharedPreferences.getInt(SEEK_BAR1, 50);
        int value2 = sharedPreferences.getInt(SEEK_BAR2, 15);
        String text1 = sharedPreferences.getString(SEEK_BAR1_TEXT, "100 km");
        String text2 = sharedPreferences.getString(SEEK_BAR2_TEXT, "15 min");

        seekBar1.setProgress(value1);
        seekBar2.setProgress(value2);
        textOfSeekBar1.setText(text1);
        textOfSeekBar2.setText(text2);
    }
}

