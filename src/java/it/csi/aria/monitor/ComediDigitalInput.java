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
// $Id: ComediDigitalInput.java,v 1.4 2005-03-15 12:00:20 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import it.csi.aria.jcomedilib.*;

public class ComediDigitalInput implements Input
{
    private ComediChannel channel;
    private DigitalInputView diView = null;

    public ComediDigitalInput(ComediChannel channel)
        throws ComediException
    {
        this.channel = channel;
    }

    public void setView(DigitalInputView diView)
    {
        this.diView = diView;
    }

    public boolean read()
    {
        try {
            int pvalue[] = new int[1];
            channel.dioRead(pvalue);
            if (diView != null) {
                diView.setValue(pvalue[0] != 0);
            }
            return(true);
        }
        catch (ComediException ce) {
            ce.printStackTrace();
            return(false);
        }
    }
}
