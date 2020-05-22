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

import com.example.smsgateway.Model.Message;
import com.example.smsgateway.R;

import java.util.List;

public class MessageRecyclerViewListAdapter extends RecyclerView.Adapter<MessageRecyclerViewListAdapter.ViewHolder> {

    private List<Message> messagesList;


    public MessageRecyclerViewListAdapter(Context context, List<Message> messages){
        this.messagesList = messages;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listitem = layoutInflater.inflate(R.layout.message_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(listitem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Message msg = messagesList.get(position);

        holder.title.setText(msg.getTitle());
        holder.content.setText(msg.getContent());
        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(msg.getTimestamp()));
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+data.getFristname(),Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timestamp;
        public TextView content;
        public TextView title;
        public TextView dot;

        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.messages_title_list);
            this.content = (TextView) itemView.findViewById(R.id.messages_content_list);
            this.dot = (TextView) itemView.findViewById(R.id.dot);
            this.timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }

    private String formatDate(String dateStr) {
//        try {
//            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = fmt.parse(dateStr);
//            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
//            return fmtOut.format(date);
//        } catch (ParseException e) {
//
//        }

        return "";
    }
}
