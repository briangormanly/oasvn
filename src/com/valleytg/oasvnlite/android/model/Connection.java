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

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;

import android.content.ContentValues;
import android.database.Cursor;

import com.valleytg.oasvnlite.android.application.OASVNApplication;
import com.valleytg.oasvnlite.android.util.DateUtil;

public class Connection extends OASVNModelLocalDB {
	public enum PROTOCOL_TYPE {HTTP, HTTPS, SVN};
	
	private String name = "";
	private String textURL = "";
	private SVNURL repositoryURL;
	private PROTOCOL_TYPE type;
	private BasicAuthenticationManager authManager;
	private String username ="";
	private String password = "";
	private String folder = "";
	private Integer head = 0;
	
	/**
	 * Default Constructor, connection is not ready to be used until url, username and password are provided
	 * and the AuthManager is initialized.
	 */
	public Connection() {
		// call the super, setting the table name
		super("connection");
		
	}
	
	/**
	 * Creates Connection and prepares it with the provided information
	 * @param url
	 * @param type
	 * @param username
	 * @param password
	 * @param folder
	 */
	public Connection(String name, String url, PROTOCOL_TYPE type, String username, String password, String folder) {
		this.setName(name);
		this.setUrl(url);
		this.setUsername(username);
		this.setPassword(password);
		this.setFolder(folder);
		this.initializeAuthManager();
	}
	
	/**
	 * Save the instance data to the local database on the device
	 * @param app - reference to the singleton application
	 */
	@Override
	public void saveToLocalDB(OASVNApplication app) {
		values.put("name", this.getName());
		values.put("url", this.getTextURL());
		values.put("protocol", this.type.toString());
		values.put("username", this.getUsername());
		values.put("password", this.getPassword());
		values.put("folder", this.getFolder());
		values.put("head", this.getHead());
		
		super.saveToLocalDB(app);
	}
	
	@Override
	public void setData(Cursor results) {
		try {
			this.setName(results.getString(results.getColumnIndex("name")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.setUsername(results.getString(results.getColumnIndex("username")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.setPassword(results.getString(results.getColumnIndex("password")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.setUrl(results.getString(results.getColumnIndex("url")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(results.getString(results.getColumnIndex("protocol")).equals(PROTOCOL_TYPE.HTTP.toString())) {
				this.type = PROTOCOL_TYPE.HTTP;
			}
			else if (results.getString(results.getColumnIndex("protocol")).equals(PROTOCOL_TYPE.HTTPS.toString())) {
				this.type = PROTOCOL_TYPE.HTTPS;
			}
			else if (results.getString(results.getColumnIndex("protocol")).equals(PROTOCOL_TYPE.SVN.toString())) {
				this.type = PROTOCOL_TYPE.SVN;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.setFolder(results.getString(results.getColumnIndex("folder")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.setHead(results.getInt(results.getColumnIndex("head")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.setData(results);
	}
	
	/**
	 * Setup the BasicAuthManager with the supplied username and password
	 */
	public void initializeAuthManager(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
		this.initializeAuthManager();
	}
	
	/**
	 * Setup the BasicAuthenticationManager with the user and password
	 * @return 
	 */
	public void initializeAuthManager() {
		try {
		// check the user name and password exist
			if(this.username.length() > 0 && this.password.length() > 0) {
				this.authManager = new BasicAuthenticationManager(this.username, this.password);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void dateUpdated() {
		this.setDateModified(DateUtil.getGMTNow());
	}

	public String getTextURL() {
		return textURL;
	}

	public void setUrl(String url) {
		this.textURL = url;
		
		// set the type
		this.setType(url);
		
		this.setRepositoryUTL();
		
		dateUpdated();
	}
	
	public SVNURL getRepositoryURL() {
		return this.repositoryURL;
	}
	
	public void setRepositoryUTL() {
		try {
			this.repositoryURL = SVNURL.parseURIEncoded(this.getTextURL());
		} catch (SVNException e) {
			e.printStackTrace();
		}
	}

	public BasicAuthenticationManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(BasicAuthenticationManager authManager) {
		this.authManager = authManager;
		dateUpdated();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		dateUpdated();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		dateUpdated();
	}

	public void setFolder(String folder) {
		this.folder = folder;
		dateUpdated();
	}

	public String getFolder() {
		return folder;
	}
	
	public PROTOCOL_TYPE getType() {
		return this.type;
	}
	
	public void setType(String url) {
		if(url.substring(0, 4).toLowerCase().equals("http")) {
			this.type = Connection.PROTOCOL_TYPE.HTTP;
		}
		else if(url.substring(0, 5).toLowerCase().equals("https")) {
			this.type = Connection.PROTOCOL_TYPE.HTTPS;
		}
		else if(url.substring(0, 3).toLowerCase().equals("svn")) {
			this.type = Connection.PROTOCOL_TYPE.SVN;
		}
		dateUpdated();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setHead(Integer head) {
		this.head = head;
	}

	public Integer getHead() {
		return head;
	}
}
