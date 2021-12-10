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
// $Id: Monitor.java,v 1.7 2008-04-18 16:40:17 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import java.util.*;
import java.awt.BorderLayout;
import it.csi.aria.jcomedilib.*;

import java.text.*;
import javax.swing.tree.*;
import javax.swing.JPanel;

public class Monitor
{
    private JPanel paGlobal;
    private StatusBar paStatus;
    private List list_comediBoards;
    private Poller poller;
    private String[] boardNames;

    public Monitor(String[] boardNames)
    {
        try {
            this.boardNames = boardNames;
            paStatus = new StatusBar();
            list_comediBoards = initComediBoards();
            poller = new Poller();
            paGlobal = new JPanel();
            paGlobal.setLayout(new BorderLayout());
            JPanel paMonitor =
                initComediMonitorComponents(list_comediBoards, poller);
            paGlobal.add(paMonitor, BorderLayout.CENTER);
            paGlobal.add(paStatus, BorderLayout.NORTH);
            poller.setStatusBar(paStatus);
            poller.start();
        }
        catch (Exception ex) {
            paStatus.setText("Inizializzazione fallita: " + ex.getMessage());
            paStatus.setRed(true);
            ex.printStackTrace();
        }
    }

    public JPanel getPanel()
    {
        return(paGlobal);
    }

    public void shutdown()
    {
        poller.stop();
        try {
            poller.join(2000);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        Iterator it_boards = list_comediBoards.iterator();
        while (it_boards.hasNext()) {
            ComediDevice cd = (ComediDevice)it_boards.next();
            try {
                cd.close();
            }
            catch (ComediException ex) {
                ex.printStackTrace();
            }
        }
        try {
			DriverManager.getInstance().clearAllBindings();
		} catch (ComediException e) {
			e.printStackTrace();
		}
    }

    private List initComediBoards()
        throws Exception
    {
        List list_boards = new ArrayList();
        Comedi.init();
        DriverManager dm = DriverManager.getInstance();
        dm.clearAllBindings();
        for (int df=0; df<Math.min(dm.getMaxDevices(), boardNames.length); df++) { 
            try {
                list_boards.add(new ComediDevice(dm.bindDriver(boardNames[df])));
            }
            catch (NoSuchDeviceComediException nsd) {
            }
        }
        return(list_boards);
    }

    private MonitorPanel initComediMonitorComponents(List list_boards,
                                                     Poller poller)
        throws ComediException
    {
        List list_ai = new ArrayList();
        List list_ao = new ArrayList();
        List list_di = new ArrayList();
        List list_do = new ArrayList();

        NumberFormat nf = new DecimalFormat("00");
        DefaultMutableTreeNode topNode = new DefaultMutableTreeNode("Schede");
        ComediChannelView chanView = null;
        int iBoard = 0;
        Iterator it_boards = list_boards.iterator();
        while (it_boards.hasNext()) {
            ComediDevice board = (ComediDevice)it_boards.next();
            DefaultMutableTreeNode devNode =
                new DefaultMutableTreeNode(new ComediDeviceView(board));
            topNode.add(devNode);
            ComediSubdevice[] subdevs = board.getSubdevices();
            for (int sub=0; sub<subdevs.length; sub++) {
                ComediSubdevice subdev = subdevs[sub];
                DefaultMutableTreeNode subdevNode = new DefaultMutableTreeNode(
                    new ComediSubdeviceView(subdev));
                devNode.add(subdevNode);
                ComediChannel[] channels = subdev.getChannels();
                for (int chan=0; chan<channels.length; chan++) {
                    ComediChannel channel = channels[chan];
                    String chanName = subdev.getTypeName() + " " + iBoard +
                        "." + nf.format(chan);
                    switch (subdev.getType()) {
                    case Comedi.SUBD_AI:
                        ComediChannelConfigurator aiConf =
                            new ComediChannelConfigurator(channel,"canale "+chan);
                        ComediAnalogicInput cai = new ComediAnalogicInput(
                            channel, Comedi.AREF_GROUND,
                            Comedi.UNIT_volt, -5.0, +5.0);
                        SimpleAIView saiv = new SimpleAIView(
                            chanName, -5.0, +5.0, 5, "V");
                        aiConf.setRangeSelectable(cai);
                        cai.setView(saiv);
                        subdevNode.add(new DefaultMutableTreeNode(aiConf));
                        list_ai.add(saiv);
                        poller.addInput(cai);
                        break;
                    case Comedi.SUBD_AO:
                        ComediChannelConfigurator aoConf =
                            new ComediChannelConfigurator(channel,"canale "+chan);
                        ComediAnalogicOutput cao = new ComediAnalogicOutput(
                            channel, Comedi.AREF_GROUND,
                            Comedi.UNIT_volt, 0.0, +5.0);
                        SimpleAOView saov = new SimpleAOView(
                            chanName, 0.0, +5.0, 5, "V");
                        aoConf.setRangeSelectable(cao);
                        cao.setView(saov);
                        saov.setOutput(cao);
                        subdevNode.add(new DefaultMutableTreeNode(aoConf));
                        list_ao.add(saov);
                        poller.addInput(cao);
                        break;
                    case Comedi.SUBD_DI:
                        chanView =
                            new ComediChannelView(channel, "canale " + chan);
                        ComediDigitalInput cdi = new ComediDigitalInput(channel);
                        SimpleDIView sdiv = new SimpleDIView(chanName); 
                        cdi.setView(sdiv);
                        subdevNode.add(new DefaultMutableTreeNode(chanView));
                        list_di.add(sdiv);
                        poller.addInput(cdi);
                        break;
                    case Comedi.SUBD_DO:
                        chanView =
                            new ComediChannelView(channel, "canale " + chan);
                        ComediDigitalOutput cdo = new ComediDigitalOutput(channel);
                        SimpleDOView sdov = new SimpleDOView(chanName);
                        cdo.setView(sdov);
                        sdov.setOutput(cdo);
                        subdevNode.add(new DefaultMutableTreeNode(chanView));
                        list_do.add(sdov);
                        poller.addInput(cdo);
                        break;
                    default:
                        break;
                    }
                }
            }
            iBoard++;
        }
        OptionsView options = new OptionsView();
        options.setPoller(poller);

        return(new MonitorPanel(topNode, options,
                                list_ai, list_ao, list_di, list_do));
    }
}
