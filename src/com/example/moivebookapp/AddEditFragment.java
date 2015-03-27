package com.example.moivebookapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AddEditFragment extends Fragment
{
// callback method implemented by MainActivity  
public interface AddEditFragmentListener
{
   // called after edit completed so Movie can be redisplayed
   public void onAddEditCompleted(long rowID);
}

private AddEditFragmentListener listener; 

private long rowID; // database row ID of the movie
private Bundle moiveInfoBundle; // arguments for editing a movie

// EditTexts for Movieinformation
private EditText titleEditText;
private EditText yearEditText;
private EditText directorEditText;
private EditText ratingEditText;
private EditText lengthEditText;
private EditText typeEditText;
private EditText soundeffectsEditText;


// set AddEditFragmentListener when Fragment attached   
@Override
public void onAttach(Activity activity)
{
   super.onAttach(activity);
   listener = (AddEditFragmentListener) activity; 
}

// remove AddEditFragmentListener when Fragment detached
@Override
public void onDetach()
{
   super.onDetach();
   listener = null; 
}

// called when Fragment's view needs to be created
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
   Bundle savedInstanceState)
{
   super.onCreateView(inflater, container, savedInstanceState);    
   setRetainInstance(true); // save fragment across config changes
   setHasOptionsMenu(true); // fragment has menu items to display
   
  
   // inflate GUI and get references to EditTexts
   View view = 
      inflater.inflate(R.layout.fragment_add_edit, container, false);
   titleEditText = (EditText) view.findViewById(R.id.titleEditText);
   yearEditText = (EditText) view.findViewById(R.id.yearEditText);
   directorEditText = (EditText) view.findViewById(R.id.directoEditText);
   ratingEditText = (EditText) view.findViewById(R.id.ratingEditText);
   lengthEditText = (EditText) view.findViewById(R.id.lengthEditText);
   typeEditText = (EditText) view.findViewById(R.id.typeEditText);
   soundeffectsEditText = (EditText) view.findViewById(R.id.soundeffectsEditText);

   moiveInfoBundle = getArguments(); // null if creating new movie

   if (moiveInfoBundle != null)
   {
 	  
       
       
 
      rowID = moiveInfoBundle.getLong(MainActivity.ROW_ID);
      titleEditText.setText(moiveInfoBundle.getString("title"));  
      yearEditText.setText(moiveInfoBundle.getString("year"));  
      directorEditText.setText(moiveInfoBundle.getString("director"));  
      ratingEditText.setText(moiveInfoBundle.getString("rating"));  
      lengthEditText.setText(moiveInfoBundle.getString("length"));  
      typeEditText.setText(moiveInfoBundle.getString("type"));  
      soundeffectsEditText.setText(moiveInfoBundle.getString("soundeffects"));  
   } 
   
   // set Save MovieButton's event listener 
   Button saveMovieButton = 
      (Button) view.findViewById(R.id.saveMovieButton);
   saveMovieButton.setOnClickListener(saveContactButtonClicked);
   return view;
}

// responds to event generated when user saves a movie
OnClickListener saveContactButtonClicked = new OnClickListener() 
{
   @Override
   public void onClick(View v) 
   {
      if (titleEditText.getText().toString().trim().length() != 0)
      {
         // AsyncTask to save movie, then notify listener 
         AsyncTask<Object, Object, Object> saveMovieTask = 
            new AsyncTask<Object, Object, Object>() 
            {
               @Override
               protected Object doInBackground(Object... params) 
               {
                  saveMovie(); // save Movie to the database
                  return null;
               } 
   
               @Override
               protected void onPostExecute(Object result) 
               {
                  // hide soft keyboard
                  InputMethodManager imm = (InputMethodManager) 
                     getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                  imm.hideSoftInputFromWindow(
                     getView().getWindowToken(), 0);

                  listener.onAddEditCompleted(rowID);
               } 
            }; // end AsyncTask
            
         // save the Movieto the database using a separate thread
            saveMovieTask.execute((Object[]) null); 
      } 
      else // required Moviename is blank, so display error dialog
      {
         DialogFragment errorSaving = 
            new DialogFragment()
            {
               @Override
               public Dialog onCreateDialog(Bundle savedInstanceState)
               {
                  AlertDialog.Builder builder = 
                     new AlertDialog.Builder(getActivity());
                  builder.setMessage(R.string.error_message);
                  builder.setPositiveButton(R.string.ok, null);                     
                  return builder.create();
               }               
            };
         
         errorSaving.show(getFragmentManager(), "error saving movie");
      } 
   } // end method onClick
}; // end OnClickListener saveMovieButtonClicked

// saves Movieinformation to the database
private void saveMovie() 
{
   // get DatabaseConnector to interact with the SQLite database
   DatabaseConnector databaseConnector = 
      new DatabaseConnector(getActivity());

   if (moiveInfoBundle == null)
   {
 	   
 	   
      // insert the Movieinformation into the database
      rowID = databaseConnector.insertMoive(
     		 titleEditText.getText().toString(),
     		 yearEditText.getText().toString(), 
     		 directorEditText.getText().toString(), 
     		 ratingEditText.getText().toString(),
     		 lengthEditText.getText().toString(), 
     		 typeEditText.getText().toString(), 
     		 soundeffectsEditText.getText().toString());
   } 
   else
   {
      databaseConnector.updateMoive(rowID,
     		 titleEditText.getText().toString(),
     		 yearEditText.getText().toString(), 
     		 directorEditText.getText().toString(), 
     		 ratingEditText.getText().toString(),
     		 lengthEditText.getText().toString(), 
     		 typeEditText.getText().toString(), 
     		 soundeffectsEditText.getText().toString());
   }
} // end method savemovie
} // end class AddEditFragment



