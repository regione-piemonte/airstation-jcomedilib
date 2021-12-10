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
// $Id: Version.java,v 1.3 2005-03-15 12:00:20 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.jcomedilib;

public class Version {

    private int major;
    private int minor;
    private int revision;

    Version(int code) {
        major = (code & 0x00FF0000) >>> 16;
        minor = (code & 0x0000FF00) >>> 8;
        revision = code & 0x000000FF;
    }

    @Override
    public String toString() {
        return (major + "." + minor + "." + revision);
    }

    public int getMajor() {
        return (major);
    }

    public int getMinor() {
        return (minor);
    }

    public int getRevision() {
        return (revision);
    }
}