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
// $Id: AnalogicOutputView.java,v 1.2 2003-12-22 16:02:45 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;


public interface AnalogicOutputView
{
    public void setName(String name);
    public void setRange(double minValue, double maxValue, int precision);
    public void setMeasureUnit(String mUnit);
    public void setValue(double value, int count);
    public void setOutput(AnalogicOutput anlOut);
}
