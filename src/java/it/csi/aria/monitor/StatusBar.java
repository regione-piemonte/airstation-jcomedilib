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
// $Id: StatusBar.java,v 1.3 2005-03-15 16:16:25 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import java.awt.*;
import javax.swing.*;

public class StatusBar extends JPanel
{
    private ImageIcon redLight, greenLight, redOffLight, greenOffLight;
    private JLabel lblText, lblGreen, lblRed;

    public StatusBar()
    {
        setBorder(BorderFactory.createEmptyBorder(2, 8, 0, 8));
        lblText = new JLabel();
        redLight = new ImageIcon(getClass().getResource("/images/on.png"));
        redOffLight = new ImageIcon(getClass().getResource("/images/off.png"));
        greenLight =
            new ImageIcon(getClass().getResource("/images/greenOn.png"));
        greenOffLight =
            new ImageIcon(getClass().getResource("/images/greenOff.png"));
        lblGreen = new JLabel(greenOffLight);
        lblRed = new JLabel(redOffLight);
        JPanel paLeds = new JPanel();
        paLeds.setLayout(new GridLayout(1, 2, 2, 2));
        paLeds.add(lblGreen);
        paLeds.add(lblRed);
        setLayout(new BorderLayout());
        add(lblText, BorderLayout.CENTER);
        add(paLeds, BorderLayout.EAST);
    }

    public void setText(final String text)
    {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    lblText.setText(text);
                }
            });        
    }

    public void setGreen(final boolean value)
    {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    lblGreen.setIcon(value ? greenLight : greenOffLight);
                }
            });        
    }

    public void setRed(final boolean value)
    {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    lblRed.setIcon(value ? redLight : redOffLight);
                }
            });        
    }  
}
