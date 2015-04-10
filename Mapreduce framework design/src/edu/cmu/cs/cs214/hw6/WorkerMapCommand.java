package edu.cmu.cs.cs214.hw6;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import edu.cmu.cs.cs214.hw6.util.Log;
import edu.cmu.cs.cs214.hw6.util.WorkerStorage;

public class WorkerMapCommand extends WorkerCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MapTask mTask;
	ArrayList<Partition> mPartitions;
	private static final String TAG = "WorkerMapCommand";
	public WorkerMapCommand(ArrayList<Partition> p, MapTask mTask) {
		this.mPartitions = p;
		this.mTask = mTask;
	}
	
    // Opens a FileInputStream for the specified file, execute the task,
    // and close the input stream once we've calculated the result.
	@Override
	public void run() {
		Socket socket = getSocket();
		FileInputStream in = null;
		String mWorkerName = mPartitions.get(0).getWorkerName();
		String resultDirectory = WorkerStorage
				.getIntermediateResultsDirectory(mWorkerName);
		

		try {
			Emitter emitter = new MapReduceEmitter(new File(resultDirectory, mWorkerName));
            for (Partition part : mPartitions) {
				for (File fileIter : part) {
					in = new FileInputStream(fileIter);
					mTask.execute(in, emitter);
				}
			}

            // Open an ObjectOutputStream to use to communicate with the client
            // that sent this command, and write the result back to the client.
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject("map done");
        } catch (IOException e) {
            Log.e(TAG, "I/O error while executing task.", e);
        }
	}

}
