package edu.cmu.cs.cs214.hw6;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import edu.cmu.cs.cs214.hw6.util.Log;
import edu.cmu.cs.cs214.hw6.util.StaffUtils;
import edu.cmu.cs.cs214.hw6.util.WorkerStorage;

/**
 * description:
 * In the mapper:
 * 	find out the partitions and their relative 
 * 	worker then assign the each partition to a worker( in this process, 
 * 	I use the partition name in to classify). After matching new a partition
 * 	Last execute the map task and master waits all the worker to finish tasks.
 * 
 * In the reducer:
 * 	It includes the shuffle and reduce. 
 *  Firstly, each reduce worker search the keys handle in itself by hash
 *  function. A list of keyvaluepair will be returned to each reducer.
 *  Secondly, the worker execute reducing.
 *  Note that: the shuffling is in reduce command. Each reducing including 
 *  			several shuffling.
 */

/**
 * This class represents the "master server" in the distributed map/reduce
 * framework. The {@link MasterServer} is in charge of managing the entire
 * map/reduce computation from beginning to end. The {@link MasterServer}
 * listens for incoming client connections on a distinct host/port address, and
 * is passed an array of {@link WorkerInfo} objects when it is first initialized
 * that provides it with necessary information about each of the available
 * workers in the system (i.e. each worker's name, host address, port number,
 * and the set of {@link Partition}s it stores). A single map/reduce computation
 * managed by the {@link MasterServer} will typically behave as follows:
 *
 * <ol>
 * <li>Wait for the client to submit a map/reduce task.</li>
 * <li>Distribute the {@link MapTask} across a set of "map-workers" and wait for
 * all map-workers to complete.</li>
 * <li>Distribute the {@link ReduceTask} across a set of "reduce-workers" and
 * wait for all reduce-workers to complete.</li>
 * <li>Write the locations of the final results files back to the client.</li>
 * </ol>
 */
public class MasterServer extends Thread {
	private final int mPort;
	private final List<WorkerInfo> mWorkers;
	private static final String TAG = "Master";

	/** Create at most one thread per available processor on this machine. */
	private static final int MAX_POOL_SIZE = Runtime.getRuntime()
			.availableProcessors();

	private final ExecutorService mExecutor;

	/**
	 * The {@link MasterServer} constructor.
	 *
	 * @param masterPort
	 *            The port to listen on.
	 * @param workers
	 *            Information about each of the available workers in the system.
	 */
	public MasterServer(int masterPort, List<WorkerInfo> workers) {
		int numThreads = Math.min(MAX_POOL_SIZE, workers.size());
		mExecutor = Executors.newFixedThreadPool(numThreads);
		mPort = masterPort;
		mWorkers = workers;
	}

	@Override
	public void run() {
		
		Random rand = new Random();
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(mPort);
		} catch (IOException e) {
			Log.e(TAG, "Could not open server socket on port " + mPort + ".", e);
			return;
		}

		Log.i(TAG, "Listening for incoming commands on port " + mPort + ".");

		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				ObjectOutputStream out = new ObjectOutputStream(
						clientSocket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(
						clientSocket.getInputStream());
				try {
					MapTask mapTask = (MapTask) in.readObject();
					ReduceTask reduceTask = (ReduceTask) in.readObject();

					/*
					 * Map
					 */

					// find each partition's workers by format string
					HashMap<String, ArrayList<WorkerInfo>> partitionMap = new HashMap<String, ArrayList<WorkerInfo>>();
					for (WorkerInfo worker : mWorkers) {
						List<Partition> partitions = worker.getPartitions();
						for (Partition partition : partitions) {

							if (partitionMap.containsKey(partition
									.getPartitionName()))
								partitionMap.get(partition.getPartitionName())
										.add(worker);
							else {
								ArrayList<WorkerInfo> partitionWorkers = new ArrayList<WorkerInfo>();
								partitionWorkers.add(worker);
								partitionMap.put(partition.getPartitionName(),
										partitionWorkers);
							}

						}
					}

					// give map callables, to let the worker map concurrently.
					ArrayList<MapCallable> mapCallables = new ArrayList<MapCallable>();
					HashMap<WorkerInfo, ArrayList<Partition>> workerPartitions = new HashMap<WorkerInfo, ArrayList<Partition>>();
					int randWorker;
					WorkerInfo worker;
					Partition partitionP;
					for (String partition : partitionMap.keySet()) {
						randWorker = rand.nextInt(partitionMap.get(partition)
								.size());
						worker = partitionMap.get(partition).get(randWorker);
						partitionP = new Partition(partition, worker.getName());
						ArrayList<Partition> list;
						if (workerPartitions.containsKey(worker)) {
							list = workerPartitions.get(worker);
							list.add(partitionP);
							workerPartitions.put(worker, list);
						} else {
							list = new ArrayList<Partition>();
							list.add(partitionP);
							workerPartitions.put(worker, list);
						}

					}
					for (WorkerInfo aWorker : workerPartitions.keySet()) {
						mapCallables.add(new MapCallable(workerPartitions
								.get(aWorker), mapTask, aWorker));
					}

					// handle the result of mapper
					List<Future<String>> results = null;
					try {

						results = mExecutor.invokeAll(mapCallables);
					} catch (InterruptedException e) {

						Thread.currentThread().interrupt();
					}

					String workerName = null;
					String status = null;
					for (int i = 0; i < results.size(); i++) {
						MapCallable callable = mapCallables.get(i);
						Future<String> result = results.get(i);

						try {
							workerName = callable.getWorker().getName();
							status = result.get();

						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						} catch (ExecutionException e) {

							String workerHost = callable.getWorker().getHost();
							int workerPort = callable.getWorker().getPort();
							String info = String.format("[host=%s, port=%d]",
									workerHost, workerPort);
							Log.e(TAG,
									"Warning! Failed to execute task for worker: "
											+ info, e.getCause());
						}
						Log.i(TAG, "mapping of " + workerName + " is " + status);
					}

					/**
					 * reduce(including shuffle)
					 * 
					 * invokeAll the reduceCallables to all the worker execute reduce job
					 * before reducing, it should shuffle first to get the key value from 
					 * intermediate results of all the workers. 
					 */
					ArrayList<ReduceCallable> reduceCallables = new ArrayList<ReduceCallable>();
					for (WorkerInfo rWorker : mWorkers)
						reduceCallables.add(new ReduceCallable(mWorkers,
								reduceTask, rWorker));

					// handle reduce result
					results = null;
					try {
						results = mExecutor.invokeAll(reduceCallables);
					} catch (InterruptedException e) {

						Thread.currentThread().interrupt();
					}

					workerName = null;
					status = null;
					ArrayList<String> finalResults = new ArrayList<String>();
					for (int i = 0; i < results.size(); i++) {
						ReduceCallable rCallable = reduceCallables.get(i);
						Future<String> result = results.get(i);

						try {
							workerName = rCallable.getWorker().getName();
							status = result.get();

						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						} catch (ExecutionException e) {

							String workerHost = rCallable.getWorker().getHost();
							int workerPort = rCallable.getWorker().getPort();
							String info = String.format("[host=%s, port=%d]",
									workerHost, workerPort);
							Log.e(TAG,
									"Warning! Failed to execute reduce task for worker: "
											+ info, e.getCause());
						}
						String[] parts = status.split("\t");
						finalResults.add(parts[1]);
						Log.i(TAG, "Reducing of " + workerName + " is "
								+ parts[0]);

					}
					out.writeObject(finalResults);
					
					//Clear the intermediate storage file
					clearStorage(mWorkers);
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					Log.i(TAG, "Listening for incoming commands from client"
							+ ".");
				}

			} catch (IOException e) {
				Log.e(TAG, "Error while listening for incoming connections.", e);
				break;
			}
		}

		Log.i(TAG, "Shutting down...");

		try {
			serverSocket.close();
		} catch (IOException e) {
			// Ignore because we're about to exit anyway.
		} finally {

			mExecutor.shutdown();
		}

	}

	private void clearStorage(List<WorkerInfo> mWorkers) {
		for (WorkerInfo cleanWorker : mWorkers) {

			String mWorkerName = cleanWorker.getName();
			String immdeateResultDirectory = WorkerStorage
					.getIntermediateResultsDirectory(mWorkerName);

			File file = new File(immdeateResultDirectory, mWorkerName);
			if (file.exists())
				file.delete();
		}
	}

	/**
	 * A command that manages a single client-worker connection to be executed
	 * reducing asynchronously.
	 * 
	 * execute the reduce command here.
	 *
	 */

	private static class ReduceCallable implements Callable<String> {
		private final ReduceTask mTask;
		private final WorkerInfo mWorker;
		private final List<WorkerInfo> mWorkers;
		private final String TAG = "ReduceCallable";

		public ReduceCallable(List<WorkerInfo> workers, ReduceTask task,
				WorkerInfo worker) {
			mTask = task;
			mWorker = worker;
			this.mWorkers = workers;
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
		public String call() throws Exception {
			Socket socket = null;
			try {
				// Establish a connection with the worker server.
				socket = new Socket(mWorker.getHost(), mWorker.getPort());

				// Create the ObjectOutputStream and write the WorkerCommand
				// over the network to be read and executed by a WorkerServer.
				ObjectOutputStream out = new ObjectOutputStream(
						socket.getOutputStream());
				out.writeObject(new WorkerReduceCommand(mWorkers, mTask,
						mWorker));

				ObjectInputStream in = new ObjectInputStream(
						socket.getInputStream());

				// Read and return the worker's final result.
				return (String) in.readObject();
			} catch (Exception e) {
				// Catch, log, and re-throw the exception. Always make sure you
				// log your exceptions, or else debugging your code will be a
				// nightmare!
				Log.e(TAG,
						"Warning! Received exception"
						+ " while interacting with worker.",e);
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

	/**
	 * A command that manages a single client-worker connection to be executed
	 * mapping asynchronously.
	 * 
	 * execute the map command here.
	 *
	 */

	private static class MapCallable implements Callable<String> {
		private final MapTask mTask;
		private final WorkerInfo mWorker;
		private final ArrayList<Partition> mPartitions;
		private final String TAG = "MapCallable";

		public MapCallable(ArrayList<Partition> partitions, MapTask task,
				WorkerInfo worker) {
			mTask = task;
			mWorker = worker;
			mPartitions = partitions;
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
		public String call() throws Exception {
			Socket socket = null;
			try {
				// Establish a connection with the worker server.
				socket = new Socket(mWorker.getHost(), mWorker.getPort());

				// Create the ObjectOutputStream and write the WorkerCommand
				// over the network to be read and executed by a WorkerServer.
				ObjectOutputStream out = new ObjectOutputStream(
						socket.getOutputStream());
				out.writeObject(new WorkerMapCommand(mPartitions, mTask));

				ObjectInputStream in = new ObjectInputStream(
						socket.getInputStream());

				// Read and return the worker's final result.
				return (String) in.readObject();
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

	/********************************************************************/
	/***************** STAFF CODE BELOW. DO NOT MODIFY. *****************/
	/********************************************************************/

	/**
	 * Starts the master server on a distinct port. Information about each
	 * available worker in the distributed system is parsed and passed as an
	 * argument to the {@link MasterServer} constructor. This information can be
	 * either specified via command line arguments or via system properties
	 * specified in the <code>master.properties</code> and
	 * <code>workers.properties</code> file (if no command line arguments are
	 * specified).
	 */
	public static void main(String[] args) {
		StaffUtils.makeMasterServer(args).start();
	}

}
