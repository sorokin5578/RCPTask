package rcptask.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import rcptask.entity.Student;

public class CSVReader {
	private CSVReader() {
	}

	public static Student readStudentFromCSV(String pathToCsv, String split)
			throws IOException, NumberFormatException, Exception {
		try (BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv))) {
			String row;
			Student student = new Student();
			boolean flag = true;
			while ((row = csvReader.readLine()) != null) {
				if (flag) {
					flag = false;
					continue;
				}
				String[] data = row.split(split);
				student.setName(data[0].trim());
				student.setGroup(Integer.parseInt(data[1].trim()));
				student.setAdress(data[2].trim());
				student.setCity(data[3].trim());
				student.setResult(Integer.parseInt(data[4].trim()));
				student.setImgPath("");
				if (data.length == 6) {
					student.setImgPath(data[5].trim());
				}
			}
			return student;
		} catch (IOException | NumberFormatException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public static Student readStudentFromCSV(String pathToCsv) throws NumberFormatException, IOException, Exception {
		return readStudentFromCSV(pathToCsv, ",");
	}
}
