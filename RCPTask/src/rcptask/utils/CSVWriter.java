package rcptask.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;

import rcptask.Student;

public class CSVWriter {
	public static boolean writeCSVInFile(Student student) {
		String groupPath = "C:\\Users\\Illia\\git\\RCPTask\\RCPTask\\Folder\\Group " + student.getGroup();
		String filePath = "C:\\Users\\Illia\\git\\RCPTask\\RCPTask\\Folder\\Group " + student.getGroup() + "\\"
				+ student.getName() + ".csv";

		File groupDir = new File(groupPath);
		if (!groupDir.exists()) {
			groupDir.mkdir();
		}
		try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filePath))) {
			char coma = ',';
			csvWriter.append("Name");
			csvWriter.append(coma);
			csvWriter.append("Group");
			csvWriter.append(coma);
			csvWriter.append("Adress");
			csvWriter.append(coma);
			csvWriter.append("City");
			csvWriter.append(coma);
			csvWriter.append("Result");
			csvWriter.append(coma);
			csvWriter.append("Image Path");
			csvWriter.append("\n");
			csvWriter.append(student.getName());
			csvWriter.append(coma);
			csvWriter.append(String.valueOf(student.getGroup()));
			csvWriter.append(coma);
			csvWriter.append(student.getAdress());
			csvWriter.append(coma);
			csvWriter.append(student.getCity());
			csvWriter.append(coma);
			csvWriter.append(String.valueOf(student.getResult()));
			csvWriter.append(coma);
			csvWriter.append(student.getImgPath()==null?"":student.getImgPath());
			csvWriter.append("\n");
			MessageDialog.openInformation(null, "Info", "Saving success.\nYour list in " + filePath);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
