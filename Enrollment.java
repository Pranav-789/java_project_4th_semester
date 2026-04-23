import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class EnrollmentData implements FileSerializable {
    String studentId;
    String courseId;

    EnrollmentData(String studentId, String courseId){
        this.studentId = studentId;
        this.courseId = courseId;
    }

    @Override
    public String toFileString(){
        return studentId + "," + courseId;
    }
}

class EnrollmentService implements DataService<EnrollmentData> {
    public static final String FILE = "Enrollment.txt";

    @Override
    public void save(EnrollmentData entity) {
        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write(entity.toFileString() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EnrollmentData> getAll() {
        List<EnrollmentData> entities = new ArrayList<>();
        File file = new File(FILE);
        
        if (!file.exists()) {
            return entities;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");
                if(data.length >= 2) {
                    entities.add(new EnrollmentData(data[0], data[1]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entities;
    }
    
    public boolean removeEnrollment(String studentId, String courseId) {
        boolean dropped = false;
        List<EnrollmentData> enrollments = getAll();
        List<EnrollmentData> updated = new ArrayList<>();
        
        for (EnrollmentData e : enrollments) {
            if (e.studentId.equals(studentId) && e.courseId.equals(courseId)) {
                dropped = true;
            } else {
                updated.add(e);
            }
        }
        
        if (dropped) {
            try {
                File file = new File(FILE);
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