package com.mvryan.csvio;

import java.io.*;
import java.util.*;

public class CSVReader implements Closeable {

	private BufferedReader reader;
	private InputStream inputStream;
	
	public CSVReader(String filePath) throws FileNotFoundException{
		this(new FileInputStream(filePath));
	}
	
	public CSVReader(InputStream inputStream){
		this.inputStream = inputStream;
		reader = new BufferedReader(new InputStreamReader(inputStream));
	}
	
	public void reset(){
		reader = new BufferedReader(new InputStreamReader(inputStream));
	}

	public void close() throws IOException {
		reader.close();
	}
	
	public List<String> nextRow() throws IOException{
		List<String> result = new ArrayList<String>();
		StringBuilder stringBuilder = new StringBuilder();
		String line = reader.readLine();
		if (line == null) {
			return null;
		}
		char[] chars = line.toCharArray();
		int state = 1;
		for (int i = 0; i < chars.length; i++) {
			switch (state) {
			case 1:
				if (chars[i] == ',') {
					result.add(stringBuilder.toString());
					stringBuilder = new StringBuilder();
				} else if (chars[i] == '"') {
					state = 2;
				} else {
					stringBuilder.append(chars[i]);
				}
				break;
			case 2:
				if (chars[i] == '"') {
					state = 3;
				} else {
					stringBuilder.append(chars[i]);
				}
				break;
			case 3:
				if (chars[i] == ',') {
					result.add(stringBuilder.toString());
					stringBuilder = new StringBuilder();
					state = 1;
				} else if (chars[i] == '"') {
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
	
	public List<List<String>> getTable() throws IOException{
		List<List<String>> table = new ArrayList<List<String>>();
		List<String> row;
		while((row = nextRow())!=null){
			table.add(row);
		}
		return table;
	}
}
