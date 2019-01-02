package com.example.user.gmailappnavigationdrawerclone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView imageViewArrow;
    private FrameLayout layoutContainer;

    //Configuration change & menu inflate change variables
    private boolean isHeaderOpened;
    private int menuOneSelectedItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        handleHeaderToggle();

        if (savedInstanceState == null) {
            initializeDefault();
        } else {
            restoreState(savedInstanceState);
        }
    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.main_drawerlayout_id);
        navigationView = findViewById(R.id.main_navigationview_id);
        toolbar = findViewById(R.id.main_toolbar_id);
        layoutContainer = findViewById(R.id.main_framelayout_id);

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        toggleNavigationDrawer();

        // Set dummy data of 5 unread messages in menu_primary menu item.
        displayCounter(R.id.menu_primary, 5);
        displayCounter(R.id.menu_social, 2);
    }

    /**
     * Initialise default if savedInstanceState is null
     */
    private void initializeDefault() {
        navigationView.setCheckedItem(R.id.menu_primary);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout_id,
                new PrimaryFragment()).commit();
        menuOneSelectedItemId = R.id.menu_primary;
    }

    /**
     * Restore state if savedInstanceState is not null
     * @param bundle
     */
    private void restoreState(Bundle bundle) {
        isHeaderOpened = bundle.getBoolean("isHeaderOpened");
        menuOneSelectedItemId = bundle.getInt("menuOneSelectedItemId");
        Log.d("MYTAG", "initializeDefault: boolean is " + isHeaderOpened);
        if (isHeaderOpened) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.nav_menu_two);
            imageViewArrow.setSelected(true);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.nav_menu_one);
            imageViewArrow.setSelected(false);
        }
    }

    /**
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isHeaderOpened", isHeaderOpened);
        outState.putInt("menuOneSelectedItemId", menuOneSelectedItemId);
        Log.d("MYTAG", "onSaveInstanceState: Saved variable as" + isHeaderOpened);
    }

    /**
     * Handle what happens when the toggle arrow is pressed
     */
    private void handleHeaderToggle() {
        View header = navigationView.getHeaderView(0);
        imageViewArrow = header.findViewById(R.id.nav_header_arrow);

        imageViewArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isSelected()) {
                    // Navigation drawer header is closed to begin with - now is opened
                    Log.d("MYTAG", "onClick: Setting to true");
                    isHeaderOpened = true;
                    menuOneSelectedItemId = navigationView.getCheckedItem().getItemId();
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.nav_menu_two);
                    v.setSelected(true);
                } else {
                    Log.d("MYTAG", "onClick: Setting to false");
                    isHeaderOpened = false;
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.nav_menu_one);
                    navigationView.setCheckedItem(menuOneSelectedItemId);
                    v.setSelected(false);
                }
            }
        });
    }

    /**
     * Creates an instance of the ActionBarDrawerToggle class:
     * 1) Handles opening and closing the navigation drawer
     * 2) Creates a hamburger icon in the toolbar
     * 3) Attaches listener to open/close drawer on icon clicked and rotates the icon
     */
    private void toggleNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Sets the counter for MenuItem with id of menuItemId with int count.
     *
     * @param menuItemId
     * @param count
     */
    private void displayCounter(int menuItemId, int count) {
        int noOfItems = navigationView.getMenu().size();
        for (int i = 0; i < noOfItems; i++) {
            MenuItem item = navigationView.getMenu().getItem(i);
            if (item.getItemId() == menuItemId) {
                TextView counter = (TextView) item.getActionView();
                counter.setText(Integer.toString(count));
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == menuOneSelectedItemId) {
            // Do nothing so that the fragment is not re-initialised
            Log.d("menu_primary", "onNavigationItemSelected: Same menuitem");
        } else {
            switch (menuItem.getItemId()) {
                case R.id.menu_primary:
                    Log.d("menu_primary", "onNavigationItemSelected:menu_primary ");
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout_id,
                            new PrimaryFragment()).commit();
                    break;
                case R.id.menu_social:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout_id,
                            new SocialFragment()).commit();
                    break;
                case R.id.menu_promotions:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout_id,
                            new PromotionsFragment()).commit();
                    break;
                case R.id.nav_menu_addaccount:
                    Toast.makeText(this, "Add accounts item pressed", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_menu_manageaccounts:
                    Toast.makeText(this, "Manage accounts item pressed", Toast.LENGTH_SHORT).show();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
