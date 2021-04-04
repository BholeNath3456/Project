package com.example.onlinemoneypay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FillCardDetailsActivity extends AppCompatActivity {
    private static final String TAG = "FillCardDetailsActivity";
    private TextView finalTotalPrice;
    private TextView textVieworderID;
    public static String orderId;
    public static String transactionId;
    private Button payNow;
    private Dialog loadingDialog;
    private ImageView loadingAnim;
    public static List<String> orderProductID;
    private Timestamp timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_card_details);
        finalTotalPrice = findViewById(R.id.final_total_price);
        textVieworderID = findViewById(R.id.orderId);
        orderId = UUID.randomUUID().toString().substring(0, 25);
        finalTotalPrice.setText("Rs." + DeliveryActivity.totalItemPrice + "/-");
        textVieworderID.setText(orderId);
        transactionId = UUID.randomUUID().toString().substring(0, 15).concat("@omp");
        Log.d(TAG, "onCreate: " + transactionId);
        payNow = findViewById(R.id.final_pay_now);
        for (int x = 0; x < MyCartFragment.cartItemModelsList.size(); x++) {
            if (MyCartFragment.cartItemModelsList.get(x).getType() == CartItemModel.CART_ITEM) {
                Log.d(TAG, "onCreate: " + MyCartFragment.cartItemModelsList.get(x).getProductID());
            }
        }
        //////////loading dialog
        loadingDialog = new Dialog(FillCardDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingAnim = loadingDialog.findViewById(R.id.loading_anim);
        Glide.with(loadingDialog.getContext()).load(R.drawable.loading).into(loadingAnim);
        //////////loading dialog

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                settingOrdersInFirebase();
            }
        });

    }


    private void settingOrdersInFirebase() {
        Map<String, Object> settingOrders = new HashMap<>();
        settingOrders.put("CustomerID", FirebaseAuth.getInstance().getUid());
        settingOrders.put("TransactionID", transactionId);
        settingOrders.put("isPaid", true);
        settingOrders.put("NumberOfProducts", (MyCartFragment.cartItemModelsList.size() - 1));
        settingOrders.put("TotalPrice", DeliveryActivity.totalItemPrice );
        settingOrders.put("OrderedDate", FieldValue.serverTimestamp());
        settingOrders.put("PackedDate",timestamp);
        settingOrders.put("ShippingDate",timestamp);
        settingOrders.put("DeliveredDate",timestamp);

        FirebaseFirestore.getInstance().collection("ORDERS").document(orderId)
                .set(settingOrders).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    loadingDialog.dismiss();
                    Intent intent = new Intent(FillCardDetailsActivity.this, FinishedOrderedActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(FillCardDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}