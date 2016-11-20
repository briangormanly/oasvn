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

import java.io.File;
import java.io.OutputStream;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.valleytg.oasvnlite.android.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

public class OASVNActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        UserRemoteThread remoteThread = new UserRemoteThread();
		remoteThread.execute();
    }
    
    class UserRemoteThread extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... unused) {
			try {
				Looper.myLooper();
				Looper.prepare();
			}
			catch(Exception e) {
				// Looper only needs to be created if the thread is new, if reusing the thread we end up here
			}
			
			try {
				



			    File folder = new File(Environment.getExternalStorageDirectory () + "/testsvn");
			    File folder2 = new File(Environment.getExternalStorageDirectory () + "/testsvn/.svn");
			    boolean success = false;
			    if(!folder.exists()){
			         success = folder.mkdir();
			    }
			    if (!success) {
			        Log.e("FILE", "can't create " + folder);
			    }
			    else 
			    {
			        Log.i("FILE", "directory is created"); 
			    }
				
				
				
				/*
				
				// SVNKit is available on the classpath: 
				SVNClientInterface client = SVNClientImpl.newInstance(); 

				// No SVNKit, use JavaHL 
				// SVNClientInterface client = new SVNClient(); 

				String path = Environment.getExternalStorageDirectory().toString();
				
				// checkout from HEAD 
				client.checkout("http://www.valleytg.com/svn/valleytgopen/OASVN/", path + "/test", Revision.HEAD, null, Depth.infinity, false, false); 

				*/
				
				/*
				String url="http://www.valleytg.com/svn/valleytgopen/OASVN";
				String name="guest";
				String password="guest";
				SVNRepository repository = null;
				try { 
					repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
				    ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
				    repository.setAuthenticationManager(authManager);
				    
				    
				    String path = Environment.getExternalStorageDirectory().toString();
				    android.util.Log.d("path", "The path is : " + path);
				    
				    //repository.se
				    
				    repository.testConnection();
				    
				    //android.util.Log.d("path", "Repository latest revision : " + repository.getLatestRevision());
				    //android.util.Log.d("path", "Repository latest revision : " + repository.get);
				    //ISVNEditor myEditor = repository.getLatestRevision()
				    
				    //repository.checkout(repository.getLatestRevision(), path, true, repository.);

				 } catch (SVNException e){
				    e.printStackTrace();
				 }
				*/
				
				
				// get the sd card directory
				String path = Environment.getExternalStorageDirectory().toString();
				//OutputStream fOut = null;
				File file = new File(path, "testsvn");
				
				//DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(file, true);
				BasicAuthenticationManager myAuthManager = new BasicAuthenticationManager("guest", "guest");
				
				
				SVNClientManager cm = SVNClientManager.newInstance();
		        cm.setAuthenticationManager(myAuthManager); 
		        SVNUpdateClient uc = cm.getUpdateClient();
		        

				   
				//cerate svn url
				SVNURL repositoryURL = null;
				try {
					repositoryURL = SVNURL.parseURIEncoded("http://www.valleytg.com/svn/valleytgopen/OASVN");
				} 
				catch ( SVNException e ) {
					e.printStackTrace();
				}

		       	try {
					uc.doCheckout(repositoryURL, file, SVNRevision.HEAD, SVNRevision.HEAD, true, true);
				} catch (SVNException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.e("success?", "I think it worked!");
				
				
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        	return null;
	        }
			return null;
		}
		
		protected void onPostExecute(Void unused) {
			android.util.Log.d("alarm", "Something happened!");
	    }

	}
       
}


