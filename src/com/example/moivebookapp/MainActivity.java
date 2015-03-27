// MainActivity.java
// Hosts Address Book app's fragments
package com.example.moivebookapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity 
   implements MoiveListFragment.ContactListFragmentListener,
      DetailsFragment.DetailsFragmentListener, 
      AddEditFragment.AddEditFragmentListener
{
   // keys for storing row ID in Bundle passed to a fragment
   public static final String ROW_ID = "row_id"; 
   
   MoiveListFragment moiveListFragment; // displays contact list
   
   // display MoiveListFragment when MainActivity first loads
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // return if Activity is being restored, no need to recreate GUI
      if (savedInstanceState != null) 
         return;

      // check whether layout contains fragmentContainer (phone layout);
      // MoiveListFragment is always displayed
      if (findViewById(R.id.fragmentContainer) != null) 
      {
         // create MoiveListFragment
         moiveListFragment = new MoiveListFragment();
         
         // add the fragment to the FrameLayout
         FragmentTransaction transaction = 
            getFragmentManager().beginTransaction();
         transaction.add(R.id.fragmentContainer, moiveListFragment);
         transaction.commit(); // causes MoiveListFragment to display
      }
   }
   
   // called when MainActivity resumes
   @Override
   protected void onResume()
   {
      super.onResume();
      
      // if moiveListFragment is null, activity running on tablet, 
      // so get reference from FragmentManager
      if (moiveListFragment == null)
      {
         moiveListFragment = 
            (MoiveListFragment) getFragmentManager().findFragmentById(R.id.contactListFragment);      
      }
   }
   
   // display DetailsFragment for selected contact
   @Override
   public void onMovieSelected(long rowID)
   {
      if (findViewById(R.id.fragmentContainer) != null) // phone
         displayMovie(rowID, R.id.fragmentContainer);
      else // tablet
      {
         getFragmentManager().popBackStack(); // removes top of back stack
         displayMovie(rowID, R.id.rightPaneContainer);
      }
   }

   // display a moive
   private void displayMovie(long rowID, int viewID)
   {
      DetailsFragment detailsFragment = new DetailsFragment();
      
      // specify rowID as an argument to the DetailsFragment
      Bundle arguments = new Bundle();
      arguments.putLong(ROW_ID, rowID);
      detailsFragment.setArguments(arguments);
      
      // use a FragmentTransaction to display the DetailsFragment
      FragmentTransaction transaction = 
         getFragmentManager().beginTransaction();
      transaction.replace(viewID, detailsFragment);
      transaction.addToBackStack(null);
      transaction.commit(); // causes DetailsFragment to display
   }
   
   // display the AddEditFragment to add a new contact
   @Override
   public void onAddMovie()
   {
      if (findViewById(R.id.fragmentContainer) != null)
         displayAddEditFragment(R.id.fragmentContainer, null); 
      else
         displayAddEditFragment(R.id.rightPaneContainer, null);
   }
   
   // display fragment for adding a new or editing an existing moive
   private void displayAddEditFragment(int viewID, Bundle arguments)
   {
      AddEditFragment addEditFragment = new AddEditFragment();
      
      if (arguments != null) // editing existing moive
         addEditFragment.setArguments(arguments);
      
      // use a FragmentTransaction to display the AddEditFragment
      FragmentTransaction transaction = 
         getFragmentManager().beginTransaction();
      transaction.replace(viewID, addEditFragment);
      transaction.addToBackStack(null);
      transaction.commit(); // causes AddEditFragment to display
   }
   
   // return to moive list when displayed moive deleted
   @Override
   public void onMovieDeleted()
   {
      getFragmentManager().popBackStack(); // removes top of back stack
      
      if (findViewById(R.id.fragmentContainer) == null) // tablet
         moiveListFragment.updateContactList();
   }

   // display the AddEditFragment to edit an existing moive
   @Override
   public void onEditMovie(Bundle arguments)
   {
      if (findViewById(R.id.fragmentContainer) != null) // phone
         displayAddEditFragment(R.id.fragmentContainer, arguments); 
      else // tablet
         displayAddEditFragment(R.id.rightPaneContainer, arguments);
   }

   // update GUI after new moive or updated moive saved
   @Override
   public void onAddEditCompleted(long rowID)
   {
      getFragmentManager().popBackStack(); // removes top of back stack

      if (findViewById(R.id.fragmentContainer) == null) // tablet
      {
         getFragmentManager().popBackStack(); // removes top of back stack
         moiveListFragment.updateContactList(); // refresh contacts

         // on tablet, display contact that was just added or edited
         displayMovie(rowID, R.id.rightPaneContainer); 
      }
   }
 
}

