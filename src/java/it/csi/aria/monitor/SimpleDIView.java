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
// $Id: SimpleDIView.java,v 1.2 2003-12-22 16:02:45 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import javax.swing.*;
import javax.swing.border.*;

public class SimpleDIView extends JToggleButton
    implements DigitalInputView
{
    public SimpleDIView(String name)
    {
        setText(name);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setIcon(new ImageIcon(getClass().getResource("/images/off.png")));
        setSelectedIcon(
            new ImageIcon(getClass().getResource("/images/on.png")));
    }

    public void setName(String name)
    {
        setText(name);
    }

    public void setValue(boolean value)
    {
        setSelected(value);
    }
}
