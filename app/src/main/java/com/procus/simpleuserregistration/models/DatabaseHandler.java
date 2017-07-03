package com.procus.simpleuserregistration.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.procus.simpleuserregistration.observers.UserObservable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

/**
 * Created by Peter on 3.7.17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static UserObservable userObservable = new UserObservable();
    private static DatabaseHandler instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDB";
    private static final String TABLE_NAME = "Users";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_REG_TIME = "registerTime";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE= "latitude";
    private static final String DEV_ID= "devID";

    private static List<User> users = new ArrayList<>();

    private static final String[] COLUMNS = { KEY_ID, KEY_NAME, KEY_SURNAME,
            KEY_REG_TIME, KEY_BIRTHDAY, KEY_PHOTO, KEY_LONGITUDE, KEY_LATITUDE };

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(Context context){
        if(instance == null){
            instance = new DatabaseHandler(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Users ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "surname TEXT, "
                + "registerTime TEXT, "
                + "birthday TEXT, "
                + "photo TEXT, "
                + "longitude TEXT, "
                + "latitude TEXT,"
                + "devID TEXT"
                + ")";
        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteOne(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(id) });
        db.close();
        this.allUsers();
    }


    public List<User> allUsers() {

        List<User> users = new LinkedList<>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user = null;

        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setSurname(cursor.getString(2));
                user.setRegisterTime(cursor.getString(3));
                user.setBirthday(cursor.getString(4));
                user.setPhoto(cursor.getString(5));
                user.setLongitude(Double.valueOf(cursor.getString(6)));
                user.setLatitude(Double.valueOf(cursor.getString(7)));
                user.setDevId(cursor.getString(8));
                users.add(user);
            } while (cursor.moveToNext());
        }
        this.users = new ArrayList<>(users);
        userObservable.usersChanged();
        userObservable.notifyObservers();
        return users;
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_SURNAME, user.getSurname());
        values.put(KEY_REG_TIME, user.getRegisterTime());
        values.put(KEY_BIRTHDAY, user.getBirthday());
        values.put(KEY_PHOTO, user.getPhoto());
        values.put(KEY_LONGITUDE, user.getLongitude());
        values.put(KEY_LATITUDE, user.getLatitude());
        values.put(DEV_ID, user.getDevId());
        db.insert(TABLE_NAME,null, values);
        db.close();
        this.allUsers();
    }

    public static List<User> getUsers() {
        return users;
    }

    public static User getUserById(Integer id){
        for(User us : users){
            if(us.getId() == id){
                return us;
            }
        }
        return null;
    }

    public static void addUserAddObserver(Observer o) {
        userObservable.addObserver(o);
    }


}
