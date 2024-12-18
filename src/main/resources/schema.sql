use loginsystem;

CREATE TABLE admin (
                       id VARCHAR(20) PRIMARY KEY,
                       user_name VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100),
                       login_attempt INT DEFAULT 0,
                       status VARCHAR(20) DEFAULT 'ACTIVE'

);

-- Inserting an admin record into the admin table
INSERT INTO admin (id, user_name, password, email, login_attempt, status)
VALUES
    ('A001', 'admin_user', '1234', 'admin1@example.com', 0, 'ACTIVE'),
    ('A002', 'john_doe', '2003', 'john.doe@example.com', 0, 'ACTIVE'),
    ('A003', 'jane_doe', '1111', 'jane.doe@example.com', 0, 'ACTIVE');


CREATE TABLE lecturer (
                          id VARCHAR(20) PRIMARY KEY,
                          password VARCHAR(255) NOT NULL,
                          name VARCHAR(100),
                          email VARCHAR(100),
                          department VARCHAR(100),
                          contact VARCHAR(15),
                          course_assign VARCHAR(100),
                          a_id VARCHAR(20),
                          FOREIGN KEY (a_id) REFERENCES admin(id)
);

-- Inserting lecturer records into the lecturer table
INSERT INTO lecturer (id, password, name, email, department, contact, course_assign, a_id)
VALUES
    ('lec001', '1234', 'Dr. John Smith', 'john.smith@example.com', 'Computer Science', '1234567890', 'Data Structures', 'A001'),
    ('lec002', '1234', 'Prof. Sarah Lee', 'sarah.lee@example.com', 'Information Technology', '9876543210', 'Database Systems', 'A002'),
    ('lec003', '1234', 'Dr. Alex Brown', 'alex.brown@example.com', 'Cybersecurity', '5432167890', 'Network Security', 'A003');



CREATE TABLE batch (
                       id VARCHAR(50) PRIMARY KEY,        -- Batch ID (BID)
                       name VARCHAR(100),                 -- Batch name (e.g., "GADSE232F")
                       start_date DATE,                   -- Start date of the batch
                       department VARCHAR(100),           -- Department associated with the batch
                       course VARCHAR(100),               -- Course assigned to the batch
                       a_id VARCHAR(20),                   -- Foreign Key to the Admin table (who created the batch)
                       FOREIGN KEY (a_id) REFERENCES admin(id)  -- Assuming there's an `admin` table to reference

);
-- Inserting some batch data
INSERT INTO batch (id, name, start_date, department, course, a_id)
VALUES
    ('gadse232f', 'GADSE232F', '2023-01-15', 'Computer Science', 'Software Engineering', 'A001'),
    ('gadse233f', 'GADSE233F', '2023-02-10', 'Information Technology', 'Data Science', 'A001'),
    ('gadse241f', 'GADSE241F', '2023-03-05', 'Computer Science', 'Cybersecurity', 'A001');

CREATE TABLE batch_gadse232f (
                                 SID VARCHAR(50) PRIMARY KEY,         -- Student ID
                                 Password VARCHAR(255),               -- Student Password (ensure to hash passwords in practice)
                                 name VARCHAR(100),                   -- Student Name
                                 email VARCHAR(100),                  -- Student Email
                                 reg_date DATE,                       -- Registration Date
                                 year VARCHAR(20),                    -- Year of Study (e.g., Year 1, Year 2, etc.)
                                 contact VARCHAR(20),                 -- Student's contact number
                                 photo VARCHAR(255),                  -- Path to photo
                                 role VARCHAR(50),                    -- Role (e.g., student)
                                 a_id VARCHAR(20),                    -- Admin's ID (Foreign Key reference, if applicable)
                                 FOREIGN KEY (a_id) REFERENCES admin(id)  -- Assuming there's an `admin` table to reference
);
-- Insert student data into batch_gadse232f table
INSERT INTO batch_gadse232f (SID, Password, name, email, reg_date, year, contact, photo, role, a_id)
VALUES
    ('gadse232f-001', '1234', 'John Doe', 'john.doe@example.com', '2023-01-15', 'Year 1', '1234567890', '/images/john_doe.jpg', 'student', 'A001'),
    ('gadse232f-002', '2345', 'Jane Smith', 'jane.smith@example.com', '2023-01-16', 'Year 1', '0987654321', '/images/jane_smith.jpg', 'student', 'A001');

CREATE TABLE batch_gadse233f (
                                 SID VARCHAR(50) PRIMARY KEY,         -- Student ID
                                 Password VARCHAR(255),               -- Student Password (ensure to hash passwords in practice)
                                 name VARCHAR(100),                   -- Student Name
                                 email VARCHAR(100),                  -- Student Email
                                 reg_date DATE,                       -- Registration Date
                                 year VARCHAR(20),                    -- Year of Study (e.g., Year 1, Year 2, etc.)
                                 contact VARCHAR(20),                 -- Student's contact number
                                 photo VARCHAR(255),                  -- Path to photo
                                 role VARCHAR(50),                    -- Role (e.g., student)
                                 a_id VARCHAR(20),                    -- Admin's ID (Foreign Key reference, if applicable)
                                 FOREIGN KEY (a_id) REFERENCES admin(id)  -- Assuming there's an `admin` table to reference
);

-- Insert student data into batch_gadse233f table
INSERT INTO batch_gadse233f (SID, Password, name, email, reg_date, year, contact, photo, role, a_id)
VALUES
    ('gadse233f-001', '1111', 'Alice Brown', 'alice.brown@example.com', '2023-02-11', 'Year 1', '1234567890', '/images/alice_brown.jpg', 'student', 'A001'),
    ('gadse233f-002', '1111', 'Bob White', 'bob.white@example.com', '2023-02-12', 'Year 1', '0987654321', '/images/bob_white.jpg', 'student', 'A001');

CREATE TABLE batch_gadse241f (
                                 SID VARCHAR(50) PRIMARY KEY,         -- Student ID
                                 Password VARCHAR(255),               -- Student Password (ensure to hash passwords in practice)
                                 name VARCHAR(100),                   -- Student Name
                                 email VARCHAR(100),                  -- Student Email
                                 reg_date DATE,                       -- Registration Date
                                 year VARCHAR(20),                    -- Year of Study (e.g., Year 1, Year 2, etc.)
                                 contact VARCHAR(20),                 -- Student's contact number
                                 photo VARCHAR(255),                  -- Path to photo
                                 role VARCHAR(50),                    -- Role (e.g., student)
                                 a_id VARCHAR(20),                    -- Admin's ID (Foreign Key reference, if applicable)
                                 FOREIGN KEY (a_id) REFERENCES admin(id)  -- Assuming there's an `admin` table to reference
);
-- Insert student data into batch_gadse241f table
INSERT INTO batch_gadse241f (SID, Password, name, email, reg_date, year, contact, photo, role, a_id)
VALUES
    ('gadse241f-001', 'password345', 'Charlie Green', 'charlie.green@example.com', '2023-03-06', 'Year 1', '1234567890', '/images/charlie_green.jpg', 'student', 'A001'),
    ('gadse241f-002', 'password678', 'Diana Blue', 'diana.blue@example.com', '2023-03-07', 'Year 1', '0987654321', '/images/diana_blue.jpg', 'student', 'A001');
