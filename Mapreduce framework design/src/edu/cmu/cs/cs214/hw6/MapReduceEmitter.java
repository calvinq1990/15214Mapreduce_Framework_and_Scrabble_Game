package edu.cmu.cs.cs214.hw6;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.cmu.cs.cs214.hw6.util.Log;

public class MapReduceEmitter implements Emitter {
	private File mFile;
	private static final String TAG = "MapReduceEmitter";
	PrintWriter out;
	public MapReduceEmitter(File file) throws IOException {
		this.mFile = file;
		
			
	}
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		mFile = null;
		
		
	}

	@Override
	public void emit(String key, String value) throws IOException {
		// make sure the output file is available
		if (!mFile.exists()) {
			try {
				mFile.createNewFile();
			} catch (IOException e) {
				
				Log.e(TAG, "I/O error while creating file.", e);
			}

		}
		out = new PrintWriter(new FileWriter(mFile, true));
		out.append(key + "\t" + value + "\n");
		out.close();


		
	}

}
