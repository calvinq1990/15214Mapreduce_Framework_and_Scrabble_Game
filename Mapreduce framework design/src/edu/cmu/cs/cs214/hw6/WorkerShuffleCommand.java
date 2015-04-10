package edu.cmu.cs.cs214.hw6;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import edu.cmu.cs.cs214.hw6.util.KeyValuePair;
import edu.cmu.cs.cs214.hw6.util.Log;
import edu.cmu.cs.cs214.hw6.util.WorkerStorage;

/**
 * In shuffle command, each worker should search the keyvalue pair in all
 * the worker by hash function.Then store it into a map.
 * 
 * If there are four worker, each worker should shuffle 4times.
 * 
 *
 */

public class WorkerShuffleCommand extends WorkerCommand {

	private final WorkerInfo mWorker;
	private final int index;
	private final int workerNum;
	private static final String TAG = "WorkerShuffleCommand";

	/*
	 * @param: index: it is the index of the main worker who are willing
	 * 				to reduce. it need to find its key value in all the other 
	 * 				worker
	 * 			workerNum: the number of total workers
	 * 			mWorker: it is the worker who the main worker is searching. The
	 * 				main worker search the intermediate result in mWorker.
	 */
	public WorkerShuffleCommand(int index, int workerNum, WorkerInfo mWorker) {

		this.index = index;
		this.mWorker = mWorker;
		this.workerNum = workerNum;
	}

	@Override
	public void run() {
		Socket socket = getSocket();
		FileInputStream in = null;

		ArrayList<KeyValuePair> pairs = new ArrayList<KeyValuePair>();

		try {
			// Opens a FileInputStream for the specified file, execute the task,
			// and close the input stream once we've calculated the result.

			String mWorkerName = mWorker.getName();
			String immdeateResultDirectory = WorkerStorage
					.getIntermediateResultsDirectory(mWorkerName);

			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			// Opens a FileInputStream for the specified file, execute the task,
			// and close the input stream once we've calculated the result.
			
			File file = new File(immdeateResultDirectory, mWorkerName);
			if (file.exists()) {
				in = new FileInputStream(file);
				Scanner scanner = new Scanner(in);
				scanner.useDelimiter("\\W+");

				while (scanner.hasNext()) {
					String key = scanner.next().trim();
					String value = scanner.next().trim();
					// make sure the shuffle worker get the assigned words
					// itself
					if (key.hashCode() % this.workerNum == index)
						pairs.add(new KeyValuePair(key, value));

				}
				scanner.close();

				in.close();
		
			}
			
			out.writeObject(pairs);
			out.reset();
		} catch (IOException e) {
			Log.e(TAG, "I/O error while executing task.", e);
		}
	}

}
