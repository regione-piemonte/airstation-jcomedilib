// ----------------------------------------------------------------------------
// Copyright Regione Piemonte - 2021
// SPDX-License-Identifier: LGPL-2.1
// ----------------------------------------------------------------------------
// Original Author of file: Pierfrancesco Vallosio
// Purpose of file: Test application for the Java JNI interface for Comedi
//   library (Linux Control and Measurement Device Interface)
// Change log:
//   2003-12-22: initial version
// ----------------------------------------------------------------------------
// $Id: Input.java,v 1.3 2003-12-22 16:02:45 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;


public interface Input
{
    public boolean read();
}
