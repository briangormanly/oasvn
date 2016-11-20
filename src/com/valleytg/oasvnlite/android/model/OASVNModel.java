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

import java.util.Date;

import com.valleytg.oasvnlite.android.util.DateUtil;

public abstract class OASVNModel {
	
	/**
	 * Last modified date.  Updated with any change to record
	 */
	private Date dateModified;
	
	/**
	 * Date record was initially created.
	 */
	private Date dateCreated;
	
	/**
	 * Active flag
	 */
	private Boolean active; 
	
	/**
	 * Constructor
	 */
	public OASVNModel() {
		// set the create time 
		this.setDateCreated(DateUtil.getGMTNow());
		this.setDateModified(DateUtil.getGMTNow());

	}

	// gettors and settors

	public Date getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getActive() {
		return active;
	}

	
}
