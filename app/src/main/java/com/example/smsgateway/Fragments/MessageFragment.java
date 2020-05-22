package com.example.smsgateway.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsgateway.Database.MessageDatabase;
import com.example.smsgateway.Model.Message;
import com.example.smsgateway.R;
import com.example.smsgateway.utils.DividerItemDecoration;
import com.example.smsgateway.utils.MessageRecyclerViewListAdapter;
import com.example.smsgateway.utils.RecyclerTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    MessageDatabase messageDatabase;
    MessageRecyclerViewListAdapter recyclerViewListAdapter;
    private TextView noNotesView1;

    private List<Message> messageList = new ArrayList<>();


    private FragmentMessageListener listener;

    public interface FragmentMessageListener {
        void onMessageSent(Message msg_model);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        messageDatabase = new MessageDatabase(getContext());

        View message_fragment_layout = inflater.inflate(R.layout.message_list, container, false);

        noNotesView1 = message_fragment_layout.findViewById(R.id.empty_message_view);

        FloatingActionButton fab = message_fragment_layout.findViewById(R.id.floatingActionButtonOnAddUser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageDialog(false, null, -1);
            }
        });


        setHasOptionsMenu(true);

        messageList.addAll(getAllMessages());

        RecyclerView recyclerView = (RecyclerView) message_fragment_layout.findViewById(R.id.recyclerView);
        recyclerViewListAdapter = new MessageRecyclerViewListAdapter(getContext(),messageList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));

        recyclerView.setAdapter(recyclerViewListAdapter);
        toggleEmptyNotes();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                showMessageDetailDialog(messageList.get(position),position);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
        return message_fragment_layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_users, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_chat) {
            Toast.makeText(getActivity(), "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
                    .show();
        }
        return true;
    }
//ArrayList als = new ArrayList<>();

    private void toggleEmptyNotes() {
        int count = messageDatabase.getMessageCount();
        if (count > 0) {
            noNotesView1.setVisibility(View.GONE);

        } else {
            noNotesView1.setVisibility(View.VISIBLE);
        }
    }


    List<Message> messages = new ArrayList<>();
    public List<Message> getAllMessages(){

        Cursor result = messageDatabase.getAllMessages();

        // looping through all rows and adding to list
        if (result.moveToFirst()) {
            do {
                Message message = new Message();
                message.setId(result.getInt(result.getColumnIndex(Message.COLUMN_ID)));
                message.setTitle(result.getString(result.getColumnIndex(Message.COLUMN_Title)));
                message.setContent(result.getString(result.getColumnIndex(Message.COLUMN_Content)));
                message.setTimestamp(result.getString(result.getColumnIndex(Message.COLUMN_TIMESTAMP)));
                messages.add(message);
            } while (result.moveToNext());
        }

        // close db connection
        messageDatabase.close();

        // return users list
        return messages;
    }

    private void showMessageDetailDialog( final Message message,final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(message.getTitle());
        builder.setMessage(message.getContent());
        builder.show();
    }




    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showMessageDialog(true, messageList.get(position), position);
                } else {
                    deleteMessage(position);
                }
            }
        });
        builder.show();
    }

    private void showMessageDialog(final boolean shouldUpdate, final Message message, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.message_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(view);

        final EditText title = view.findViewById(R.id.title_dialog_editTxt);
        final EditText content = view.findViewById(R.id.content_dialog_editTxt);

        TextView dialogTitle = view.findViewById(R.id.message_dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_msg_title) : getString(R.string.lbl_show_message));

        if (shouldUpdate && message != null) {
            title.setText(message.getTitle());
            content.setText(message.getContent());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(title.getText().toString())) {
                    Toast.makeText(getContext(), "Enter message!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating user
                if (shouldUpdate && message != null) {
                    // update note by it's id
                    updateMessage(title.getText().toString(),content.getText().toString(), position);
                } else {
                    createMessage(title.getText().toString(),content.getText().toString());
                }
            }
        });

    }
    public void createMessage(String title,String content) {
        // inserting note in db and getting
        // newly inserted note id
        if(messageDatabase==null){
            System.out.println("Creating new message DatabaseHelper"+"\n");
            messageDatabase= new MessageDatabase(getContext());
        }
        long id = messageDatabase.addMessage(title,content);


        // get the newly inserted user from db
        Message n = messageDatabase.getMessage(id);

        if (n != null) {
            // adding new note to array list at 0 position
            messageList.add(0, n);

            // refreshing the list
            recyclerViewListAdapter.notifyDataSetChanged();
            toggleEmptyNotes();
        }
    }
    private void updateMessage(String title,String content, int position) {
        Message message = messageList.get(position);
        // updating note text
        message.setTitle(title);
        message.setContent(content);

        // updating message in db
        messageDatabase.updateMessage(message);

        // refreshing the list
        messageList.set(position, message);
        recyclerViewListAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }
    private void deleteMessage(int position) {
        // deleting the note from db
        messageDatabase.deleteMessage(messageList.get(position));

        // removing the note from the list
        messageList.remove(position);
        recyclerViewListAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentMessageListener) {
            listener = (FragmentMessageListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentMessageListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


}
