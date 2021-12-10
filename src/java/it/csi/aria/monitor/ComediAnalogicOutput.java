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
// $Id: ComediAnalogicOutput.java,v 1.4 2005-03-16 16:06:26 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import it.csi.aria.jcomedilib.*;

public class ComediAnalogicOutput
    implements AnalogicOutput, Input, RangeSelectable
{
    private ComediChannel channel;
    private int aref;
    private int unit;
    private LSamplT maxdata;
    private int range;
    private ComediRange cRange;
    private AnalogicOutputView aoView = null;

    public ComediAnalogicOutput(ComediChannel channel,
                                int aref, int unit, double min, double max)
        throws ComediException
    {
        this.channel = channel;
        this.aref = aref;
        this.unit = unit;
        
        maxdata = channel.getMaxdata();
        setRange(min, max);
    }

    public void setRange(double min, double max)
    {
        try {
            range = channel.findRange(unit, min, max);
            cRange = channel.getRangeInfo(range);
            setViewMeasureUnit();
        }
        catch (ComediException ce) {
            throw new RuntimeException(ce);
        } 
    }

    public void setView(AnalogicOutputView aoView)
    {
        this.aoView = aoView;
        setViewMeasureUnit();
    }

    private void setViewMeasureUnit()
    {
        if (aoView != null) {
            int precision = Integer.toString(maxdata.getValue()).length();
            aoView.setRange(cRange.getMin(), cRange.getMax(), precision);
            switch (cRange.getUnit()) {
            case Comedi.UNIT_volt:
                aoView.setMeasureUnit("V");
                break;
            case Comedi.UNIT_mA:
                aoView.setMeasureUnit("mA");
                break;
            default:
                aoView.setMeasureUnit("");
                break;
            }
        }
    }

    public boolean read()
    {
        try {
            LSamplT data = new LSamplT(0);
            channel.setActiveRange(range);
            channel.setAnalogRef(aref);
            channel.dataRead(data);
            double physValue = Comedi.to_phys(data, cRange, maxdata);
            if (aoView != null)
                aoView.setValue(physValue, data.getValue());
            //System.out.println(physValue + ", " + data.value);
            return(true);
        }
        catch (ComediException ce) {
            ce.printStackTrace();
            return(false);
        }
    }

    public boolean write(double value)
    {
        try {
            LSamplT data = Comedi.from_phys(value, cRange, maxdata);
            //System.out.println("DBG: AOWrite = " + value + ", "+ data.value);
            channel.setActiveRange(range);
            channel.setAnalogRef(aref);
            channel.dataWrite(data);
            return(true);
        }
        catch (ComediException ce) {
            ce.printStackTrace();
            return(false);
        }
    }
}
