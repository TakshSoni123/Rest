package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import maes.tech.intentanim.CustomIntent;

public class SelectNumberOfSets extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    QueryDetails queryDetails;

    int noOfSets=-1;
    int weight=-1;
    EditText editText1;
    EditText editText2;
    Button button;

    int bundles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_number_of_sets);

        intent=getIntent();
        bundle = intent.getExtras();

        queryDetails = (QueryDetails)bundle.getSerializable(Constants.BUNDLE_KEY);

        editText1 = (EditText)findViewById(R.id.numberOfSets);
        editText2 = (EditText) findViewById(R.id.weight);

        button = (Button)findViewById(R.id.confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText1.getText().toString().trim().equalsIgnoreCase("")) {
                    noOfSets = Integer.valueOf(editText1.getText().toString());
                }
                else{
                    editText1.setError("Cannot be empty");
                }

                if (!editText2.getText().toString().trim().equalsIgnoreCase("")) {
                    weight = Integer.parseInt(editText2.getText().toString());
                }
                else {
                    editText2.setError("Cannot be empty");
                }
                if (noOfSets !=-1 && weight !=-1) {
                    queryDetails.totalNumberOfSets = noOfSets;
                    queryDetails.totalWeight = weight;

                    bundle.putSerializable(Constants.BUNDLE_KEY, queryDetails);
                    Intent intent = new Intent(SelectNumberOfSets.this, ShowPreview.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    CustomIntent.customType(SelectNumberOfSets.this,"fadein-to-fadeout");
                }
            }
        });




    }

}
