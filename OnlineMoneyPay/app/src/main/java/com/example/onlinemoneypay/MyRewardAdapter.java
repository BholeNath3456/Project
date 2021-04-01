package com.example.onlinemoneypay;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardAdapter extends RecyclerView.Adapter<MyRewardAdapter.Viewholder> {
    private static final String TAG = "MyRewardAdapter";
    private List<RewardModel> rewardModelList;
    private Boolean useMiniLayout=false;


    public MyRewardAdapter(List<RewardModel> rewardModelList,Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout=useMiniLayout;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
       View view;

        if(useMiniLayout){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout, viewGroup, false);

        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout, viewGroup, false);
        }

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        String type = rewardModelList.get(position).getType();
        Date validity = rewardModelList.get(position).getTimestamp().toDate();
        String body = rewardModelList.get(position).getCoupenBody();
        String lowerLimit=rewardModelList.get(position).getLowerLimit();
        String upperLimit=rewardModelList.get(position).getUpperLimit();
        String discOramt=rewardModelList.get(position).getDiscOramt();
        viewholder.setData(type,validity,body,upperLimit,lowerLimit,discOramt);

    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            coupenTitle = itemView.findViewById(R.id.coupen_title);
            coupenExpiryDate = itemView.findViewById(R.id.coupen_validity);
            coupenBody = itemView.findViewById(R.id.coupen_body);

        }

        private void setData(String type, Date validity, String body,String upperLimit, String lowerLimit, String discOramt) {

            if(type.equals("Discount")){
                coupenTitle.setText(type);
            }
            else {
                coupenTitle.setText("FLat Rs."+discOramt+"Off");
            }

            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MMMM/YYYY");
            coupenExpiryDate.setText(simpleDateFormat.format(validity));
            coupenBody.setText(body);
            if(useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        if(Long.valueOf(ProductDetailsActivity.forCalculationOriginalPrice)>Long.valueOf(lowerLimit)&&Long.valueOf(ProductDetailsActivity.forCalculationOriginalPrice)<Long.valueOf(upperLimit))
                        {
                            //(Long.valueOf(ProductDetailsActivity.forCalculationOriginalPrice)-disAmount)
                            ProductDetailsActivity.originalPrice.setText(ProductDetailsActivity.forCalculationOriginalPrice);
                           // String DisPrice=String.valueOf(Integer.parseInt(ProductDetailsActivity.forCalculationOriginalPrice)-Integer.parseInt(discOramt));
                            ProductDetailsActivity.discountedPrice.setText(discOramt);
                        }else {
                            ProductDetailsActivity.discountedPrice.setText("Invalid Reward!");
                        }
                        ProductDetailsActivity.coupenTitle.setText(type);
                        ProductDetailsActivity.coupenExpiryDate.setText(simpleDateFormat.format(validity));
                        ProductDetailsActivity.coupenBody.setText(body);
                        Log.d(TAG, "onClick: "+ProductDetailsActivity.forCalculationOriginalPrice);
                        ProductDetailsActivity.showDialogRecyclerView();
                    }
                });
            }
        }
    }
}
