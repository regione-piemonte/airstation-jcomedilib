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
// $Id: Test.java,v 1.6 2005-03-18 16:39:26 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import it.csi.aria.jcomedilib.*;

public class Test
{
    public void doTest(String boardName)
    {
        try {
            Comedi.init();
            DriverManager dm = DriverManager.getInstance();
            dm.clearAllBindings();
            String devName = dm.bindDriver(boardName);
            long fd = Comedi.open(devName);
            LSamplT maxdata = Comedi.get_maxdata(fd, 0, 0);
            System.out.println("Max data value = " + maxdata.getValue());
            boolean rs = Comedi.range_is_chan_specific(fd, 0) == 1;
            System.out.println("Range is channel specific = " + rs);
            int nranges = Comedi.get_n_ranges(fd, 0, 0);
            System.out.println("Number of ranges = " + nranges);
            ComediRange cr_current = null;
            for (int i=0; i<nranges; i++) {
                ComediRange cr = Comedi.get_range(fd, 0, 0, i);
                System.out.println("Range "+i+" ["+cr.getMin()+","+cr.getMax()+"]");
                if (i == 0)
                    cr_current = cr;
            }
            LSamplT data = new LSamplT(0);
            for (int i=0; i<1000; i++) {
                for (int ch=0; ch<16; ch++) {
                    Comedi.data_read(fd, 0, ch, 0, Comedi.AREF_GROUND, data);
                    //cdev.data_read(0, ch, 0, Comedi.AREF_GROUND, data);
                    double volts= Comedi.to_phys(data, cr_current, maxdata);
                    System.out.println("Channel " + ch + ": " + "Count = " +
                                       data.getValue() + ", Volts = " + volts);
                }
                try {
                    Thread.sleep(250);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            Comedi.close(fd);
            dm.unbindDriver(devName);
            
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Test test = new Test();
        test.doTest(args[0]);
        return;
    }

}
