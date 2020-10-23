package rcptask;

import java.util.Objects;

public class Student {

	private String name;
	private int group;
	private String adress;
	private String city;
	private int result;

	public Student(String name, int group, String adress, String city, int result) {
		super();
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

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
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

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", group=" + group + ", adress=" + adress + ", city=" + city + ", result="
				+ result + "]";
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
