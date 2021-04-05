package com.example.onlinemoneypay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailsActivity extends AppCompatActivity {
    private static final String TAG = "OrderDetailsActivity";
    private TextView orderId, totalPrice, fullname, fulladdress, pincode;
    private ProgressBar orderProgressBar, packedShippingProgress, shippingDeliveredProgress;
    private ImageView orderedIndicator, packedIndicator, shippingIndicator, deliveredIndicator;
    //cart
    private TextView cartTotalItems, cartotalItemsPrice, cartTotalPrice, cartSavedAmount;
    //cart
    //Order Date
    private TextView orderedDate, packedDate, shippingDate, deliveredDate;
    //Order Date

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
        //Progress Date
        orderedDate = findViewById(R.id.ordered_date);
        packedDate = findViewById(R.id.packed_date);
        shippingDate = findViewById(R.id.shipping_date);
        deliveredDate = findViewById(R.id.delivered_date);
        //Progress Date


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


    }

    @Override
    protected void onStart() {
        super.onStart();  /// firebase
        int position = getIntent().getIntExtra("Position", 0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMMM/YYYY");
        loadingOrders(position, simpleDateFormat);
    }

    private void loadingOrders(int position, SimpleDateFormat simpleDateFormat) {
        FirebaseFirestore.getInstance().collection("ORDERS").document(MyOrdersFragment.myOrderItemModelList.get(position).getOrderID()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        orderId.setText("Your Order Id: " + task.getResult().get("OrderID").toString());
                        Log.d(TAG, "onComplete: Number of Product: " + task.getResult().get("NumberOfProducts"));
                        Log.d(TAG, "onComplete: Total Price :" + task.getResult().get("TotalPrice"));
                        Log.d(TAG, "onComplete: Total Items price" + task.getResult().get("TotalPrice"));
                        cartSavedAmount.setText("Your transction ID : " + task.getResult().get("TransactionID").toString());

                        fullname.setText(task.getResult().get("FullName").toString());
                        fulladdress.setText(task.getResult().get("FullAddress").toString());
                        pincode.setText(task.getResult().get("Pincode").toString());

                        totalPrice.setText("Rs. " + task.getResult().get("TotalPrice").toString() + "/-");
                        cartotalItemsPrice.setText("Rs. " + task.getResult().get("TotalPrice").toString() + "/-");
                        cartTotalPrice.setText("Rs. " + task.getResult().get("TotalPrice").toString() + "/-");
                        cartTotalItems.setText("Price (" + task.getResult().get("NumberOfProducts").toString() + ") item");
                        // Date Checking...
                        Date order = task.getResult().getDate("OrderedDate");
                        if (order != null) {
                            orderedIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
                            orderProgressBar.setProgress(20);
                            orderedDate.setText(simpleDateFormat.format(order));

                            Date place = task.getResult().getDate("PackedDate");
                            if (place != null) {
                                orderedIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
                                packedIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
                                packedShippingProgress.setProgress(20);
                                orderProgressBar.setProgress(100);
                                packedDate.setText(simpleDateFormat.format(place));

                                Date shipping = task.getResult().getDate("ShippingDate");
                                if (shipping != null) {
                                    orderedIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
                                    packedIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
                                    shippingIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
                                    packedShippingProgress.setProgress(100);
                                    orderProgressBar.setProgress(100);
                                    shippingDeliveredProgress.setProgress(20);
                                    shippingDate.setText(simpleDateFormat.format(shipping));

                                    Date deliver = task.getResult().getDate("DeliveredDate");
                                    if (deliver != null) {
                                        orderedIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
                                        packedIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
                                        shippingIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
                                        deliveredIndicator.setColorFilter(Color.argb(255, 0, 255, 0));
                                        packedShippingProgress.setProgress(100);
                                        orderProgressBar.setProgress(100);
                                        shippingDeliveredProgress.setProgress(100);
                                        deliveredDate.setText(simpleDateFormat.format(deliver));
                                    }
                                }
                            }
                        }


                        // Date Checking...


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