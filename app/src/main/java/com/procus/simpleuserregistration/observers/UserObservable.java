package com.procus.simpleuserregistration.observers;

import java.util.Observable;

/**
 * Created by Peter on 3.7.17.
 */

public class UserObservable extends Observable {
    public void usersChanged() {
        setChanged();
    }
}
