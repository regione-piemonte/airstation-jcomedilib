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
// $Id: TableLayout.java,v 1.4 2005-03-15 16:28:04 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import java.awt.*;


public class TableLayout
    implements LayoutManager, java.io.Serializable
{
    private int hgap;
    private int vgap;
    private int rows;
    private int cols;

    public TableLayout(int rows, int cols)
    {
        this(rows, cols, 0, 0);
    }

    public TableLayout(int rows, int cols, int hgap, int vgap)
    {
        if ((rows == 0) && (cols == 0)) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.rows = rows;
        this.cols = cols;
        this.hgap = hgap;
        this.vgap = vgap;
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        if ((rows == 0) && (this.cols == 0)) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.rows = rows;
    }

    public int getColumns()
    {
        return cols;
    }

    public void setColumns(int cols)
    {
        if ((cols == 0) && (this.rows == 0)) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.cols = cols;
    }

    public int getHgap()
    {
        return hgap;
    }
 
    public void setHgap(int hgap)
    {
        this.hgap = hgap;
    }
    
    public int getVgap()
    {
        return vgap;
    }
    
    public void setVgap(int vgap)
    {
        this.vgap = vgap;
    }

    public void addLayoutComponent(String name, Component comp)
    {
    }

    public void removeLayoutComponent(Component comp)
    {
    }

    public Dimension preferredLayoutSize(Container parent)
    {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = rows;
            int ncols = cols;
            
            if (ncols == 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            }
            if (nrows == 0) {
                nrows = (ncomponents + ncols - 1) / ncols;
            }
            int w = 0;
            int h = 0;
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getPreferredSize();
                if (w < d.width) {
                    w = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }
            return new Dimension(insets.left + insets.right +
                                 ncols*w + (ncols-1)*hgap, 
                                 insets.top + insets.bottom +
                                 nrows*h + (nrows-1)*vgap);
        }
    }

    public Dimension minimumLayoutSize(Container parent)
    {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = rows;
            int ncols = cols;
            
            if (ncols == 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            }
            if (nrows == 0) {
                nrows = (ncomponents + ncols - 1) / ncols;
            }
            int w = 0;
            int h = 0;
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getMinimumSize();
                if (w < d.width) {
                    w = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }
            return new Dimension(insets.left + insets.right +
                                 ncols*w + (ncols-1)*hgap, 
                                 insets.top + insets.bottom +
                                 nrows*h + (nrows-1)*vgap);
        }
    }

    public void layoutContainer(Container parent)
    {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = rows;
            int ncols = cols;
            boolean ltr = parent.getComponentOrientation().isLeftToRight();
            
            if (ncomponents == 0) {
                return;
            }
            if (ncols == 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            }
            if (nrows == 0) {
                nrows = (ncomponents + ncols - 1) / ncols;
            }
            int w = parent.getWidth() - (insets.left + insets.right);
            int h = parent.getHeight() - (insets.top + insets.bottom);
            w = (w - (ncols - 1) * hgap) / ncols;
            h = (h - (nrows - 1) * vgap) / nrows;
            
            if (ltr) {
                for (int c=0, x=insets.left; c<ncols; c++, x+=w+hgap) {
                    for (int r=0, y=insets.top; r<nrows; r++, y+=h+vgap) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, w, h);
                        }
                    }
                }
            }
            else {
                for (int c=0, x=parent.getWidth()-insets.right-w; c<ncols;
                     c++, x-=w+hgap) {
                    for (int r=0, y=insets.top; r<nrows; r++, y+=h+vgap) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, w, h);
                        }
                    }
                }
            }
        }
    }
    
    public String toString()
    {
        return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + 
                ",rows=" + rows + ",cols=" + cols + "]";
    }
}
