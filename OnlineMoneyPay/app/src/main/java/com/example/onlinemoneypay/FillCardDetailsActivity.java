package com.example.onlinemoneypay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

public class FillCardDetailsActivity extends AppCompatActivity {
    private static final String TAG = "FillCardDetailsActivity";
  private TextView finalTotalPrice;
  private TextView textVieworderID;
  public static String orderId;
  public static String transactionId;
  private Button payNow;
  public static List<String> orderProductID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_card_details);
        finalTotalPrice=findViewById(R.id.final_total_price);
        textVieworderID=findViewById(R.id.orderId);
        orderId= UUID.randomUUID().toString().substring(0,25);
        finalTotalPrice.setText("Rs."+DeliveryActivity.totalItemPrice+"/-");
        textVieworderID.setText(orderId);
        transactionId=UUID.randomUUID().toString().substring(0,15).concat("@omp");
        Log.d(TAG, "onCreate: "+transactionId);
        payNow=findViewById(R.id.final_pay_now);
        for (int x = 0; x < MyCartFragment.cartItemModelsList.size(); x++) {
            if (MyCartFragment.cartItemModelsList.get(x).getType() == CartItemModel.CART_ITEM) {
                Log.d(TAG, "onCreate: "+MyCartFragment.cartItemModelsList.get(x).getProductID());
            }
        }


        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FillCardDetailsActivity.this,FinishedOrderedActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}