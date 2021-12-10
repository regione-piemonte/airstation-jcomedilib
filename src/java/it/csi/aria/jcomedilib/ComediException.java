// ----------------------------------------------------------------------------
// Copyright Regione Piemonte - 2021
// SPDX-License-Identifier: LGPL-2.1
// ----------------------------------------------------------------------------
// Original Author of file: Pierfrancesco Vallosio
// Purpose of file: Java JNI interface for Comedi library (Linux Control and
//   Measurement Device Interface https://www.comedi.org/)
// Change log:
//   2003-10-31: initial version
// ----------------------------------------------------------------------------
// $Id: ComediException.java,v 1.5 2008-04-18 16:40:17 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.jcomedilib;


public class ComediException extends Exception {
    
	private static final long serialVersionUID = 4017419233158571237L;

	public ComediException() {
        this(Comedi.strerror(Comedi.errno()));
    }

    public ComediException(String message) {
        super(message);
    }
    
    public ComediException(String message, Throwable cause) {
    	super(message, cause);
    }
}
