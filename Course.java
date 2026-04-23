import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

class CourseData implements FileSerializable {
    String courseId;
    String courseName;
    String facultyId;

    CourseData(String courseId, String courseName, String facultyId){
        this.courseId = courseId;
        this.courseName = courseName;
        this.facultyId = facultyId;
    }

    @Override
    public String toFileString(){
        return courseId + "," + courseName + "," + facultyId;
    }
}

class CourseService implements DataService<CourseData> {
    public static final String FILE = "Courses.txt";

    @Override
    public void save(CourseData entity) {
        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write(entity.toFileString() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CourseData> getAll() {
        List<CourseData> entities = new ArrayList<>();
        File file = new File(FILE);
        
        if (!file.exists()) {
            return entities;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");
                if(data.length >= 3) {
                    entities.add(new CourseData(data[0], data[1], data[2]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entities;
    }

    public boolean updateCourseFaculty(String courseId, String newFacultyId) {
        boolean updated = false;
        List<CourseData> courses = getAll();
        
        for (CourseData c : courses) {
            if (c.courseId.equals(courseId)) {
                c.facultyId = newFacultyId;
                updated = true;
            }
        }
        
        if (updated) {
            try {
                File file = new File(FILE);
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