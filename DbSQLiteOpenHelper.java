package io.github.dt_team.common.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.github.dt_team.common.model.BusinessCard;
import io.github.dt_team.common.model.User;

/**
 * Created by ansidev on 10/14/15.
 * Email: ansidev@gmail.com
 */
public class DbSQLiteOpenHelper extends SQLiteOpenHelper {
    //Ap dung Singleton Design Pattern
    private static DbSQLiteOpenHelper dbInstance;
    // Database information
    private static final String DbName = "BusinessCard.db";
    private static final int DbVersion = 1;
    private SQLiteDatabase database;
    //    Init Table BusinessCard
    public static final String tableBusinessCard = "business_cards";
    public static final String tableUsers = "users";
    //    Init Table Columns
    public static final String CL_id = "_id";
    public static final String CL_name = "name";
    public static final String CL_first_name = "first_name";
    public static final String CL_middle_name = "middle_name";
    public static final String CL_last_name = "last_name";
    public static final String CL_job_title = "job_title";
    public static final String CL_phone_number = "phone_number";
    public static final String CL_email = "email";
    public static final String CL_company = "company";
    public static final String CL_website = "website";
    public static final String CL_address = "address";
    public static final String CL_notes = "notes";
    public static final String CL_front_image_path = "front_image_path";
    public static final String CL_back_image_path = "back_image_path";
    public static final String CL_synced = "synced";
    public static final String CL_created_at = "created_at";
    public static final String CL_updated_at = "updated_at";


    public static final String CL_username = "username";
    public static final String CL_avatar = "avatar";
    public static final String CL_facebook_id = "facebook_id";
    public static final String CL_google_id = "google_id";
    public static final String CL_remember_token = "remember_token";

    private DbSQLiteOpenHelper(Context context) {
        super(context, DbName, null, DbVersion);
        database = this.getWritableDatabase();
    }

    public static DbSQLiteOpenHelper getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new DbSQLiteOpenHelper(context.getApplicationContext());
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateBusinessCard = "CREATE TABLE " + tableBusinessCard +
                "(" +
                CL_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                CL_name + " VARCHAR(255) DEFAULT NULL, " +
                CL_first_name + " VARCHAR(255) DEFAULT NULL, " +
                CL_middle_name + " VARCHAR(255) DEFAULT NULL, " +
                CL_last_name + " VARCHAR(255) DEFAULT NULL, " +
                CL_job_title + " VARCHAR(255) DEFAULT NULL, " +
                CL_phone_number + " VARCHAR(20), " +
                CL_email + " VARCHAR(50), " +
                CL_company + " TEXT, " +
                CL_website + " TEXT, " +
                CL_address + " TEXT, " +
                CL_notes + " TEXT, " +
                CL_front_image_path + " TEXT, " +
                CL_back_image_path + " TEXT, " +
                CL_synced + " TINYINT(1) DEFAULT '0', " +
                CL_created_at + " TIMESTAMP DEFAULT '0000-00-00 00:00:00', " +
                CL_updated_at + " TIMESTAMP DEFAULT '0000-00-00 00:00:00' " +
                ")";
        String sqlCreateUsers = "CREATE TABLE " + tableUsers +
                "(" +
                CL_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CL_name + " VARCHAR(255) DEFAULT NULL, " +
                CL_first_name + " VARCHAR(255) DEFAULT NULL, " +
                CL_middle_name + " VARCHAR(255) DEFAULT NULL, " +
                CL_last_name + " VARCHAR(255) DEFAULT NULL, " +
                CL_username + " VARCHAR(255) DEFAULT NULL, " +
                CL_email + " VARCHAR(255) DEFAULT NULL, " +
                CL_avatar + " VARCHAR(255) DEFAULT NULL, " +
                CL_facebook_id + " VARCHAR(255) DEFAULT NULL, " +
                CL_google_id + " VARCHAR(255) DEFAULT NULL, " +
                CL_remember_token + " VARCHAR(255) DEFAULT NULL, " +
                CL_synced + " TINYINT(1) DEFAULT '0', " +
                CL_created_at + " TIMESTAMP DEFAULT '0000-00-00 00:00:00', " +
                CL_updated_at + " TIMESTAMP DEFAULT '0000-00-00 00:00:00' " +
                ")";
        Log.d("sql_create", sqlCreateBusinessCard);
        Log.d("sql_create", sqlCreateUsers);
        db.execSQL(sqlCreateBusinessCard);
        db.execSQL(sqlCreateUsers);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableBusinessCard);
        onCreate(db);
    }

    // Cac thao tac voi table business_cards
    public int insertBusinessCard(BusinessCard bCard) {
        ContentValues values = this.getBusinessCardValues(bCard);
        String NOW = this.getNow();
        values.put(CL_created_at, NOW);
        values.put(CL_updated_at, NOW);
        return (int) database.insertOrThrow(tableBusinessCard, null, values);
    }

    public int updateBusinessCard(BusinessCard bCard, int id) {
        int affectedRow = 0;
        ContentValues values = this.getBusinessCardValues(bCard);
        values.put(CL_updated_at, this.getNow());
        if (values != null) {
            affectedRow = database.update(tableBusinessCard, values, CL_id + " = " + id, null);
        }
        return affectedRow;
    }

    public long deleteBusinessCard(long id) {
        return database.delete(tableBusinessCard, CL_id + " = " + id, null);
    }

    public BusinessCard selectBusinessCard(int id) {
        BusinessCard bCard;
        String sql = "SELECT * FROM " + DbSQLiteOpenHelper.tableBusinessCard + " WHERE " + DbSQLiteOpenHelper.CL_id + " = " + id + " LIMIT 1";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        bCard = BusinessCard.fromCursor(cursor);
        return bCard;
    }

    public Cursor selectAllBusinessCard() {
        String sql = "SELECT * FROM " + DbSQLiteOpenHelper.tableBusinessCard + " ORDER BY " + DbSQLiteOpenHelper.CL_updated_at + " DESC";
        return database.rawQuery(sql, null);
    }

    public Cursor selectAllBusinessCard(String[] columns) {
        return database.query(tableBusinessCard, columns, null, null, null, null, null);
    }

    // Cac thao tac voi table users
    public int insertUser(User user) {
        ContentValues values = this.getUserValues(user);
        String NOW = this.getNow();
        values.put(CL_created_at, NOW);
        values.put(CL_updated_at, NOW);
        return (int) database.insertOrThrow(tableUsers, null, values);
    }


    public Cursor rawQuery(String sql) {
        return database.rawQuery(sql, null);
    }

    public void closeDbConnection() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public String getNow() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }

    public ContentValues getBusinessCardValues(BusinessCard bCard) {
        ContentValues values = new ContentValues();
//        String cardName = bCard.getName();
//        PersonName personName = bCard.getPersonName();
        String fName = new String();
        String mName = new String();
        String lName = new String();
//        if (personName != null) {
//            fName = personName.getFirstName();
//            mName = personName.getMiddleName();
//            lName = personName.getLastName();
//        }
        String jobTitle = bCard.getJobTitle();
        String phone = bCard.getPhoneNumber();
        String email = bCard.getEmail();
        String company = bCard.getCompany();
        String website = bCard.getWebsite();
        String address = bCard.getAddress();
        String notes = bCard.getNotes();
        String frontImagePath = bCard.getFrontImagePath();
        String backImagePath = bCard.getBackImagePath();

//        values.put(CL_name, cardName);
        values.put(CL_first_name, fName);
        values.put(CL_middle_name, mName);
        values.put(CL_last_name, lName);
        values.put(CL_job_title, jobTitle);
        values.put(CL_phone_number, phone);
        values.put(CL_email, email);
        values.put(CL_company, company);
        values.put(CL_website, website);
        values.put(CL_address, address);
        values.put(CL_notes, notes);
//        values.put(CL_front_image_path, frontImagePath);
//        values.put(CL_back_image_path, backImagePath);
//        if (cardName != null && !cardName.isEmpty()) {
//            values.put(CL_name, cardName);
//        }
//        if (fName != null && !fName.isEmpty()) {
//            values.put(CL_first_name, fName);
//        }
//        if (mName != null && !mName.isEmpty()) {
//            values.put(CL_middle_name, mName);
//        }
//        if (lName != null && !lName.isEmpty()) {
//            values.put(CL_last_name, lName);
//        }
//        if (jobTitle != null && !jobTitle.isEmpty()) {
//            values.put(CL_job_title, jobTitle);
//        }
//        if (phone != null && !phone.isEmpty()) {
//            values.put(CL_phone_number, phone);
//        }
//        if (email != null && !email.isEmpty()) {
//            values.put(CL_email, email);
//        }
//        if (company != null && !company.isEmpty()) {
//            values.put(CL_company, company);
//        }
//        if (website != null && !website.isEmpty()) {
//            values.put(CL_website, website);
//        }
//        if (address != null && !address.isEmpty()) {
//            values.put(CL_address, address);
//        }
//        if (notes != null && !notes.isEmpty()) {
//            values.put(CL_notes, notes);
//        }
        if (frontImagePath != null && !frontImagePath.isEmpty()) {
            values.put(CL_front_image_path, frontImagePath);
        }
        if (backImagePath != null && !backImagePath.isEmpty()) {
            values.put(CL_back_image_path, backImagePath);
        }
        return values;
    }

    public ContentValues getUserValues(User user) {
        ContentValues values = new ContentValues();
        String name = user.getName();
        String firstName = user.getFirstName();
        String middleName = user.getMiddleName();
        String lastName = user.getLastName();
        String username = user.getUsername();
        String email = user.getEmail();
        String avatar = user.getAvatar();
        String facebookId = user.getFacebookId();
        String googleId = user.getGoogleId();
        String rememberToken = user.getRememberToken();

        values.put(CL_name, name);
        values.put(CL_first_name, firstName);
        values.put(CL_middle_name, middleName);
        values.put(CL_last_name, lastName);
        values.put(CL_name, name);
        values.put(CL_username, username);
        values.put(CL_email, email);
        values.put(CL_avatar, avatar);
        values.put(CL_facebook_id, facebookId);
        values.put(CL_google_id, googleId);
        values.put(CL_remember_token, rememberToken);
//        values.put(CL_front_image_path, frontImagePath);
//        values.put(CL_back_image_path, backImagePath);
//        if (cardName != null && !cardName.isEmpty()) {
//            values.put(CL_name, cardName);
//        }
//        if (fName != null && !fName.isEmpty()) {
//            values.put(CL_first_name, fName);
//        }
//        if (mName != null && !mName.isEmpty()) {
//            values.put(CL_middle_name, mName);
//        }
//        if (lName != null && !lName.isEmpty()) {
//            values.put(CL_last_name, lName);
//        }
//        if (jobTitle != null && !jobTitle.isEmpty()) {
//            values.put(CL_job_title, jobTitle);
//        }
//        if (phone != null && !phone.isEmpty()) {
//            values.put(CL_phone_number, phone);
//        }
//        if (email != null && !email.isEmpty()) {
//            values.put(CL_email, email);
//        }
//        if (company != null && !company.isEmpty()) {
//            values.put(CL_company, company);
//        }
//        if (website != null && !website.isEmpty()) {
//            values.put(CL_website, website);
//        }
//        if (address != null && !address.isEmpty()) {
//            values.put(CL_address, address);
//        }
//        if (notes != null && !notes.isEmpty()) {
//            values.put(CL_notes, notes);
//        }
        return values;
    }


}
