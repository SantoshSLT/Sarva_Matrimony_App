package com.thegreentech.chat.helperClass;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import Models.Chat_Model;

public class Chathelper {

    ChatInterface chatInterface;
    Context mContext;
    MatriMoniDbHelper roraaDbHelper;
    String userId;

    public Chathelper(Context context, ChatInterface cInterface, String uId) {
        mContext = context;
        chatInterface = cInterface;
        roraaDbHelper=new MatriMoniDbHelper(context);
        userId=uId;
     }


    public List<Chat_Model> getChatData() {
       return roraaDbHelper.getChatData(userId);
    }

    public void saveChatData(List<Chat_Model> messageDataList) {

        roraaDbHelper.addChatData(userId,getByteArray(messageDataList));

    }

    private List<byte[]> getByteArray(List<Chat_Model> consolidatedList) {

        List<byte[]> dataArray = new ArrayList<>();

        for (int i = 0; i < consolidatedList.size(); i++) {
            byte[] mybytes;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(consolidatedList.get(i));
                out.flush();
                mybytes = bos.toByteArray();
                if (mybytes != null)
                    dataArray.add(mybytes);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException ex) {
                    // ignore close exception
                }
            }
        }
        if (dataArray.size() > 0) {
            return dataArray;
        }
        return null;
    }

}
