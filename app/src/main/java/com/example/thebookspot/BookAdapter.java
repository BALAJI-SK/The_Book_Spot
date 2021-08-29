package com.example.thebookspot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, @NonNull List<Book> objects) {
        super(context, 0, objects);
    }
    static class  ViewHolderItem{
  TextView title,cost,author;
  RatingBar ratingBar;
  ImageView bookImage;
      //  DownloadImageTask  imageAsync;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View listView, @NonNull ViewGroup parent) {

      ViewHolderItem viewHolderItem;
        if(listView ==null){
            listView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_view, parent, false);
            viewHolderItem  =  new ViewHolderItem();
          viewHolderItem.title= listView.findViewById(R.id.title);
          viewHolderItem.bookImage=listView.findViewById(R.id.bookImage);
          viewHolderItem.cost =listView.findViewById(R.id.cost);
          viewHolderItem.author=listView.findViewById(R.id.author);
            viewHolderItem.ratingBar = listView.findViewById(R.id.rating_bar);
          listView.setTag(viewHolderItem);
        }
        else{
            viewHolderItem= (ViewHolderItem)listView.getTag();
        }
        Book currentBook= getItem(position);
        viewHolderItem.title.setText(currentBook.getBookTitle());
        viewHolderItem.author.setText(currentBook.getAuthor());
        viewHolderItem.cost.setText(String.valueOf(currentBook.getBookCost()));

        Picasso.get().load(currentBook.getBookImage()).into(viewHolderItem.bookImage);
        viewHolderItem.ratingBar.setRating(currentBook.getAvgRating());



        return listView;
    }

}
