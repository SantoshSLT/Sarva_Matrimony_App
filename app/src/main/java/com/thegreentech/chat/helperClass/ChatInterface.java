package com.thegreentech.chat.helperClass;
/*Create by MINAXI
* DATE =  21 AGUST 2019*/
public interface ChatInterface {


    void NotifyAdapterItem(int i);

    void displayUsersAndImage();

    void setMemberNames(String names);

    void notifyDataSetChanged();

    void scrollToLast();

    void showProgressBar();

    void hideProgressBar();

    void notifyDataSetChanged(int i);

    void updateVideoProgress(int i, String id);

    void DialogModified();
}

