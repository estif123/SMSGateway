package com.example.smsgateway.utils;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsgateway.Model.User;
import com.example.smsgateway.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserRecyclerViewListAdapter extends RecyclerView.Adapter<UserRecyclerViewListAdapter.ViewHolder>{

    private List<User> usersList;


    public UserRecyclerViewListAdapter(Context context, List<User> users){
        this.usersList = users;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listitem = layoutInflater.inflate(R.layout.user_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(listitem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User data = usersList.get(position);

        holder.firstname.setText(data.getFristname());
        holder.lastname.setText(data.getLastname());
        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(data.getTimestamp()));
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+data.getFristname(),Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timestamp;
        public TextView firstname;
        public TextView lastname;
        public TextView dot;

        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.firstname = (TextView) itemView.findViewById(R.id.user_list_tV_first_name);
            this.lastname = (TextView) itemView.findViewById(R.id.user_list_textView_lastname);
            this.dot = (TextView) itemView.findViewById(R.id.dot);
            this.timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
