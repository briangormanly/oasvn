/**
 * @author brian.gormanly
 * OASVN (Open Android SVN)
 * Copyright (C) 2012 Brian Gormanly
 * Valley Technologies Group
 * http://www.valleytg.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version. 
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */

package com.valleytg.oasvnlite.android.ui.activity;

import java.util.Collections;
import java.util.Comparator;

import com.valleytg.oasvnlite.android.R;
import com.valleytg.oasvnlite.android.application.OASVNApplication;
import com.valleytg.oasvnlite.android.model.Connection;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainMenu extends ListActivity {
	
	Button btnAddRepo;
	Button btnBack;
	
	OASVNApplication app;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // get the application
        this.app = (OASVNApplication)getApplication();
        
        btnAddRepo = (Button) findViewById(R.id.main_add_repository);
        btnBack = (Button) findViewById(R.id.main_back);
        
        // set the list adapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.connection_item));
        
        // populate the list
        populateList();
        
        // button listeners     
        this.btnBack.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				MainMenu.this.finish();
			}
		});
        
        this.btnAddRepo.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// open the add repository activity
				Intent intent = new Intent(MainMenu.this, AddRepository.class);
				startActivity(intent);
			}
		});
        
    }
    
    
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		// populate the list
        populateList();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		try {
			final Connection thisConnection = app.getAllConnections().get(position);
			
			// set the current connection
			app.setCurrentConnection(thisConnection);
			
			// go to the connection screen
			Intent intent = new Intent(MainMenu.this, ConnectionDetails.class);
			startActivity(intent);
		} 
		catch (Exception e) {
			Toast.makeText(this, getString(R.string.create_connection), 3500).show();
			e.printStackTrace();
		}
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// populate the list
        populateList();
	}


	private void populateList() {
    	// intialize array of choices
        String[] connections;
        connections = null;
 
        // try to retrieve the local staff / users
        try {
        	
        	// do the retrieval
        	app.retrieveAllConnections();
        	
        	// check to see if there are any
        	if(app.getAllConnections().size() > 0) {
        		// sort the fist by staff first name
        		Collections.sort(app.getAllConnections(), new Comparator<Connection>(){

					public int compare(Connection lhs, Connection rhs) {
						Connection p1 = (Connection) lhs;
    	                Connection p2 = (Connection) rhs;
    	               return p1.getName().compareTo(p2.getName());
					}
    	 
    	        });
        		
        		// connections ready to go
        		connections = new String[app.getAllConnections().size()];
        		for(Connection connection : app.getAllConnections()) {
            		connections[app.getAllConnections().indexOf(connection)] = connection.getName() + "\n" + connection.getTextURL();
            	}
        		
        		
        	}
        	else {
        		// no users in the local db
        		connections = new String[1];
        		connections[0]= getString(R.string.no_connections);
        		Toast toast=Toast.makeText(this, getString(R.string.no_connections), 1500);
        		toast.show();
        	}
        	
        	
        }
        catch(Exception e) {
        	// no user/staff data in application yet. call for a refresh
        	connections = new String[1];
        	connections[0] = getString(R.string.no_connections);
        }
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.connection_item, connections));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

    }
    
}
