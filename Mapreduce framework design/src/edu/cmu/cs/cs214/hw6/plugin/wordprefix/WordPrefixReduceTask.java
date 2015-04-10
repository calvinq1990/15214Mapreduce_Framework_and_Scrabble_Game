package edu.cmu.cs.cs214.hw6.plugin.wordprefix;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import edu.cmu.cs.cs214.hw6.Emitter;
import edu.cmu.cs.cs214.hw6.ReduceTask;

/**
 * The reduce task for a word-prefix map/reduce computation.
 */
public class WordPrefixReduceTask implements ReduceTask {
    private static final long serialVersionUID = 6763871961687287020L;

    @Override
    public void execute(String key, Iterator<String> values, Emitter emitter) throws IOException {
       
    	HashMap<String, Integer> unsorted = new HashMap<String, Integer>();
    	String value;
        while (values.hasNext()) {
            value = values.next();
            if (unsorted.containsKey(value))
            	unsorted.put(value, unsorted.get(value)+1);
            else
            	unsorted.put(value, 1);
        }
        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(unsorted);
        emitter.emit(key, sortedMap.descendingMap().firstKey());
    }

}
