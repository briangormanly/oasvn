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

package com.valleytg.oasvnlite.android.database;

/**
 * @author brian.gormanly
 * <p>Copyright 2011 Valley Technologies Group, Brian Gormanly and 
 * Commercial Site Services all rights reserved.</p>
 * 
 * <p>This is software is not to be distributed, copied or used in any way.</p>
 * 
 * <p>VALLEY TECHNOLOGIES GROUP AND COMMERCIAL SITE SERVICES WILL BE NOT
 * LIABLE TO NMR FOR ANY SPECIAL, INDIRECT, INCIDENTAL, CONSEQUENTIAL OR
 * STATUTORY DAMAGES RELATED TO ANY CAUSE OF ACTION ARISING OUT OF THIS
 * AGREEMENT, EVEN IF NRI IS INFORMED OF THE POSSIBILITY THEREOF IN ADVANCE.</p>
 * 
 * <p>LICENSEE agrees not to create, or attempt to create, or permit or help 
 * others to create, the source code from the SOFTWARE furnished pursuant 
 * to this Agreement.</p>
 * 
 * <p>The SOFTWARE and documentation are provided with RESTRICTED RIGHTS. Use, 
 * duplication, or disclosure by the Government is subject to restrictions 
 * as set forth in the Rights in Technical Data and Computer SOFTWARE 
 * Regulations. Contractor/manufacturer is Valley Technologies Group, 
 * 110 Oak Ridge Rd, Hopewell Junction, NY, 12533 (646) 801-6004. Owner 
 * is Commercial Site Services.</p>
 * 
 */

import com.valleytg.oasvnlite.android.R;
import com.valleytg.oasvnlite.android.application.OASVNApplication;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * <p>Database control layer</p>
 * 
 * <p>Sets up and maintains local database</p>
 * 
 * @author brian.gormanly
 * @since version 1.0
 */
public class DatabaseHelper extends SQLiteOpenHelper {
Context mContext;
	
	public static final String DB_NAME = "OASVNlite";
	public static final int VERSION = 1;
	
	public String pNumber;
	
	public DatabaseHelper(Context context, OASVNApplication app) {
		super(context, DB_NAME, null, VERSION);
		mContext = context;
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// upgrades will go here
		
		try {
			//sql execs go here
			//db.execSQL(sql1);
			
		}
		catch(SQLException e) {
			Log.e("Database upgrade for version 1.1.0 failed", e.toString());
		}
	}
	
	/**
	 * Create database tables from the create scripts stored in the
	 * db_create resource in database.xml
	 * @param db
	 */
	private void createTables(SQLiteDatabase db) {
		String[] sql = mContext.getString(R.string.db_create).split("\n");
		db.beginTransaction();
		try {
			// Create tables & test data
			execMultipleSQL(db, sql);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
            Log.e("Error creating tables and debug data", e.toString());
        } finally {
        	db.endTransaction();
        }
        
        /*
        
        String sql2 = "insert into phone (id, fullNumber, name, currentUser, defaultRouteId, isActive) values (1, '" + this.pNumber + "', 'Default Phone', 'Default User', 1, 1)";
		db.beginTransaction();
		try {
			// Create tables & test data
			db.execSQL(sql2);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
            Log.e("Error creating tables and debug data", e.toString());
        } finally {
        	db.endTransaction();
        }
        
        */
	}

	/**
     * Execute all of the SQL statements in the String[] array
     * @param db The database on which to execute the statements
     * @param sql An array of SQL statements to execute
     */
    private void execMultipleSQL(SQLiteDatabase db, String[] sql){
    	for( String s : sql )
    		if (s.trim().length()>0)
    			db.execSQL(s);
    }

}