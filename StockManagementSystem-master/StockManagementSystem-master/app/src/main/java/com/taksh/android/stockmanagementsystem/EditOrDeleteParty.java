package com.taksh.android.stockmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditOrDeleteParty extends AppCompatActivity {

    EditText partyName;
    Button save;
    Button delete;

    String party;

    SQLiteDatabase partyDatabase;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_party);

        partyName = (EditText)findViewById(R.id.partyName);
        save = (Button)findViewById(R.id.save);
        delete = (Button)findViewById(R.id.delete);

        intent = getIntent();
        party = intent.getStringExtra("party");

        partyName.setText(party);

        partyDatabase = openOrCreateDatabase(Constants.PARTY_LIST_DATBASE_NAME,MODE_PRIVATE,null);
        partyDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+Constants.PARTY_LIST_DATABASE_TABLE+"(name VARCHAR);");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                partyDatabase.execSQL("DELETE FROM "+Constants.PARTY_LIST_DATABASE_TABLE+" WHERE name = '"+party+"';");
                Toast.makeText(EditOrDeleteParty.this, "Party "+party+"deleted",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditOrDeleteParty.this, EditPartyList.class);
                partyDatabase.close();
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = partyName.getText().toString();

                partyDatabase.execSQL("DELETE FROM "+Constants.PARTY_LIST_DATABASE_TABLE+" WHERE name = '"+party+"';");
                partyDatabase.execSQL("INSERT INTO "+Constants.PARTY_LIST_DATABASE_TABLE+" VALUES('"+name+"');");
                Toast.makeText(EditOrDeleteParty.this, "Party "+name+"saved",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditOrDeleteParty.this, EditPartyList.class);
                partyDatabase.close();
                startActivity(intent);
            }
        });
    }
}
