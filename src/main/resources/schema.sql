DROP TABLE IF EXISTS student_courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS courses;


CREATE TABLE groups (
                        group_id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        group_name text NOT NULL
);

CREATE TABLE courses (
                         course_id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                         course_name text NOT NULL,
                         course_description text NOT NULL
);

CREATE TABLE students (
                          student_id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                          group_id integer REFERENCES groups(group_id) ON DELETE SET NULL,
                          first_name text NOT NULL,
                          last_name text NOT NULL
);

CREATE TABLE student_courses (
                                 student_id integer REFERENCES students(student_id) ON DELETE CASCADE,
                                 course_id integer REFERENCES courses(course_id) ON DELETE CASCADE,
                                 PRIMARY KEY (student_id, course_id)
);
