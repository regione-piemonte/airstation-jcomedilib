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
// $Id: Poller.java,v 1.3 2003-12-22 16:02:45 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import java.util.*;

public class Poller implements Runnable
{
    private Thread pollerThread = null;
    private List list_inputs = new ArrayList();
    private int period = 1000;
    private StatusBar statusBar = null;
    
    public Poller()
    {
    }

    public void start()
    {
        if (pollerThread == null) {
            pollerThread = new Thread(this, "MonitorPoller");
            pollerThread.start();
        }
    }

    public void stop()
    {
        pollerThread = null;
    }

    public void join(int millis)
        throws InterruptedException
    {
        if (pollerThread != null)
            pollerThread.join(millis);
    }

    public void run()
    {
        Thread thisThread = Thread.currentThread();
        while (pollerThread == thisThread) {
            if (statusBar != null) {
                statusBar.setGreen(true);
                statusBar.setRed(false);
            }
            ListIterator li = list_inputs.listIterator();
            while (li.hasNext()) {
                Input input = (Input)li.next();
                if (!input.read() && statusBar != null) {
                    statusBar.setGreen(false);
                    statusBar.setRed(true);
                }
            }
            try {
                Thread.sleep(period);
            }
            catch (InterruptedException e) {
            }
        }
    }

    public void addInput(Input input)
    {
        list_inputs.add(input);
    }

    public void removeInput(Input input)
    {
        list_inputs.remove(input);
    }

    public void removeAllInputs()
    {
        list_inputs.clear();
    }

    public void setPollPeriod(int period)
    {
        this.period = period;
    }

    public int getPollPeriod()
    {
        return(period);
    }
    
    public void setStatusBar(StatusBar statusBar)
    {
        this.statusBar = statusBar;
    }

}
