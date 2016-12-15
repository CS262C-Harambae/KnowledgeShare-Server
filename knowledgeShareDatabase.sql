-- This code was obtained and modified from:
-- https://github.com/calvinballing/StudentNews/blob/master/sql/DatabaseConstruction.sql
-- This SQL script builds a Student News database
-- @author Andrew Gbeddy
-- @author Andrew Darmawan
-- @version Fall, 2016

-- Drops previous versions of the tables if they already exist, in reverse order
-- of foreign keys.
DROP TABLE IF EXISTS Issue;
DROP TABLE IF EXISTS Article;
DROP TABLE IF EXISTS Author;
-- DROP TABLE IF EXISTS Student;

-- Create the schema
CREATE TABLE Author (
    ID SERIAL NOT NULL PRIMARY KEY,
    Email VARCHAR(255),
    Name VARCHAR(255)
);

CREATE TABLE Article (
    ID SERIAL NOT NULL PRIMARY KEY,
    Subject VARCHAR(255),
    Body TEXT,
    AuthorID SERIAL REFERENCES Author(ID)
);

CREATE TABLE Issue (
    ID SERIAL NOT NULL PRIMARY KEY,
    PublishDateTime TIMESTAMP,
    ArticleID SERIAL REFERENCES Article(ID)
);

-- CREATE TABLE Student (
--    ID SERIAL NOT NULL PRIMARY KEY,
--    Email VARCHAR(255),
--    Name VARCHAR(255)
-- );

-- Allow users to select data from the tables.
GRANT SELECT ON Author TO PUBLIC;
GRANT SELECT ON Article TO PUBLIC;
GRANT SELECT ON Issue TO PUBLIC;
-- GRANT SELECT ON Student TO PUBLIC;

-- Add sample records
INSERT INTO Author VALUES (1, 'johncalvin@calvin.edu', 'John Calvin');
INSERT INTO Author VALUES (2, 'kdykhouse@calvin.edu', 'Kristy Dykhouse');
INSERT INTO Author VALUES (3, 'payroll@calvin.edu', 'Payroll');
INSERT INTO Author VALUES (4, 'zand@calvin.edu', 'Dianne Zandstra');
INSERT INTO Author VALUES (5, 'health@calvin.edu', 'Health Services');

INSERT INTO Article VALUES (1, 'New Calvin StudentNews App', 'There is a new app for Calvin StudentNews. Download from the Google Play Store.', 1);
INSERT INTO Article VALUES (2, 'Career Center Closed', 'Calvins Career Center will be closed on Wednesday, August 3, for a staff retreat. We apologize for any inconvenience this may cause.', 2);
INSERT INTO Article VALUES (3, 'Pay Day for Student Employees', 'It is pay day for Student employees! Please take a minute to review your electronic advice in the portal.', 3);
INSERT INTO Article VALUES (4, 'Spanish 301 students lead Spanish Chapel Tuesday', 'On Tuesday a group of Spanish 301 students will lead us in praise and will reflect with us on the theme of forgiveness. Come join us in the Commons Lecture Hall at 10:00AM.', 4);
INSERT INTO Article VALUES (5, 'Flu Clinic TODAY at Health Services', 'Calvin College Health Services is hosting a final flu clinic today from 10AM to 2PM. The initial check-in point will be located in Health Services.', 5);

INSERT INTO Issue VALUES (1, '2016-11-04 07:00:00', 1);
INSERT INTO Issue VALUES (2, '2016-09-20 07:04:00', 2);
INSERT INTO Issue VALUES (3, '2016-09-20 07:04:00', 3);
INSERT INTO Issue VALUES (4, '2016-10-16 07:02:00', 4);
INSERT INTO Issue VALUES (5, '2016-10-07 07:00:00', 5);

-- INSERT INTO Student Values (

SELECT * FROM Author;
SELECT * FROM Article;
SELECT * FROM Issue;

-- Search for a specific author
SELECT *
FROM Author
WHERE Author.name = 'Health Services';

-- Search for a specific word in a subject line of a news article
SELECT *
FROM Article
WHERE Article.subject LIKE '%Career%';

-- Search for a specific word in a body of a news article
SELECT *
FROM Article
WHERE Article.body LIKE '%employee%';

-- Search for an article published on a specific issue date
SELECT *
FROM Issue
WHERE Issue.publishdatetime = '2016-09-20 07:04:00';

-- Search for a specific article published on a specific issue date
SELECT Subject, PublishDateTime
FROM Article, Issue
WHERE Article.ID = Issue.articleID
	AND Article.subject = 'New Calvin StudentNews App';

-- Search for a specific word in the subject line of a news article published on a specific date

-- Search for a specific author who published an article on a specific date