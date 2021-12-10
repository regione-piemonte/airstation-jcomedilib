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
// $Id: ComediChannelConfigurator.java,v 1.3 2005-03-15 12:00:20 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import it.csi.aria.jcomedilib.*;

public class ComediChannelConfigurator extends JPanel
    implements ActionListener
{
    private ComediChannel cc;
    private String name;
    private Map map_actions;
    private RangeSelectable rs;
    private ComediRange currentRange;
         
    public ComediChannelConfigurator(ComediChannel cc, String name)
    {
        this.cc = cc;
        this.name = name;
        map_actions = new HashMap();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel(name));
        add(new JLabel("valore massimo: " + cc.getMaxdata()));
        ComediRange[] cRanges = cc.getRangeInfos();
        add(new JLabel("scale: " + cRanges.length));
        ButtonGroup btnGroup = new ButtonGroup();
        for (int i=0; i<cRanges.length; i++) {
            ComediRange cRange = cRanges[i];
            String label = " " + i + ": " + cRange;
            map_actions.put(label, cRange);
            JRadioButton rb = new JRadioButton(label);
            rb.addActionListener(this);
            if (i == 0) {
                rb.setSelected(true);
                currentRange = cRange;
            }
            btnGroup.add(rb);
            add(rb);
        }
    }

    public void setRangeSelectable(RangeSelectable rs)
    {
        try {
            this.rs = rs;
            if (rs != null)
                rs.setRange(currentRange.getMin(), currentRange.getMax());
        }
        catch (RuntimeException re) {
            re.printStackTrace();
        }
    }

    public String toString()
    {
        return(name);
    }

    public void actionPerformed(ActionEvent ae)
    {
        currentRange = (ComediRange)map_actions.get(ae.getActionCommand());
        try {
            if (rs != null)
                rs.setRange(currentRange.getMin(), currentRange.getMax());
        }
        catch (RuntimeException re) {
            re.printStackTrace();
        }
    }
}
