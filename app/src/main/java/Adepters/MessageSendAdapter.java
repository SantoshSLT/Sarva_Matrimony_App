package Adepters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thegreentech.R;

import java.util.ArrayList;
import java.util.Locale;

import Models.MessageSend;
import Models.beanCity;
import utills.AppConstants;

public class MessageSendAdapter extends RecyclerView.Adapter<MessageSendAdapter.MyViewHolder> {

    public Context context;
    ArrayList<MessageSend> arrSendList;
    private ArrayList<MessageSend> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtCity;
    public setOnClickLis setOnClickLis;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public LinearLayout cardView;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            cardView = (LinearLayout) view.findViewById(R.id.cardView);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_row, parent, false);
        return new MyViewHolder(itemView);
    }

    public MessageSendAdapter(Context context, ArrayList<MessageSend> fields_list, RelativeLayout SlidingDrawer, LinearLayout Slidingpage,
                              ImageView btnMenuClose, EditText edtCityCode) {

        this.context = context;
        this.arrSendList = fields_list;
        this.SlidingDrawer = SlidingDrawer;
        this.Slidingpage = Slidingpage;
        this.btnMenuClose = btnMenuClose;
        this.edtCity = edtCityCode;
        this.arrFilter = new ArrayList<MessageSend>();
        this.arrFilter.addAll(arrSendList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MessageSend msgbean = arrSendList.get(position);

        if (msgbean.getUser_name() != null && !msgbean.getUser_name().equalsIgnoreCase("")) {
            Log.e("name", msgbean.getUser_name());
            holder.tv_name.setText(msgbean.getUser_name() + " (" + msgbean.getUser_id() + ")");
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppConstants.UserName = msgbean.getUser_name();
                AppConstants.UserID = msgbean.getUser_id();

                Log.e("constaNAme", msgbean.getUser_name());


                SlidingDrawer.setVisibility(View.GONE);
                SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

                Slidingpage.setVisibility(View.GONE);
                Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

                btnMenuClose.setVisibility(View.GONE);
                btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);

                if (setOnClickLis != null) {
                    setOnClickLis.clickOnListMatriId(position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if (arrSendList == null && arrSendList.size() < 0)
            return 0;
        else
            return arrSendList.size();
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrSendList.clear();
        if (charText.length() == 0) {
            arrSendList.addAll(arrFilter);
        } else {
            for (MessageSend wp : arrFilter) {
                String ProductName = wp.getUser_name();
                String UserId = wp.getUser_id();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText) || UserId.toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrSendList.add(wp);
                }
            }
        }

        if (arrSendList.size() == 0) {
            arrSendList.addAll(arrFilter);
        } else {
            notifyDataSetChanged();
        }
    }

    public interface setOnClickLis {
        void clickOnListMatriId(int position);
    }

    public void clickOnListMatriId(setOnClickLis setOnClickLis) {
        this.setOnClickLis = setOnClickLis;
    }
}
