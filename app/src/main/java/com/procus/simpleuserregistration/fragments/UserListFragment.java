package com.procus.simpleuserregistration.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.procus.simpleuserregistration.R;
import com.procus.simpleuserregistration.adapters.UserListRecyclerViewAdapter;
import com.procus.simpleuserregistration.listeners.OnUserListInteractionListener;
import com.procus.simpleuserregistration.models.DatabaseHandler;

/**
 * Created by Peter on 2.7.17.
 */

public class UserListFragment extends Fragment {
    private OnUserListInteractionListener mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userlist, container, false);
        if(view instanceof RecyclerView){
            Context context = view.getContext ( );
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager (new LinearLayoutManager(context));


            recyclerView.setAdapter (new UserListRecyclerViewAdapter( mListener));

            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback (0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    DatabaseHandler.getInstance(getContext()).deleteOne(DatabaseHandler.getUsers().get(viewHolder.getAdapterPosition()).getId());

                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper (simpleCallback);

            itemTouchHelper.attachToRecyclerView (recyclerView);
        }


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach (context);
        if (context instanceof OnUserListInteractionListener) {
            mListener = (OnUserListInteractionListener) context;
        } else {
            throw new RuntimeException (context.toString ( ) + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach ( );
        mListener = null;
    }

}
