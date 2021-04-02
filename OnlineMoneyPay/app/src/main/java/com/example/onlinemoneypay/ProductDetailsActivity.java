package com.example.onlinemoneypay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.onlinemoneypay.MainActivity.showCart;
import static com.example.onlinemoneypay.RegisterActivity.setSignUpFragment;


public class ProductDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ProductDetailsActivity";
    public static boolean running_rating_query = false;
    private ViewPager productImagesViewPager;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TextView rewardTitle;
    private TextView rewardBody;
    // product description
    private TabLayout viewpagerIndicator;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTablayout;
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;

    private TextView productOnlyDescriptionBody;


    private String productDescription;
    private String productOtherDetails;
    // public static int tabPosition = -1;
    public static List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    // product description

    private LinearLayout coupenRedemptionLayout;
    private Button coupenRedeemBtn;


    /////// rating Layout...
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private TextView averageRating;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsNoContainer;
    private LinearLayout ratingsProgressBarContainer;
    private LinearLayout addToCartBtn;

    ////coupendialog
    public static TextView coupenTitle;
    public static TextView coupenExpiryDate;
    public static TextView coupenBody;
    private static RecyclerView coupensRecyclerView;
    private static LinearLayout selectedCoupen;
    private Button applyBtn;
    public static String selectedRewardId;
    public static TextView originalPrice;
    public static TextView discountedPrice;
    public static String   forCalculationOriginalPrice;

    ////coupendialog

    private Dialog signInDialog;
    public static int initialRating;
    /////// rating Layout...
    private Button buyNowBtn;

    private FloatingActionButton addToWishlistBtn;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;

    private FirebaseFirestore firebaseFirestore;
    private String product_ID;
    private FirebaseUser currentUser;
    public static List<UserWishListProductModel> userWishListProductModelList = new ArrayList<>();

    private DocumentSnapshot documentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Product Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);
        productDetailsViewPager = findViewById(R.id.product_details_viewpager);
        productDetailsTablayout = findViewById(R.id.product_details_tablayout);
        buyNowBtn = findViewById(R.id.buy_now_btn);
        coupenRedeemBtn = findViewById(R.id.coupen_redemption_btn);
        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_ratings_miniview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        codIndicator = findViewById(R.id.cod_indicator_imageview);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_numbers_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
        coupenRedemptionLayout = findViewById(R.id.coupen_redemption_layout);
        firebaseFirestore = FirebaseFirestore.getInstance();
        averageRating = findViewById(R.id.average_rating);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        List<String> productImages = new ArrayList<>();
        product_ID = getIntent().getStringExtra("PRODUCT_ID");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        initialRating = -1;
        firebaseFirestore.collection("PRODUCTS").document(product_ID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    for (long x = 1; x <= (long) documentSnapshot.get("no_of_product_images"); x++) {
                        productImages.add(documentSnapshot.get("product_image_" + x).toString());
                    }

                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);
                    productTitle.setText(documentSnapshot.get("product_title").toString());
                    averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                    totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                    productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
                     forCalculationOriginalPrice=documentSnapshot.get("product_price").toString();
                    cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");
                    if ((boolean) documentSnapshot.get("COD")) {
                        codIndicator.setVisibility(View.VISIBLE);
                        tvCodIndicator.setVisibility(View.VISIBLE);
                    } else {
                        codIndicator.setVisibility(View.INVISIBLE);
                        tvCodIndicator.setVisibility(View.INVISIBLE);
                    }
                    rewardTitle.setText((long) documentSnapshot.get("free_coupens") + " " + documentSnapshot.get("free_coupen_title").toString());
                    rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());
                    if ((boolean) documentSnapshot.get("use_tab_layout")) {

                        productDetailsTabsContainer.setVisibility(View.VISIBLE);
                        productDetailsOnlyContainer.setVisibility(View.GONE);
                        productDescription = documentSnapshot.get("product_description").toString();
                        productOtherDetails = documentSnapshot.get("product_other_details").toString();
                        //                   ProductSpecificationFragment.productSpecificationModelList = new ArrayList<>();
                        for (long x = 1; x <= (long) documentSnapshot.get("total_spec_titles"); x++) {
                            productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                            for (long y = 1; y <= (long) documentSnapshot.get("spec_title_" + x + "_total_fields"); y++) {
                                productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));
                                Log.d(TAG, " for X " + x + " Y is: " + y);
                            }
                            Log.d(TAG, "onComplete Y loop completed  X is : " + x);
                        }

                    } else {

                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                        productDetailsTabsContainer.setVisibility(View.GONE);
                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);

                    }
                    totalRatings.setText("(" + (long) documentSnapshot.get("total_ratings") + ") ratings");

                    for (int x = 0; x < 5; x++) {
                        TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                        rating.setText(String.valueOf((long) documentSnapshot.get(5 - x + "_star")));
                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                        int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                        progressBar.setMax(maxProgress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get(5 - x + "_star"))));
                    }

                    totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                    averageRating.setText(documentSnapshot.get("average_rating").toString());

                    productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTablayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }
        });

        viewpagerIndicator.setupWithViewPager(productImagesViewPager, true);

        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    if (ALREADY_ADDED_TO_WISHLIST) {
//                        DBqueries.removeWishlist(ProductDetailsActivity.this, product_ID);
                        ALREADY_ADDED_TO_WISHLIST = false;
                        Toast.makeText(ProductDetailsActivity.this, "Already Added to WishList", Toast.LENGTH_SHORT).show();
                        addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                    } else {
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                        addWishListInFireBase(product_ID);
                    }
                }
            }
        });
        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /////// rating Layout...

        rateNowContainer = findViewById(R.id.rate_now_container);

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {


//                        if (!running_rating_query) {
//                            running_rating_query=true;
                        setRating(starPosition);
                        if (DBqueries.myRatedIds.contains(product_ID)) {
                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                            Map<String, Object> updateRating = new HashMap<>();
                            updateRating.put(initialRating - 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                            updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                            firebaseFirestore.collection("PRODUCTS").document(product_ID)
                                    .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Map<String, Object> rating = new HashMap<>();
                                        rating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                        rating.put("product_ID_" + DBqueries.myRatedIds.size(), product_ID);
                                        rating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);

                                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                .update(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DBqueries.myRatedIds.add(product_ID);
                                                    DBqueries.myRating.add((long) starPosition + 1);
                                                    TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                    rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                    //     totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings")+1 + ")ratings");
                                                    //     totalRatings.setText("(" + (long) documentSnapshot.get("total_ratings")+1 + ") ratings");
                                                    //    totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")+1));

                                                    //       averageRating.setText(String.valueOf( calculateAverageRating(starPosition + 1)));
                                                    //     averageRatingMiniView.setText(String.valueOf( calculateAverageRating(starPosition + 1)));

                                                    for (int x = 0; x < 5; x++) {
                                                        TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);
                                                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                        int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));
                                                        progressBar.setMax(maxProgress);
                                                        progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                    }
                                                    Toast.makeText(ProductDetailsActivity.this, "Thank you! for rating.", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    setRating(initialRating);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {

                                    }
                                }
                            });
                        } else {

                            Map<String, Object> productRating = new HashMap<>();
                            productRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                            //   productRating.put("average_rating", calculateAverageRating(starPosition + 1));
                            //  productRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);


                            firebaseFirestore.collection("PRODUCTS").document(product_ID)
                                    .update(productRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Map<String, Object> rating = new HashMap<>();
                                        rating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                        rating.put("product_ID_" + DBqueries.myRatedIds.size(), product_ID);
                                        rating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);

                                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                .update(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DBqueries.myRatedIds.add(product_ID);
                                                    DBqueries.myRating.add((long) starPosition + 1);
                                                    TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                    rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));


                                                    for (int x = 0; x < 5; x++) {
                                                        TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);
                                                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                        int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));
                                                        progressBar.setMax(maxProgress);
                                                        progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                    }
                                                    Toast.makeText(ProductDetailsActivity.this, "Thank you! for rating.", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    setRating(initialRating);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        setRating(initialRating);
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        //                      }
                    }

                }
            });
        }
        /////// rating Layout...

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(deliveryIntent);

                }
            }
        });
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    // to do: add to cart
                    addToCartInFirebase(product_ID);
                    Toast.makeText(ProductDetailsActivity.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /////// coupen  diaglog....

        //forCalculationOriginalPrice=productPrice.getText().toString();
        MyRewardAdapter myRewardAdapter = new MyRewardAdapter(DBqueries.rewardModelList, true);


        Dialog checkCoupenPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView toggleRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
        coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
        selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);

        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
        coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_validity);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);
        applyBtn = checkCoupenPriceDialog.findViewById(R.id.apply_button);

        originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
        discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupensRecyclerView.setLayoutManager(layoutManager);
        //   List<RewardModel> rewardModelList = new ArrayList<>();
        coupensRecyclerView.setAdapter(myRewardAdapter);
        myRewardAdapter.notifyDataSetChanged();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USERS_REWARDS").document(selectedRewardId)
                        .update("is_applied",true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()){
                              Log.d(TAG, "onClick: "+selectedRewardId);
                              Toast.makeText(ProductDetailsActivity.this, "Congratulation, coupen has been Applied!", Toast.LENGTH_SHORT).show();
                          }else {
                              String error=task.getException().getMessage();
                              Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                          }
                    }
                });

                }
        });
        /////// coupen  diaglog....

        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCoupenPriceDialog.show();

            }
        });

// sigin dialog

        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);
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

// sigin dialog

    }

    public static void addToCartInFirebase(String product_ID) {
        Map<String, Object> updateList = new HashMap<>();
        Map<String, Object> updateProduct = new HashMap<>();
        String id = product_ID;
        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int i = Integer.parseInt(task.getResult().get("list_size").toString()) + 1;
                // userWishListProductModelList.add(new UserWishListProductModel((int) task.getResult().get("list_size")+1,id));
                updateList.put("list_size", (long) task.getResult().get("list_size") + 1);
                updateProduct.put("product_ID_" + i, id);
                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                        .update(updateList);
                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                        .update(updateProduct);
                Log.d(TAG, "onComplete: Added To Cart Successfully" + updateList + "  " + updateProduct);
            }
        });
    }

    public static void addWishListInFireBase(String product_ID) {
        Map<String, Object> updateList = new HashMap<>();
        Map<String, Object> updateProduct = new HashMap<>();
        String id = product_ID;
        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int i = Integer.parseInt(task.getResult().get("list_size").toString()) + 1;
                // userWishListProductModelList.add(new UserWishListProductModel((int) task.getResult().get("list_size")+1,id));
                updateList.put("list_size", (long) task.getResult().get("list_size") + 1);
                updateProduct.put("product_ID_" + i, id);
                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                        .update(updateList);
                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                        .update(updateProduct);
                Log.d(TAG, "onComplete: Added To Wishlist" + updateList + "  " + updateProduct);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser != null) {
            coupenRedemptionLayout.setVisibility(View.VISIBLE);

            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        long listSize = (long) task.getResult().get("list_size");
                        for (long x = 1; x <= listSize; x++) {
                            String id = task.getResult().get("product_ID_" + x).toString();
                            if (id.equals(product_ID)) {
                                Log.d(TAG, "onComplete: " + product_ID);
                                Log.d(TAG, "onComplete: Accessing Id.." + id);
                                addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                            }
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            DBqueries.loadRatingList(ProductDetailsActivity.this, product_ID);
            DBqueries.loadReward(ProductDetailsActivity.this, false);


        }

    }

    public static void showDialogRecyclerView() {
        if (coupensRecyclerView.getVisibility() == View.GONE) {
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        } else {
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

    public static void setRating(int starPosition) {

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));

            }
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            // to do search..

            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);

            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}