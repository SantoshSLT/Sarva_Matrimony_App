package com.thegreentech.chat.helperClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import Models.Chat_Model;
import utills.AppConstants;

public class ChatDbHelper {

    SQLiteOpenHelper sqLiteOpenHelper;
    Context mContext;

    public ChatDbHelper(Context context, SQLiteOpenHelper sql) {
        mContext = context;
        sqLiteOpenHelper = sql;
    }

    public void addChatData(String dialogId, List<byte[]> consolidatedList  ){
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        if (sqLiteDatabase != null) {
            if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
                sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            }

            if (!sqLiteDatabase.isReadOnly()) {
                sqLiteDatabase.close();
                sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            }
        }
        sqLiteDatabase.execSQL("DELETE  FROM " + dialogId);
        sqLiteDatabase.beginTransaction();

        try {
            if (consolidatedList != null)

                for (int i = 0; i < consolidatedList.size(); i++) {
                    ContentValues values = null;
                    values = new ContentValues();
                    values.put(AppConstants.chatDb.COLUMN_NAME_DATE, System.currentTimeMillis() / 1000);
                    values.put(AppConstants.chatDb.COLUMN_NAME_RECORD, consolidatedList.get(i));
                    long value = sqLiteDatabase.insert(dialogId, null, values);
                     Log.e("succed", "adding : " + i);
                }
                sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ChatDbhelper", "60:");
            e.printStackTrace();
        }finally {
            try {
                sqLiteDatabase.endTransaction();
            }catch (Exception e){

            }
        }


    }

    public List<Chat_Model> getChatData(String dialogId) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM xx" + dialogId;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        List<Chat_Model> chatObjectList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                byte[] data = cursor.getBlob(cursor.getColumnIndex(AppConstants.chatDb.COLUMN_NAME_RECORD));
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInput in = null;
                try {
                    in = new ObjectInputStream(bis);
                    Chat_Model o = (Chat_Model) in.readObject();
                    chatObjectList.add(o);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException ex) {
                        // ignore close exception
                    }
                }
            } while (cursor.moveToNext());
            sqLiteDatabase.close();
            cursor.close();
        }

        if (chatObjectList != null && chatObjectList.size() > 0) Log.e("retrieved:", "success");
        return chatObjectList;
    }


    public void createChatTable(String dialogId) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        Log.e("DB", "Create Table Called");
        String createTable = "CREATE TABLE "
                + dialogId
                + " ("
                + AppConstants.chatDb.COLUMN_NAME_DATE
                + " INTEGER,"
                + AppConstants.chatDb.COLUMN_NAME_RECORD
                + " BLOB)";
        sqLiteDatabase.execSQL(createTable);
    }


    public void deleteChatById(String dialogId, String msgId) {
        byte[] record = getMessageRecord(dialogId, msgId);
        if (record != null) {
            SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
            sqLiteDatabase.execSQL("DELETE  FROM xx" + dialogId + " WHERE " + AppConstants.chatDb.COLUMN_NAME_RECORD + " =?", new Object[]{record});
            sqLiteDatabase.close();
            Log.e("Removed ", "From Chat :" + msgId);
        } else {
            Log.e("Some", "Locha");
        }
    }


    public byte[] getMessageRecord(String dialogId, String msgId) {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM xx" + dialogId;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
//        List<ChatObject> chatObjectList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                byte[] data = cursor.getBlob(cursor.getColumnIndex(AppConstants.chatDb.COLUMN_NAME_RECORD));
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInput in = null;
                try {
                    in = new ObjectInputStream(bis);
                    Chat_Model o = (Chat_Model) in.readObject();
//                    if (o.getType(ChillwhrApp.getInstance().getUserId()) != ChatObject.TYPE_DATE)
                        if (o.getId().equalsIgnoreCase(msgId)) {
                            Log.e("Found", "OfflineChat:");
                            sqLiteDatabase.close();
                            cursor.close();
                            return data;
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException ex) {
                        // ignore close exception
                    }
                }
            } while (cursor.moveToNext());
            sqLiteDatabase.close();
            cursor.close();
            return null;
        }
        return null;
    }
}
