import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class EnrollmentData{
    String StudentUsername;
    String CourseId;

    EnrollmentData(String StudentUsername, String CourseId){
        this.StudentUsername = StudentUsername;
        this.CourseId = CourseId;
    }

    public String toFileString(){
        return StudentUsername + "," + CourseId;
    }
}

class EnrollmentService{
    public static final String FILE = "Enrollment.txt";

    static void enroll(EnrollmentData enrollment){
        try(FileWriter fw = new FileWriter(FILE, true)){
            fw.write(enrollment.toFileString() + "\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static EnrollmentData[] getAllEnrollments(){
        try {
            File file = new File(FILE);
            if (!file.exists()) {
                return new EnrollmentData[0];
            }
            Scanner sc = new Scanner(file);
            List<EnrollmentData> allEnrollmentData = new ArrayList<>();

            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] data = line.split(",");
                if(data.length < 2)
                    continue;
                String StudentUsername = data[0];
                String CourseId = data[1];
                EnrollmentData enrollment = new EnrollmentData(StudentUsername, CourseId);
                allEnrollmentData.add(enrollment);
            }
            sc.close();
            return allEnrollmentData.toArray(new EnrollmentData[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new EnrollmentData[0];
    }
    
    static boolean removeEnrollment(String studentId, String courseId) {
        boolean dropped = false;
        try {
            File file = new File(FILE);
            if (!file.exists()) return false;
            
            List<String> lines = new ArrayList<>();
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split(",");
                if (data.length >= 2 && data[0].equals(studentId) && data[1].equals(courseId)) {
                    dropped = true;
                    continue;
                }
                lines.add(line);
            }
            sc.close();
            
            if (dropped) {
                FileWriter fw = new FileWriter(file, false);
                for (String l : lines) {
                    fw.write(l + "\n");
                }
                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dropped;
    }
}


public class Enrollment {
    public static void main(String[] args) {
        
    }
}
