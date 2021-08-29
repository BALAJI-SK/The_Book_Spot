package com.example.thebookspot;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class BookAsyncTaskLoader extends AsyncTaskLoader<List<Book>> {
    String url;
    public BookAsyncTaskLoader(@NonNull Context context,String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        Log.i("On Loader", "onStartLoading()");
        forceLoad();
    }

    @Nullable
    @Override
    public List<Book> loadInBackground() {
        Log.i("On Loader", "LoadInBackground()");
        return FetchData.fetchDataFromHTTP(url);
    }
}
