package com.example.onlinemoneypay;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {
    private static final String TAG = "DBqueries";
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static String email, name, profile;
    public static List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();
    public static List<String> idList = new ArrayList<>();
    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();
    public static int selectedAddress = -1;
    public static List<AddressesModel> addressesModelList = new ArrayList<>();
    public static List<RewardModel> rewardModelList = new ArrayList<>();

    public static void loadCategories(RecyclerView categoryRecyclerView, Context context) {


        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public static void loadFragmentdata(RecyclerView homepageRecyclerView, Context context, final int index, String categoryName) {

        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if ((long) documentSnapshot.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                    for (long x = 1; x <= no_of_banners; x++) {
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + x).toString()
                                                , documentSnapshot.get("banner_" + x + "_background").toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0, sliderModelList));


                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    lists.get(index).add(new HomePageModel(1, documentSnapshot.get("strip_ad_banner").toString()
                                            , documentSnapshot.get("background").toString()));


                                } else if ((long) documentSnapshot.get("view_type") == 2) {

                                    List<WishlistModel> viewAllProductList = new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x <= no_of_products; x++) {

                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_" + x).toString(), documentSnapshot.get("product_image_" + x).toString(), documentSnapshot.get("product_title_" + x).toString(), documentSnapshot.get("product_subtitle_" + x).toString(), documentSnapshot.get("product_price_" + x).toString()));
                                        viewAllProductList.add(new WishlistModel(documentSnapshot.get("product_ID_" + x).toString()
                                                , documentSnapshot.get("product_image_" + x).toString()
                                                , documentSnapshot.get("product_full_title_" + x).toString()
                                                , (long) documentSnapshot.get("free_coupens_" + x)
                                                , documentSnapshot.get("average_rating_" + x).toString()
                                                , (long) documentSnapshot.get("total_ratings_" + x)
                                                , documentSnapshot.get("product_price_" + x).toString()
                                                , documentSnapshot.get("cutted_price_" + x).toString()
                                                , (boolean) documentSnapshot.get("COD_" + x)));


                                    }
                                    lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList, viewAllProductList));


                                } else if ((long) documentSnapshot.get("view_type") == 3) {
                                    List<HorizontalProductScrollModel> GridLayoutModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x <= no_of_products; x++) {

                                        GridLayoutModelList.add(new HorizontalProductScrollModel(
                                                documentSnapshot.get("product_ID_" + x).toString(),
                                                documentSnapshot.get("product_image_" + x).toString(),
                                                documentSnapshot.get("product_title_" + x).toString(),
                                                documentSnapshot.get("product_subtitle_" + x).toString(),
                                                documentSnapshot.get("product_price_" + x).toString()));

                                    }
                                    lists.get(index).add(new HomePageModel(3, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), GridLayoutModelList));

                                }
                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homepageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    public static void removeWishlist(Context context, String productID) {
        ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST");
        Map<String, Object> updates = new HashMap<>();
        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (task.isSuccessful()) {
                    long listSize = (long) task.getResult().get("list_size");
                    int intlist = Integer.parseInt(String.valueOf(listSize));
                    idList.clear();
                    for (int x = 1; x <= intlist; x++) {
                        idList.add(documentSnapshot.get("product_ID_" + x).toString());
                        Log.d(TAG, "Original List: " + documentSnapshot.get("product_ID_" + x).toString());
                    }

                    for (int i = 0; i < intlist; i++) {
                        String id = idList.get(i);
                        if (id.contentEquals(productID)) {
                            Log.d(TAG, "This is Remove " + idList.get(i));
                            idList.remove(i);
                            break;
                        }
                    }
                    intlist--;
                    for (int i = 0; i < idList.size(); i++) {
                        int j = i + 1;
                        updates.put("product_ID_" + j, idList.get(i));
                        docRef.update(updates);
                        Log.d(TAG, "After remove " + idList.get(i));
                    }

                    updates.put("list_size", intlist);
                    docRef.update(updates);

                    updates.put("product_ID_" + listSize, FieldValue.delete());
                    docRef.update(updates);

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void removeCartlist(Context context, String productID) {
        //  ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART");
        Map<String, Object> updates = new HashMap<>();
        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (task.isSuccessful()) {
                    long listSize = (long) task.getResult().get("list_size");
                    int intlist = Integer.parseInt(String.valueOf(listSize));
                    idList.clear();
                    for (int x = 1; x <= intlist; x++) {
                        idList.add(documentSnapshot.get("product_ID_" + x).toString());
                        Log.d(TAG, "Original List: " + documentSnapshot.get("product_ID_" + x).toString());
                    }

                    for (int i = 0; i < intlist; i++) {
                        String id = idList.get(i);
                        if (id.contentEquals(productID)) {
                            Log.d(TAG, "This is Remove " + idList.get(i));
                            idList.remove(i);
                            break;
                        }
                    }
                    intlist--;
                    for (int i = 0; i < idList.size(); i++) {
                        int j = i + 1;
                        updates.put("product_ID_" + j, idList.get(i));
                        docRef.update(updates);
                        Log.d(TAG, "After remove " + idList.get(i));
                    }

                    updates.put("list_size", intlist);
                    docRef.update(updates);

                    updates.put("product_ID_" + listSize, FieldValue.delete());
                    docRef.update(updates);
//                    if (ProductDetailsActivity.cartItem!=null&&MainActivity.cartItem!=null) {
//                        ProductDetailsActivity.cartItem.setActionView(null);
//                        MainActivity.cartItem.setActionView(null);
//                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void loadRatingList(Context context, String productID) {
        myRatedIds.clear();
        myRating.clear();
        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                        myRating.add((long) task.getResult().get("rating_" + x));
                        if (task.getResult().get("product_ID_" + x).toString().contentEquals(productID) && ProductDetailsActivity.rateNowContainer != null) {
                            ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                            ProductDetailsActivity.setRating(ProductDetailsActivity.initialRating);
                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public static void loadAddresses(Context context) {
        addressesModelList.clear();
        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if (task.isSuccessful()) {
                    long listSize = (long) documentSnapshot.get("list_size");
                    Intent deliveryIntent;
                    if (listSize == 0) {
                        deliveryIntent = new Intent(context, AddAddressActivity.class);
                        deliveryIntent.putExtra("INTENT", "deliveryIntent");
                    } else {

                        for (long x = 1; x < listSize + 1; x++) {
                            addressesModelList.add(new AddressesModel(task.getResult().get("fullname_" + x).toString(),
                                    task.getResult().get("address_" + x).toString(),
                                    task.getResult().get("pincode_" + x).toString(),
                                    (boolean) task.getResult().get("selected_" + x)));
                            if ((boolean) task.getResult().get("selected_" + x)) {
                                selectedAddress = Integer.parseInt(String.valueOf(x - 1));
                            }
                        }

                        deliveryIntent = new Intent(context, DeliveryActivity.class);
                    }
                    context.startActivity(deliveryIntent);

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public static void loadReward(Context context,boolean onRewardFargment) {
        rewardModelList.clear();
        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Timestamp timestamp = task.getResult().getTimestamp("Last_seen");
                            Log.d(TAG, "onComplete: " + timestamp.toDate());


                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USERS_REWARDS").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                    if (documentSnapshot.get("type").toString().equals("Discount") && timestamp.toDate().before(documentSnapshot.getDate("validity"))) {
                                                        rewardModelList.add(new RewardModel(
                                                                documentSnapshot.get("type").toString()
                                                                , documentSnapshot.get("lower_limit").toString()
                                                                , documentSnapshot.get("upper_limit").toString()
                                                                , documentSnapshot.get("percentage").toString()
                                                                , documentSnapshot.get("body").toString()
                                                                , (Timestamp) documentSnapshot.get("validity")));
                                                    } else if (documentSnapshot.get("type").toString().equals("Flat Rs. *OFF") && timestamp.toDate().before(documentSnapshot.getDate("validity"))) {
                                                        rewardModelList.add(new RewardModel(
                                                                documentSnapshot.get("type").toString()
                                                                , documentSnapshot.get("lower_limit").toString()
                                                                , documentSnapshot.get("upper_limit").toString()
                                                                , documentSnapshot.get("amount").toString()
                                                                , documentSnapshot.get("body").toString()
                                                                , (Timestamp) documentSnapshot.get("validity")));

                                                    }

                                                }
                                                if (onRewardFargment) {
                                                    MyRewardsFragment.myRewardAdapter.notifyDataSetChanged();
                                                }
                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public static void clearData() {
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        idList.clear();
        myRatedIds.clear();
        myRating.clear();
        addressesModelList.clear();
        rewardModelList.clear();
        ProductDetailsActivity.productSpecificationModelList.clear();
        MyWishlistFragment.wishlistModelList.clear();
        MyCartFragment.cartItemModelsList.clear();

    }

}
