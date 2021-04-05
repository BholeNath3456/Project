package com.example.onlinemoneypay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class OrderDetailsActivity extends AppCompatActivity {
    private static final String TAG = "OrderDetailsActivity";
    private TextView orderId, totalPrice, fullname, fulladdress, pincode;
    private ProgressBar orderProgressBar, packedShippingProgress, shippingDeliveredProgress;
    private ImageView orderedIndicator, packedIndicator, shippingIndicator, deliveredIndicator;
    //cart
    private TextView cartTotalItems, cartotalItemsPrice, cartTotalPrice, cartSavedAmount;

    //cart

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Orders Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        orderId = findViewById(R.id.product_title);
        totalPrice = findViewById(R.id.product_price);

        fullname = findViewById(R.id.fullname);
        fulladdress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);

        orderedIndicator = findViewById(R.id.ordered_indicator);
        packedIndicator = findViewById(R.id.packed_indicator);
        shippingIndicator = findViewById(R.id.shipping_indicator);
        deliveredIndicator = findViewById(R.id.delivered_indicator);


        orderProgressBar = findViewById(R.id.ordered_packed_progress);
        packedShippingProgress = findViewById(R.id.packed_shipping_progress);
        shippingDeliveredProgress = findViewById(R.id.shipping_delivered_progress);

        //Cart
        cartTotalItems = findViewById(R.id.total_items);
        cartotalItemsPrice = findViewById(R.id.total_items_price);
        cartTotalPrice = findViewById(R.id.total_price);
        cartSavedAmount = findViewById(R.id.saved_amount);
        //Cart


        orderedIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
        orderProgressBar.setProgress(50);
        int position = getIntent().getIntExtra("Position", 0);

        /// firebase
        FirebaseFirestore.getInstance().collection("ORDERS").document(MyOrdersFragment.myOrderItemModelList.get(position).getOrderID()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        orderId.setText(task.getResult().get("OrderID").toString());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}