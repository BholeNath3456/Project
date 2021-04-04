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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.local.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment {
    private static final String TAG = "MyOrdersFragment";
    private RecyclerView myOrdersRecyclerView;

    public static List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        myOrdersRecyclerView = view.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);
        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);

        for (int i = 0; i < MainActivity.orderID.size(); i++) {
            int finalI = i;
            FirebaseFirestore.getInstance().collection("ORDERS").document(MainActivity.orderID.get(i).getOrderID()).collection("ORDER_ITEMS").document("PRODUCT_LIST").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            //   DocumentSnapshot documentSnapshot=task.getResult();
                            Log.d(TAG, "onComplete: List Size" + task.getResult().get("list_size"));
                            for (int j = 1; j <= Integer.parseInt(task.getResult().get("list_size").toString()); j++) {
                                myOrderItemModelList.add(new MyOrderItemModel(
                                        task.getResult().get("productImage_" + j).toString()
                                        , task.getResult().get("productTitle_" + j).toString()
                                        , MainActivity.orderID.get(finalI).getOrderStatus()));
                            }
                            myOrderAdapter.notifyDataSetChanged();
                        }
                    });


        }
        myOrderAdapter.notifyDataSetChanged();


//        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mobile1, "Pixel 3XL (Blue)", "Delivered on Thursday"));
//        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.bin, "Pixel 3XL (Blue)", "Delivered on Monday"));
//        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.check_icon, "Pixel 3XL (Blue)", "Cancelled"));
//        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mobile1, "Pixel 3XL (Blue)", "Delivered on Thursday"));


        return view;
    }
}