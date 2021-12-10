/*
 *Copyright Regione Piemonte - 2021
 *SPDX-License-Identifier: LGPL-2.1
 */

//$Id: ComediInsn.java,v 1.4 2005-03-15 12:00:20 pfvallosio Exp $

package it.csi.aria.jcomedilib;

public class ComediInsn {

    private int insn;
    private int[] data;
    private int subdev;
    private int chanspec;
    private int[] unused = new int[3];

    public int getInsn() {
        return insn;
    }

    public void setInsn(int insn) {
        this.insn = insn;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public int getSubdev() {
        return subdev;
    }

    public void setSubdev(int subdev) {
        this.subdev = subdev;
    }

    public int getChanspec() {
        return chanspec;
    }

    public void setChanspec(int chanspec) {
        this.chanspec = chanspec;
    }

    int[] getUnused() {
        return unused;
    }

    void setUnused(int[] unused) {
        this.unused = unused;
    }

}
