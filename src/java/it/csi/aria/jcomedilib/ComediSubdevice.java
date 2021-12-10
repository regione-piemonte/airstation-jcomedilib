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
// $Id: ComediSubdevice.java,v 1.3 2005-04-08 15:58:27 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.jcomedilib;


/**
 * @author pierfrancesco.vallosio@consulenti.csi.it
 *
 */
public class ComediSubdevice {
    
    private static final String subdevNames[] = {
            "inutilizzato",             //"unused",
            "ingresso analogico",       //"analog input",
            "uscita analogica",         //"analog output",
            "ingresso digitale",        //"digital input",
            "uscita digitale",          //"digital output",
            "ingresso/uscita digitale", //"digital I/O",
            "contatore",                //"counter",
            "temporizzatore",           //"timer",
            "memoria",                  //"memory",
            "calibrazione",             //"calibration",
            "processore",               //"processor"
            };
    private long devDesc;
    private int subdev;
    private int type;
    private boolean maxdataIsChanSpecific;
    private boolean rangeIsChanSpecific;
    private ComediChannel[] channels;

    ComediSubdevice(long devDesc, int subdev, int type) throws ComediException {
        this.devDesc = devDesc;
        this.subdev = subdev;
        this.type = type;
        int numChans = Comedi.get_n_channels(devDesc, subdev);
        if (numChans < 0)
            throw new ComediException("Unable to read subdevice info");
        int result = Comedi.maxdata_is_chan_specific(devDesc, subdev);
        if (result < 0 || result > 1)
            throw new ComediException("Unable to read subdevice info");
        maxdataIsChanSpecific = result == 1;
        result = Comedi.range_is_chan_specific(devDesc, subdev);
        if (result < 0 || result > 1)
            throw new ComediException("Unable to read subdevice info");
        rangeIsChanSpecific = result == 1;
        channels = new ComediChannel[numChans];
        for (int chan = 0; chan < numChans; chan++) {
            channels[chan] = new ComediChannel(devDesc, subdev, chan);
        }
    }

    public int getType() {
        return(type);
    }

    public String getTypeName() {
        if (type < 0 || type >= subdevNames.length)
            return("?");
        return(subdevNames[type]);
    }
    
    public int getIndex() {
        return(subdev);
    }

    public boolean getMaxdataIsChanSpecific() {
        return(maxdataIsChanSpecific);
    }

    public boolean getRangeIsChanSpecific() {
        return(rangeIsChanSpecific);
    }

    public int getChanNumber() {
        return(channels.length);
    }

    public ComediChannel getChannel(int chan) {
        if (chan < 0 || chan >= channels.length)
            return(null);
        return(channels[chan]);
    }
    
    public ComediChannel[] getChannels() {
        return(channels);
    }

    public void lock() throws ComediException {
        if (Comedi.lock(devDesc, subdev) != 0)
            throw new ComediException("Unable to lock subdevice");
    }
    
    public void unlock() throws ComediException {
        if (Comedi.unlock(devDesc, subdev) != 0)
            throw new ComediException("Unable to unlock subdevice");
    }
    
    public int getBufferSize() throws ComediException {
        int size = Comedi.get_buffer_size(devDesc, subdev);
        if (size == -1)
            throw new ComediException("Unable to read buffer size");
        return(size);
    }

    public int setBufferSize(int size) throws ComediException {
        int oldsize =  Comedi.set_buffer_size(devDesc, subdev, size);
        if (oldsize < 0)
            throw new ComediException("Unable to set buffer size");
        return(oldsize);
    }
    
    public int getMaxBufferSize() throws ComediException {
        int size = Comedi.get_max_buffer_size(devDesc, subdev);
        if (size == -1)
            throw new ComediException("Unable to read maximum buffer size");
        return(size);
    }

    public int setMaxBufferSize(int size)  throws ComediException {
        int oldsize = Comedi.set_max_buffer_size(devDesc, subdev, size);
        if (oldsize < 0)
            throw new ComediException("Unable to set maximum buffer size");
        return(oldsize);
    }
    
    public int getSubdeviceFlags() throws ComediException {
        int flags = Comedi.get_subdevice_flags(devDesc, subdev);
        if (flags == -1)
            throw new ComediException("Unable to read subdevice flags");
        return(flags);
    }

    public void dioBitfield(int write_mask, int[] bits) throws ComediException {
        if (Comedi.dio_bitfield(devDesc, subdev, write_mask, bits) < 0)
            throw new ComediException();
    }
    
    public String toString() {
        String ls = System.getProperty("line.separator");
        int indent = 4;
        char pad[] = new char[indent];
        for (int i=0; i<pad.length; i++)
            pad[i] = ' ';
        StringBuffer buffer = new StringBuffer();
        buffer.append(pad).append("tipo = ").append(getTypeName()).append(ls);
        buffer.append(pad);
        if (maxdataIsChanSpecific)
            buffer.append("valore massimo specifico per canale");
        else
            buffer.append("valore massimo uguale per tutti i canali");
        buffer.append(ls);
        buffer.append(pad);
        if (rangeIsChanSpecific)
            buffer.append("scala specifica per canale");
        else
            buffer.append("scala uguale per tutti i canali");
        buffer.append(ls);
        buffer.append(pad).append("numero di canali = " + channels.length)
            .append(ls);
        if (!maxdataIsChanSpecific && !rangeIsChanSpecific &&
            channels.length > 0) {
            buffer.append(pad).append("tutti i canali:").append(ls);
            buffer.append(channels[0].toString());
        }
        else {
            for (int i=0; i<channels.length; i++) {
                buffer.append(pad).append("canale n. " + i + ":").append(ls);
                buffer.append(channels[i].toString());
            }
        }
        return(buffer.toString());
    }
}
