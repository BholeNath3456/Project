package com.example.onlinemoneypay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.onlinemoneypay.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {
    private RecyclerView myAddressesRecyclerView;
    private Button deliverHereBtn;
    private LinearLayout addNewAddressBtn;
    private static AddressesAdapter addressesAdapter;
    private TextView addressSaved;
    private int previousAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myAddressesRecyclerView = findViewById(R.id.addresses_recyclerview);
        deliverHereBtn=findViewById(R.id.deliver_here_btn);
        addNewAddressBtn=findViewById(R.id.add_new_address_btn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(layoutManager);
        addressSaved=findViewById(R.id.address_saved);
        previousAddress=DBqueries.selectedAddress;
//        List<AddressesModel> addressesModelList = new ArrayList<>();
//        addressesModelList.add(new AddressesModel("Bhole Nath", "Chappwa, Nautanwa", "273417",true));
//        addressesModelList.add(new AddressesModel("Niyanta Karki", "Chappwa, Nautanwa", "273417",false));
//        addressesModelList.add(new AddressesModel("Shivangini", "Chappwa, Nautanwa", "273417",false));
//        addressesModelList.add(new AddressesModel("Anjali Chaudhary", "Chappwa, Nautanwa", "273417",false));
//


        int mode=getIntent().getIntExtra("MODE",-1);
        if(mode==SELECT_ADDRESS){
            deliverHereBtn.setVisibility(View.VISIBLE);
        }
        else {
            deliverHereBtn.setVisibility(View.GONE);

        }
        deliverHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyAddressesActivity.this, "continue To Deliver", Toast.LENGTH_SHORT).show();
            }
        });
        addressesAdapter = new AddressesAdapter(DBqueries.addressesModelList,mode);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)myAddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();
        addNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressIntent=new Intent(MyAddressesActivity.this,AddAddressActivity.class);
                startActivity(addAddressIntent);
            }
        });
        addressSaved.setText(String.valueOf(DBqueries.addressesModelList.size()));
    }

    public static void refreshItem(int deselect,int select){
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);
    }

    @Override

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}