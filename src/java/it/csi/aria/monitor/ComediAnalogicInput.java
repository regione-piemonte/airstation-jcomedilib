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
// $Id: ComediAnalogicInput.java,v 1.5 2005-03-16 16:06:26 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import it.csi.aria.jcomedilib.*;

public class ComediAnalogicInput implements Input, RangeSelectable
{
    public static final int AI_SET_TIME_NS = 15000;

    private ComediChannel channel;
    private int aref;
    private int unit;
    private LSamplT maxdata;
    private int range;
    private ComediRange cRange;
    private AnalogicInputView aiView = null;

    public ComediAnalogicInput(ComediChannel channel,
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

    public void setView(AnalogicInputView aiView)
    {
        this.aiView = aiView;
        setViewMeasureUnit();
    }

    private void setViewMeasureUnit()
    {
        if (aiView != null) {
            int precision = Integer.toString(maxdata.getValue()).length();
            aiView.setRange(cRange.getMin(), cRange.getMax(), precision);
            switch (cRange.getUnit()) {
            case Comedi.UNIT_volt:
                aiView.setMeasureUnit("V");
                break;
            case Comedi.UNIT_mA:
                aiView.setMeasureUnit("mA");
                break;
            default:
                aiView.setMeasureUnit("");
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
            channel.dataReadDelayed(data, AI_SET_TIME_NS);
            double physValue = Comedi.to_phys(data, cRange, maxdata);
            if (aiView != null)
                aiView.setValue(physValue, data.getValue());
            return(true);
        }
        catch (ComediException ce) {
            ce.printStackTrace();
            return(false);
        }
    }
}
