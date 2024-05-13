package com.example.shopping.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping.BasicActivityInterface;
import com.example.shopping.MyNotifications;
import com.example.shopping.R;
import com.example.shopping.activity.NotificationsActivity;
import com.example.shopping.activity.OrderSuccessActivity;
import com.example.shopping.adapter.CartAdapter;
import com.example.shopping.domain.Address;
import com.example.shopping.domain.Payment;
import com.example.shopping.helper.ChangeNumberItemsListener;
import com.example.shopping.helper.ManagmentCart;
import com.example.shopping.helper.SQLiteHelper;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentCart extends Fragment implements BasicActivityInterface {
    private FirebaseDatabase database;
    private double tax;
    private ManagmentCart managmentCart;
    private ScrollView scrollViewCart;
    private RecyclerView rcvCart;
    private Spinner spinnerAddress;
    private TextView subtotal, feeDelivery, totalTax, totalTxt;
    private ImageView emptyCart;
    private Button btnCheckout;
    private SQLiteHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        db = new SQLiteHelper(getActivity());
        emptyCart = view.findViewById(R.id.emptyTxt);
        scrollViewCart = view.findViewById(R.id.scrollViewCart);
        rcvCart = view.findViewById(R.id.rcvCart);
        subtotal = view.findViewById(R.id.priceSubtotal);
        feeDelivery = view.findViewById(R.id.priceDelivery);
        totalTax = view.findViewById(R.id.priceTax);
        totalTxt = view.findViewById(R.id.priceTotal);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        spinnerAddress = view.findViewById(R.id.spinner_address);

        loadAddressData();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderSuccessActivity.class);
                startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Xóa toàn bộ dữ liệu cũ
                        managmentCart.clearCart();
                        // Cập nhật giao diện
                        initCartList();
                    }
                }, 1000);
                String priceTotalString = totalTxt.getText().toString();
                priceTotalString = priceTotalString.replace("$", "");
                double priceTotal = Double.parseDouble(priceTotalString);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentDateAndTime = dateFormat.format(calendar.getTime());

                Payment payment = new Payment(priceTotal, currentDateAndTime);
                db.insertNoti(payment);

                sendNotification();
            }
        });

        managmentCart = new ManagmentCart(getContext());
        caculatorCart();
        initCartList();
        
        return view;
    }

    private void loadAddressData() {
        db = new SQLiteHelper(getActivity());
        List<Address> addresses = db.getAll();
        ArrayAdapter<Address> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, addresses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddress.setAdapter(adapter);
    }

    private void sendNotification() {
        String totalPrice = totalTxt.getText().toString();
        Address selectedAddress = (Address) spinnerAddress.getSelectedItem();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.conner_shop);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent resultIntent = new Intent(getActivity(), NotificationsActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getActivity());
        taskStackBuilder.addParentStack(NotificationsActivity.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(getNotificationId()
                , PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Notification notification =new NotificationCompat.Builder(requireContext(), MyNotifications.CHANNEL_ID)
                .setContentTitle("Đặt hàng thành công")
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Tổng tiền: " + totalPrice)
                        .addLine("Địa chỉ nhận hàng: " + selectedAddress))
                .setSmallIcon(R.drawable.ic_noti_active)
                .setLargeIcon(bitmap)
                .setSound(uri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(getNotificationId(), notification);
        }
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }

    private void initCartList() {
        if(managmentCart.getListCart().isEmpty()) {
            emptyCart.setVisibility(View.VISIBLE);
            scrollViewCart.setVisibility(View.GONE);
        } else {
            emptyCart.setVisibility(View.GONE);
            scrollViewCart.setVisibility(View.VISIBLE);
        }
        rcvCart.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvCart.setAdapter(new CartAdapter(getContext(), managmentCart.getListCart(), new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                caculatorCart();
            }
        }));
    }

    private void caculatorCart() {
        double precentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee()) * precentTax * 100.0) / 100.0;

        double total = Math.round((managmentCart.getTotalFee()+ tax+delivery) *100 ) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee()*100)/100;

        subtotal.setText("$"+itemTotal);
        feeDelivery.setText("$"+delivery);
        totalTax.setText("$"+tax);
        totalTxt.setText("$"+total);
    }

    @Override
    public FirebaseDatabase getFirebaseDatabase() {
        return database;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAddressData();
    }
}
