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
// $Id: MonitorApp.java,v 1.3 2005-03-18 16:39:26 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import java.awt.event.*;
import javax.swing.*;

public class MonitorApp extends JFrame
{
    private Monitor monitor;

    public MonitorApp(String[] args)
    {
        monitor = new Monitor(args);
        getContentPane().add(monitor.getPanel());
        addWindowListener(new WindowAdapter (){
                public void windowClosing(WindowEvent e) {
                    monitor.shutdown();
                    System.exit(0);
                }});
        
    }


    public static void main(String[] args)
    {
        JFrame f = new MonitorApp(args);
        f.setTitle("Monitor");
        f.pack();
        f.setResizable(false);
        f.setVisible(true);
    }
}
