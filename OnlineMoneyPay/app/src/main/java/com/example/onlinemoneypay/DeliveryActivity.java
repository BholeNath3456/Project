package com.example.onlinemoneypay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {
    private static final String TAG = "DeliveryActivity";
    private Toolbar toolbar;
    private RecyclerView deliveryRecyclerView;
    private Button changeORaddNewAddressBtn;
    private TextView totalAmount;
    private Button continueBtn;
    private TextView fullname, fullAddress, pincode;
    public static final int SELECT_ADDRESS = 0;
    public static int  totalItemPrice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");
        fullname = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        continueBtn=findViewById(R.id.cart_continue_btn);
        deliveryRecyclerView = findViewById(R.id.delivery_recyclerview);
        changeORaddNewAddressBtn=findViewById(R.id.change_or_add_address_btn);
        totalAmount=findViewById(R.id.total_cart_amount);

        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);
      //  List<CartItemModel> cartItemModelsList=new ArrayList<>();
//        cartItemModelsList.add(new CartItemModel(0,R.drawable.mobile1,"Pixel 2",2,"Rs. 3999/-","Rs. 9999/-",1,0,0));
//        cartItemModelsList.add(new CartItemModel(0,R.drawable.mobile1,"Pixel 2",1,"Rs. 2999/-","Rs. 8999/-",1,1,1));
//        cartItemModelsList.add(new CartItemModel(0,R.drawable.mobile1,"Pixel 2",0,"Rs. 1999/-","Rs. 5999/-",1,2,0));
//        cartItemModelsList.add(new CartItemModel(1,"Price (3 items)", "Rs. 1673","Free","Rs.9899","Rs 50090/"));

        CartAdapter cartAdapter = new CartAdapter(MyCartFragment.cartItemModelsList, totalAmount,false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        for (int x = 0; x < MyCartFragment.cartItemModelsList.size(); x++) {
            if (MyCartFragment.cartItemModelsList.get(x).getType() == CartItemModel.CART_ITEM) {
                totalItemPrice = totalItemPrice + Integer.parseInt(MyCartFragment.cartItemModelsList.get(x).getProductPrice());
            }
        }


        changeORaddNewAddressBtn.setVisibility(View.VISIBLE);
        changeORaddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressesIntent=new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Pressed..."+totalItemPrice);
                Intent fillCardDetailsIntent=new Intent(DeliveryActivity.this,FillCardDetailsActivity.class);
                startActivity(fillCardDetailsIntent);


            }
        });

      }

    private void gettingAmount() {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        fullname.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullname());
        fullAddress.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

    }
}