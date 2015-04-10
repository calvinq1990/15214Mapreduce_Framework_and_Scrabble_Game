package edu.cmu.cs.cs214.hw6;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.cmu.cs.cs214.hw6.util.KeyValuePair;
import edu.cmu.cs.cs214.hw6.util.Log;
import edu.cmu.cs.cs214.hw6.util.WorkerStorage;

public class WorkerReduceCommand extends WorkerCommand {

	private ReduceTask mTask;
	private List<WorkerInfo> workers;
	private WorkerInfo worker;
	private static final String TAG = "WorkerMapCommand";
	private static final int MAX_POOL_SIZE = Runtime.getRuntime()
			.availableProcessors();
	HashMap<String, ArrayList<String>> pairs;

	public WorkerReduceCommand(List<WorkerInfo> workers, ReduceTask mTask,
			WorkerInfo worker) {
		this.worker = worker;
		this.workers = workers;
		this.mTask = mTask;
		pairs = new HashMap<String, ArrayList<String>>();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		/**
		 * shuffle
		 */
		ExecutorService mExecutor = Executors.newFixedThreadPool(Math.min(
				MAX_POOL_SIZE, workers.size()));
		Socket socket = getSocket();

		ArrayList<ShuffleCallable> shuffleCallables = new ArrayList<ShuffleCallable>();
		for (WorkerInfo circulateWorker : workers) {
			shuffleCallables
					.add(new ShuffleCallable(workers.indexOf(worker),
							workers.size(), circulateWorker));
		}

		String workerName = null;

		List<Future<ArrayList<KeyValuePair>>> results = null;
		try {
			results = mExecutor.invokeAll(shuffleCallables);
		} catch (InterruptedException e) {

			Thread.currentThread().interrupt();
		}
		
		/**
		 * handle the shuffle result.
		 * give a list key value pair to store the work's key value
		 * pairs
		 */
		for (int i = 0; i < results.size(); i++) {
			ShuffleCallable sCallable = shuffleCallables.get(i);
			Future<ArrayList<KeyValuePair>> result = results.get(i);

			try {
				workerName = sCallable.getWorker().getName();
				ArrayList<KeyValuePair> kvPairs = result.get();
				for (KeyValuePair kv : kvPairs) {
					String key = kv.getKey();
					String value = kv.getValue();

					if (pairs.containsKey(key))
						pairs.get(key).add(value);
					else {
						ArrayList<String> list = new ArrayList<String>();
						list.add(value);
						pairs.put(key, list);
					}
				}

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {

				String workerHost = sCallable.getWorker().getHost();
				int workerPort = sCallable.getWorker().getPort();
				String info = String.format("[host=%s, port=%d]", workerHost,
						workerPort);
				Log.e(TAG,
						"Warning! Failed to execute task for worker in shuffle: "
								+ info, e.getCause());
			}

			Log.e(TAG, "shuffle of " + workerName + " is done");

		}

		/**
		 * execute reduce task
		 */
		try {
			// Opens a FileInputStream for the specified file, execute the task,
			// and close the input stream once we've calculated the result.
		

			String mWorkerName = this.worker.getName();
			String resultDirectory = WorkerStorage
					.getFinalResultsDirectory(mWorkerName);
			String timeStanmp = new SimpleDateFormat("yyyyMMddhhmm'.txt'").format(new Date());
			Emitter emitter = new MapReduceEmitter(new File(resultDirectory,
					mWorkerName + " " + timeStanmp));
	
			for (String key : pairs.keySet())
				mTask.execute(key, pairs.get(key).iterator(), emitter);

			// Open an ObjectOutputStream to use to communicate with the client
			// that sent this command, and write the result back to the client.
			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			out.writeObject("reduce done" + "\t" + resultDirectory);
		} catch (IOException e) {
			Log.e(TAG, "I/O error while executing reduce task.", e);
		}

		mExecutor.shutdown();
	}

	/**
	 * A command that manages a single client-worker connection to be executed
	 * shuffling asynchronously.
	 *
	 */
	private static class ShuffleCallable implements
			Callable<ArrayList<KeyValuePair>> {

		private final WorkerInfo mWorker;
		private final int index;
		private final int workerNum;
		private final String TAG = "ShuffleCallable";

		public ShuffleCallable(int indexOfWorkingWorker, int totalWorkerNum,
				WorkerInfo circulateWorker) {
			this.index = indexOfWorkingWorker;
			this.mWorker = circulateWorker;
			this.workerNum = totalWorkerNum;
		}

		/**
		 * Returns the {@link WorkerConfig} object that provides information
		 * about the worker that this callable task is responsible for
		 * interacting with.
		 */
		public WorkerInfo getWorker() {
			return mWorker;
		}

		@Override
		public ArrayList<KeyValuePair> call() throws Exception {
			Socket socket = null;
			try {
				// Establish a connection with the worker server.
				socket = new Socket(mWorker.getHost(), mWorker.getPort());

				// Create the ObjectOutputStream and write the WorkerCommand
				// over the network to be read and executed by a WorkerServer.
				ObjectOutputStream out = new ObjectOutputStream(
						socket.getOutputStream());
				out.writeObject(new WorkerShuffleCommand(index, workerNum,
						mWorker));

				ObjectInputStream in = new ObjectInputStream(
						socket.getInputStream());

				// Read and return the worker's final result.
				return (ArrayList<KeyValuePair>) in.readObject();
			} catch (Exception e) {
				// Catch, log, and re-throw the exception. Always make sure you
				// log your exceptions, or else debugging your code will be a
				// nightmare!
				Log.e(TAG,
						"Warning! Received exception while interacting with worker.",
						e);
				throw e;
			} finally {
				try {
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
					// Ignore because we're about to exit anyway.
				}
			}
		}
	}

}
