package com.fjln1920.smarclock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.fjln1920.smarclock.Models.Alarm;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class DataBaseManager extends SQLiteOpenHelper {

    // DATAbase constants
    private static final int DATABASE_VERSION = 1; // data version
    private static final String DATABASE_NAME = "db_alarm"; // database name
    private static final String TABLE_NAME = "alarm";  // table name
    private static final String COL_ID = "id";          // this column store alarm' ids
    private static final String COL_TITLE = "alarm_name";    // this column store alarm'names
    private static final String COL_HOUR = "hour";        // this column store alarm' hours
    private static final String COL_MINUTE = "minute";  // this column store alarm' minutes
    private static final String COL_TOGGLE = "toggle";  // this column store alarm' state
    private static final String COL_LABEL_COLOR = "labelColor";  // this column store alarm' label color
    private static final String COL_WEEKDAYS = "weekDays";// this column store alarm' weekdays




    // this string defines table and data type use for onCreate method
    private String CREATE_TABLE_ALARM = "CREATE TABLE " + TABLE_NAME + " ("
            + COL_ID + " INTEGER, "  // this column contain alarm's id
            + COL_HOUR + " INTEGER, "  // this column contain alarm's hour
            + COL_MINUTE + " INTEGER, " // alarm's minute
            + COL_TITLE + " TEXT, "      // alarm's name
            + COL_LABEL_COLOR + " TEXT, "  // alarm's label color
            + COL_WEEKDAYS + " TEXT, "   // alarm's week days
            + COL_TOGGLE + " INTEGER)";    // alarm's toggle detail 1 is on 0 is off

    // TODO:   this is data base constructor
    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create data base for the first time
        db.execSQL(CREATE_TABLE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format(" DROP TABLE IF EXISTS %s", CREATE_TABLE_ALARM));
        onCreate(db);

    }

    // TODO: insert alarm to database
    public void insert(Alarm alarm) {
        // getting write data

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // ContentValues like a box to contain value in there
            ContentValues values = new ContentValues();
            // put value to each column
            values.put(COL_ID, alarm.getId());
            values.put(COL_HOUR, alarm.getHour_x());
            values.put(COL_MINUTE, alarm.getMinute_x());
            values.put(COL_TITLE, alarm.getAlarm_title());
            values.put(COL_LABEL_COLOR, alarm.getAlarm_LabelColor());
            values.put(COL_WEEKDAYS, alarm.getAlarm_WeekDays());
            values.put(COL_TOGGLE, alarm.getOnOff());
            // insert to table
            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // finally close it


    }

    // TODO: this method update new alarm to database
    public void update(Alarm alarm) {
        String where = COL_ID + " = " + alarm.getId();
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // getting write data
            // ContentValues like a box to contain value in there
            ContentValues values = new ContentValues();

            values.put(COL_ID, alarm.getId());
            values.put(COL_HOUR, alarm.getHour_x());
            values.put(COL_MINUTE, alarm.getMinute_x());
            values.put(COL_TITLE, alarm.getAlarm_title());
            values.put(COL_LABEL_COLOR, alarm.getAlarm_LabelColor());
            values.put(COL_WEEKDAYS, alarm.getAlarm_WeekDays());
            values.put(COL_TOGGLE, alarm.getOnOff());
            db.update(TABLE_NAME, values, where, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // always close at the finally


    }

    // TODO: this delete row in databse if the row has alarm'id equal alarmId
    public void delete(Long alarmId) {
        // getting write data

        String where = COL_ID + " = " + alarmId;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(TABLE_NAME, where, null);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    // TODO: get all Alarm from database and return arrayList alarm
    public ArrayList<Alarm> getAlarmList() {
        // getting read data
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Alarm> alarmArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            // method moveToFirst return true if cursor not empty
            if (cursor.moveToFirst()) {
                do {

                    Alarm alarm = new Alarm(cursor.getInt(1), cursor.getInt(2), cursor.getString(3),
                            cursor.getString(4), cursor.getString(5), cursor.getInt(6));
                    alarm.setId(cursor.getLong(0));
                    alarmArrayList.add(alarm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getAlarmList: exception cause " + e.getCause() + " message " + e.getMessage());
        }

        return alarmArrayList;

    }

}
