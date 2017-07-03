package com.procus.simpleuserregistration.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.procus.simpleuserregistration.R;
import com.procus.simpleuserregistration.activities.UserProfileActivity;
import com.procus.simpleuserregistration.listeners.OnUserListInteractionListener;
import com.procus.simpleuserregistration.models.DatabaseHandler;
import com.procus.simpleuserregistration.models.User;
import com.procus.simpleuserregistration.views.UserViewHolder;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Peter on 3.7.17.
 */

public class UserListRecyclerViewAdapter extends RecyclerView.Adapter<UserViewHolder> implements Observer {
    private final OnUserListInteractionListener mListener;


    public UserListRecyclerViewAdapter(OnUserListInteractionListener listener) {
        this.mListener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ( ))
                .inflate (R.layout.fragment_user, parent, false);
        final UserViewHolder result = new UserViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("id", String.valueOf(result.getUserItem().getId()));
                view.getContext().startActivity(intent);
            }
        });
        return result;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        User user = DatabaseHandler.getUsers().get(position);
        if(null != user) {
            holder.setUserItem(user);
            holder.getPersonNameView ().setText (user.getName() + " " + user.getSurname());
        }
    }


    @Override
    public int getItemCount() {
        return DatabaseHandler.getUsers().size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView (recyclerView);
        DatabaseHandler.addUserAddObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        notifyDataSetChanged();
    }
}
