// ----------------------------------------------------------------------------
// Copyright Regione Piemonte - 2021
// SPDX-License-Identifier: LGPL-2.1
// ----------------------------------------------------------------------------
// Original Author of file: Pierfrancesco Vallosio
// Purpose of file: Java JNI interface for Comedi library (Linux Control and
//   Measurement Device Interface https://www.comedi.org/)
// Change log:
//   2005-03-10: initial version
// ----------------------------------------------------------------------------
// $Id: ComediChannel.java,v 1.4 2008-05-23 07:45:20 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.jcomedilib;

public class ComediChannel {
    
    private long devDesc;
    private int subdev;
    private int chan;
    private int range;
    private int aref;
    private LSamplT maxdata;
    private ComediRange[] cRanges;

    public ComediChannel(long devDesc, int subdev, int chan) throws ComediException {
        this.devDesc = devDesc;
        this.subdev = subdev;
        this.chan = chan;
        maxdata = Comedi.get_maxdata(devDesc, subdev, chan);
        if (maxdata.getValue() <= 0)
            throw new ComediException("Unable to read channel info");
        int numRanges = Comedi.get_n_ranges(devDesc, subdev, chan);
        if (numRanges <= -1)
            throw new ComediException("Unable to read channel info");
        cRanges = new ComediRange[numRanges];
        for (int range = 0; range < numRanges; range++) {
            cRanges[range] = Comedi.get_range(devDesc, subdev, chan, range);
            if (cRanges[range] == null)
                throw new ComediException("Unable to read channel info");
        }
        range = 0;
        aref = 0;
    }

    public int getIndex() {
        return(chan);
    }

    public LSamplT getMaxdata() {
        return(maxdata);
    }

    public int findRange(int unit, double min, double max) throws ComediException {
        int index = Comedi.find_range(devDesc, subdev, chan, unit, min, max);
        if (index == -1)
            throw new ComediException("Unable to find requested range");
        return(index);
    }
    
    public int setActiveRange(int range) throws ComediException {
        if (range < 0 || range >= cRanges.length)
            throw new ComediException("Uknown range");
        int old = this.range;
        this.range = range;
        return(old);
    }
    
    public int getActiveRange() {
        return(range);
    }
    
    public ComediRange getRangeInfo(int range) throws ComediException {
        if (range < 0 || range >= cRanges.length)
            throw new ComediException("Uknown range");
        return(cRanges[range]);
    }
    
    public ComediRange[] getRangeInfos() {
         return(cRanges);
    }

    public int setAnalogRef(int aref) throws ComediException {
        if (aref != Comedi.AREF_GROUND && aref != Comedi.AREF_COMMON &&
                aref != Comedi.AREF_DIFF && aref != Comedi.AREF_OTHER)
            throw new ComediException("Unkown refernce");
        int old = this.aref;
        this.aref = aref;
        return(old);
    }

    public int getAnalogRef() {
        return(aref);
    }

    public void dataRead(LSamplT data) throws ComediException {
        if (Comedi.data_read(devDesc, subdev, chan, range, aref, data) < 0)
            throw new ComediException();
    }
    
    public void dataReadDelayed(LSamplT data, int nsDelay) throws ComediException {
        if (Comedi.data_read_delayed(devDesc, subdev, chan, range,
                aref, data, nsDelay) < 0)
            throw new ComediException();
    }
    
    public void nextToRead() throws ComediException {
        if (Comedi.data_read_hint(devDesc, subdev, chan, range, aref) < 0)
            throw new ComediException();
    }
    
    public void dataWrite(LSamplT data) throws ComediException {
        if (Comedi.data_write(devDesc, subdev, chan, range, aref, data) < 0)
            throw new ComediException();
    }
    
    public void dioConfig(int direction) throws ComediException {
        if (Comedi.dio_config(devDesc, subdev, chan, direction) != 0)
            throw new ComediException();
    }
    
    public void dioRead(int[] bit) throws ComediException {
        if (Comedi.dio_read(devDesc, subdev, chan, bit) < 0)
            throw new ComediException();
    }
    
    public void dioWrite(int bit) throws ComediException {
        if (Comedi.dio_write(devDesc, subdev, chan, bit) < 0)
            throw new ComediException();
    }
    
    public String toString()  {
        String ls = System.getProperty("line.separator");
        int indent = 4;
        char pad[] = new char[indent];
        for (int i=0; i<pad.length; i++)
            pad[i] = ' ';
        StringBuffer buffer = new StringBuffer();
        buffer.append(pad).append("valore massimo = ").append(maxdata)
            .append(ls);
        buffer.append(pad).append("scale = ").append(cRanges.length)
            .append(ls);
        for (int i=0; i<cRanges.length; i++) {
            buffer.append(pad);
            buffer.append(i + ": " + cRanges[i]);
            buffer.append(ls);
        }
        return(buffer.toString());
    }
}
