package org.radioflex.ide.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class ASMTranslator {
	public static void convert(Reader reader, Writer writer) throws IOException {
		BufferedReader lines = new BufferedReader(reader);
		String title = String.valueOf(lines.readLine());
		String line;
		writer.write(title);
		while (null != (line = lines.readLine())) {
			writer.write(line);
			writer.write('\n');
		}

		writer.flush();
	}
}
