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
// $Id: ComediSubdeviceView.java,v 1.3 2005-03-15 12:00:20 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import javax.swing.*;
import it.csi.aria.jcomedilib.*;


public class ComediSubdeviceView extends JPanel
{
    private String name;
    
    public ComediSubdeviceView(ComediSubdevice cs)
    {
        name = cs.getTypeName();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel(name));
        add(new JLabel("valore massimo:"));
        add(new JLabel(cs.getMaxdataIsChanSpecific() ?
                       "   specifico per canale" :
                       "   uguale per tutti i canali"));
        add(new JLabel("scala:"));
        add(new JLabel(cs.getRangeIsChanSpecific() ? 
                       "   specifica per canale" :
                       "   uguale per tutti i canali"));
        add(new JLabel("numero canali: " + cs.getChanNumber()));
    }
    
    public String toString()
    {
        return(name);
    }
}
