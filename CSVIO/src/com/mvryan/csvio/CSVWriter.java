package com.mvryan.csvio;

import java.io.*;
import java.util.*;

/**
 * 
 * @author Michael Ryan
 *
 */
public class CSVWriter implements Closeable, Flushable {

	private Writer writer;
	
	/**
	 * 
	 * @param filePath
	 * @throws FileNotFoundException
	 */
	public CSVWriter(String filePath) throws FileNotFoundException{
		this(new FileOutputStream(filePath));
	}
	
	/**
	 * 
	 * @param outputStream
	 */
	public CSVWriter(OutputStream outputStream){
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));
	}
	
	/**
	 * 
	 */
	public void flush() throws IOException{
		writer.flush();
	}
	
	/**
	 * 
	 */
	public void close() throws IOException{
		writer.close();
	}
	
	/**
	 * 
	 * @param row
	 * @throws IOException
	 */
	public void writeRow(List<String> row) throws IOException{
		StringBuilder rowBuilder = new StringBuilder();
		boolean first = true;
		for (String entry : row) {
			StringBuilder elementBuilder = new StringBuilder();
			boolean quoteFlag = false;
			for (char c : entry.toCharArray()) {
				if (c == '"') {
					quoteFlag = true;
					elementBuilder.append("\"\"");
				} else if (c == ',') {
					quoteFlag = true;
					elementBuilder.append(',');
				} else {
					elementBuilder.append(c);
				}
			}
			if(!first){
				rowBuilder.append(',');
			}
			if(quoteFlag){
				rowBuilder.append('"');
			}
			rowBuilder.append(elementBuilder);
			if(quoteFlag){
				rowBuilder.append('"');
			}
			first = false;
		}
		rowBuilder.append("\n");
		writer.write(rowBuilder.toString());
	}
	
	/**
	 * 
	 * @param table
	 * @throws IOException
	 */
	public void writeTable(List<List<String>>table) throws IOException{
		for(List<String> row : table){
			writeRow(row);
		}
	}

}


