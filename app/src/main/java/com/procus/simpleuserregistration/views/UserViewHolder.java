package com.procus.simpleuserregistration.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.procus.simpleuserregistration.R;
import com.procus.simpleuserregistration.models.User;

/**
 * Created by Peter on 3.7.17.
 */

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    final private View view;
    final private TextView personNameView;
    private User userItem;

    public UserViewHolder(View view) {
        super(view);
        this.view = view;
        personNameView = (TextView) view.findViewById (R.id.usernameViewHolder);
    }

    @Override
    public String toString() {
        return super.toString ( ) + " '" + personNameView.getText ( ) + "'";
    }

    @Override
    public void onClick(View view) {
        System.out.print("click");
    }

    public View getView() {
        return view;
    }

    public TextView getPersonNameView() {
        return personNameView;
    }

    public User getUserItem() {
        return userItem;
    }

    public void setUserItem(User userItem) {
        this.userItem = userItem;
    }
}
