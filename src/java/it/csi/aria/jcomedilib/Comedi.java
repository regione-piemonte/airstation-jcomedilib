// ----------------------------------------------------------------------------
// Copyright Regione Piemonte - 2021
// SPDX-License-Identifier: LGPL-2.1
// ----------------------------------------------------------------------------
// Original Author of file: Pierfrancesco Vallosio
// Purpose of file: Java JNI interface for Comedi library (Linux Control and
//   Measurement Device Interface https://www.comedi.org/)
//   This is the main class with the definitions and methods from Comedi library
// Change log:
//   2003-10-31: initial version
// ----------------------------------------------------------------------------
// $Id: Comedi.java,v 1.3 2008-05-23 07:45:20 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.jcomedilib;

public class Comedi {
    
    public static final int AREF_GROUND = 0x00;
    public static final int AREF_COMMON = 0x01;
    public static final int AREF_DIFF =   0x02;
    public static final int AREF_OTHER =  0x03;

    public static final int OOR_NUMBER = 0;
    public static final int OOR_NAN =    1;

    public static final int INPUT =  0;
    public static final int OUTPUT = 1;

    public static final int INSN_READ =    0;
    public static final int INSN_WRITE =   1;
    public static final int INSN_BITS =    2;
    public static final int INSN_CONFIG =  3;
    public static final int INSN_GTOD =    4;
    public static final int INSN_WAIT =    5;
    public static final int INSN_INTTRIG = 6;

    public static final int UNIT_volt = 0;
    public static final int UNIT_mA = 1;
    public static final int UNIT_none = 2;

    public static final int SUBD_UNUSED =  0;  // unused
    public static final int SUBD_AI =      1;  // analog input
    public static final int SUBD_AO =      2;  // analog output
    public static final int SUBD_DI =      3;  // digital input
    public static final int SUBD_DO =      4;  // digital output
    public static final int SUBD_DIO =     5;  // digital input/output
    public static final int SUBD_COUNTER = 6;  // counter
    public static final int SUBD_TIMER =   7;  // timer
    public static final int SUBD_MEMORY =  8;  // memory, EEPROM, DPRAM
    public static final int SUBD_CALIB =   9;  // calibration DACs
    public static final int SUBD_PROC =   10;  // processor, DSP

    private static boolean initDone = false;

    private Comedi() {
    }

    public static synchronized void init() throws ComediException {
    	try {
	        if (!initDone) {
	            System.loadLibrary("comedijni");
	            initDone = true;
	        }
    	}
        catch (Throwable t) {
        	throw new ComediException("Error loading comedijni library" ,t);
        }
    }

    public native static int close(long devDesc);
    public native static long open(String filename);
    public native static String strerror(int errnum);
    public native static int errno();
    public native static int get_n_subdevices(long devDesc);
    public native static int get_version_code(long devDesc);
    public native static String get_driver_name(long devDesc);
    public native static String get_board_name(long devDesc);
    public native static int get_subdevice_type(long devDesc, int subdevice);
    public native static int find_subdevice_by_type(
        long devDesc, int type, int start_subdevice);
    public native static int get_subdevice_flags(long devDesc, int subdevice);
    public native static int get_n_channels(long devDesc, int subdevice);
    public native static int range_is_chan_specific(long devDesc, int subdevice);
    public native static int maxdata_is_chan_specific(long devDesc, int subdevice);
    public native static LSamplT get_maxdata(long devDesc, int subdevice, int channel);
    public native static int get_n_ranges(long devDesc, int subdevice, int channel);
    public native static ComediRange get_range(
        long devDesc, int subdevice, int channel, int range);
    public native static int find_range(long devDesc, int subdevice, int channel,
        int unit, double min, double max);
    public native static int get_buffer_size(long devDesc, int subdevice);
    public native static int get_max_buffer_size(long devDesc, int subdevice);
    public native static int set_buffer_size(long devDesc, int subdevice, int size);
    public native static int do_insnlist(long devDesc, ComediInsn[] list);
    public native static int do_insn(long devDesc, ComediInsn instruction);
    public native static int lock(long devDesc, int subdevice);
    public native static int unlock(long devDesc, int subdevice);
    public native static double to_phys(
        LSamplT data, ComediRange range, LSamplT maxdata);
    public native static LSamplT from_phys(
        double data, ComediRange range, LSamplT maxdata);
    public native static int data_read(long devDesc, int subdevice, int channel,
        int range, int aref, LSamplT data);
    public native static int data_read_delayed(long devDesc, int subdevice,
            int channel, int range, int aref, LSamplT data, int nanosec);
    public native static int data_read_hint(long devDesc, int subdevice,
            int channel, int range, int aref);
    public native static int data_write(long devDesc, int subdevice, int channel,
        int range, int aref, LSamplT data);
    public native static int dio_config(
        long devDesc, int subdevice, int channel, int direction);
    public native static int dio_read(
        long devDesc, int subdevice, int channel, int[] bit);
    public native static int dio_write(
        long devDesc, int subdevice, int channel, int bit);
    public native static int dio_bitfield(
        long devDesc, int subdevice, int write_mask, int[] bits);
    public native static int set_max_buffer_size(
        long devDesc, int subdevice, int max_size);
    public native static int set_global_oor_behavior(int behavior);
}
