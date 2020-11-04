package my.extension.point;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * @author Illia
 * @version 1.3
 * 
 * Class for read CSV files
 */
public class CSVReaderEP {
	
	private CSVReaderEP() {}

	/**
	 * Reading the csv file at the specified path with the passed delimiter
	 * CSV file format:
	 * The file can contain various separators.
	 * first line - header: Name, Group, Address, City, Result, Image Path
	 * second line: one line with data.
	 * @param CSV file path
	 * @param csv file separator character
	 * @return returns array of strings containing csv file values
	 */
	public static String[] readStudentFromCSV(String pathToCsv, String split) throws FileNotFoundException, IOException {
		try (BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv))) {
			String row;
			String[] data = null;
			
			//title skip
			csvReader.readLine();
			
			if((row=csvReader.readLine())!=null) {
				data = row.split(split);
			}
			
			return data;
		}
	}
	
}
