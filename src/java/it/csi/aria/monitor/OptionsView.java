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
// $Id: OptionsView.java,v 1.2 2003-12-22 16:02:45 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import javax.swing.*;
import javax.swing.event.*;

public class OptionsView extends JPanel
{
    Poller poller = null;

    public OptionsView()
    {
        SpinnerModel spinModel = new SpinnerNumberModel(500, 50, 5000, 50);
        final JSpinner sp_pollPeriod = new JSpinner(spinModel);
        JLabel lbl_pollPeriod = new JLabel("Periodo di polling: ");
        lbl_pollPeriod.setLabelFor(sp_pollPeriod);
        add(lbl_pollPeriod);
        add(sp_pollPeriod);
        sp_pollPeriod.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (poller != null)
                        poller.setPollPeriod(
                            ((Integer)sp_pollPeriod.getValue()).intValue());
                }});
    }

    public void setPoller(Poller poller)
    {
        this.poller = poller;
    }
}
