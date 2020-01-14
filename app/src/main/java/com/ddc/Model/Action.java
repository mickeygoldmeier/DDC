package com.ddc.Model;

public interface Action<T> {
    void onSuccess(T obj);

    void onFailure(Exception exception);

    void onProgress(String status, double percent);
}
