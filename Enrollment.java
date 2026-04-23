import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

class EnrollmentData implements FileSerializable {
    String StudentUsername;
    String CourseId;

    EnrollmentData(String StudentUsername, String CourseId){
        this.StudentUsername = StudentUsername;
        this.CourseId = CourseId;
    }

    @Override
    public String toFileString(){
        return StudentUsername + "," + CourseId;
    }
}

class EnrollmentService extends AbstractFileService<EnrollmentData> {
    public static final String FILE = "Enrollment.txt";

    public EnrollmentService() {
        super(FILE);
    }

    @Override
    protected EnrollmentData parseLine(String[] data) {
        if(data.length < 2) return null;
        return new EnrollmentData(data[0], data[1]);
    }
    
    public boolean removeEnrollment(String studentId, String courseId) {
        boolean dropped = false;
        List<EnrollmentData> enrollments = getAll();
        List<EnrollmentData> updated = new ArrayList<>();
        
        for (EnrollmentData e : enrollments) {
            if (e.StudentUsername.equals(studentId) && e.CourseId.equals(courseId)) {
                dropped = true;
            } else {
                updated.add(e);
            }
        }
        
        if (dropped) {
            try {
                File file = new File(filePath);
                FileWriter fw = new FileWriter(file, false);
                for (EnrollmentData l : updated) {
                    fw.write(l.toFileString() + "\n");
                }
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dropped;
    }
}


public class Enrollment {
    public static void main(String[] args) {
        
    }
}
