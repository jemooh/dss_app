package com.example.leticia.dss.database;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.leticia.dss.Model.Myrating;
import com.example.leticia.dss.Model.Options;
import com.example.leticia.dss.Model.SeekBars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 11;

    // Database Name
    private static final String DATABASE_NAME = "dss.db";

    // Login table name
    private static final String TABLE_LOGIN = "user";
    private static final String TABLE_OPTIONS = "Options";
    private static final String TABLE_MYRATING = "myrating";
    private static String DB_PATH = "/data/data/com.example.leticia.dss/databases/";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    // Login Table Columns names

    private static final String KEY_OPTION_ID = "option_id";
    private static final String KEY_OPTION_TITLE = "title";
    private static final String KEY_OPTION_RATING = "rating";
    private static final String KEY_OPTION_POINTS = "points";
    private static final String KEY_OPTION_COLOR= "color";
    private static final String KEY_OPTION_COLOR_OPPONENT= "opponent_color";
    private static final String KEY_OPTION_COLOR_ID= "color_id";
    private static final String KEY_OPTION_COLOR_OPPONENT_ID= "opponent_color_id";
    private static final String KEY_OPTION_NEGOTIATION_ID = "negotiation_id";
    private static final String KEY_ID = "id";

    private static final String KEY_MYRATING = "myrating";
    private static final String KEY_POINTS = "points";

    private static final String KEY_USERNAME= "username";
    private static final String KEY_PASSWORD = "password";






    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
            } catch (IOException e) {

                throw new Error(e.toString());

            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        File dbFile = new File(DB_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_OPTION_TABLE = "CREATE TABLE " + TABLE_OPTIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_OPTION_ID + " TEXT ,"
                + KEY_OPTION_TITLE + " TEXT,"
                + KEY_OPTION_RATING + " TEXT,"
                + KEY_OPTION_POINTS + " TEXT,"
                + KEY_OPTION_COLOR_ID + " TEXT,"
                + KEY_OPTION_COLOR + " TEXT,"
                + KEY_OPTION_COLOR_OPPONENT_ID + " TEXT,"
                + KEY_OPTION_COLOR_OPPONENT + " TEXT,"
                + KEY_OPTION_NEGOTIATION_ID + " TEXT)";
        db.execSQL(CREATE_OPTION_TABLE);

        String CREATE_MYRATING_TABLE = "CREATE TABLE " + TABLE_MYRATING + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_OPTION_ID + " TEXT UNIQUE,"
                + KEY_MYRATING + " TEXT,"
                + KEY_POINTS + " TEXT)";
        db.execSQL(CREATE_MYRATING_TABLE);

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);




    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYRATING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }
    /**
     * Storing user details in database
     * */
    public void adduserdetails(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);

        // Inserting Row ,
        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }

    public void addOptions(String id, String title, String rating, String points,String color_id,String mycolor,String op_color_id,String negotiation_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OPTION_ID, id);
        values.put(KEY_OPTION_TITLE, title);
        values.put(KEY_OPTION_RATING, rating);
        values.put(KEY_OPTION_POINTS, points);
        values.put(KEY_OPTION_COLOR_ID, color_id);
        values.put(KEY_OPTION_COLOR, mycolor);
        values.put(KEY_OPTION_COLOR_OPPONENT, mycolor);
        values.put(KEY_OPTION_COLOR_OPPONENT_ID, op_color_id);
        values.put(KEY_OPTION_NEGOTIATION_ID, negotiation_id);

        // Inserting Row ,
        db.insert(TABLE_OPTIONS, null, values);
        db.close();
    }

    public void addmyrating(String id, String myrating, String points) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OPTION_ID, id);
        values.put(KEY_MYRATING, myrating);
        values.put(KEY_POINTS, points);

        // Inserting Row ,
        db.insert(TABLE_MYRATING, null, values);
        db.close();
    }

    public void insertPoints(String option_id,String rating, String points ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OPTION_RATING, rating);
        values.put(KEY_OPTION_POINTS, points);
        //values.put(KEY_OPTION_COLOR, color);

        // Inserting Row ,
        db.update(TABLE_OPTIONS, values, KEY_OPTION_ID+"="+option_id, null);
        //db.update(TABLE_OPTIONS, values,  KEY_OPTION_ID  +"="+option_id, null);
        db.close();
    }

    public void insertColor(String option_id,String color_id,String mycolor ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OPTION_COLOR_ID, color_id);
        values.put(KEY_OPTION_COLOR, mycolor);

        // Inserting Row ,
        db.update(TABLE_OPTIONS, values, KEY_OPTION_ID+"="+option_id, null);
        db.close();
    }

    public void insertOpColor(String option_id,String color,String oppcolor ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OPTION_COLOR_OPPONENT_ID, color);
        values.put(KEY_OPTION_COLOR_OPPONENT, oppcolor);

        // Inserting Row ,
        db.update(TABLE_OPTIONS, values, KEY_OPTION_ID+"="+option_id, null);
        db.close();
    }

    /**
     * Get list of Users from SQLite DB as Array List
     * @return

    public List<Myrating> getAllpoints() {
        List<Myrating> optionsList;
        optionsList = new ArrayList<Myrating>();
        String selectQuery = "SELECT * FROM " + TABLE_MYRATING;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String option_id = cursor.getString(2);
                String rating = cursor.getString(3);


                // Loop round our JSON list of lessons creating Lesson objects to use within our app
                // Create the video object and add it to our list
                optionsList.add(new Myrating( option_id, rating,null));


            } while (cursor.moveToNext());
        }
        database.close();
        return optionsList;
    }*/


    /**
     * Getting user data from database
     *
    public ArrayList<HashMap<String, String>>  getmyratings(){

        ArrayList<HashMap<String, String>> rating = new ArrayList<HashMap<String, String>>();
        HashMap<String,String> map = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_MYRATING;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                map.put("option_id", cursor.getString(1));
                map.put("myrating", cursor.getString(2));
                map.put("point", cursor.getString(3));

                rating.add(map);


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return rating ;

    }*/


    /**
     * Getting user data from database
     * */
		  /**/  public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("pass", cursor.getString(2));
        }
        cursor.close();
        db.close();
        return user;
        }

    public JSONArray getJsonmyratings() throws JSONException {

        JSONObject rowobject;

        JSONArray jsonarray = new JSONArray();

        String selectQuery = "SELECT  * FROM " + TABLE_MYRATING;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int totalColumn = cursor.getColumnCount();
        // Move to first row
        if (cursor.moveToFirst()) {
            do {

                rowobject = new JSONObject();
                rowobject.put("preference", "preferences[" + cursor.getString(1) + "]");
                rowobject.put("myrating", cursor.getString(2));


                jsonarray.put(rowobject);


            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return jsonarray ;

    }
    /**
     * Get list of Users from SQLite DB as Array List
     * @return
      */
    public List<Options> getAllOptions() {
        List<Options> optionsList;
        optionsList = new ArrayList<Options>();
        String selectQuery = "SELECT * FROM " + TABLE_OPTIONS;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String option_id = cursor.getString(1);
                String title = cursor.getString(2);
                String rating = cursor.getString(3);
                String points = cursor.getString(4);
                String color_id = cursor.getString(5);
                String mycolor = cursor.getString(6);
                String opponent_color_id = cursor.getString(7);
                // Loop round our JSON list of lessons creating Lesson objects to use within our app
                // Create the video object and add it to our list
                optionsList.add(new Options(option_id , title, rating,points,color_id,mycolor,opponent_color_id));
            } while (cursor.moveToNext());
            Log.d("optionsList:check",""+optionsList.toString());
        }
        database.close();
        return optionsList;
    }



    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public List<SeekBars> getAllSeekBarList() {
        List<SeekBars> seekBarsList;
        seekBarsList = new ArrayList<SeekBars>();
        String selectQuery = "SELECT * FROM " + TABLE_OPTIONS;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(1);
                String title = cursor.getString(2);
                String negotiation_d = cursor.getString(5);

                // Loop round our JSON list of lessons creating Lesson objects to use within our app
                // Create the video object and add it to our list
                seekBarsList.add(new SeekBars(id,title,negotiation_d));

            } while (cursor.moveToNext());
        }
        database.close();
        return seekBarsList;
    }


    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTableLogin(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_OPTIONS, null, null);
        db.delete(TABLE_MYRATING, null, null);
        db.close();
    }

}