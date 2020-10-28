package rcptask;

import java.util.Objects;

public class Student {

	private String name;
	private Integer group;
	private String adress;
	private String city;
	private Integer result;
	private String imgPath;
	private String filePath;

	public Student() {
	}
	
	public Student(String name, int group, String adress, String city, int result, String imgPath) {
		this.name = name;
		this.group = group;
		this.adress = adress;
		this.city = city;
		this.result = result;
		this.imgPath = imgPath;
	}
	
	public Student(String name, int group, String adress, String city, int result) {
		this.name = name;
		this.group = group;
		this.adress = adress;
		this.city = city;
		this.result = result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", group=" + group + ", adress=" + adress + ", city=" + city + ", result="
				+ result + "imgPath=" + imgPath+"]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Student) {
			Student student = (Student) obj;
			return getName().equals(student.getName()) 
					&& getCity().equals(student.getCity())
					&& getAdress().equals(student.getAdress()) 
					&& getGroup() == student.getGroup();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, group, adress, city);
	}

}
