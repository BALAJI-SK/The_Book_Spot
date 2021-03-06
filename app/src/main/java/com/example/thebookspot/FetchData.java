package com.example.thebookspot;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchData {
    public static List<Book> fetchDataFromHTTP(String urls){
        Log.i("On Loader", "fetchData()");
        URL url = create(urls);
        String jsonResponse="";
        try{
        jsonResponse= makeHTTPRequest(url);
        }catch (Exception ignored){
    //TODO: catch
        }
        return extractData(jsonResponse);
    }

    private static List<Book> extractData(String jsonResponse) {
        List<Book> bookList = new ArrayList<>();

        try{
         if(jsonResponse==null)return null;
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray items =root.optJSONArray(  "items");
            for(int i=0;items !=null &&i<items.length();i++){String author;
                JSONObject currentBook =items.optJSONObject(i);
                 JSONObject volumeInfo= currentBook.optJSONObject("volumeInfo");
                assert volumeInfo != null;
                String title = volumeInfo.optString("title");
                String infoLink = volumeInfo.optString("infoLink");
                JSONArray authorsNames =volumeInfo.optJSONArray("authors");
               if( authorsNames != null){
                   author = authorsNames.optString(0);}
               else {
                   author="Name not Mentioned";
               }
                JSONObject salesInfo = currentBook.optJSONObject("saleInfo");
                assert salesInfo != null;
                String saleability= salesInfo.optString("saleability");
                String cost="NA";
                if(saleability.equals("FOR_SALE")){
                  JSONObject listPrice= salesInfo.optJSONObject("listPrice");
                    assert listPrice != null;
                    cost = listPrice.optString("amount");
                }
                // For a given book, extract the JSONObject associated with the
                // key called "imageLinks", which represents a list of all cover
                // images in a different size
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                // Extract String URL of specific cover
                String coverImageUrl = imageLinks.getString("smallThumbnail");

                float rating =(float)volumeInfo.optDouble("averageRating");
             bookList.add(new Book(title, coverImageUrl,author, cost, infoLink, rating));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    private static String makeHTTPRequest(URL url) {
        String jsonResponse =null;
        if(url==null) return null;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        try {
            httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(1500);
            httpURLConnection.setConnectTimeout(2500);
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode()==200){
                inputStream=httpURLConnection.getInputStream();
                jsonResponse= getInputStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       return jsonResponse;
    }

    private static String getInputStream(InputStream inputStream) throws IOException{
        if(inputStream==null)return null;
        InputStreamReader inputStreamReader= new InputStreamReader(inputStream);
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        StringBuilder data = new StringBuilder();
while (line!=null){
    data.append(line);
    line=bufferedReader.readLine();
}
return data.toString();
    }

    private static URL create(String link){
        URL url =null;
        if(link==null)return null;
  try {
      url =new URL(link);

  } catch (MalformedURLException e) {
      Log.e("FetchData", "URL Not Found");
  }
        return url;
    }
}
