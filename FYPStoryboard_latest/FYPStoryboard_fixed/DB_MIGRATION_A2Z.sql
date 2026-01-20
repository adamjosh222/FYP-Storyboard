-- DB_MIGRATION_A2Z.sql
-- This project version uses existing columns in your current schema:
-- STUDENTS(STUDENT_NAME, STUDENT_EMAIL, MATRICNUM, PROGRAM, SUPERVISORID, USERID)
-- SUPERVISORS(SV_NAME, SV_EMAIL, PHONENUM, DEPARTMENT, ROOMNO, USERID)
-- REMINDERS(REMINDERID, REMINDER_NAME, DUE_DATE, SUBMISSION_STATUS, RECURRENCE, PROJECTID)
--
-- If your database schema is missing any of the above columns, add them accordingly.
-- Many Derby setups already have them.

-- =============================================================
-- V7+ Feature: Progress submission attachments
-- =============================================================
-- Student can upload a file when submitting progress.
-- Supervisor can upload a return/review file back to the student.
--
-- Run ONCE. If columns already exist, Derby will throw an error.

ALTER TABLE PROGRESSSUBMISSION ADD COLUMN STUDENT_FILE_NAME VARCHAR(255);
ALTER TABLE PROGRESSSUBMISSION ADD COLUMN STUDENT_FILE_PATH VARCHAR(4000);
ALTER TABLE PROGRESSSUBMISSION ADD COLUMN SUPERVISOR_FILE_NAME VARCHAR(255);
ALTER TABLE PROGRESSSUBMISSION ADD COLUMN SUPERVISOR_FILE_PATH VARCHAR(4000);

