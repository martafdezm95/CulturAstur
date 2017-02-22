package es.uniovi.imovil.user.courses;

public class Course {
	
	private String mName;
	private String mTeacher;
	
	public Course(String name, String teacher) {
		
		if (name == null || teacher == null || name.isEmpty() || teacher.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		mName = name;
		mTeacher = teacher;
	}

	public String getName() {
		
		return mName;
	}

	public String getTeacher() {
		
		return mTeacher;
	}	
}
