package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import org.fhi360.lamis.mobile.cparp.adapter.DefaulterListAdapter;
import org.fhi360.lamis.mobile.cparp.dao.PatientDAO;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;

import java.util.ArrayList;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class DefaulterListActivity extends AppCompatActivity {
    private int patientId;
    private int facilityId;
    private Cursor cursor;
    private SQLiteDatabase db;
    private DefaulterListAdapter adapter;
    private ListView defaulterListView;
    private ArrayList<Patient> patients;
    private SharedPreferences preferences;
    private String SEARCH_OPENED = "search_opened";
    private boolean searchMenuOpened;  //keeps track if the search bar is opened
    private String SEARCH_QUERY = "search_query";
    private String searchQuery;    //holds current text in the search bar
    private Drawable iconCloseSearch;       //icon that shows when the search bar is closed (magnifier)
    private Drawable iconOpenSearch;        //icon that shows when the search bar is opened (x sign)
    private EditText searchEditText;        //search bar text field
    private MenuItem searchAction;          //search bar action button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaulter_list);

        //Getting the icons
        iconOpenSearch = getResources().getDrawable(R.drawable.ic_search);
        iconCloseSearch = getResources().getDrawable(R.drawable.ic_clear);

        if (savedInstanceState != null) {
            searchMenuOpened = savedInstanceState.getBoolean(SEARCH_OPENED);
            searchQuery = savedInstanceState.getString(SEARCH_QUERY);
        }
        else {
            searchMenuOpened = false;
            searchQuery = "";
        }

        //If the search bar was opened previously, open it on recreate
        if(searchMenuOpened) {
            openSearchBar(searchQuery);
        }

        defaulterListView = (ListView) findViewById(R.id.defaulterlistView);
        patients = new PatientDAO(this).getDefaulters();

        adapter = new DefaulterListAdapter(this, patients);
        defaulterListView.setAdapter(adapter);
        defaulterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                savePreferences(patients.get(position));
                Intent intent = new Intent(getApplicationContext(), ClientTrackingActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Defaulters");
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putBoolean(SEARCH_OPENED, searchMenuOpened);
        saveInstanceState.putString(SEARCH_QUERY, searchQuery);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle other ActionBar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            //When the search button is clicked we have to either open or close the input field
            case R.id.action_search:
                if(searchMenuOpened) {
                    closeSearchBar();
                }
                else {
                    openSearchBar(searchQuery);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Extract the search menu item, we want to save in the bundle
        searchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    //This method sets a custom edit text view on the action bar
    //A textChangedListener (implemented by a TextWatcher) is set on the edit text view, the content of the text view is used to filter the the patient list
    private void openSearchBar(String queryText) {
        //Get an action bar and the set the custom view (search_bar layout) on action bar
        //The search bar layout contains an editable text view
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_bar);

        //Get the editable text view from the search layout and set a TextChangedListener to listen to text changed
        searchEditText = (EditText) getSupportActionBar().getCustomView().findViewById(R.id.editSearch);
        searchEditText.addTextChangedListener(filterTextWatcher);
        searchEditText.setText(queryText);
        searchEditText.requestFocus();

        //Change search icon accordingly
        searchAction.setIcon(iconCloseSearch);
        searchMenuOpened = true;
    }

    private void closeSearchBar() {
        //Remove custom view
        getSupportActionBar().setDisplayShowCustomEnabled(false);

        //Change search icon accordingly
        searchAction.setIcon(iconOpenSearch);
        searchMenuOpened = false;
    }

    //The TextChangedListener reads the text entered in the editable view and pass the content to the refreshView method of the HomeFragment
    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            refreshListView(s);
        }
    };

    public void refreshListView(CharSequence s) {
        if (!s.toString().equals("")) {
            patients = new PatientDAO(this).getDefaulters(s.toString());
        }
        else {
            patients = new PatientDAO(this).getDefaulters();
        }
        adapter = new DefaulterListAdapter(this, patients);
        defaulterListView.setAdapter(adapter);
    }

    private void savePreferences(Patient patient) {
        preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("patient", new Gson().toJson(patient));
        editor.putBoolean("edit_mode", false);
        editor.commit();

    }

}
