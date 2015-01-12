package com.radioflex.ide.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.eclipse.core.runtime.Status;

import com.radioflex.ide.Activator;
import com.radioflex.ide.Constants;
import com.radioflex.ide.Messages;

public class ProgramExecuter {
	private ProgramExecuter() {
		// Must not be instantiated.
	}

	/**
	 * Execute a program.
	 * 
	 * @param execFile
	 *            Path to executing file.
	 * @param parameters
	 *            for executing file.
	 * @param paramFile
	 *            File-parms executing file.
	 * @param execDirectory
	 *            Executing-directory for executing file.
	 * @param wait
	 *            If true then wait process for application terminate.
	 * @param stdout
	 *            Enable/Disable standard output.
	 * @param verbose
	 *            More information of execute-process.
	 *
	 * @return stdout Standard output/error from executing file.
	 */
	public static String exec(String execFile, String parameters,
			String paramFile, String execDirectory, boolean wait,
			boolean stdout, boolean verbose) {
		String[] item = new String[1];
		item[0] = paramFile;

		return exec(execFile, parameters, item, execDirectory, wait, stdout,
				verbose);
	}

	/**
	 * Execute a program.
	 * 
	 * @param execFile
	 *            Path to executing file.
	 * @param parameters
	 *            for executing file.
	 * @param paramFiles
	 *            File-parms executing file.
	 * @param execDirectory
	 *            Executing-directory for executing file.
	 * @param wait
	 *            If true then wait process for application terminate.
	 * @param stdout
	 *            Enable/Disable standard output.
	 * @param verbose
	 *            More information of execute-process.
	 *
	 * @return stdout Standard output/error from executing file.
	 */
	public static String exec(String execFile, String parameters,
			String[] paramFiles, String execDirectory, boolean wait,
			boolean stdout, boolean verbose) {
		if (!fileExists(execFile))
			return "File not found!";

		String dir = execDirectory;
		String execStr = execFile;

		String fileparams = "";
		String fileparamsname = "";
		int pos = 0;

		if (dir.length() > 0) {
			pos = dir.lastIndexOf(System.getProperty("file.separator"));

			if (pos == (dir.length() - System.getProperty("file.separator")
					.length()))
				dir = dir.substring(0, pos);
		}

		for (int i = 0; i < paramFiles.length; i++) {
			if ((fileparams.length() > 0)
					&& (fileparams.charAt(fileparams.length()) != ' '))
				fileparams += " ";
			if ((fileparamsname.length() > 0)
					&& (fileparamsname.charAt(fileparamsname.length()) != ' '))
				fileparamsname += " ";
			if (fileExists(paramFiles[i]))
				fileparams += paramFiles[i];

			pos = paramFiles[i].lastIndexOf(System
					.getProperty("file.separator"));

			if (pos > 1) {
				paramFiles[i] = paramFiles[i].substring(pos + 1,
						paramFiles[i].length());
			}

			pos = paramFiles[i].lastIndexOf(".");

			if (pos > 0)
				fileparamsname += paramFiles[i].substring(0, pos);
			else
				fileparamsname += paramFiles[i];
		}

		if (parameters.toLowerCase().indexOf("{workdir}") > -1)
			parameters = replace(
					parameters,
					"{workdir}",
					dir.substring(
							0,
							dir.toLowerCase().lastIndexOf(
									System.getProperty("file.separator"))),
					true);

		if (parameters.toLowerCase().indexOf("{outputname}") > -1)
			parameters = replace(parameters, "{outputname}", fileparamsname,
					true) + ".hex";

		if (parameters.toLowerCase().indexOf("{srcfile}") > -1) {
			parameters = replace(parameters, "{srcfile}", fileparams, true);

			if (parameters.length() > 0)
				execStr = execStr + " " + parameters;
		} else {
			if (parameters.length() > 0)
				execStr = execStr + " " + parameters;

			if (fileparams.length() > 0)
				execStr = execStr + " " + fileparams;
		}

		String result = "";

		if (verbose) {
			result = Messages.RUN + ": " + execStr + "\n";

			if (dir.length() > 0)
				result += Messages.FORM + ": " + dir + "\n";
			result += "\n";
		}

		result += execFile(execStr, dir, wait, stdout);

		return result;
	}

	/**
	 * Execute a program
	 * @param fileName Path to executing file.
	 * @param directory Execute-Directory for executing file.
	 * @param wait If true then wait process for application terminate.
	 * @param stdout Enable/Disable STDOUT output.
	 * @return STDOUT from executing file. 
	 */
	private static String execFile(String fileName, String directory,
			boolean wait, boolean stdout) {
		String line;
		StringBuffer text = null;
		InputStream inputstream = null;
		BufferedReader bufferedreader = null;
		Process process = null;

		try {
			if (directory.length() < 1)
				process = Runtime.getRuntime().exec(fileName);
			else {
				Map<String, String> envs = System.getenv();
				String[] execEnvs = new String[envs.size()];

				int i = 0;

				for (String env : envs.keySet()) {
					execEnvs[i] = new String(env + "=" + envs.get(env));
					i++;
				}

				process = Runtime.getRuntime().exec(fileName, execEnvs,
						new File(directory));
			}

			if (stdout) {
				text = new StringBuffer("");

				inputstream = process.getErrorStream();

				bufferedreader = new BufferedReader(new InputStreamReader(
						inputstream));

				while ((line = bufferedreader.readLine()) != null) {
					text.append(line);

					if ((line.length() < 1)
							|| ((line.length() > 0) && (line.charAt(line
									.length() - 1) != '\n')))
						text.append("\n");
				}
			}

			if (wait)
				process.waitFor();
			if (stdout) {
				inputstream.close();
				bufferedreader.close();
			}
		} catch (Exception e) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(Status.ERROR, Constants.PLUGIN_ID,
							Status.OK, Messages.LAUNCH_ERROR, e));
		}

		if (text != null)
			return text.toString();
		return "";
	}

	/**
	 * Simple Search-Replace-Function. Need because String.replaceAll destroy file path.
	 * @param text Input text
	 * @param search Search Text
	 * @param replace Replace text
	 * @param insensitive If true, ignore case-sensitive.
	 * @return Input text with replaced text.
	 */
	private static String replace(String text, String search, String replace,
			boolean insensitive) {
		String result = text;
		int pos = 0;
		int slen = search.length();
		int rlen = replace.length();
		int oldpos = 0;

		if (slen > 0) {
			if (insensitive) {
				pos = result.toLowerCase().indexOf(search);

				while (pos > -1) {
					result = result.substring(0, pos) + replace
							+ result.substring(pos + slen, result.length());
					oldpos = pos;
					pos = result.toLowerCase().indexOf(search, oldpos + rlen);
				}
			} else {
				pos = result.indexOf(search);

				while (pos > -1) {
					result = result.substring(0, pos) + replace
							+ result.substring(pos + slen, result.length());
					oldpos = pos;
					pos = result.indexOf(search, oldpos + rlen);
				}
			}
		}
		return result;
	}

	/**
	 * Detect if file exists.
	 * @param fe Filename(with path)
	 * @return Result true if File exists.
 	 */
	private static boolean fileExists(String fe) {
		if (fe.length() < 1)
			return false;
		File f = new File(fe);
		return f.exists();
	}
}
