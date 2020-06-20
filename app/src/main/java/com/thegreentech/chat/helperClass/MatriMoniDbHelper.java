package com.thegreentech.chat.helperClass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Models.Chat_Model;

public class MatriMoniDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MatriMonyApp";
    public static final String CHAT_TABLE="xx";
    public static final String TAG = MatriMoniDbHelper.class.getSimpleName();
    private static ChatDbHelper chatDbHelper;
    Context mContext;

    public MatriMoniDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        try {
            if (chatDbHelper == null)
                chatDbHelper = new ChatDbHelper(mContext, this);

//            if (contactsDbHelper == null)
//                contactsDbHelper = new ContactsDbHelper(mContext, this);

//            if (dialogsDBHelper == null)
//                dialogsDBHelper = new DialogsDBHelper(mContext, this);
//
//            if (favouriteVendorHelper == null)
//                favouriteVendorHelper = new FavouriteVendorHelper(mContext, this);
//
//            if (calendarEventHelper == null)
//                calendarEventHelper = new CalendarEventHelper(mContext, this);
//
//            if (offlineChatHelper == null) {
//                offlineChatHelper = new OfflineChatHelper(mContext, this);
//            }

//            if (notificationHelper == null) {
//                notificationHelper = new NotificationHelper(mContext, this);
//            }
//            //Create table for dialogs if not exists
//            if (!isTableExists(Constants.DIALOG_TABLE)) {
//                dialogsDBHelper.createDialogsTable();
//            }
//
//            if (!isTableExists(Constants.OFFLINE_CHAT_TABLE)) {
//                offlineChatHelper.createOfflineChatTable();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        try {
//            String CREATE_USER_TABLE = "CREATE TABLE if not exists " + TABLE_NAME + "("
//                    + KEY_ID + " INTEGER PRIMARY KEY autoincrement," + COLUMN_VENDOR_ID + " INTEGER," + COLUMN_IS_FAV + " INTEGER," + COLUMN_VENDOR_NAME + " TEXT," + COLUMN_VENDOR_ADDRESS + " TEXT" + ")";
//            db.execSQL(CREATE_USER_TABLE);
//            //db.execSQL(VendorFavContract.SQL_CREATE_FAV_VENDOR);
////            Log.e(TAG, "Created Successssss");
//        } catch (Exception e) {
//            Log.e("OnCreate", "MatriMoniDbHelper:88");
//            e.printStackTrace();
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            clearAllTables();
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("OnUpgrade", "MatriMoniDbHelper:101");
        }
    }

    public boolean isTableExists(String dialogId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase != null) {
            if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
                sqLiteDatabase = getReadableDatabase();
            }

            if (!sqLiteDatabase.isReadOnly()) {
                sqLiteDatabase.close();
                sqLiteDatabase = getReadableDatabase();
            }
        }

        Cursor cursor = sqLiteDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + dialogId + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void clearAllTables() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase != null) {
            if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
                sqLiteDatabase = getReadableDatabase();
            }

            if (!sqLiteDatabase.isReadOnly()) {
                sqLiteDatabase.close();
                sqLiteDatabase = getReadableDatabase();
            }
        }
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        //noinspection TryFinallyCanBeTryWithResources not available with API < 19
        try {
            List<String> tables = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0));
            }

            for (String table : tables) {
                if (table.startsWith("sqlite_")) {
                    continue;
                }
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table);
                Log.v("DATABSE OP", "Dropped table " + table);
            }
        } finally {
            cursor.close();
        }
    }

    //For Chatting
    public void addChatData(String dialogId, List<byte[]> consolidatedList ) {
        chatDbHelper.addChatData(CHAT_TABLE +dialogId, consolidatedList);
    }

    public List<Chat_Model> getChatData(String dialogId) {
        if (isTableExists(CHAT_TABLE + dialogId))
            return chatDbHelper.getChatData(dialogId);
        else {
            chatDbHelper.createChatTable(CHAT_TABLE + dialogId);
            return chatDbHelper.getChatData(dialogId);
        }
    }

    // For Contacts
//    public void createContactTable() {
//        contactsDbHelper.createContactTable();
//    }
//
//    public void addContactData(List<byte[]> consolidatedList) {
//        contactsDbHelper.addContactData(consolidatedList);
//    }
//
//    public ArrayList<CustomQbUser> getContactData() {
//        return contactsDbHelper.getContactData();
//
//    }
//
//    // For Dialogs
//    public void addDialogs(List<QBChatDialog> consolidatedList, Long updateTime) {
//        dialogsDBHelper.addDialogs(consolidatedList, updateTime);
//    }
//
//    public QBChatDialog getDialogByID(String dialogId) {
//        try {
//            return dialogsDBHelper.getDialogByID(dialogId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void updateDialogsIfNotSame(ArrayList<QBChatDialog> consolidatedList, Long updateTime, Boolean isRemove) {
//
//        try {
//            if (isRemove)
//                dialogsDBHelper.removeAllDialogs();
//            dialogsDBHelper.updateDialogsIfNotSame(consolidatedList, updateTime);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public boolean updateDialog(QBChatDialog chatDialog, Long updateTime) {
//        try {
//            return dialogsDBHelper.updateDialog(chatDialog, updateTime);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public ArrayList<QBChatDialog> getDialogsFromDb() {
//        try {
//            return dialogsDBHelper.getDialogsFromDb();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void deleteDialog(String dialogId) {
//        try {
//            dialogsDBHelper.deleteDialog(dialogId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void createFavVendorTable() {
//        favouriteVendorHelper.createFavVendorTable();
//    }
//
//    public boolean getFavVendor(int id) {
//        return favouriteVendorHelper.getFavVendor(id);
//    }
//
//    public boolean checkFavAlreadyExist(VendorNew vendor) {
//        return favouriteVendorHelper.checkItemExist(vendor);
//    }
//
//    public boolean updateFavVandor(VendorNew vendor, boolean isFav) {
//        return favouriteVendorHelper.updateFavVandor(vendor, isFav);
//    }
//
//    public boolean insertFavVandor(VendorNew vendor, boolean isFav) {
//        return favouriteVendorHelper.insertFavVandor(vendor, isFav);
//    }
//
//    public List<Vendor> getAllFavVondorsName() {
//        return favouriteVendorHelper.getAllFavVondorsName();
//    }
//
//    public ArrayList<FavItem> getAllFavVondors() {
//        return favouriteVendorHelper.getAllFavVondors();
//    }
//
//
//    public void addOfflineChatData(String dialogId, List<ChatObject> consolidatedList) {
//        offlineChatHelper.addOfflineChatData(dialogId, consolidatedList);
//    }
//
//    public void removeOfflineChatData(String dialogId) {
//        offlineChatHelper.removeOfflineChatData(dialogId);
//    }
//
//    public ArrayList<OfflineChatModel> getAllOfflineChatData() {
//        return offlineChatHelper.getAllOfflineChatData();
//    }
//
//    public ArrayList<ChatObject> getOfflineChatData(String dialogId) {
//        return offlineChatHelper.getOfflineChatData(dialogId);
//    }
//
//    public void removeOfflineChatById(String id) {
//        offlineChatHelper.removeOfflineChatById(id);
//    }


    public void deleteChatMessage(String dialogID, String id) {
        chatDbHelper.deleteChatById(dialogID, id);
    }

//    public void createCalendarEventTable() {
//        calendarEventHelper.createCalendarEventTable();
//    }
//
//    public void insertCalendarEvent(UpcomingPlanDetails upcomingPlanDetails, boolean isActive, String eventId) {
//        calendarEventHelper.insertCalendarEvent(upcomingPlanDetails, isActive, eventId);
//    }
//
//    public void updateCalendarEvent(UpcomingPlanDetails upcomingPlanDetails, boolean isActive, String eventId) {
//        calendarEventHelper.updateCalendarEvent(upcomingPlanDetails, isActive, eventId);
//    }
//
//    public boolean checkEventExist(Long activityId) {
//        return calendarEventHelper.checkEventExist(activityId);
//    }
//
//    public String getEventId(String activityId) {
//        return calendarEventHelper.getCalendarEvent(activityId);
//    }
//
//    public boolean deleteEvent(String eventId) {
//        return calendarEventHelper.deleteEvent(eventId);
//    }
//
//    public void createNotificationTable() {
//        notificationHelper.createNotificationTable();
//    }
//
//    public void insertNotification(LstPushNotificationLog lstPushNotificationLog) {
//        notificationHelper.insertNotification(lstPushNotificationLog);
//    }
//
//    public void insertNotificationStatus(String notificationId, int status) {
//        notificationHelper.insertNotificationStatus(notificationId, status);
//    }
//
//    public void updateNotificationStatus(String notificationId, int status) {
//        notificationHelper.updateNotificationStatus(notificationId, status);
//    }
//
//    public ArrayList<LstPushNotificationLog> getAllNotifications() {
//        return notificationHelper.getAllNotifications();
//    }
//
//    public ArrayList<NotificationStatusBean> getAllNotificationStatus() {
//        return notificationHelper.getAllNotificationStatus();
//    }
//
//    public int countNotificationBadge() {
//        return notificationHelper.countNotificationBadge();
//    }
}
