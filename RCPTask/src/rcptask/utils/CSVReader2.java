package rcptask.utils;

import java.io.IOException;

import my.extension.point.CSVReaderEP;
import rcptask.entity.Student;

public class CSVReader2 extends CSVReaderEP {
	public static Student readStudent(String pathToCsv) throws NumberFormatException, IOException, Exception {
		return readStudent(pathToCsv, ",");
	}

	public static Student readStudent(String pathToCsv, String split)
			throws NumberFormatException, IOException, Exception {
		String[] data = readStudentFromCSV(pathToCsv, split);
		Student student = new Student();
		student.setName(data[0].trim());
		student.setGroup(Integer.parseInt(data[1].trim()));
		student.setAdress(data[2].trim().replace("%", ","));
		student.setCity(data[3].trim());
		student.setResult(Integer.parseInt(data[4].trim()));
		student.setImgPath("");
		if (data.length == 6) {
			student.setImgPath(data[5].trim());
		}
		return student;
	}
}