package com.example.thebookspot;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
BookAdapter bookArrayAdapter;
    String link="  https://www.googleapis.com/books/v1/volumes?q=atomic+habits&maxResults=4";
    List<Book> bookList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
        bookList= FetchData.fetchDataFromHTTP(link);
        runOnUiThread(() -> {
            ListView bookListView = findViewById(R.id.list_view);
            bookArrayAdapter =new BookAdapter(MainActivity.this, bookList);
            bookListView.setAdapter(bookArrayAdapter);
        });
        });
    }


}