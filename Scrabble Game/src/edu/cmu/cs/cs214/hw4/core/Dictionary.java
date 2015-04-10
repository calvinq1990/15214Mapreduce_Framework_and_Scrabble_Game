package edu.cmu.cs.cs214.hw4.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * 
 * use hashset to store the word
 *
 */
public class Dictionary {
	private HashSet<String> dic = new HashSet<String>();
	public Dictionary() {
		
	    try {
	    	BufferedReader br = new BufferedReader(new FileReader("assets/words.txt"));
	        String line = br.readLine();
	        while (line != null) {
	            dic.add(line);
	            line = br.readLine();
	        }
	        br.close();
	    }catch(IOException e){
	    	 e.printStackTrace();
		}
	}
	public boolean lookUp(String word){
	

		if (dic.contains(word)){
			return true;
		}
		else{
			return false;
		}
			
	}

}
