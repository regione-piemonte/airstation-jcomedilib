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
// $Id: ComediRange.java,v 1.7 2005-03-15 12:00:20 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.jcomedilib;

import java.text.*;

public class ComediRange {

    private double min;
    private double max;
    private int unit;
    private NumberFormat nf;

    public ComediRange(double min, double max, int unit) {
        this.min = min;
        this.max = max;
        this.unit = unit;
        this.nf = NumberFormat.getInstance();
        // this.nf.setMaximumFractionDigits(6);
    }

    public double getMin() {
        return (min);
    }

    public double getMax() {
        return (max);
    }

    public int getUnit() {
        return (unit);
    }

    @Override
    public String toString() {
        return ("[" + nf.format(min) + ":" + nf.format(max) + "]");
    }
}
