
CREATE TABLE users (
    userID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name VARCHAR(100),
    user_email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    user_role VARCHAR(20)
);

CREATE TABLE supervisors (
    supervisorID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sv_name VARCHAR(100),
    sv_email VARCHAR(100),
    phoneNum VARCHAR(20),
    department VARCHAR(100),
    roomNo VARCHAR(20),
    profile_pic VARCHAR(255),
    userID INT,
    CONSTRAINT fk_supervisor_user FOREIGN KEY (userID)
        REFERENCES users(userID)
);

-- Buat students TANPA constraint ke supervisors dulu
CREATE TABLE students (
    studentID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_name VARCHAR(100),
    student_email VARCHAR(100),
    matricNum VARCHAR(20),
    program VARCHAR(50),
    profile_pic VARCHAR(255),
    userID INT,
    supervisorID INT,
    CONSTRAINT fk_student_user FOREIGN KEY (userID)
        REFERENCES users(userID)
);

-- Tambah constraint ke supervisors SETELAH table dibuat
ALTER TABLE students
ADD CONSTRAINT fk_student_supervisor FOREIGN KEY (supervisorID)
REFERENCES supervisors(supervisorID);

CREATE TABLE projects (
    projectID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_title VARCHAR(200),
    description CLOB,
    start_date DATE,
    end_date DATE,
    project_status VARCHAR(20),
    studentID INT,
    CONSTRAINT fk_project_student FOREIGN KEY (studentID)
        REFERENCES students(studentID)
);

CREATE TABLE progresssubmission (
    progressID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    submitted_date DATE,
    progress_status VARCHAR(50),
    student_file_name VARCHAR(255),
    student_file_path VARCHAR(4000),
    supervisor_file_name VARCHAR(255),
    supervisor_file_path VARCHAR(4000),
    projectID INT,
    CONSTRAINT fk_progress_project FOREIGN KEY (projectID)
        REFERENCES projects(projectID)
);

CREATE TABLE feedback (
    feedbackID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    feedback_text CLOB,
    feedback_date DATE,
    supervisorID INT,
    progressID INT,
    CONSTRAINT fk_feedback_supervisor FOREIGN KEY (supervisorID)
        REFERENCES supervisors(supervisorID),
    CONSTRAINT fk_feedback_progress FOREIGN KEY (progressID)
        REFERENCES progresssubmission(progressID)
);

CREATE TABLE notes (
    notesID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    notes_title VARCHAR(200),
    notes_content CLOB,
    created_date DATE,
    userID INT,
    projectID INT,
    CONSTRAINT fk_notes_user FOREIGN KEY (userID)
        REFERENCES users(userID),
    CONSTRAINT fk_notes_project FOREIGN KEY (projectID)
        REFERENCES projects(projectID)
);

CREATE TABLE reminders (
    reminderID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    reminder_name VARCHAR(200),
    due_date DATE,
    submission_status VARCHAR(50),
    recurrence VARCHAR(50),
    projectID INT,
    CONSTRAINT fk_reminder_project FOREIGN KEY (projectID)
        REFERENCES projects(projectID)
);

-- 1. INSERT INTO USERS
INSERT INTO users (user_name, user_email, password, user_role) VALUES
('Dr Ahmad Zainal', 'ahmad.zainal@uni.edu.my', 'zainal', 'SUPERVISOR'),
('Dr Siti Nur', 'siti.nur@uni.edu.my', 'siti', 'SUPERVISOR'),
('Dr Farid Hassan', 'farid.hassan@uni.edu.my', 'farid', 'SUPERVISOR'),
('Dr Nor Aisyah', 'aisyah.nor@uni.edu.my', 'aisyah', 'SUPERVISOR'),
('Dr Khairul Anuar', 'khairul.anuar@uni.edu.my', 'anuar', 'SUPERVISOR'),
('Aina Farhana', 'aina@student.uni.edu.my', 'aina', 'STUDENT'),
('Daniel Hakim', 'daniel@student.uni.edu.my', 'hakim', 'STUDENT'),
('Nur Izzati', 'izzati@student.uni.edu.my', 'izzati', 'STUDENT'),
('Amirul Syafiq', 'amirul@student.uni.edu.my', 'amirul', 'STUDENT'),
('Siti Aishah', 'aishah@student.uni.edu.my', 'aishah', 'STUDENT'),
('Muhammad Irfan', 'irfan@student.uni.edu.my', 'irfan', 'STUDENT'),
('Alya Sofea', 'alya@student.uni.edu.my', 'sofea', 'STUDENT'),
('Haziq Daniel', 'haziq@student.uni.edu.my', 'daniel', 'STUDENT'),
('Nur Athirah', 'athirah@student.uni.edu.my', 'athirah', 'STUDENT'),
('Adam Rizqi', 'adam@student.uni.edu.my', 'rizqi', 'STUDENT');

-- 2. INSERT INTO SUPERVISORS (tidak ada dalam data asal, jadi saya buat berdasarkan userID)
INSERT INTO supervisors (sv_name, sv_email, phoneNum, department, roomNo, userID, profile_pic) VALUES
('Dr Ahmad Zainal', 'ahmad.zainal@uni.edu.my', '012-3456789', 'Computer Science', 'C-301', 1, 'ahmad.jpg'),
('Dr Siti Nur', 'siti.nur@uni.edu.my', '012-9876543', 'Information Systems', 'B-205', 2, 'siti.jpg'),
('Dr Farid Hassan', 'farid.hassan@uni.edu.my', '013-4567890', 'Software Engineering', 'D-101', 3, 'farid.jpg'),
('Dr Nor Aisyah', 'aisyah.nor@uni.edu.my', '014-5678901', 'Data Science', 'A-308', 4, 'aisyah.jpg'),
('Dr Khairul Anuar', 'khairul.anuar@uni.edu.my', '015-6789012', 'Cyber Security', 'E-412', 5, 'khairul.jpg');

-- 3. INSERT INTO STUDENTS (update supervisorID dari NULL ke nilai sebenar)
INSERT INTO students (student_name, student_email, matricNum, program, userID, supervisorID, profile_pic) VALUES
('Aina Farhana', 'aina@student.uni.edu.my', '2025238836', 'Computer Science', 6, 1, 'aina.jpg'),
('Daniel Hakim', 'daniel@student.uni.edu.my', '2025238841', 'Information Systems', 7, 2, 'daniel.png'),
('Nur Izzati', 'izzati@student.uni.edu.my', '2025238854', 'Software Engineering', 8, 3, 'izzati.jpg'),
('Amirul Syafiq', 'amirul@student.uni.edu.my', '2025238867', 'Computer Science', 9, 1, 'amirul.png'),
('Siti Aishah', 'aishah@student.uni.edu.my', '2025238873', 'Data Science', 10, 4, 'aishah.jpg'),
('Muhammad Irfan', 'irfan@student.uni.edu.my', '2025238889', 'Cyber Security', 11, 5, 'irfan.png'),
('Alya Sofea', 'alya@student.uni.edu.my', '2025238895', 'Information Systems', 12, 2, 'alya.jpg'),
('Haziq Daniel', 'haziq@student.uni.edu.my', '2025238902', 'Software Engineering', 13, 3, 'haziq.png'),
('Nur Athirah', 'athirah@student.uni.edu.my', '2025238916', 'Data Science', 14, 4, 'athirah.jpg'),
('Adam Rizqi', 'adam@student.uni.edu.my', '2025238924', 'Cyber Security', 15, 5, 'adam.png');

-- 4. INSERT INTO PROJECTS (tidak ada dalam data asal, jadi saya buat berdasarkan studentID)
INSERT INTO projects (project_title, description, start_date, end_date, project_status, studentID) VALUES
('FYP Management System', 'Web-based system for managing final year projects', '2025-03-01', '2025-08-31', 'IN_PROGRESS', 1),
('AI-Powered Chatbot', 'Intelligent chatbot for university FAQ', '2025-03-01', '2025-08-31', 'IN_PROGRESS', 2),
('E-Commerce Platform', 'Online shopping system with recommendation engine', '2025-03-01', '2025-08-31', 'IN_PROGRESS', 3),
('Mobile Health App', 'Health monitoring application for smartphones', '2025-03-01', '2025-08-31', 'IN_PROGRESS', 4),
('Cybersecurity Dashboard', 'Real-time network threat monitoring system', '2025-03-01', '2025-08-31', 'IN_PROGRESS', 5),
('IoT Smart Home', 'Internet of Things based home automation', '2025-03-01', '2025-08-31', 'IN_PROGRESS', 6),
('Student Portal Redesign', 'Modern redesign of university student portal', '2025-03-01', '2025-08-31', 'IN_PROGRESS', 7),
('Data Analytics Tool', 'Tool for analyzing large datasets', '2025-03-01', '2025-08-31', 'IN_PROGRESS', 8),
('Blockchain Voting System', 'Secure voting system using blockchain', '2025-03-01', '2025-08-31', 'IN_PROGRESS', 9),
('AR Navigation App', 'Augmented Reality indoor navigation', '2025-03-01', '2025-08-31', 'IN_PROGRESS', 10);

-- 5. INSERT INTO PROGRESSSUBMISSION (tidak ada dalam data asal)
INSERT INTO progresssubmission (submitted_date, progress_status, projectID) VALUES
('2025-03-15', 'COMPLETED', 1),
('2025-03-20', 'COMPLETED', 2),
('2025-03-22', 'PENDING_REVIEW', 3),
('2025-03-25', 'COMPLETED', 4),
('2025-03-28', 'IN_PROGRESS', 5),
('2025-04-01', 'COMPLETED', 6),
('2025-04-05', 'PENDING_REVIEW', 7),
('2025-04-08', 'IN_PROGRESS', 8),
('2025-04-10', 'COMPLETED', 9),
('2025-04-12', 'PENDING_REVIEW', 10);

-- 6. INSERT INTO FEEDBACK (tidak ada dalam data asal)
INSERT INTO feedback (feedback_text, feedback_date, supervisorID, progressID) VALUES
('Good progress on the initial prototype. Focus on improving UI design.', '2025-03-16', 1, 1),
('Literature review needs more recent references from 2024-2025.', '2025-03-21', 2, 2),
('Database schema looks good but needs normalization.', '2025-03-23', 3, 3),
('Excellent work on the mobile app interface.', '2025-03-26', 4, 4),
('Add more security features to the dashboard.', '2025-03-29', 5, 5);

-- 7. INSERT INTO NOTES (dari data asal anda)
INSERT INTO notes (notes_title, notes_content, created_date, userID, projectID) VALUES
('Initial Project Discussion', 'Discussed overall project scope, objectives, and expected outcomes during the first meeting.', '2025-03-10', 6, 1),
('Literature Review Progress', 'Completed searching for related journals and articles. Need to summarize findings.', '2025-03-18', 7, 2),
('Supervisor Feedback Summary', 'Supervisor suggested improving system flow and database normalization.', '2025-03-25', 1, 1),
('System Design Ideas', 'Outlined initial UI sketches and key system modules.', '2025-04-02', 6, 1),
('Meeting Notes – Week 5', 'Agreed to finalize ERD and use case diagrams before next submission.', '2025-04-08', 7, 2),
('Progress Reflection', 'Development is on track but needs better time management.', '2025-04-12', 6, 1),
('Supervisor Review Notes', 'Minor corrections required in Chapter 2 documentation.', '2025-04-18', 2, 2);

-- 8. INSERT INTO REMINDERS (tidak ada dalam data asal)
INSERT INTO reminders (reminder_name, due_date, submission_status, recurrence, projectID) VALUES
('Submit Chapter 1', '2025-03-31', 'PENDING', 'ONCE', 1),
('Progress Report 1', '2025-04-15', 'PENDING', 'ONCE', 2),
('Meeting with Supervisor', '2025-04-20', 'PENDING', 'WEEKLY', 3),
('Submit Prototype', '2025-05-10', 'PENDING', 'ONCE', 4),
('Final Documentation', '2025-08-25', 'PENDING', 'ONCE', 5);