package edu.cmu.cs.cs214.hw6.plugin.wordprefix;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import edu.cmu.cs.cs214.hw6.Emitter;
import edu.cmu.cs.cs214.hw6.MapTask;

/**
 * The map task for a word-prefix map/reduce computation.
 */
public class WordPrefixMapTask implements MapTask {
	private static final long serialVersionUID = 3046495241158633404L;

	@Override
	public void execute(InputStream in, Emitter emitter) throws IOException {
		// TODO: Implement this!
		String prefix;
		Scanner scanner = new Scanner(in);
		scanner.useDelimiter("\\W+");
		while (scanner.hasNext()) {
			String key = scanner.next().trim().toLowerCase();
			for (int i = 1; i <= key.length(); i++) {
				prefix = key.substring(0, i);
				emitter.emit(prefix, key);
			}
		}
		scanner.close();
	}

}
