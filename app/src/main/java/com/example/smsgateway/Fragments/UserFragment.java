package com.example.smsgateway.Fragments;

import android.app.AlertDialog;
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

import com.example.smsgateway.Database.UserDatabase;
import com.example.smsgateway.Model.User;
import com.example.smsgateway.R;
import com.example.smsgateway.utils.DividerItemDecoration;
import com.example.smsgateway.utils.RecyclerTouchListener;
import com.example.smsgateway.utils.UserRecyclerViewListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    UserDatabase userDatabase;
    UserRecyclerViewListAdapter recyclerViewListAdapter;
    private TextView noNotesView;
    private List<User> usersList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userDatabase = new UserDatabase(getContext());

        View user_fragment_layout = inflater.inflate(R.layout.user_list, container, false);
        noNotesView = user_fragment_layout.findViewById(R.id.empty_notes_view);

        FloatingActionButton fab = user_fragment_layout.findViewById(R.id.floatingActionButtonOnAddUser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserDialog(false, null, -1);
            }
        });

        setHasOptionsMenu(true);

        usersList.addAll(getAllUsers());

        RecyclerView recyclerView = (RecyclerView) user_fragment_layout.findViewById(R.id.recyclerView);
        recyclerViewListAdapter = new UserRecyclerViewListAdapter(getContext(),usersList);
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

            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        return user_fragment_layout;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_users, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_m) {
//            Toast.makeText(getActivity(), "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
//                    .show();
//        }
        return true;
    }

    List<User> users = new ArrayList<>();
    public List<User> getAllUsers(){

        Cursor result = (Cursor) userDatabase.getAllUsers();

        // looping through all rows and adding to list
        if (result.moveToFirst()) {
            do {
                User user = new User();
                user.setId(result.getInt(result.getColumnIndex(User.COLUMN_ID)));
                user.setFristname(result.getString(result.getColumnIndex(User.COLUMN_FRIST_NAME)));
                user.setLastname(result.getString(result.getColumnIndex(User.COLUMN_LAST_NAME)));
                user.setPhone_number(result.getString(result.getColumnIndex(User.COLUMN_PHONE_NUMBER)));
                user.setTimestamp(result.getString(result.getColumnIndex(User.COLUMN_TIMESTAMP)));
                users.add(user);
            } while (result.moveToNext());
        }

        // close db connection
        userDatabase.close();

        // return users list
        return users;
    }

    private void toggleEmptyNotes() {

        if (userDatabase.getUsersCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showUserDialog(true, usersList.get(position), position);
                } else {
                    deleteUser(position);
                }
            }
        });
        builder.show();
    }
    private void showUserDialog(final boolean shouldUpdate, final User user, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.user_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(view);

        final EditText first_name = view.findViewById(R.id.firstname_dialog_editTxt);
        final EditText last_name = view.findViewById(R.id.last_name_dialog_editTxt);
        final EditText phone_number = view.findViewById(R.id.phone_number_dialog_editTxt);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_user_title) : getString(R.string.lbl_edit_user_title));

        if (shouldUpdate && user != null) {
            first_name.setText(user.getFristname());
            last_name.setText(user.getLastname());
            phone_number.setText(user.getPhone_number());
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
                if (TextUtils.isEmpty(first_name.getText().toString())) {
                    Toast.makeText(getContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating user
                if (shouldUpdate && user != null) {
                    // update note by it's id
                    updateUser(first_name.getText().toString(),last_name.getText().toString(),phone_number.getText().toString(), position);
                } else {
                    createUser(first_name.getText().toString(),last_name.getText().toString(),phone_number.getText().toString());
                }
            }
        });
    }
    private void createUser(String fname,String lname,String phone) {
        // inserting note in db and getting
        // newly inserted note id
        long id = userDatabase.addUser(fname,lname,phone);


        // get the newly inserted user from db
        User n = userDatabase.getUser(id);

        if (n != null) {
            // adding new note to array list at 0 position
            usersList.add(0, n);

            // refreshing the list
            recyclerViewListAdapter.notifyDataSetChanged();
            toggleEmptyNotes();
        }
    }

    private void updateUser(String firstname,String lastname,String phone_number, int position) {
        User user = usersList.get(position);
        // updating note text
        user.setFristname(firstname);
        user.setLastname(lastname);
        user.setPhone_number(phone_number);


        // updating note in db
        userDatabase.updateUser(user);

        // refreshing the list
        usersList.set(position, user);
        recyclerViewListAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    private void deleteUser(int position) {
        // deleting the note from db
        userDatabase.deleteUser(usersList.get(position));

        // removing the note from the list
        usersList.remove(position);
        recyclerViewListAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }
}
