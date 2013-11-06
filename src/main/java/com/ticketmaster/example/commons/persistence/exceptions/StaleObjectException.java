/*
This file is part of the com.xpierre Java framework
Copyright (C) 1999-2001 Pierre Adriaans

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact: xpierre00@yahoo.com
Releases and information available at http://www.xpierre.com
*/
package com.ticketmaster.example.commons.persistence.exceptions;

/**
 * It carries information about a particular entity instance that was the source of the failure.
 *
 */
public class StaleObjectException extends RuntimeException {
	
	/**
	 * Represents source of failure
	 */
	private final Object object;
	/**
	 * Represents original exception
	 */
	private final Throwable originalException;
	
	/**
	 * Instantiated with object and exception
	 * @param object 
	 *          represents source of failure
	 * @param originalException
	 *          represents original exception
	 */
	public StaleObjectException(Object object,Throwable originalException) {
		super();
		this.object = object;
		this.originalException = originalException;
	}
	
	/**
	 * @return object which represents source of failure
	 */
	public Object getObject() {
		return object;
	}
	
	/**
	 * @return Throwable reprsents origianl exception
	 */
	public Throwable getOriginalException() {
		return originalException;
	}

	/**
	 * @return String represents message 
	 */
	public String getMessage() {
		return "Object of class " + object.getClass().getCanonicalName() + " has been modified by another thread or process.";
	}
}
