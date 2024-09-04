package com.example.quiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;

public class OpeningActivity extends AppCompatActivity {
Button playButton,chose_category,prize_range;
ArrayList<String> category;
String Category_Selected="Any Category";
HashMap<String,Integer> categoryFlagValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opening);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        playButton=findViewById(R.id.playButton);
        chose_category=findViewById(R.id.chose_category);
        prize_range=findViewById(R.id.prize_range);
        setCategoryVal();
        setCategoryFlagValue();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iMain=new Intent(OpeningActivity.this,MainActivity.class);
                iMain.putExtra("Flag",categoryFlagValue.get(Category_Selected));
                startActivity(iMain);
            }
        });
        chose_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(OpeningActivity.this);
                dialog.setContentView(R.layout.dialog_layout);


                Button spinnerBtn=dialog.findViewById(R.id.spinnerBtn);
                Spinner spinner=dialog.findViewById(R.id.spinner);
                ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<>(OpeningActivity.this, android.R.layout.simple_spinner_dropdown_item,category);
                spinner.setAdapter(spinnerAdapter);
                spinnerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Category_Selected=spinner.getSelectedItem().toString();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        prize_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OpeningActivity.this, "Working on this Activity", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setCategoryVal()
    {
        category=new ArrayList<>();
        category.add("Any Category");
        category.add("General Knowledge");
        category.add("Entertainment : Books");
        category.add("Entertainment : Film");
        category.add("Entertainment : Music");
        category.add("Entertainment : Musical & Theaters");
        category.add("Entertainment : Television");
        category.add("Entertainment : Video Games");
        category.add("Entertainment : Board Games");
        category.add("Entertainment : Cartoon & Animation");
        category.add("Entertainment : Anime & Manga");
        category.add("Entertainment : Comics");
        category.add("Any Category");
        category.add("Science & Nature");
        category.add("Science : Mathematics");
        category.add("Science : Computers");
        category.add("Science : Gadgets");
        category.add("Mythology");
        category.add("Sports");
        category.add("Geography");
        category.add("History");
        category.add("Politics");
        category.add("Arts");
        category.add("Celebrities");
        category.add("Animals");
        category.add("Vehicles");
    }
    private void setCategoryFlagValue()
    {
        categoryFlagValue=new HashMap<>();
        categoryFlagValue.put("Any Category",0);
        categoryFlagValue.put("General Knowledge",9);
        categoryFlagValue.put("Entertainment : Books",10);
        categoryFlagValue.put("Entertainment : Film",11);
        categoryFlagValue.put("Entertainment : Music",12);
        categoryFlagValue.put("Entertainment : Musical & Theaters",13);
        categoryFlagValue.put("Entertainment : Television",14);
        categoryFlagValue.put("Entertainment : Video Games",15);
        categoryFlagValue.put("Entertainment : Board Games",16);
        categoryFlagValue.put("Entertainment : Cartoon & Animation",32);
        categoryFlagValue.put("Entertainment : Anime & Manga",31);
        categoryFlagValue.put("Entertainment : Comics",29);
        categoryFlagValue.put("Science & Nature",17);
        categoryFlagValue.put("Science : Mathematics",19);
        categoryFlagValue.put("Science : Computers",18);
        categoryFlagValue.put("Science : Gadgets",30);
        categoryFlagValue.put("Mythology",20);
        categoryFlagValue.put("Sports",21);
        categoryFlagValue.put("Geography",22);
        categoryFlagValue.put("History",23);
        categoryFlagValue.put("Politics",24);
        categoryFlagValue.put("Arts",25);
        categoryFlagValue.put("Celebrities",26);
        categoryFlagValue.put("Animals",27);
        categoryFlagValue.put("Vehicles",28);
    }
}