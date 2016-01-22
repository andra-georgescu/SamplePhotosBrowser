package com.andra.samplephotosbrowser.network;

/**
 * Simple interface allowing for smooth communication between AsyncTasks and their requester.
 * @param <T> the type of the object that the requester is expecting in response
 */
public interface Callback<T> {

    void onSuccess(T response);

    void onError();
}
