// ----------------------------------------------------------------------------
// Copyright Regione Piemonte - 2021
// SPDX-License-Identifier: LGPL-2.1
// ----------------------------------------------------------------------------
// Original Author of file: Pierfrancesco Vallosio
// Purpose of file: Java JNI interface for Comedi library (Linux Control and
//   Measurement Device Interface https://www.comedi.org/)
// Change log:
//   2003-12-22: initial version
// ----------------------------------------------------------------------------
// $Id: NoSuchDeviceComediException.java,v 1.4 2008-04-18 16:40:17 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.jcomedilib;

public class NoSuchDeviceComediException extends ComediException {
    
	private static final long serialVersionUID = 2932036228335260637L;

	public NoSuchDeviceComediException() {
        this(Comedi.strerror(Comedi.errno()));
    }

    public NoSuchDeviceComediException(String message) {
        super(message);
    }
}
