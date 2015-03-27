package com.example.moivebookapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseConnector 
{
   // database name
   private static final String DATABASE_NAME = "UserMoviesDB";
      
   private SQLiteDatabase database; // for interacting with the database
   private DatabaseOpenHelper databaseOpenHelper; // creates the database

   // public constructor for DatabaseConnector
   public DatabaseConnector(Context context) 
   {
      // create a new DatabaseOpenHelper
      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   }

   // open the database connection
   public void open() throws SQLException 
   {
      // create or open a database for reading/writing
      database = databaseOpenHelper.getWritableDatabase();
   }

   // close the database connection
   public void close() 
   {
      if (database != null)
         database.close(); // close the database connection
   } 

   // inserts a new contact in the database
   public long insertMoive(String title, String year, String director,  
      String rating, String length, String type, String soundeffects) 
   {
      ContentValues newMoive = new ContentValues();
      newMoive.put("title", title);
      newMoive.put("year", year);
      newMoive.put("director", director);
      newMoive.put("rating", rating);
      newMoive.put("length", length);
      newMoive.put("type", type);
      newMoive.put("soundeffects", soundeffects);

      open(); // open the database
      long rowID = database.insert("movies", null, newMoive);
      close(); // close the database
      return rowID;
   } 

   // updates an existing contact in the database
   public void updateMoive(long id,String title, String year, String director,  
		      String rating, String length, String type, String soundeffects)  
   {
      ContentValues editMoive = new ContentValues();
      editMoive.put("title", title);
      editMoive.put("year", year);
      editMoive.put("director", director);
      editMoive.put("rating", rating);
      editMoive.put("length", length);
      editMoive.put("type", type);
      editMoive.put("soundeffects", soundeffects);

      open(); // open the database
      database.update("movies", editMoive, "_id=" + id, null);
      close(); // close the database
   } // end method updateContact

   // return a Cursor with all contact names in the database
   public Cursor getAllMoives() 
   {
      return database.query("movies", new String[] {"_id", "title"}, 
         null, null, null, null, "title");
   } 

   // return a Cursor containing specified contact's information 
   public Cursor getOneMoive(long id) 
   {
      return database.query(
         "movies", null, "_id=" + id, null, null, null, null);
   } 

   // delete the contact specified by the given String name
   public void deleteMoive(long id) 
   {
      open(); // open the database
      database.delete("movies", "_id=" + id, null);
      close(); // close the database
   } 
   
   private class DatabaseOpenHelper extends SQLiteOpenHelper 
   {
      // constructor
      public DatabaseOpenHelper(Context context, String title,
         CursorFactory factory, int version) 
      {
         super(context, title, factory, version);
      }

      // creates the contacts table when the database is created
      @Override
      public void onCreate(SQLiteDatabase db) 
      {
    	
         // query to create a new table named contacts
         String createQuery = "CREATE TABLE movies" +
            "(_id integer primary key autoincrement," +
            "title TEXT, year TEXT, director TEXT, " +
            "rating TEXT, length TEXT, type TEXT, soundeffects TEXT);";
                  
         db.execSQL(createQuery); // execute query to create the database
      } 

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      }
   } // end class DatabaseOpenHelper
} 
