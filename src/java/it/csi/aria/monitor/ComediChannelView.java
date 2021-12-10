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
//$Id: ComediChannelView.java,v 1.4 2005-03-15 16:16:25 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import javax.swing.*;
import it.csi.aria.jcomedilib.*;

public class ComediChannelView extends JPanel
{
    private ComediChannel cc;
    private String name;
             
    public ComediChannelView(ComediChannel cc, String name)
    {
        this.cc = cc;
        this.name = name;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel(name));
        add(new JLabel("valore massimo: " + cc.getMaxdata()));
        ComediRange[] cranges = cc.getRangeInfos();
        add(new JLabel("scale: " + cranges.length));
        for (int i=0; i<cranges.length; i++) {
            ComediRange cRange = cranges[i];
            String label = " " + i + ": " + cRange;
            JLabel lbl = new JLabel(label);
            add(lbl);
        }
    }

    public String toString()
    {
        return(name);
    }
}
