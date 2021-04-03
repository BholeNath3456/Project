package com.example.onlinemoneypay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class FillCardDetailsActivity extends AppCompatActivity {
  private TextView finalTotalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_card_details);
        finalTotalPrice=findViewById(R.id.final_total_price);
        finalTotalPrice.setText("Rs."+DeliveryActivity.totalItemPrice+"/-");
    }
}