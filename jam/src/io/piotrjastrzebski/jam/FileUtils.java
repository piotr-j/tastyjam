package io.piotrjastrzebski.jam;

import java.io.File;

/**
 * Some utils for Java Files
 *
 * Created by EvilEntity on 26/01/2016.
 */
public class FileUtils {

	public static void deleteDirectory(File file) {
		if (!file.exists() || !file.isDirectory()) return;
		emptyDirectory(file);
		file.delete();
	}

	public static void emptyDirectory (File file) {
		File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0, n = files.length; i < n; i++) {
				if (!files[i].isDirectory())
					files[i].delete();
				else
					deleteDirectory(files[i]);
			}
		}
	}
}
