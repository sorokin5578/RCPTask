package rcptask.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jface.dialogs.MessageDialog;

import rcptask.entity.Student;

public class CSVWriter {

	private CSVWriter() {
	}

	public static boolean writeCSVInFile(Student newStudent, Student oldStudent, String folderPath) {
		String groupPath = folderPath + "\\Group " + newStudent.getGroup();
		String filePath = getAbsolutePath(folderPath, newStudent.getGroup(), newStudent.getName());

		if (!newStudent.equals(oldStudent) && (oldStudent.getName() != null || oldStudent.getGroup() != null)) {
			try {
				Files.deleteIfExists(
						Paths.get(getAbsolutePath(folderPath, oldStudent.getGroup(), oldStudent.getName())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File groupDir = new File(groupPath);
		File file = new File(filePath);
		if (!groupDir.exists()) {
			groupDir.mkdir();
		}
		String msg = "Student " + newStudent.getName() + " already exists. Want to change data?";
		if (file.exists() && !MessageDialog.openConfirm(null, "Info", msg)) {
			return false;
		}
		try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(file))) {
			char coma = ',';
			csvWriter.append("Name");
			csvWriter.append(coma);
			csvWriter.append("Group");
			csvWriter.append(coma);
			csvWriter.append("Address");
			csvWriter.append(coma);
			csvWriter.append("City");
			csvWriter.append(coma);
			csvWriter.append("Result");
			csvWriter.append(coma);
			csvWriter.append("Image Path");
			csvWriter.append("\n");
			csvWriter.append(newStudent.getName());
			csvWriter.append(coma);
			csvWriter.append(String.valueOf(newStudent.getGroup()));
			csvWriter.append(coma);
			csvWriter.append(newStudent.getAdress().replace(",", "%"));
			csvWriter.append(coma);
			csvWriter.append(newStudent.getCity());
			csvWriter.append(coma);
			csvWriter.append(String.valueOf(newStudent.getResult()));
			csvWriter.append(coma);
			csvWriter.append(newStudent.getImgPath() == null ? "" : newStudent.getImgPath());
			csvWriter.append("\n");
			MessageDialog.openInformation(null, "Info", "Saving success.\nYour list in " + filePath);
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}

	private static String getAbsolutePath(String folderPath, Integer studentGroup, String studentName) {
		return String.format("%s\\Group %d\\%s.csv", folderPath, studentGroup, studentName);
	}
}
