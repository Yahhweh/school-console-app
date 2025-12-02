package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroup;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcStudent;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;

import java.util.*;

public class StudentsGenerator implements Seeder {

    private final JdbcStudent jdbcStudent;
    private final JdbcGroup jdbcGroup;
    private final Random random = new Random();

    private static final List<String> FIRST_NAMES = List.of(
        "Liam", "Noah", "Oliver", "Elijah", "James",
        "William", "Benjamin", "Lucas", "Henry", "Alexander",
        "Mason", "Michael", "Ethan", "Daniel", "Jacob",
        "Logan", "Jackson", "Levi", "Sebastian", "Mateo"
    );

    private static final List<String> LAST_NAMES = List.of(
        "Smith", "Johnson", "Williams", "Brown", "Jones",
        "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
        "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
        "Thomas", "Taylor", "Moore", "Jackson", "Martin"
    );

    public StudentsGenerator(JdbcStudent jdbcStudent, JdbcGroup jdbcGroup) {
        this.jdbcStudent = jdbcStudent;
        this.jdbcGroup = jdbcGroup;
    }

    @Override
    public void generate(int amount) {
        for (int i = 0; i < amount; i++) {
            String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
            String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));

            jdbcStudent.save(new Student(null, null, firstName, lastName));
        }
    }

    public void assignRandomGroups(int minStudents, int maxStudents) {
        List<Group> groups = jdbcGroup.findAll();
        List<Student> allStudents = jdbcStudent.findAll();

        Collections.shuffle(allStudents);
        Queue<Student> studentQueue = new ArrayDeque<>(allStudents);

        for (Group group : groups) {
            if (studentQueue.isEmpty()) {
                break;
            }

            int count = random.nextInt(maxStudents - minStudents + 1) + minStudents;

            for (int i = 0; i < count; i++) {
                if (studentQueue.isEmpty()) {
                    break;
                }

                Student student = studentQueue.poll();

                if (student != null) {
                    student.setGroupId(group.getId());
                    jdbcStudent.update(student);
                }
            }
        }
    }
}