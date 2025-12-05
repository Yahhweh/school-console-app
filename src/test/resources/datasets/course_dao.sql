INSERT INTO groups (group_id, group_name)
VALUES (1, 'Science Group');

INSERT INTO courses (course_id, course_name,
                     course_description)
VALUES (10, 'Math', 'Algebra and Geometry');
INSERT INTO courses (course_id, course_name,
                     course_description)
VALUES (11, 'Biology', 'Basic Biology');
INSERT INTO courses (course_id, course_name,
                     course_description)
VALUES (12, 'History', 'World History');

INSERT INTO students (student_id, group_id, first_name,
                      last_name)
VALUES (100, 1, 'John', 'Doe');
INSERT INTO students (student_id, group_id, first_name,
                      last_name)
VALUES (101, 1, 'Jane', 'Smith');

INSERT INTO student_courses (student_id, course_id)
VALUES (100, 10);
INSERT INTO student_courses (student_id, course_id)
VALUES (100, 11);
