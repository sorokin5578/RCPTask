package my.extension.point;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReaderEP {

	public static String[] readStudentFromCSV(String pathToCsv, String split)
			throws IOException, NumberFormatException, Exception {
		try (BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv))) {
			String row;
			boolean flag = true;
			String[] data = null;
			while ((row = csvReader.readLine()) != null) {
				if (flag) {
					flag = false;
					continue;
				}
				data = row.split(split);
			}
			return data;
		} catch (IOException | NumberFormatException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
