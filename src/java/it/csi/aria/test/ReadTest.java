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
// $Id:$
// ----------------------------------------------------------------------------
package it.csi.aria.test;

import it.csi.aria.jcomedilib.Comedi;
import it.csi.aria.jcomedilib.ComediChannel;
import it.csi.aria.jcomedilib.ComediDevice;
import it.csi.aria.jcomedilib.ComediException;
import it.csi.aria.jcomedilib.ComediRange;
import it.csi.aria.jcomedilib.ComediSubdevice;
import it.csi.aria.jcomedilib.DriverManager;
import it.csi.aria.jcomedilib.LSamplT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadTest {

	private static final int AI_SET_TIME_NS = 15000;

	public ReadTest(String moduleName, String driverName, String driverParams,
			int analogReference) throws ComediException {
		Comedi.init();
		List<String> driverOptions = new ArrayList<String>();
		driverOptions.add("comedi_autoconfig=0");
		DriverManager dm = DriverManager.getInstance(driverOptions);
		dm.loadDriverModule(moduleName);
		ComediDevice comediDevice = new ComediDevice(dm.bindDriver(driverName,
				driverParams));
		System.out.println("Scheda rilevata:");
		System.out.println(comediDevice.toString(true));
		ComediSubdevice[] subdevs = comediDevice.getSubdevices();
		for (int i = 0; i < subdevs.length; i++) {
			ComediSubdevice subdev = subdevs[i];
			if (subdev.getType() == Comedi.SUBD_AI) {
				ComediChannel[] channels = subdev.getChannels();
				System.out.println("Rilevati " + channels.length
						+ " ingressi analogici");
				for (int chan = 0; chan < channels.length; chan++) {
					ComediChannel channel = channels[chan];
					int range = channel.findRange(Comedi.UNIT_volt, -10, +10);
					ComediRange cRange = channel.getRangeInfo(range);
					channel.setActiveRange(range);
					channel.setAnalogRef(analogReference);
					LSamplT data = new LSamplT(0);
					channel.dataReadDelayed(data, AI_SET_TIME_NS);
					double physValue = Comedi.to_phys(data, cRange, channel
							.getMaxdata());
					System.out.println("canale " + chan + ": " + physValue
							+ "[V]");
				}
			}
		}
	}

	public static void main(String[] args) throws ComediException {
		String moduleName;
		String driverName;
		String driverParams;
		int analogReference;
		try {
			Map<String, String> argMap = new HashMap<String, String>();
			for (int i = 0; i < args.length; i++) {
				String[] keyVal = args[i].split("=", 2);
				argMap.put(keyVal[0], keyVal[1]);
			}
			moduleName = argMap.get("module");
			driverName = argMap.get("driver");
			driverParams = argMap.get("driverParams");
			analogReference = Integer.parseInt(argMap.get("analogRef"));
		} catch (Exception e) {
			System.out.println("Argomenti: module=nome driver=nome driverParams=parametri "
					+ "analogRef=0(terra),1(comune),2(differenziale)");
			return;
		}
		new ReadTest(moduleName, driverName, driverParams, analogReference);
	}

}
