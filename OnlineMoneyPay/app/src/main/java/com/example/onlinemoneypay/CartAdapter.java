package com.example.onlinemoneypay;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private  boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList,TextView cartTotalAmount,boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount=cartTotalAmount;
        this.showDeleteBtn=showDeleteBtn;

    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new CartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:

                View cartTotalAmountView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new CartTotalAmountViewholder(cartTotalAmountView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productId = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                long freeCoupens = cartItemModelList.get(position).getFreeCoupens();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                long offeredApplied = cartItemModelList.get(position).getOffersApplied();
                ((CartItemViewholder) viewHolder).setItemsDetails(productId, resource, title, freeCoupens, productPrice, cuttedPrice, offeredApplied);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount=0;
                for (int x = 0; x < cartItemModelList.size(); x++) {
                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM) {
                        totalItems++;
                        totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice());
                    }
                }
                if(totalItemPrice>500){
                    deliveryPrice="FREE";
                    totalAmount=totalItemPrice;
                }else {
                    deliveryPrice="60";
                    totalAmount=totalItemPrice+60;
                }

                ((CartTotalAmountViewholder) viewHolder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, savedAmount);

                break;
            default:
                return;
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private ImageView freeCoupenIcon;
        private TextView productTitle;
        private TextView freeCoupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView coupensApplied;
        private TextView productQuantity;
        private LinearLayout deletBtn;

        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupenIcon = itemView.findViewById(R.id.free_coupen_icon);
            freeCoupens = itemView.findViewById(R.id.tv_free_coupen);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            coupensApplied = itemView.findViewById(R.id.coupens_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            deletBtn = itemView.findViewById(R.id.remove_item_btn);
        }

        private void setItemsDetails(String productID, String resource, String title, long freeCoupensNo, String productPriceText, String cuttedPriceText, long offersAppliedNo) {

            // productImage.setImageResource(resource);
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.fakeimage)).into(productImage);
            productTitle.setText(title);
            if (freeCoupensNo > 0) {

                freeCoupenIcon.setVisibility(View.VISIBLE);
                freeCoupens.setVisibility(View.VISIBLE);
                if (freeCoupensNo == 1) {
                    freeCoupens.setText("free " + freeCoupensNo + " Coupen");
                } else {
                    freeCoupens.setText("free " + freeCoupensNo + " Coupens");
                }
            } else {

                freeCoupenIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
            }
            productPrice.setText(productPriceText);
            cuttedPrice.setText(cuttedPriceText);
            if (offersAppliedNo > 0) {
                offersApplied.setVisibility(View.VISIBLE);
                offersApplied.setText(offersAppliedNo + " Offers Applied");

            } else {
                offersApplied.setVisibility(View.INVISIBLE);
            }

            productQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog quantityDialog = new Dialog(itemView.getContext());
                    quantityDialog.setContentView(R.layout.quantity_dialog);
                    quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    quantityDialog.setCancelable(false);
                    EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                    Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                    Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quantityDialog.dismiss();
                        }
                    });
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productQuantity.setText("Qty: " + quantityNo.getText());
                            quantityDialog.dismiss();
                        }
                    });
                    quantityDialog.show();


                }
            });
            if(showDeleteBtn){
                deletBtn.setVisibility(View.VISIBLE);
            }else {
                deletBtn.setVisibility(View.GONE);
            }
            deletBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBqueries.removeCartlist(itemView.getContext(), productID);
                    Toast.makeText(itemView.getContext(), "Item Deleted..", Toast.LENGTH_SHORT).show();
                    MyCartFragment.cartAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    class CartTotalAmountViewholder extends RecyclerView.ViewHolder {
        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;


        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
        }

        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int savedAmountText) {
            totalItems.setText("Price("+totalItemText+" items)");
            totalItemPrice.setText("Rs."+totalItemPriceText+"/-");
            if(deliveryPriceText.equals("FREE")){
                deliveryPrice.setText(deliveryPriceText);
            }else {
                deliveryPrice.setText("Rs."+deliveryPriceText+"/-");
            }

            totalAmount.setText("Rs."+totalAmountText+"/-");
            cartTotalAmount.setText("Rs."+totalAmountText+"/-");
            savedAmount.setText("You saved Rs."+savedAmountText+"/- on this order.");


        }
    }


}