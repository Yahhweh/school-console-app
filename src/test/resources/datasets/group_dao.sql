INSERT INTO groups (group_id, group_name)
VALUES (100, 'Group A');

INSERT INTO groups (group_id, group_name)
VALUES (200, 'Group B');

INSERT INTO students (student_id, group_id, first_name,
                      last_name)
VALUES (1, 100, 'Student', 'One');

INSERT INTO students (student_id, group_id, first_name,
                      last_name)
VALUES (2, 100, 'Student', 'Two');

INSERT INTO students (student_id, group_id, first_name,
                      last_name)
VALUES (3, 200, 'Student', 'Three');