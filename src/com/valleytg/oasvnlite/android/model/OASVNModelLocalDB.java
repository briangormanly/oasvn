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

package com.valleytg.oasvnlite.android.model;

import java.sql.Statement;

import com.valleytg.oasvnlite.android.application.OASVNApplication;
import com.valleytg.oasvnlite.android.util.DateUtil;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class OASVNModelLocalDB extends OASVNModel {

	/**
	 * Stores the local Database Id, if null it indicates that this is a new record.  
	 * A class that stores to the local db should implement LocalDatabase
	 */
	private Integer localDBId;
	
	/**
	 * Table name: Stores the name of the table if the object implements the LocalDatabase
	 */
	private String tableName;
	
	
	protected ContentValues values;
	
	/**
	 * Default Constructor
	 * Generally it is better to use supply the table name when
	 * creating the object, Best practice is that the extending
	 * class passes the tablename in the super call.
	 */
	public OASVNModelLocalDB() {
		super();
		
		// set the initial database id to -1
		this.setLocalDBId(-1);
		
		// initilize the values
		values = new ContentValues();
		
		// make active by default
		this.setActive(true);
	}
	
	/**
	 * Create model object providing the name of the table
	 * @param tableName
	 */
	public OASVNModelLocalDB(String tableName) {
		super();
		
		// set the initial database id to -1
		this.setLocalDBId(-1);
		
		// set the table name
		this.setTableName(tableName);
		
		// Initialize the values
		values = new ContentValues();
		
		// make active by default
		this.setActive(true);
	}
	
	//methods
	
	/**
	 * <p>Required method for the extending class.</p>
	 * <p>Sets up the ContentValues object with the data that will be inserted or
	 * updated into the database using the saveToLocalDB method.</p>
	 */
	public void saveToLocalDB(OASVNApplication app) {
		if(this.getActive()) {
			values.put("active", "1");
		}
		else {
			values.put("active", "0");
		}
		
		values.put("date_modified", this.getDateModified().toString());
		if(this.getDateCreated() != null) {
			values.put("date_created", this.getDateCreated().toString());
		}
		
		// call the database insert
		this.databaseInsertOrUpdate(values, app);
	}
	
	/**
	 * <p>Looks to see if there is an existing row for this model object and then does either
	 * a insert or update appropriately.</p>
	 * 
	 * <p>Requires that database and model abide by certain rules:<br />
	 * <ul>
	 * 	<li>Database table has a auto-incrementing id called 'id'</li>
	 * 	<li>Data object implements LocalDatabase, which requires localDBId is used. Where clause
	 * will compare this localDBId to the id field in the table.</li>
	 * </ul>
	 * </p>
	 */
	private void databaseInsertOrUpdate(ContentValues values, OASVNApplication app) {
		// do a check select to see if the record already exists in the local db
		Statement stmt;
		int rowCount = 0;
		try {
			String sql = "select * from " + tableName + " where id='" + this.getLocalDBId() + "';";
			Cursor dbCursor = app.database.rawQuery(sql, null);
			dbCursor.moveToFirst();
			
			if(!dbCursor.isAfterLast()) {
				//row exists
				rowCount++;
			}
			dbCursor.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// if there is an existing row, update, else insert
		if(rowCount > 0) {
			// update
			String where = String.format("%s = %d", "id", this.getLocalDBId());
			app.database.update(tableName, values, where, null);
		}
		else {
			// insert
			app.database.insert(tableName, null, values);
		}
	}
	
	
	/**
	 * <p>Call this method to retrieve the object data from the database.  Calling this
	 * will pass a Cursor object contianing the query resluts to the setData method that
	 * is required to be implemented by an extending class.  This is where the object
	 * specific data mapping is handled.</p>
	 * @param app - Android Application
	 
	public void populateFromDatabase(OASVNApplication app) {
		//First the data needs to be retrieved from the database
		String sql = "select * from " + this.tableName + " where id = " + this.localDBId + ";";
		Cursor dbCursor = app.database.rawQuery(sql, null);
		
		// if anything was returned, send the data up to the local impl to assign data
		if(!dbCursor.isAfterLast()) {
			// call the local implementation for the data assignment
			
		}
		else {
			// no data was retrieved
			
		}
	}
	*/
	
	/**
	 * <p>Required method for an extending class.</p>
	 * <p>This method will be called from the populateFromDatabase method and will be passed
	 * a Cursor object containing the resultset of the database query produced here.</p>
	 */
	protected void setData(Cursor results) {
		
		try {
			if(results.getString(results.getColumnIndex("id")) != null) {
				this.setLocalDBId(results.getInt(results.getColumnIndex("id")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(results.getString(results.getColumnIndex("date_created")) != null) {
				this.setDateCreated(DateUtil.toDate(results.getString(results.getColumnIndex("date_created"))));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(results.getString(results.getColumnIndex("date_modified")) != null) {
				this.setDateCreated(DateUtil.toDate(results.getString(results.getColumnIndex("date_modified"))));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(results.getInt(results.getColumnIndex("active")) > 0) {
				this.setActive(true);
			}
			else {
				this.setActive(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	// gettors and settors
	
	public Integer getLocalDBId() {
		return localDBId;
	}

	public void setLocalDBId(Integer localDBId) {
		this.localDBId = localDBId;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}
}
