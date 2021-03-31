package com.example.onlinemoneypay;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
 * Use the {@link MyCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCartFragment extends Fragment {
    private RecyclerView cartItemsRecyclerView;
    private Button continueBtn;
    private static final String TAG = "MyCartFragment";
    public static List<CartItemModel> cartItemModelsList = new ArrayList<>();

    public static long listSize;
    private TextView totalAmount;
    private LinearLayout linearLayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCartFragment newInstance(String param1, String param2) {
        MyCartFragment fragment = new MyCartFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        cartItemsRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        linearLayout=view.findViewById(R.id.linearLayout5);
        continueBtn = view.findViewById(R.id.cart_continue_btn);
        totalAmount=view.findViewById(R.id.total_cart_amount);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);
        CartAdapter cartAdapter = new CartAdapter(cartItemModelsList,totalAmount);
//        cartItemModelsList.add(new CartItemModel(0,R.drawable.mobile1,"Pixel 2",2,"Rs. 3999/-","Rs. 9999/-",1,0,0));
//        cartItemModelsList.add(new CartItemModel(0,R.drawable.mobile1,"Pixel 2",1,"Rs. 2999/-","Rs. 8999/-",1,1,1));
//        cartItemModelsList.add(new CartItemModel(0,R.drawable.mobile1,"Pixel 2",0,"Rs. 1999/-","Rs. 5999/-",1,2,0));
//        cartItemModelsList.add(new CartItemModel(1,"Price (3 items)", "Rs. 1673","Free","Rs.9899","Rs 50090/"));
        cartItemModelsList.clear();

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    listSize = (long) task.getResult().get("list_size");
                    //  String id = task.getResult().get("product_ID_1").toString();
                    for (long x = 1; x <= listSize; x++) {

                        String id = task.getResult().get("product_ID_" + x).toString();
                        /// /////////  loop start...
                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(id)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

//        cartItemModelsList.add(new CartItemModel(1,"Price (3 items)", "Rs. 1673","Free","Rs.9899","Rs 50090/"));
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (task.isSuccessful()) {
                                    cartItemModelsList.add(new CartItemModel(CartItemModel.CART_ITEM
                                            , documentSnapshot.get("product_ID").toString()
                                            , documentSnapshot.get("product_image_1").toString()
                                            , documentSnapshot.get("product_title").toString()
                                            , (long) documentSnapshot.get("free_coupens")
                                            , documentSnapshot.get("product_price").toString()
                                            , documentSnapshot.get("cutted_price").toString()
                                            , (long) 1, (long) 2, (long) 0));


                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                }


                                cartItemsRecyclerView.setAdapter(cartAdapter);
                                cartAdapter.notifyDataSetChanged();
                            }
                        });
                        Log.d(TAG, "onComplete: " + listSize + id);

                    }
                    if (listSize != 0) {
                        linearLayout.setVisibility(View.VISIBLE);
                        cartItemModelsList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                        cartItemsRecyclerView.setAdapter(cartAdapter);
                        cartAdapter.notifyDataSetChanged();
                        ////////////////////////////// looping
                    }

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent deliveryIntent = new Intent(getContext(), AddAddressActivity.class);
//                getContext().startActivity(deliveryIntent);
                DBqueries.loadAddresses(getContext());
            }
        });
        return view;
    }
}