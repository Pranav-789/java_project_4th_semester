import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class CourseData{
    String courseId;
    String courseName;
    String facultyUsername;

    CourseData(String courseId, String courseName, String facultyUsername){
        this.courseId = courseId;
        this.courseName = courseName;
        this.facultyUsername = facultyUsername;
    }

    public String toFileString(){
        return courseId + "," + courseName + "," + facultyUsername;
    }
}

class CourseService{

    public static final String FILE = "Courses.txt";

    static void addCourse(CourseData course){
        try(FileWriter fw = new FileWriter(FILE, true)){
            fw.write(course.toFileString() + "\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static CourseData[] getAllCourses(){
        try {
            File file = new File(FILE);
            if (!file.exists()) {
                return new CourseData[0];
            }
            Scanner sc = new Scanner(file);
            List<CourseData> allCourseData = new ArrayList<>();

            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] data = line.split(",");
                if(data.length < 3)
                    continue;
                String courseId = data[0];
                String courseName = data[1];
                String facultyUsername = data[2];
                CourseData course = new CourseData(courseId, courseName, facultyUsername);
                allCourseData.add(course);
            }
            sc.close();
            return allCourseData.toArray(new CourseData[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CourseData[0];
    }
}

public class Course{
    public static void main(String[] args) {
        
    }
}
