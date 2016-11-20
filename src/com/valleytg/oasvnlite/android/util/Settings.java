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

package com.valleytg.oasvnlite.android.util;

import android.database.Cursor;

import com.valleytg.oasvnlite.android.application.OASVNApplication;
import com.valleytg.oasvnlite.android.model.OASVNModelLocalDB;

public class Settings extends OASVNModelLocalDB {
	private String rootFolder = "";
	
	public Settings() {
		super("setting");
		
		//Settings id will always be 1
		this.setLocalDBId(1);

	}
	
	private static class SettingsHolder { 
        public static final Settings instance = new Settings();
	}
	
	public static Settings getInstance() {
		return SettingsHolder.instance;
	}
	
	@Override
	public void saveToLocalDB(OASVNApplication app) {
		values.put("root_folder", this.getRootFolder());
		
		super.saveToLocalDB(app);
	}

	@Override
	public void setData(Cursor results) {
		try {
			this.setRootFolder(results.getString(results.getColumnIndex("root_folder")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.setData(results);
	}
	
	public Settings(String rootFolder) {
		this.setRootFolder(rootFolder);
	}

	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}

	public String getRootFolder() {
		return rootFolder;
	}

	
}
