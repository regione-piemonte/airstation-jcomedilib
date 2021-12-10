// ----------------------------------------------------------------------------
// Copyright Regione Piemonte - 2021
// SPDX-License-Identifier: LGPL-2.1
// ----------------------------------------------------------------------------
// Original Author of file: Pierfrancesco Vallosio
// Purpose of file: Java JNI interface for Comedi library (Linux Control and
//   Measurement Device Interface https://www.comedi.org/)
// Change log:
//   2003-10-31: initial version
// ----------------------------------------------------------------------------
// $Id: DriverManager.java,v 1.5 2012/10/15 12:14:05 pfvallosio Exp $
// ----------------------------------------------------------------------------

package it.csi.aria.jcomedilib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DriverManager {

	private static final String MODPROBE_CMD = "modprobe";

	private static final String COMEDI_KERNEL_MODULE_NAME = "comedi";

	private static final String DEV_DIR = "/dev";

	private static final String DEV_NAME = "comedi";

	private static final String BIND_UTIL = "comedi_config";

	private static final String UNBIND_CMD = "-r";

	private static DriverManager instance = null;

	private List<DevBind> list_devfiles = null;

	private DriverManager(List<String> driverOptions) throws ComediException {
		loadKernelModule(COMEDI_KERNEL_MODULE_NAME, driverOptions);
		list_devfiles = getDeviceDescriptors();
		clearAllBindings();
	}

	public static DriverManager getInstance() throws ComediException {
		return (getInstance(null));
	}

	public synchronized static DriverManager getInstance(
			List<String> driverOptions) throws ComediException {
		if (instance == null)
			instance = new DriverManager(driverOptions);
		return (instance);
	}

	public int getMaxDevices() {
		return (list_devfiles.size());
	}

	public void loadDriverModule(String moduleName) throws ComediException {
		loadKernelModule(moduleName);
	}

	public String bindDriver(String driverName) throws ComediException {
		return (bindDriver(driverName, (String) null));
	}

	public synchronized String bindDriver(String driverName, String params)
			throws ComediException {
		DevBind dbfree = null;
		for (DevBind db : list_devfiles) {
			if (db.free) {
				dbfree = db;
				break;
			}
		}
		if (dbfree == null)
			throw new ComediException("No free comedi device files available");
		String strError;
		if (params == null)
			strError = execCommand(BIND_UTIL, dbfree.devName, driverName);
		else
			strError = execCommand(BIND_UTIL, dbfree.devName, driverName,
					params);
		if (strError != null)
			throw new ComediException("Error binding driver " + driverName
					+ " to device " + dbfree.devName + ": " + strError);
		dbfree.free = false;
		return (dbfree.devName);
	}

	public synchronized void unbindDriver(String devName)
			throws ComediException {
		DevBind dbToFree = null;
		for (DevBind db : list_devfiles) {
			if (db.devName.equals(devName)) {
				dbToFree = db;
				break;
			}
		}
		if (dbToFree == null)
			throw new ComediException("Cannot unbind non existent device "
					+ devName);
		if (dbToFree.free)
			throw new ComediException("Device " + devName + " already unbinded");
		doUnbind(devName);
		dbToFree.free = true;
	}

	public synchronized void clearAllBindings() throws ComediException {
		for (DevBind db : list_devfiles) {
			doUnbind(db.devName);
		}
	}

	private List<DevBind> getDeviceDescriptors() {
		List<DevBind> list_dd = new ArrayList<DevBind>();
		String array_dd[] = null;
		File devDir = new File(DEV_DIR);
		if (devDir.exists() && devDir.isDirectory()) {
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (!name.startsWith(DEV_NAME))
						return false;
					String strIndex = name.substring(DEV_NAME.length());
					try {
						Integer.parseInt(strIndex);
						return true;
					} catch (NumberFormatException nfe) {
						return false;
					}
				}
			};
			array_dd = devDir.list(filter);
			if (array_dd != null)
				for (int i = 0; i < array_dd.length; i++)
					list_dd.add(new DevBind(DEV_DIR + "/" + array_dd[i]));
		}
		Collections.sort(list_dd, new Comparator<DevBind>() {
			@Override
			public int compare(DevBind o1, DevBind o2) {
				return o1.compareDevName(o2);
			}
		});
		return (list_dd);
	}

	private void doUnbind(String devName) throws ComediException {
		String errMessage = execCommand(BIND_UTIL, UNBIND_CMD, devName);
		if (errMessage != null)
			throw new ComediException("Error unbinding device " + devName
					+ ": " + errMessage);
	}

	private void loadKernelModule(String name) throws ComediException {
		loadKernelModule(name, null);
	}

	private void loadKernelModule(String name, List<String> options)
			throws ComediException {
		if (name == null)
			throw new ComediException("Null argument not allowed");
		String errMessage;
		String optionsMessage = "";
		if (options == null || options.isEmpty()) {
			errMessage = execCommand(MODPROBE_CMD, name);
		} else {
			optionsMessage = " with option(s)";
			String[] args = new String[2 + options.size()];
			args[0] = MODPROBE_CMD;
			args[1] = name;
			int i = 2;
			for (String option : options) {
				args[i++] = option;
				optionsMessage += " " + option;
			}
			errMessage = execCommand(args);
		}
		if (errMessage != null)
			throw new ComediException("Error loading " + name
					+ " kernel module" + optionsMessage + ": " + errMessage);
	}

	private String execCommand(String... command) throws ComediException {
		String ls = System.getProperty("line.separator");
		ProcessBuilder pb = new ProcessBuilder(command);
		Process p = null;
		BufferedReader br = null;
		try {
			p = pb.start();
			br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			StringBuffer errMessage = new StringBuffer();
			String line;
			boolean firstLine = true;
			while ((line = br.readLine()) != null) {
				if (!firstLine)
					errMessage.append(ls);
				errMessage.append(line);
				firstLine = false;
			}
			if (p.waitFor() == 0)
				return null;
			return errMessage.toString();
		} catch (Exception e) {
			throw new ComediException("Cannot execute " + command[0]
					+ " command", e);
		} finally {
			if (p != null) {
				try {
					p.getOutputStream().close();
				} catch (IOException e) {
				}
				try {
					p.getInputStream().close();
				} catch (IOException e) {
				}
				try {
					if (br != null)
						br.close();
					else
						p.getErrorStream().close();
				} catch (Exception e) {
				}
			}
		}
	}

	private class DevBind {
		String devName;

		boolean free = true;

		DevBind(String devName) {
			this.devName = devName;
		}

		public int compareDevName(Object o) {
			DevBind other = (DevBind) o;
			if (this.devName == null)
				return other.devName == null ? 0 : -1;
			if (other.devName == null)
				return 1;
			int diffLength = this.devName.length() - other.devName.length();
			if (diffLength != 0)
				return diffLength;
			return this.devName.compareTo(other.devName);
		}
	}
}
