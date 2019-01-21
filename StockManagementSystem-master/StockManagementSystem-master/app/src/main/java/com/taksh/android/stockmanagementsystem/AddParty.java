package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddParty extends AppCompatActivity {

    EditText newPartyName;
    Button confirmBtn;

    SQLiteDatabase partyDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_party);

        newPartyName = (EditText)findViewById(R.id.newPartyName);
        confirmBtn = (Button)findViewById(R.id.confirm);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPartyName.toString().trim().equalsIgnoreCase("")){
                    newPartyName.setError("Cannot be empty");
                }
                else{
                    String newParty = newPartyName.getText().toString();

                    partyDatabase = openOrCreateDatabase(Constants.PARTY_LIST_DATBASE_NAME, MODE_PRIVATE,null);
                    partyDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+Constants.PARTY_LIST_DATABASE_TABLE+"(name VARCHAR);");
                    partyDatabase.execSQL("INSERT INTO "+Constants.PARTY_LIST_DATABASE_TABLE+" VALUES('"+newParty+"');");
                    partyDatabase.close();
                    Toast.makeText(AddParty.this, "Party "+newParty+" added to the lisr",Toast.LENGTH_SHORT ).show();

                    Intent intent = new Intent(AddParty.this, EditPartyList.class);
                    startActivity(intent);
                }
            }
        });

    }
}
