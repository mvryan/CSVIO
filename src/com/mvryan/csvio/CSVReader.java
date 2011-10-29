package com.mvryan.csvio;

import java.io.*;
import java.util.*;

/**
 * 
 * @author Michael Ryan
 *
 */
public class CSVReader implements Closeable {

	private BufferedReader reader;
	private InputStream inputStream;
	
	/**
	 * 
	 * @param filePath
	 * @throws FileNotFoundException
	 */
	public CSVReader(String filePath) throws FileNotFoundException{
		this(new FileInputStream(filePath));
	}
	
	/**
	 * 
	 * @param inputStream
	 */
	public CSVReader(InputStream inputStream){
		this.inputStream = inputStream;
		reader = new BufferedReader(new InputStreamReader(inputStream));
	}
	
	/**
	 * 
	 */
	public void reset(){
		reader = new BufferedReader(new InputStreamReader(inputStream));
	}

	/**
	 * 
	 */
	public void close() throws IOException {
		reader.close();
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<String> nextRow() throws IOException{
		List<String> result = new ArrayList<String>();
		StringBuilder stringBuilder = new StringBuilder();
		String line = reader.readLine();
		if (line == null) {
			return null;
		}
		int state = 1;
		for (char c : line.toCharArray()) {
			switch (state) {
			case 1:
				if (c == ',') {
					result.add(stringBuilder.toString());
					stringBuilder = new StringBuilder();
				} else if (c == '"') {
					state = 2;
				} else {
					stringBuilder.append(c);
				}
				break;
			case 2:
				if (c == '"') {
					state = 3;
				} else {
					stringBuilder.append(c);
				}
				break;
			case 3:
				if (c == ',') {
					result.add(stringBuilder.toString());
					stringBuilder = new StringBuilder();
					state = 1;
				} else if (c == '"') {
					stringBuilder.append('"');
					state = 2;
				} else {
					return result;
				}
				break;
			}
		}
		result.add(stringBuilder.toString());
		return result;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<List<String>> getTable() throws IOException{
		List<List<String>> table = new ArrayList<List<String>>();
		List<String> row;
		while((row = nextRow())!=null){
			table.add(row);
		}
		return table;
	}
}
