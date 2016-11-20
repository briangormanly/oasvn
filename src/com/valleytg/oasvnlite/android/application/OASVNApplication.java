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

package com.valleytg.oasvnlite.android.application;

import java.io.File;
import java.util.ArrayList;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import com.valleytg.oasvnlite.android.database.DatabaseHelper;
import com.valleytg.oasvnlite.android.model.Connection;
import com.valleytg.oasvnlite.android.util.Settings;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class OASVNApplication extends Application {

	/**
	 * database
	 */
	public SQLiteDatabase database;
	
	/**
	 * Full path to main folder
	 */
	private String fullPathToMain = "";
	
	/**
	 * Maintains application perspective on whether or not there is external 
	 * storage available.
	 */
	private boolean mExternalStorageAvailable = false;
	
	/**
	 * Maintains applications perspective on whether or not the external storage
	 * is writable.
	 */
    private boolean mExternalStorageWriteable = false;
    
    /**
     * Path information
     */
    private File rootPath = null;
    
    /**
     * Current connection
     */
    Connection currentConnection;
    
    /**
     * All connections
     */
    ArrayList<Connection> allConnections;
    
    /**
     * BasicAithenticationManager sets up the svn authentication with the server.
     */
    BasicAuthenticationManager myAuthManager;
    
    /**
     * 
     */
    SVNClientManager cm;
    
    /**
     * 
     */
    SVNUpdateClient uc;
    
    /**
     * 
     */
    SVNCommitClient cc;
    
    /**
     * 
     */
    private SVNCommitInfo info;
    
    /**
     * Commit comments
     */
    private String commitComments = "";
    
    
    /**
     * Constructor
     */
    public OASVNApplication() {
    	
    	// initialize arraylists
    	this.allConnections = new ArrayList<Connection>();
    	
    	// Initialize the settings
		Settings.getInstance();
    	
    	// initialize the storage state
    	this.discoverStorageState();
    	
    }
    
    /**
     * OnCreate
     */
    @Override
	public void onCreate() {
		super.onCreate();
		
		// retrieve the database
		DatabaseHelper helper = new DatabaseHelper(this, this);
		database = helper.getWritableDatabase();
		
		// try to retrieve the settings data
		this.initalizeSettings();
		
		// make sure the app is initialized
		this.initAuthManager();
		
		// make sure the path is ready
		this.initializePath();
    }
    
    public void initAuthManager() {
    	try {
	    	// check to see that we have a current connection
	    	if(currentConnection != null) { 
	    		// initialize the Auth manager
	    		myAuthManager = new BasicAuthenticationManager(this.currentConnection.getUsername(), this.currentConnection.getPassword());
	    		
	    		cm = SVNClientManager.newInstance();
		        cm.setAuthenticationManager(myAuthManager); 
		        uc = cm.getUpdateClient();
		        cc = cm.getCommitClient();
	    	}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    

    
    public File assignPath() {
    	// check to see that there is a path
    	try {
	    	if(this.currentConnection != null && this.currentConnection.getFolder().length() > 0) {
	    		// get the sd card directory
				File file = new File(this.getRootPath(), this.currentConnection.getFolder());
				return file;
	    	}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    	return null;
    }
    
    public void deleteRecursive(File tree) {
    	if (tree.isDirectory())
	        for (File child : tree.listFiles())
	            this.deleteRecursive(child);

		tree.delete();
    }
    
    
    public void discoverStorageState() {
    	// get the current state of external storage
    	String state = Environment.getExternalStorageState();

	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        // We can read and write the media
	        setmExternalStorageAvailable(setmExternalStorageWriteable(true));
	    } 
	    else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        // We can only read the media
	        setmExternalStorageAvailable(true);
	        setmExternalStorageWriteable(false);
	    } 
	    else {
	        // Something else is wrong. It may be one of many other states, but all we need
	        //  to know is we can neither read nor write
	        setmExternalStorageAvailable(setmExternalStorageWriteable(false));
	    }
    }
    
    public void initializePath() {
    	try {
    		String mainFolder = "";
    		
    		// check to see if there is a default folder from the settings
    		if(Settings.getInstance().getRootFolder().length() == 0) {
    			mainFolder = "OASVNlite/";
    		}
    		else {
    			mainFolder = Settings.getInstance().getRootFolder();
    		}
    		
    		// set the full path to main
    		this.setFullPathToMain(Environment.getExternalStorageDirectory() + "/" + mainFolder);
    		
	    	File folder = new File(this.getFullPathToMain());
	    	
		    if(!folder.exists()){
		    	// folder does not yet exist, create it.
		         folder.mkdir();
		         this.setRootPath(folder);
		         Log.i("FILE", "directory is created"); 
		    }
		    else {
		    	// folder already exists
		    	this.setRootPath(folder);
		    	Log.i("FILE", "directory already exists"); 
		    }
		    
    	}
    	catch(Exception e) {
    		Log.e("FILE", "can't create folder");
    		e.printStackTrace();
    	}
    }
    

    public String fullHeadCheckout() {
    	try {
    		// initialize the auth manager
    		this.initAuthManager();
    		
    		// make sure the path is ready
    		initializePath();
    		
    		SVNURL myURL = this.currentConnection.getRepositoryURL();
    		File myFile = this.assignPath();
    		SVNRevision myRevision = SVNRevision.HEAD;
    		
    		try {
    			uc.doCheckout(myURL, myFile, myRevision, myRevision, true, true);
    		}
    		catch(SVNException e) {
    			String msg = e.getMessage();
    			return msg;
    		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		return "success";
    }
    
    public String fullCommit() {
    	// initialize the auth manager
		this.initAuthManager();
		
		// make sure the path is ready
		initializePath();
		
		SVNURL myURL = this.currentConnection.getRepositoryURL();
		File myFile = this.assignPath();
		SVNRevision myRevision = SVNRevision.HEAD;

		try {
			setInfo(cc.doCommit( new File[] {myFile} , false, this.commitComments , false , true));
			System.out.println("Revision " + getInfo().getNewRevision());
		}
		catch(SVNException e) {
			String msg = e.getMessage();
			return msg;
		}
		
		return Long.toString(getInfo().getNewRevision());
		
    }
    
    public Integer getRevisionNumber() {
    	
    	try {
    		// make sure there is a selected connection
        	if(this.getCurrentConnection() != null) {
        		// initialize the auth manager
        		this.initAuthManager();
        		
        		return (int) cm.getStatusClient().doStatus(this.assignPath(), false).getRevision().getNumber();
        	}
        	else {
        		return 0;
        	}
			
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

    }
    
    
    /**
     * Local member special methods
     */
    
    /**
     * Get all stored connections from the local database
     */
    public void retrieveAllConnections() {
    	String sql = "select * from connection where active > 0;";
		Cursor dbCursor = this.database.rawQuery(sql, null);
		dbCursor.moveToFirst();
		
		if(!dbCursor.isAfterLast()) {
			
			// clear out any user currently stored in mem
			this.allConnections.removeAll(this.allConnections);
			
			// iterate through local and populate
			while(!dbCursor.isAfterLast()) {
				Connection thisConnection = new Connection();
				thisConnection.setData(dbCursor);
				dbCursor.moveToNext();
				
				this.allConnections.add(thisConnection);
			}
		}
		dbCursor.close();
    }
    
    /**
	 * Try to retrieve the settings from the database if they exist.
	 * If they do not yet exist, create them.
	 */
	public void initalizeSettings() {
		// try to retrieve the data
		this.retrieveSettings();
		
		// see if settings existed
		if(Settings.getInstance().getRootFolder().length() == 0) {
			// there are no settings in the database create default
			Settings.getInstance().setRootFolder("OASVNlite/");
			Settings.getInstance().saveToLocalDB(this);
		}
		
	}
    
    public void retrieveSettings() {
    	String sql = "select * from setting where id = 1;";
		Cursor dbCursor = this.database.rawQuery(sql, null);
		dbCursor.moveToFirst();
		
		if(!dbCursor.isAfterLast()) {
			// iterate through local and populate
			while(!dbCursor.isAfterLast()) {
				Settings.getInstance().setData(dbCursor);
				dbCursor.moveToNext();
			}
		}
		dbCursor.close();
    }

	public void setmExternalStorageAvailable(boolean mExternalStorageAvailable) {
		this.mExternalStorageAvailable = mExternalStorageAvailable;
	}

	public boolean ismExternalStorageAvailable() {
		return mExternalStorageAvailable;
	}

	public boolean setmExternalStorageWriteable(boolean mExternalStorageWriteable) {
		this.mExternalStorageWriteable = mExternalStorageWriteable;
		return mExternalStorageWriteable;
	}

	public boolean ismExternalStorageWriteable() {
		return mExternalStorageWriteable;
	}

	public void setRootPath(File rootPath) {
		this.rootPath = rootPath;
	}

	public File getRootPath() {
		return rootPath;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	public Connection getCurrentConnection() {
		return currentConnection;
	}

	public void setCurrentConnection(Connection currentConnection) {
		this.currentConnection = currentConnection;
	}

	public BasicAuthenticationManager getMyAuthManager() {
		return myAuthManager;
	}

	public void setMyAuthManager(BasicAuthenticationManager myAuthManager) {
		this.myAuthManager = myAuthManager;
	}

	public SVNClientManager getCm() {
		return cm;
	}

	public void setCm(SVNClientManager cm) {
		this.cm = cm;
	}

	public SVNUpdateClient getUc() {
		return uc;
	}

	public void setUc(SVNUpdateClient uc) {
		this.uc = uc;
	}

	public void setAllConnections(ArrayList<Connection> allConnections) {
		this.allConnections = allConnections;
	}
	
	public ArrayList<Connection> getAllConnections() {
		return this.allConnections;
	}

	public void setInfo(SVNCommitInfo info) {
		this.info = info;
	}

	public SVNCommitInfo getInfo() {
		return info;
	}

	public void setCommitComments(String commitComments) {
		this.commitComments = commitComments;
	}

	public String getCommitComments() {
		return commitComments;
	}

	public void setFullPathToMain(String fullPathToMain) {
		this.fullPathToMain = fullPathToMain;
	}

	public String getFullPathToMain() {
		return fullPathToMain;
	}
}
