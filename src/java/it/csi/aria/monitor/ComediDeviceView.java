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
// $Id: ComediDeviceView.java,v 1.3 2005-03-15 12:00:20 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import javax.swing.*;
import it.csi.aria.jcomedilib.*;

public class ComediDeviceView extends JPanel
{
    private String name;
    
    public ComediDeviceView(ComediDevice cd)
    {
        name = cd.getBoardName();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel("scheda: " + cd.getBoardName()));
        add(new JLabel("driver: " + cd.getDriverName()));
        add(new JLabel("versione: " + cd.getVersion().toString()));
        add(new JLabel("numero dispositivi: " + cd.getSubdevNumber()));
    }
    
    public String toString()
    {
        return(name);
    }
}
