package com.example.onlinemoneypay;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.onlinemoneypay.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.onlinemoneypay.RegisterActivity.setSignUpFragment;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static List<String> orderID=new ArrayList<>();
    private FrameLayout frameLayout;
    private FirebaseUser currentUser;
    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int REWARDS_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static Boolean showCart = false;
    private int currentFragment = -1;
    private ImageView actionBarLogo;
    private NavigationView navigationView;
    private Window window;
    private Toolbar toolbar;
    private Dialog signInDialog;
    public static DrawerLayout drawer;
    // User Profile..
    private CircleImageView userProfile;
    private TextView userName, userEmail;
    private ImageView profileIcon;
    // User Profile..


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        actionBarLogo = findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        frameLayout = findViewById(R.id.main_framelayout);

        userProfile = navigationView.getHeaderView(0).findViewById(R.id.main_profile_image);
        userName = navigationView.getHeaderView(0).findViewById(R.id.main_fullname);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.main_email);
        profileIcon = navigationView.getHeaderView(0).findViewById(R.id.addProfileIcon);
        if (showCart) {
            drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart", new MyCartFragment(), -2);
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.open, R.string.close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            setFragment(new HomeFragment(), HOME_FRAGMENT);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (currentUser != null) {
                    int id = item.getItemId();
                    if (id == R.id.nav_my_mall) {
                        actionBarLogo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new HomeFragment(), HOME_FRAGMENT);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    } else if (id == R.id.nav_my_orders) {
                        gotoFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    } else if (id == R.id.nav_my_rewards) {

                        gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);

                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    } else if (id == R.id.nav_my_cart) {


                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    } else if (id == R.id.nav_my_wishlist) {
                        gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    } else if (id == R.id.nav_my_account) {
                        gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);

                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    } else if (id == R.id.nav_my_signout) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                        finish();

                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }

                } else {
                    signInDialog.show();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }


                return false;

            }

        });


        signInDialog = new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

        FirebaseFirestore.getInstance().collection("ORDERS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        orderID.add( documentSnapshot.get("OrderID").toString());
                    }
                }
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {

            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {
            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DBqueries.email = task.getResult().get("email").toString();
                        DBqueries.name = task.getResult().get("name").toString();
                        DBqueries.profile = task.getResult().get("profile").toString();
                        userName.setText(DBqueries.name);
                        userEmail.setText(DBqueries.email);
                        if (DBqueries.profile.isEmpty()) {
                             profileIcon.setVisibility(View.VISIBLE);
                        } else {
                            profileIcon.setVisibility(View.INVISIBLE);
                            Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.profile_placeholder)).into(userProfile);
                            Log.d(TAG, "onComplete: gettting Name and Email.. " + DBqueries.email + DBqueries.name);
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });


            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);

           DBqueries.checkNotifications(false,null);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        DBqueries.checkNotifications(true,null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
                super.onBackPressed();

            } else {
                if (showCart) {
                    showCart = false;
                    finish();
                } else {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment == HOME_FRAGMENT) {

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);

        }


        return true;

        // Inflate the menu; this adds items to the action bar if it is present.
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_search_icon) {
            Intent searchIntent=new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (id == R.id.main_notification_icon) {
            // to do notification..
             Intent notificationIntent=new Intent(this, NotificationActivity.class);
             startActivity(notificationIntent);
            //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();

            return true;
        } else if (id == R.id.main_cart_icon) {

            if (currentUser == null) {
                signInDialog.show();

            } else {
                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);

            }

            return true;
        } else if (id == android.R.id.home) {

            if (showCart) {
                showCart = false;
                finish();
                return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {

        actionBarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment, fragmentNo);
        if (fragmentNo == CART_FRAGMENT) {
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }

    private void setFragment(Fragment fragment, int fragmentNo) {

        if (currentFragment != fragmentNo) {
            if (fragmentNo == REWARDS_FRAGMENT) {
                window.setStatusBarColor(Color.parseColor("#0C0C25"));
                toolbar.setBackgroundColor(Color.parseColor("#0C0C25"));
            } else {
                window.setStatusBarColor(Color.parseColor("#0B0B47"));
                toolbar.setBackgroundColor(Color.parseColor("#0B0B47"));
                // red #E60000
            }
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }

}