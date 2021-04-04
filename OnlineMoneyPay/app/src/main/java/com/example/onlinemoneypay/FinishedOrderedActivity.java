package com.example.onlinemoneypay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class FinishedOrderedActivity extends AppCompatActivity {
    private TextView textViewOrderId,textViewTransactionId,textViewFrom,textViewPaidAmount;
    private Button buyAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_ordered);
        textViewOrderId=findViewById(R.id.paid_order_id);
        textViewTransactionId=findViewById(R.id.paid_transaction_id);
        textViewFrom=findViewById(R.id.from);
        textViewPaidAmount=findViewById(R.id.paid_amount);
        buyAgain=findViewById(R.id.paid_buy_again);

        textViewFrom.setText(":"+FirebaseAuth.getInstance().getCurrentUser().getEmail());
        textViewOrderId.setText(":"+FillCardDetailsActivity.orderId);
        textViewTransactionId.setText(":"+FillCardDetailsActivity.transactionId);
        textViewPaidAmount.setText("Rs."+DeliveryActivity.totalItemPrice+"/-");

        buyAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FinishedOrderedActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}