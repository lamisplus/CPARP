package org.fhi360.lamis.mobile.cparp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.cparp.service.ServiceScheduler;
import org.fhi360.lamis.mobile.cparp.webservice.HttpGetRequest;
import org.fhi360.lamis.mobile.cparp.webservice.HttpPostRequest;
import org.fhi360.lamis.mobile.cparp.webservice.WebserviceResponseHandler;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.dao.AccountDAO;

public class MainActivity extends AppCompatActivity
        implements SynchronizeFragment.OnSyncButtonClickListener, ActivateFragment.OnActivateButtonClickListener {
    private String[] drawerItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ProgressDialog progressDialog;
    private Fragment attachedFragment;
    private ServiceScheduler serviceScheduler;
    private String POSITION = "position";
    private int currentPosition = 0;    //track the current Fragment
    private String SEARCH_OPENED = "search_opened";
    private boolean searchMenuOpened;  //keeps track if the search bar is opened
    private String SEARCH_QUERY = "search_query";
    private String searchQuery;    //holds current text in the search bar
    private String serverUrl;

    private Drawable iconCloseSearch;       //icon that shows when the search bar is closed (magnifier)
    private Drawable iconOpenSearch;        //icon that shows when the search bar is opened (x sign)
    private EditText searchEditText;        //search bar text field
    private MenuItem searchAction;          //search bar action button

    //Create a DrawerItemClickListener and attach it to the ListView in MainActivity onCreate method
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set a title for the dialog window
        //progressDialog.setTitle("Accessing Server");
        serverUrl = getResources().getString(R.string.server_url);

        //Use the ArrayAdapter to populate the ListView with options array items
        //Using the simple_list_item_activated_1 means that the item the user clicks on is highlighted
        drawerItems = getResources().getStringArray(R.array.drawer_items);
        drawerListView = findViewById(R.id.drawer);
        drawerListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, drawerItems));
        //Add a new instance of OnItemClickListener to the drawer's ListView.
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        //Get a reference to the DrawerLayout and create the ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.drawer_layout);

        //Getting the icons
        iconOpenSearch = getResources().getDrawable(R.drawable.ic_search);
        iconCloseSearch = getResources().getDrawable(R.drawable.ic_clear);

        //If the MainActivity is newly created, use the selectItem() method to display TopFragment
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(POSITION);
            setActionBarTitle(currentPosition);
            searchMenuOpened = savedInstanceState.getBoolean(SEARCH_OPENED);
            searchQuery = savedInstanceState.getString(SEARCH_QUERY);
        } else {
            //When MainActivity is newly created
            //Attach the HomeFragment to the Activity
            selectItem(0);
            searchMenuOpened = false;
            searchQuery = "";
        }


        //If the search bar was opened previously, open it on recreate
        if (searchMenuOpened) {
            openSearchBar(searchQuery);
        }

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            //Called when a drawer has settled in a completely closed state
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();  //Called to recreate the ActionBar menu
            }

            //Called when a drawer has settled in a completely open state
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Add a new FragmentManager OnBackStackChangedListener, implementing its onBackStakeChanged() method.
        //This method is called whenever the back stack changes.
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        attachedFragment = fragmentManager.findFragmentByTag("visible_fragment"); //Search the back stack and get the fragment with tag 'visible_fragment'

                        //Check which class the fragment currently attached to the activity is an instance of,
                        //and set currentPosition accordingly
                        if (attachedFragment instanceof PatientListFragment) {
                            currentPosition = 0;
                        }
                        if (attachedFragment instanceof DefaulterListFragment) {
                            currentPosition = 1;
                        }
                        if (attachedFragment instanceof HtcServicesFragment) {
                            currentPosition = 2;
                        }
                        if (attachedFragment instanceof ReportingPeriodFragment) {
                            currentPosition = 3;
                        }
                        if (attachedFragment instanceof SynchronizeFragment) {
                            currentPosition = 4;
                        }
                        if (attachedFragment instanceof ActivateFragment) {
                            currentPosition = 5;
                        }
                        setActionBarTitle(currentPosition);
                        drawerListView.setItemChecked(currentPosition, true);  //Set the action bar title and highlight the correct item in the drawer ListView
                    }
                }
        );
        //Set intent service and broadcast receiver alarms
        serviceScheduler = new ServiceScheduler(this);
        serviceScheduler.start();
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putInt(POSITION, currentPosition);
        saveInstanceState.putBoolean(SEARCH_OPENED, searchMenuOpened);
        saveInstanceState.putString(SEARCH_QUERY, searchQuery);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Add this method to the activity so that the state of the ActionBarDrawerToggle is in sync with the state of the drawer
        //Sync the toggle state after onRestoreInstanceState has occurred
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //If the device configuration changes, pass the details of the configuration to the ActionBarDrawerToggle
        drawerToggle.onConfigurationChanged(newConfig);
    }

    //Create the menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle ActionBarDrawerToggle clicks
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle other ActionBar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            //When the search button is clicked we have to either open or close the input field
            case R.id.action_search:
                //Code to run when search is clicked
                if (searchMenuOpened) {
                    closeSearchBar();
                } else {
                    openSearchBar(searchQuery);
                }
                break;
            case R.id.action_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;


            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    //Called when we call invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //If a drawer is open, hide action items related to the current view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListView);
        switch (currentPosition) {
            case 0:
                menu.findItem(R.id.action_search).setVisible(!drawerOpen);  //Set the visibility of the Search Patient action when the drawer is opened or closed
                break;
            case 1:
                menu.findItem(R.id.action_search).setVisible(!drawerOpen);
                break;
            case 2:
                menu.findItem(R.id.action_search).setVisible(false);
                break;
            case 3:
                menu.findItem(R.id.action_search).setVisible(false);
                break;
            case 4:
                menu.findItem(R.id.action_search).setVisible(false);
                break;
            case 5:
                menu.findItem(R.id.action_search).setVisible(false);
                break;
            default:
                menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        }
        //Extract the search menu item, we want to save in the bundle
        searchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }


    private void selectItem(int position) {
        //Switching fragment
        currentPosition = position; //update current position when an item is selected
        switch (position) {
            case 0:
                attachedFragment = new PatientListFragment();
                break;
            case 1:
                attachedFragment = new DefaulterListFragment();
                break;
            case 2:
                attachedFragment = new HtcServicesFragment();
                break;
            case 3:
                attachedFragment = new ReportingPeriodFragment();
                break;
            case 4:
                attachedFragment = new SynchronizeFragment();
                break;
            case 5:
                int id = new AccountDAO(this).getAccountId();
                if (id == 0) {
                    attachedFragment = new ActivateFragment();
                } else {
                    attachedFragment = new AccountMessageFragment();
                }
                break;
            default:
                attachedFragment = new PatientListFragment();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, attachedFragment, "visible_fragment"); //Replace the place holder (FrameLayout) for fragments layout on the main activity
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        //After switching the fragment, change the action bar title to reflect the fragment displayed
        setActionBarTitle(position);
        //Close the drawer
        drawerLayout.closeDrawer(drawerListView);
    }

    private void setActionBarTitle(int position) {
        String title;
        //If the user click on the 'Home' option, use the app name for the title
        //Otherwise, get the string from the titles array for the position that was clicked and use that
        if (position == 0) {
            title = getResources().getString(R.string.title_activity_main);
        } else {
            if (position == 1) {
                title = getResources().getString(R.string.title_activity_defaulter);
            } else {
                title = drawerItems[position];
            }
        }
        getSupportActionBar().setTitle(title); //Display the title in the action bar
    }

    //This method sets a custom edit text view on the action bar
    //A textChangedListener (implemented by a TextWatcher) is set on the edit text view, the content of the text view is used to filter the the patient list
    private void openSearchBar(String queryText) {
        //Get an action bar and the set the custom view (search_bar layout) on action bar
        //The search bar layout contains an editable text view
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_bar);

        //attachedFragment = getFragmentManager().findFragmentByTag("visible_fragment");

        //The the editable text view from the search layout and set a TextChangedListener to listen to text changed
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
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (attachedFragment instanceof PatientListFragment) {
                ((PatientListFragment) attachedFragment).refreshListView(s);
            }
            if (attachedFragment instanceof DefaulterListFragment) {
                ((DefaulterListFragment) attachedFragment).refreshListView(s);
            }
        }
    };

    //Upload content to the server only
    @Override
    public void sync(String method, String url, String content) {
        //Create a progress dialog window
        progressDialog = new ProgressDialog(this);
        //Close window on pressing the back button
        progressDialog.setCancelable(false);
        //Set a message
        progressDialog.setMessage("Please wait sync in progress.....");
        progressDialog.show();
        url = serverUrl + url;
        new WebserviceInvocator(this).execute(method, url, content);
    }

    @Override
    public void activate(String method, String url) {
        //Create a progress dialog window
        progressDialog = new ProgressDialog(this);
        //Close window on pressing the back button
        progressDialog.setCancelable(false);
        //Set a message
        progressDialog.setMessage("Please wait sync in progress.....");
        progressDialog.show();
        url = serverUrl + url;
        Log.v("URL", url);
        new WebserviceInvocator(this).execute(method, url);
    }

    //Perform network access on a different thread from the UI thread using AsyncTask`
    private class WebserviceInvocator extends AsyncTask<String, Void, String> {
        Context context;

        WebserviceInvocator(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String method = params[0];
            if (method.equalsIgnoreCase("POST"))
                result = new HttpPostRequest(context).postData(params[1], params[2]);

            if (method.equalsIgnoreCase("GET"))
                result = new HttpGetRequest(context).getData(params[1]);
            progressDialog.dismiss();


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!result.isEmpty()) new WebserviceResponseHandler(context).parseResult(result);
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        serviceScheduler.cancel();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
