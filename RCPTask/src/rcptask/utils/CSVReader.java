package rcptask.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import rcptask.Student;

public class CSVReader {
	public static Student readStudentListFromCSV(String pathToCsv, String split)
			throws IOException, NumberFormatException, Exception {
		try (BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv))) {
			String row;
			Student student = null;
			boolean flag = true;
			while ((row = csvReader.readLine()) != null) {
				if (flag) {
					flag = false;
					continue;
				}
				String[] data = row.split(split);
				student = new Student(data[0].trim(), Integer.parseInt(data[1].trim()), data[2].trim(),
						data[3].trim(), Integer.parseInt(data[4].trim()));
			}
			return student;
		} catch (IOException | NumberFormatException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static Student readStudentListFromCSV(String pathToCsv)
			throws NumberFormatException, IOException, Exception {
		return readStudentListFromCSV(pathToCsv, ",");
	}
}
