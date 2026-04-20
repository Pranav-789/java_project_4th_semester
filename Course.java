import java.util.List;
import java.io.File;
import java.io.FileWriter;

class CourseData implements FileSerializable {
    String courseId;
    String courseName;
    String facultyUsername;

    CourseData(String courseId, String courseName, String facultyUsername){
        this.courseId = courseId;
        this.courseName = courseName;
        this.facultyUsername = facultyUsername;
    }

    @Override
    public String toFileString(){
        return courseId + "," + courseName + "," + facultyUsername;
    }
}

class CourseService extends AbstractFileService<CourseData> {
    public static final String FILE = "Courses.txt";

    public CourseService() {
        super(FILE);
    }

    @Override
    protected CourseData parseLine(String[] data) {
        if(data.length < 3) return null;
        return new CourseData(data[0], data[1], data[2]);
    }

    public boolean updateCourseFaculty(String courseId, String newFacultyUsername) {
        boolean updated = false;
        List<CourseData> courses = getAll();
        
        for (CourseData c : courses) {
            if (c.courseId.equals(courseId)) {
                c.facultyUsername = newFacultyUsername;
                updated = true;
            }
        }
        
        if (updated) {
            try {
                File file = new File(filePath);
                FileWriter fw = new FileWriter(file, false);
                for (CourseData c : courses) {
                    fw.write(c.toFileString() + "\n");
                }
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return updated;
    }
}

public class Course{
    public static void main(String[] args) {
        
    }
}
