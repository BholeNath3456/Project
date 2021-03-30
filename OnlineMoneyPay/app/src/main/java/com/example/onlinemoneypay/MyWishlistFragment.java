package com.example.onlinemoneypay;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyWishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyWishlistFragment extends Fragment {
    private static final String TAG = "MyWishlistFragment";

    private RecyclerView wishlistRecyclerView;
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();
    public static WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlistModelList, true);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyWishlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyWishlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyWishlistFragment newInstance(String param1, String param2) {
        MyWishlistFragment fragment = new MyWishlistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wishlist, container, false);
        wishlistRecyclerView = view.findViewById(R.id.my_wishlist_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.clearOnChildAttachStateChangeListeners();
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);
        wishlistModelList.clear();

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    long listSize = (long) task.getResult().get("list_size");
                    //  String id = task.getResult().get("product_ID_1").toString();
                    for (long x = 1; x <= listSize; x++) {

                        String id = task.getResult().get("product_ID_" + x).toString();
                        /// /////////  loop start...
                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(id)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (task.isSuccessful()) {
                                    wishlistModelList.add(new WishlistModel(documentSnapshot.get("product_ID").toString()
                                            , documentSnapshot.get("product_image_1").toString()
                                            , documentSnapshot.get("product_title").toString()
                                            , (long) documentSnapshot.get("free_coupens")
                                            , documentSnapshot.get("average_rating").toString()
                                            , (long) documentSnapshot.get("total_ratings")
                                            , documentSnapshot.get("product_price").toString()
                                            , documentSnapshot.get("cutted_price").toString()
                                            , (boolean) documentSnapshot.get("COD")));
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                }

                                wishlistRecyclerView.setAdapter(wishlistAdapter);
                                wishlistAdapter.notifyDataSetChanged();
                            }
                        });
                        Log.d(TAG, "onComplete: " + listSize + id);

                    }
                    ////////////////////////////// looping



                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}