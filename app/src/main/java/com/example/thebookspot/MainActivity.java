package com.example.thebookspot;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{
    private BookAdapter bookArrayAdapter;
    private ProgressBar progressBar;
    private static final int LOADER_ID = 1;

    private TextView emptyTextView;
    private boolean isConnected;
    /**
     * This Search helps to get Book Title. {@mSearchViewField }
     */
    private SearchView mSearchViewField;
    /**
     * This is acts link a link. {@mUrlRequestGoogleBooks}
     */
    private String mUrlRequestGoogleBooks = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("On Loader", "initLoader()");

        progressBar = findViewById(R.id.showProgress);
        emptyTextView = findViewById(R.id.emptyData);
        Button mSearchButton = findViewById(R.id.search);
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        checkConnection(cm);


       mSearchViewField = (SearchView) findViewById(R.id.search_view_field);
        mSearchViewField.onActionViewExpanded();
        mSearchViewField.setIconified(true);
        mSearchViewField.setQueryHint("Enter a book title");

mSearchButton.setOnClickListener(v ->{
    checkConnection(cm);
    if(isConnected){
        updateQueryUrl(mSearchViewField.getQuery().toString());
        restartLoader();
        Log.i("Main Activity", "Search value: " + mSearchViewField.getQuery().toString());
    }else{
        bookArrayAdapter.clear();
        emptyTextView.setVisibility(View.VISIBLE);
        emptyTextView.setText(R.string.no_internet_connection);
    }
});

        if (!isConnected) {
            progressBar.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_internet_connection);
        } else {
            emptyTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.INVISIBLE);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            ListView bookListView = findViewById(R.id.list_view);
            bookArrayAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());
            bookListView.setAdapter(bookArrayAdapter);
            bookListView.setOnItemClickListener((parent, view, position, id) -> {
                Book currentBook = bookArrayAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getWebLink()));
                startActivity(intent);
            });
            mSearchViewField.setOnClickListener(v ->{
                checkConnection(cm);
                if (isConnected) {
                    // Update URL and restart loader to displaying new result of searching
                    updateQueryUrl(mSearchViewField.getQuery().toString());
                    restartLoader();
                    Log.i("Main Activity", "Search value: " + mSearchViewField.getQuery().toString());
                } else {
                    // Clear the adapter of previous book data
                    bookArrayAdapter.clear();
                    // Set mEmptyStateTextView visible
                    emptyTextView.setVisibility(View.VISIBLE);
                    // ...and display message: "No internet connection."
                    emptyTextView.setText(R.string.no_internet_connection);
                }


            });
        }



}

    /**
     * {@mUrlRequestGoogleBooks} helps  to  get new data from URL.
     */
    private void restartLoader() {
        emptyTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
    }

    private void updateQueryUrl(String searchValue) {
        if (searchValue.contains(" ")) {
            searchValue = searchValue.replace(" ", "+");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("https://www.googleapis.com/books/v1/volumes?q=").append(searchValue).append("&filter=paid-ebooks&maxResults=10");
        mUrlRequestGoogleBooks  = sb.toString();
        Log.i("Main Activity", mUrlRequestGoogleBooks);
//mUrlRequestGoogleBooks="  https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";
    }

    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i("On Loader", "onCreateLoader()");
        return new BookAsyncTaskLoader(MainActivity.this, mUrlRequestGoogleBooks);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> data) {
        Log.i("On Loader", "onLoadFinished()");
        bookArrayAdapter.clear();
        progressBar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            bookArrayAdapter.addAll(data);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_data_present);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        bookArrayAdapter.clear();
    }

    public void checkConnection(ConnectivityManager connectivityManager) {
        // Status of internet connection
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {
            isConnected = true;

            Log.i("Main Activity", "INTERNET connection status: " + String.valueOf(true) + ". It's time to play with LoaderManager :)");

        } else {
            isConnected = false;

        }
    }
}