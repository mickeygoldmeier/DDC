package com.ddc.Model;

public interface NotifyDataChange<T> {
    void OnDataChanged(T obj);

    void onFailure(Exception exception);
}
