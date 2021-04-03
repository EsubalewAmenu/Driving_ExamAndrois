package com.herma.apps.drivertraining.questions;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import com.hermasoft.grade10.exam.Question;

public class DB extends SQLiteOpenHelper {

    private static String DB_PATH = "";//databases/";
    private static String DB_NAME = "";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DB(Context context, String db_name) {
        super(context, db_name, null, 1);
        DB_PATH = context.getFilesDir().getPath()+"/";
        DB_NAME = db_name;
        this.myContext = context;
    }

    // Creates a empty database
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public void writeDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getWritableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    // Check if the database already exist
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            // If database does't exist.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        //SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    ///////////////////////////////////////////////////////////////////////////////////
    public Cursor getSelect(String select, String from, String where) {
        Cursor cursor = myDataBase.rawQuery("SELECT "+select+" FROM "+from+" WHERE "+ where, null);
        return cursor;
    }

    public Cursor getUpdate(String table, String set, String where) {
        Cursor cursor = myDataBase.rawQuery("UPDATE "+table+" SET "+set+" WHERE "+ where, null);
        return cursor;
    }

    public Cursor getInsertPrize(String date, String price) {
        Cursor cursor = myDataBase.rawQuery("INSERT INTO prize (`date`,`price`) VALUES ('"+date+"', '"+price+"')", null);
        return cursor;
    }

    public Cursor doExcute(String command){
        return myDataBase.rawQuery(command, null);
    }
    public String[][] getSelectArray(String select, String from, String where){
        Cursor c = myDataBase.rawQuery("SELECT "+select+" FROM "+from+" WHERE "+ where, null);
        String arrayString[][] = new String[c.getCount()][7];
        int i = 0;
        if (c.moveToFirst()) {
            do{
                arrayString[i][0] = c.getString(0);
                arrayString[i][1] = c.getString(1);
                arrayString[i][2] = c.getString(2);
                arrayString[i][3] = c.getString(3);
                arrayString[i][4] = c.getString(4);
                arrayString[i][5] = c.getString(5);
                arrayString[i][6] = c.getString(6);
                i++;
            } while (c.moveToNext());
        }
        return arrayString;
    }
}