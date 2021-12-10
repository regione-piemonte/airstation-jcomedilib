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
// $Id: SimpleAOView.java,v 1.3 2005-03-15 12:00:20 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.monitor;

import java.util.*;
import java.awt.*;
import java.text.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class SimpleAOView extends JPanel
    implements AnalogicOutputView, ChangeListener
{
    private static final int COUNT_DIGITS = 6;

    private double minValue, maxValue, scale, value = Double.NaN;
    private int numInt;
    private String mUnit;
    private int count = -1;
    private JLabel lbl_Name;
    private JSlider sl_analog;
    private JLabel lbl_count;
    private JLabel lbl_value;
    private NumberFormat nf_count, nf_value;
    private AnalogicOutput anlOut;
    private int valueDigits;
    
    public SimpleAOView()
    {
        this("", -1.0, +1.0, 2, "");
    }

    public SimpleAOView(String name, double minValue, double maxValue,
                        int precision, String mUnit)
    {
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        lbl_Name = new JLabel(name, JLabel.LEFT);
        lbl_Name.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lbl_Name.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
        add(lbl_Name);

        sl_analog = new JSlider();
        sl_analog.setBorder(BorderFactory.createEmptyBorder(4, 0, 1, 3));
        sl_analog.setPaintTicks(true);
        sl_analog.setFocusable(false);
        sl_analog.setPreferredSize(new Dimension(180, 48));
        sl_analog.addChangeListener(this);
        add(sl_analog);

        JPanel pa_value = new JPanel();
        pa_value.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        pa_value.setLayout(new BoxLayout(pa_value, BoxLayout.X_AXIS));
        Font fnt_fixspaced = new Font("Monospaced", Font.BOLD, 12);

        lbl_count = new JLabel();
        lbl_count.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
            BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        lbl_count.setFont(fnt_fixspaced);
        pa_value.add(lbl_count);
    
        pa_value.add(new Box.Filler(new Dimension(0, 0),
                                    new Dimension(4, 0),
                                    new Dimension(16, 0)));

        lbl_value = new JLabel();
        lbl_value.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
            BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        lbl_value.setFont(fnt_fixspaced);
        pa_value.add(lbl_value);

        add(pa_value);

        this.mUnit = mUnit;
        setRange(minValue, maxValue, precision);
    }

    public void setName(String name)
    {
        lbl_Name.setText(name);
    }

    public void setMeasureUnit(String mUnit)
    {
        this.mUnit = mUnit;
        Dictionary dic_labels = sl_analog.getLabelTable();
        dic_labels.put(new Integer((int)(maxValue * scale)),
                       new JLabel(maxValue +" [" + mUnit +"]", JLabel.CENTER));
        sl_analog.setLabelTable(dic_labels);
        lbl_value.setText(formatValue(value));
    }

    public void setRange(double minValue, double maxValue, int precision)
    {
        this.minValue = minValue;
        this.maxValue = maxValue;
        double scale = 500.0 / (maxValue - minValue);
        this.scale = scale;
        long maxNum = (long)Math.max(Math.abs(minValue), Math.abs(maxValue));
        int numInt = Long.toString(maxNum).length();
        if (maxNum == 0)
            precision++;
        if (precision < numInt)
            precision = numInt;
        this.numInt = numInt;
        int numDec = precision - numInt;
        valueDigits = precision + ((numDec > 0) ? 2 : 1);
        nf_count = new DecimalFormat();
        nf_count.setMaximumIntegerDigits(COUNT_DIGITS);
        nf_count.setMinimumIntegerDigits(1);
        nf_count.setGroupingUsed(false);
        nf_value = new DecimalFormat();
        nf_value.setMaximumIntegerDigits(numInt);
        nf_value.setMinimumIntegerDigits(1);
        nf_value.setMaximumFractionDigits(numDec);
        nf_value.setMinimumFractionDigits(numDec);
        nf_value.setGroupingUsed(false);
        double midValue = (maxValue - minValue) / 2 + minValue;
        int i_min = (int)(minValue * scale);
        int i_max = (int)(maxValue * scale);
        int i_mid = (int)(midValue * scale);
        sl_analog.setMinimum(i_min);
        sl_analog.setMaximum(i_max);
        sl_analog.setValue(i_mid);
        sl_analog.setMajorTickSpacing((i_max - i_min) / 2); 
        sl_analog.setMinorTickSpacing((i_max - i_min) / 20);
        Dictionary dic_labels = new Hashtable();
        dic_labels.put(new Integer(i_min),
                       new JLabel(Double.toString(minValue), JLabel.CENTER)); 
        dic_labels.put(new Integer(i_mid),
                       new JLabel(Double.toString(midValue), JLabel.CENTER)); 
        dic_labels.put(new Integer(i_max),
                       new JLabel(maxValue +" [" + mUnit +"]", JLabel.CENTER));
        sl_analog.setLabelTable(dic_labels);
        sl_analog.setPaintLabels(true);
        lbl_count.setText(formatCount(-1));
        lbl_value.setText(formatValue(Double.NaN));
    }
    
    public void setValue(double value, int count)
    {
        this.value = value;
        this.count = count;
        final int i_val = (int)(value * scale);
        final String str_count = formatCount(count);
        final String str_value = formatValue(value);

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    sl_analog.setValue(i_val);
                    lbl_count.setText(str_count);
                    lbl_value.setText(str_value);
                }
            });
    }

    private double getSliderValue()
    {
        int i_val = sl_analog.getValue();
        double value = i_val / scale;
        return(value);
    }

    public void setOutput(AnalogicOutput anlOut)
    {
        this.anlOut = anlOut;
    }

    public void stateChanged(ChangeEvent ce)
    {
        if (ce.getSource() == sl_analog && anlOut != null) {
            value = getSliderValue();
            count = -1;
            lbl_count.setText(formatCount(count));
            lbl_value.setText(formatValue(value));
            if (anlOut != null)
                anlOut.write(value);
        }
    }

    private String formatValue(double number)
    {
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(NumberFormat.INTEGER_FIELD);

        if (number >= -Double.MAX_VALUE && number <= Double.MAX_VALUE ) {
            nf_value.format(number, result, pos);
            int index = pos.getEndIndex();
            if (index < numInt) {
                char[] padding = new char[numInt - index];
                for (int i = 0; i < padding.length; i++)
                    padding[i] = ' ';
                result.insert(0, padding);
            }
            if (number >= 0.0)
                result.insert(0, " ");
        }
        else {
            char[] padding = new char[valueDigits];
            for (int i = 0; i < padding.length; i++)
                padding[i] = ' ';
            result.insert(0, padding);
        }
        result.append(" ");
        result.append(mUnit);

        return(result.toString());
    }

    private String formatCount(int count)
    {
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(NumberFormat.INTEGER_FIELD);

        int index = 0;
        if (count >= 0) {
            nf_count.format(count, result, pos);
            index = pos.getEndIndex();
        }
        if (index < COUNT_DIGITS) {
            char[] padding = new char[COUNT_DIGITS - index];
            for (int i = 0; i < padding.length; i++)
                padding[i] = ' ';
            result.insert(0, padding);
        }
        result.insert(0, "DAC ");

        return(result.toString());
    }
}
