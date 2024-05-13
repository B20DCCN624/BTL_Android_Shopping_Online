package com.example.shopping.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.shopping.adapter.AddressAdapter;
import com.example.shopping.adapter.WishlistAdapter;
import com.example.shopping.domain.Address;
import com.example.shopping.domain.Items;
import com.example.shopping.domain.Payment;
import com.example.shopping.domain.Profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Address.db";
    private static int DATABASE_VERSION = 1;
    private AddressAdapter addressAdapter;
    private WishlistAdapter wishlistAdapter;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateDBAddress = "CREATE TABLE address(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "address TEXT)";
        db.execSQL(sqlCreateDBAddress);

        String sqlCreateDBWishlist = "CREATE TABLE wishlist(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "price TEXT," +
                "rating TEXT," +
                "picUrl TEXT)";
        db.execSQL(sqlCreateDBWishlist);

        String sqlCreateDBNotifications = "CREATE TABLE notifications(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "price TEXT," +
                "date TEXT)";
        db.execSQL(sqlCreateDBNotifications);

        String sqlCreateDBProfile = "CREATE TABLE profile(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "email TEXT," +
                "date TEXT," +
                "contact TEXT)";
        db.execSQL(sqlCreateDBProfile);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    //getAll
    public List<Address> getAll() {
        List<Address> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor rs = sqLiteDatabase.query("address",
                null, null, null,
                null, null, null);
        while ((rs != null) && (rs.moveToNext())) {
            int id= rs.getInt(0);
            String address = rs.getString(1);
            list.add(new Address(id, address));
        }
        return list;
    }
    //add
    public long addAddress(Address i){
        ContentValues values = new ContentValues();
        values.put("address", i.getAdd());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long result = sqLiteDatabase.insert("address", null, values);

        // Sau khi thêm một mục mới, cập nhật dữ liệu cho RecyclerView
        if (addressAdapter != null) {
            List<Address> newList = getAll();
            addressAdapter.setData(newList);
        }
        return result;
    }

    //Update
    public int updateAddress(Address address) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("address", address.getAdd());
        return sqLiteDatabase.update("address", values, "id = ?", new String[]{String.valueOf(address.getId())});

    }

    //Delete
    public boolean deleteAddress(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int result = sqLiteDatabase.delete("address", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }


    //getAllItems
    public ArrayList<Items> getAllItem() {
        ArrayList<Items> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor rs = sqLiteDatabase.query("wishlist",
                null, null, null,
                null, null, "id DESC");
        while ((rs != null) && (rs.moveToNext())) {
            int id= rs.getInt(0);
            String title = rs.getString(1);
            String price = rs.getString(2);
            String rating = rs.getString(3);
            String picUrlString = rs.getString(4);
            ArrayList<String> picUrl = new ArrayList<>(Arrays.asList(picUrlString.split(",")));
//            System.out.println("ID: " + id + ", Title: " + title + ", Price: " + price + ", Rating: " + rating + ", PicUrl: " + picUrl);
            list.add(new Items(id, title, picUrl, Double.parseDouble(price), Float.parseFloat(rating)));
        }
        return list;
    }

    public boolean itemExists(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM wishlist WHERE title = ?";
        Cursor cursor = db.rawQuery(query, new String[]{title});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    //insert
    public long insertItem(Items item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("price", item.getPrice());
        values.put("rating", item.getRating());
        values.put("picUrl", item.getPicUrl().get(0));
        if (!itemExists(item.getTitle())) {
            return db.insert("wishlist", null, values);
        } else {
            return -1;
        }
    }

    //Delete
    public boolean deleteItem(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int result = sqLiteDatabase.delete("wishlist", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }


    //getAllNotifications
    public List<Payment> getAllNoti() {
        List<Payment> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sortOrder = "date DESC";
        Cursor rs = sqLiteDatabase.query("notifications",
                null, null, null,
                null, null, sortOrder);
        while ((rs != null) && (rs.moveToNext())) {
            int id= rs.getInt(0);
            String price = rs.getString(1);
            String date = rs.getString(2);
//            System.out.println("ID: " + id + ", Title: " + date + ", Price: " + price);
            list.add(new Payment(id, Double.parseDouble(price), date));
        }
        return list;
    }
    //insert
    public long insertNoti(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("price", payment.getPriceTotal());
        values.put("date", payment.getDate());
        return db.insert("notifications", null, values);
    }

    //getAllProfile
    public Profile getAllProfile() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor rs = sqLiteDatabase.query("profile",
                null, null, null,
                null, null, "id DESC", "1"); // Sắp xếp theo ID giảm dần và giới hạn là 1

        Profile profile = null;
        if (rs != null && rs.moveToFirst()) {
            int id= rs.getInt(0);
            String username = rs.getString(1);
            String email = rs.getString(2);
            String date = rs.getString(3);
            String contact = rs.getString(4);
            profile = new Profile(id, username, email, date, contact);
            rs.close();
        }
        return profile;
    }
    //insert
    public long insertProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", profile.getUsername());
        values.put("email", profile.getEmail());
        values.put("date", profile.getDate());
        values.put("contact", profile.getContact());
        return db.insert("profile", null, values);
    }
}
