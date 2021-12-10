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
//$Id: MonitorPanel.java,v 1.5 2005-03-15 16:16:25 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import javax.swing.tree.*;
import javax.swing.event.*;


public class MonitorPanel extends JPanel
{
    public MonitorPanel(DefaultMutableTreeNode topNode, JComponent options,
                        java.util.List list_ai, java.util.List list_ao,
                        java.util.List list_di, java.util.List list_do)
    {
        ArrayList listAIPages = new ArrayList();
        ArrayList listAOPages = new ArrayList();
        ArrayList listDIPages = new ArrayList();
        ArrayList listDOPages = new ArrayList();

        int count = 0;
        JPanel panel = null;
        Iterator it_ai = list_ai.iterator();
        while (it_ai.hasNext()) {
            if (count % 16 == 0) {
                panel = new JPanel();
                panel.setLayout(new TableLayout(4, 4, 8, 8));
                panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                listAIPages.add(panel);
            }
            panel.add((JComponent)it_ai.next());
            count++;
        }

        count = 0;
        Iterator it_ao = list_ao.iterator();
        while (it_ao.hasNext()) {
            if (count % 16 == 0) {
                panel = new JPanel();
                panel.setLayout(new TableLayout(4, 4, 8, 8));
                panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                listAOPages.add(panel);
            }
            panel.add((JComponent)it_ao.next());
            count++;
        }

        boolean digitalCommonPanel = 
            list_di.size() > 0 && list_di.size() <= 16 &&
            list_do.size() > 0 && list_do.size() <= 16;
        if (digitalCommonPanel) {
            JPanel pa_digitalInput = new JPanel();
            pa_digitalInput.setLayout(new TableLayout(8, 2, 4, 4));
            pa_digitalInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
            Iterator it_di = list_di.iterator();
            while (it_di.hasNext()) {
                pa_digitalInput.add((JComponent)it_di.next());
            }
            JPanel pa_digitalOutput = new JPanel();
            pa_digitalOutput.setLayout(new TableLayout(8, 2, 4, 4));
            pa_digitalOutput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
            Iterator it_do = list_do.iterator();
            while (it_do.hasNext()) {
                pa_digitalOutput.add((JComponent)it_do.next());
            }
            panel = new JPanel();
            panel.setLayout(new GridLayout(1, 2, 20, 20));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.add(pa_digitalInput);
            panel.add(pa_digitalOutput);
            listDIPages.add(panel);
        }
        else {
            count = 0;
            Iterator it_di = list_di.iterator();
            while (it_di.hasNext()) {
                if (count % 32 == 0) {
                    panel = new JPanel();
                    panel.setLayout(new TableLayout(8, 4, 8, 8));
                    panel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
                    listDIPages.add(panel);
                }
                panel.add((JComponent)it_di.next());
                count++;
            }
            count = 0;
            Iterator it_do = list_do.iterator();
            while (it_do.hasNext()) {
                if (count % 32 == 0) {
                    panel = new JPanel();
                    panel.setLayout(new TableLayout(8, 4, 8, 8));
                    panel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
                    listDOPages.add(panel);
                }
                panel.add((JComponent)it_do.next());
                count++;
            }
        }
        
        JTabbedPane tp_main = new JTabbedPane();
        tp_main.add("Configurazione", initConfig(topNode, options));
        Iterator it_panels = listAIPages.listIterator();
        while (it_panels.hasNext())
            tp_main.add("Ingressi analogici", (JPanel)it_panels.next());
        it_panels = listAOPages.listIterator();
        while (it_panels.hasNext())
            tp_main.add("Uscite analogiche", (JPanel)it_panels.next());
        if (digitalCommonPanel) {
            tp_main.add("Ingressi/Uscite digitali",(JPanel)listDIPages.get(0));
        }
        else {
            it_panels = listDIPages.listIterator();
            while (it_panels.hasNext())
                tp_main.add("Ingressi digitali", (JPanel)it_panels.next());
            it_panels = listDOPages.listIterator();
            while (it_panels.hasNext())
                tp_main.add("Uscite digitali", (JPanel)it_panels.next());
        }
        add(tp_main);
    }

    private JPanel initConfig(DefaultMutableTreeNode topNode,
                              JComponent options)
    {
        final JTree jTree = new JTree(topNode);
        jTree.setRootVisible(true);
    jTree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
        ((DefaultTreeModel)jTree.getModel()).reload(topNode);
        JScrollPane jsp_tree = new JScrollPane(jTree);
        jsp_tree.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        
        final JPanel pa_info = new JPanel();
        pa_info.setLayout(new BoxLayout(pa_info, BoxLayout.PAGE_AXIS));
        pa_info.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        jTree.addTreeSelectionListener(new TreeSelectionListener() {
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
            jTree.getLastSelectedPathComponent();
                    pa_info.removeAll();
                    if (node != null) {
                        Object nodeObj = node.getUserObject();
                        if (nodeObj instanceof JComponent)
                            pa_info.add((JComponent)nodeObj);
                    }
                    pa_info.repaint();
                }
        });

        options.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        
        JPanel pa_config = new JPanel();
        pa_config.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pa_config.setLayout(new GridLayout(1, 3, 20, 20));
        pa_config.add(jsp_tree);
        pa_config.add(pa_info);
        pa_config.add(options);
        
        return(pa_config);
    }
}
