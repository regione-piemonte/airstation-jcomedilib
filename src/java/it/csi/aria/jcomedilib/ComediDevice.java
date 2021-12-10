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
// $Id: ComediDevice.java,v 1.7 2005-03-18 16:39:26 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.jcomedilib;

public class ComediDevice {
    
    private long deviceDescriptor;
    private String deviceName;
    private Version version;
    private String driverName;
    private String boardName;
    private ComediSubdevice[] subdevices;

    public ComediDevice(String filename) throws ComediException {
        deviceDescriptor = Comedi.open(filename);
        deviceName = filename;
        if (deviceDescriptor == 0) {
            if (Comedi.errno() == 19) /* No Such Device */
                throw new NoSuchDeviceComediException("No such device: " + filename);
            else
                throw new ComediException("Error opening device: " + filename);
        }
        int code = Comedi.get_version_code(deviceDescriptor);
        if (code == -1)
            throw new ComediException("Unable to read version code");
        version = new Version(code);
        driverName = Comedi.get_driver_name(deviceDescriptor);
        if (driverName == null)
            throw new ComediException("Unable to read driver name");
        boardName = Comedi.get_board_name(deviceDescriptor);
        if (boardName == null)
            throw new ComediException("Unable to read board name");
        int numSubdevs = Comedi.get_n_subdevices(deviceDescriptor);
        if (numSubdevs < 0)
            throw new ComediException("Unable to read subdevice number");
        subdevices = new ComediSubdevice[numSubdevs];
        for (int subdev = 0; subdev < numSubdevs; subdev++) {
            int type = Comedi.get_subdevice_type(deviceDescriptor, subdev);
            subdevices[subdev] = new ComediSubdevice(deviceDescriptor, subdev, type);
        }
    }
    
    public void close() throws ComediException {
        boolean error = Comedi.close(deviceDescriptor) != 0;
        deviceDescriptor = 0;
        if (error)
            throw new ComediException("Error closing device");
    }
    
    public String getDeviceName() {
        return(deviceName);
    }
    
    public Version getVersion() {
        return (version);
    }

    public String getDriverName() {
        return (driverName);
    }

    public String getBoardName() {
        return (boardName);
    }

    public int getSubdevNumber() {
        return (subdevices.length);
    }
    
    public ComediSubdevice getSubdevice(int index) {
        if (index < 0 || index >= subdevices.length)
            return(null);
        return(subdevices[index]);
    }

    public ComediSubdevice[] getSubdevices() {
        return(subdevices);
    }

    public ComediSubdevice[] getSubdevicesByType(int type) {
        int count = 0;
        for (int s=0; s<subdevices.length; s++)
            if (subdevices[s].getType() == type)
                count++;
        if (count == 0)
            return(null);
        ComediSubdevice[] foundSubdevs = new ComediSubdevice[count];
        int fs = 0;
        for (int s=0; s<subdevices.length; s++) {
            if (subdevices[s].getType() == type)
                foundSubdevs[fs++] = subdevices[s];
        }
        return(foundSubdevs);
    }
    
    public int doInsn(ComediInsn instruction) throws ComediException {
        int num_samples = Comedi.do_insn(deviceDescriptor, instruction);
        if (num_samples == -1)
            throw new ComediException("Failure executing instruction");
        return(num_samples);
    }

    public int doInsnList(ComediInsn[] list) throws ComediException {
        int num_instructions = Comedi.do_insnlist(deviceDescriptor, list);
        if (num_instructions == -1)
            throw new ComediException("Failure executing instruction list");
        return(num_instructions);
    }
    
    public String toString() {
        return(toString(false));
    }
    
    public String toString(boolean printSubdevs) {
        String ls = System.getProperty("line.separator");
        StringBuffer buffer = new StringBuffer();
        buffer.append("ComediDevice").append(ls);
        buffer.append("  versione = ").append(version).append(ls);
        buffer.append("  driver = ").append(driverName).append(ls);
        buffer.append("  scheda = ").append(boardName).append(ls);
        buffer.append("  numero di dispositivi = ").append(getSubdevNumber());
        buffer.append(ls);
        if (printSubdevs)
            for (int i = 0; i < subdevices.length; i++) {
                buffer.append("  dispositivo n. " + i + ":").append(ls);
                buffer.append(subdevices[i].toString());
            }
        return (buffer.toString());
    }
}

