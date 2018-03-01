package comq.russia.app_sqlite_demo_1.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import comq.russia.app_sqlite_demo_1.R;
import comq.russia.app_sqlite_demo_1.database.DBHelper;

/**
 * Created by VLADIMIR PUTIN on 2/28/2018.
 */

public class DisplayContact extends AppCompatActivity {
    int from_Where_I_Am_Coming = 0;
    private DBHelper mydb;
    EditText editTextName;
    EditText editTextPhone;
    EditText editTextEmail;
    EditText editTextStreet;
    EditText editTextPlace;
    Button saveContact;
    int id_To_Update = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectView();
    }

    private void connectView() {
        editTextName = findViewById(R.id.editName);
        editTextPhone = findViewById(R.id.editPhone);
        editTextEmail = findViewById(R.id.editEmail);
        editTextPlace = findViewById(R.id.editCountry);
        editTextStreet = findViewById(R.id.editStreet);
        saveContact = findViewById(R.id.btn_SaveContact);
        mydb = new DBHelper(this);
        /*use Bundle to tranfer data*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();
                String name = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String email = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
                String phone = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
                String street = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_STREET));
                String place = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                getMenuInflater().inflate(R.menu.display_contact, menu);
            } else {
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Edit_Contact: {
                editTextName.setEnabled(true);
                editTextName.setFocusableInTouchMode(true);
                editTextName.setClickable(true);

                editTextPhone.setEnabled(true);
                editTextPhone.setFocusableInTouchMode(true);
                editTextPhone.setClickable(true);

                editTextEmail.setEnabled(true);
                editTextEmail.setClickable(true);
                editTextEmail.setFocusableInTouchMode(true);

                editTextStreet.setEnabled(true);
                editTextStreet.setFocusableInTouchMode(true);
                editTextStreet.setClickable(true);

                editTextPlace.setClickable(true);
                editTextPlace.setFocusableInTouchMode(true);
                editTextPlace.setClickable(true);
                return true;
            }
            case R.id.Delete_Contact: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.delete_contact).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mydb.deleteContact(id_To_Update);
                        Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // user cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Are you sure?");
                d.show();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void run(View view) {
        /*use Bundle to transfer data*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateContact(id_To_Update, editTextName.getText().toString(), editTextPhone.getText().toString(), editTextEmail.getText().toString(), editTextStreet.getText().toString(), editTextPlace.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Update", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (mydb.inSertContact(editTextName.getText().toString(), editTextPhone.getText().toString(), editTextEmail.getText().toString(), editTextStreet.getText().toString(), editTextPlace.getText().toString())) {
                Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
