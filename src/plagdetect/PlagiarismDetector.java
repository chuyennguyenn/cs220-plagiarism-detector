package plagdetect;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class PlagiarismDetector implements IPlagiarismDetector {
	
	int n;
	Set<String> uniqueNgram = new HashSet<String>();
	Map<String, Set<String>> finalNgram = new HashMap<String, Set<String>>();
	Map<String, Map<String, Integer>> finalList = new HashMap<String, Map<String, Integer>>();
	Map<String, Integer> newMap = new HashMap<String, Integer>();
	Collection<String> pair = new HashSet<String>();
	
	
	public PlagiarismDetector(int n) {
		n = n;
	}
	
	@Override
	public int getN() {
		return n;
	}

	@Override
	public Collection<String> getFilenames() {
		return finalList.keySet();
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		return finalNgram.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		return uniqueNgram.size();
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		return finalList;
	}

	@Override
	public void readFile(File file) throws IOException {
		Set<String> uniqueNgram = new HashSet<String>();
		Map<String, Set<String>> finalNgram = new HashMap<String, Set<String>>();
		Map<String, Map<String, Integer>> finalList = new HashMap<String, Map<String, Integer>>();
		Map<String, Integer> newMap = new HashMap<String, Integer>();
		
		StringBuilder result = new StringBuilder();
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			result.append(scanner.nextLine());
			result.append(" ");
		}
		scanner.close();
		
		String result3 = result.toString();
		
		String a;
		int count=1;
	    for(int i = 0; i < result3.length() - n + 1; i++) {
	    	a=result3.substring(i, i+n);
	    	uniqueNgram.add(a);
	    	if (uniqueNgram.contains(a)) {
	    		count++;
	    	}
	    	finalNgram.put(file.getName(), uniqueNgram);
	    	newMap.put(a , count);	
	    }
	    finalList.put(file.getName(), newMap);
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		int count =0;
		for (String x: getNgramsInFile(file1)) {
			for (String y: getNgramsInFile(file2)) {
				if (x.equals(y)) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		Collection<String> pair = new HashSet<String>();
		for (Map.Entry p1: finalList.entrySet()) {
			String o1 = finalList.get(p1).toString();
			for (Map.Entry p2: finalList.entrySet()) {
				String o2 = finalList.get(p2).toString();
				int i= (int) p2.getValue();
				if (i>=minNgrams) {
					if (o1.compareTo(o2)<0) {
						String s1 = p1 + " " + p2 + " " + String.valueOf(i);
						pair.add(s1);
					}
					String s2 = p2 + " " + p1 + " " + String.valueOf(i);
					pair.add(s2);
				}
			}
		
		}
		return pair;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		for (File f : dir.listFiles()) {
			readFile(f);
		}
	}
}
