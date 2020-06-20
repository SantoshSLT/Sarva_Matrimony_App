package utills;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.thegreentech.R;

public class AppMethods
{

    //TODO: Show soft keyboard
    public static void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
     /*   view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);*/
        InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //TODO: Hide soft keyboard
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
