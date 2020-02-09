-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 09, 2020 at 08:17 PM
-- Server version: 8.0.13-4
-- PHP Version: 7.2.24-0ubuntu0.18.04.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `8NTJLatA69`
--

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE `attendance` (
  `presenceID` int(10) NOT NULL,
  `studentID` int(10) NOT NULL,
  `lessonid` int(10) NOT NULL,
  `present` tinyint(1) NOT NULL,
  `date` varchar(10) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`presenceID`, `studentID`, `lessonid`, `present`, `date`) VALUES
(125, 112, 1, 1, '2020-02-09'),
(126, 116, 1, 0, '2020-02-09'),
(127, 112, 3, 1, '2020-02-10'),
(128, 116, 3, 1, '2020-02-10');

-- --------------------------------------------------------

--
-- Table structure for table `cases`
--

CREATE TABLE `cases` (
  `caseid` int(10) NOT NULL,
  `receiverid` int(10) NOT NULL,
  `senderid` int(10) NOT NULL,
  `topic` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `cases`
--

INSERT INTO `cases` (`caseid`, `receiverid`, `senderid`, `topic`) VALUES
(119, 94, 99, 'Annual work assessment.'),
(121, 105, 99, 'Adding new student.');

-- --------------------------------------------------------

--
-- Table structure for table `classes`
--

CREATE TABLE `classes` (
  `classid` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `classes`
--

INSERT INTO `classes` (`classid`, `name`) VALUES
(1, '1A'),
(2, '2B');

-- --------------------------------------------------------

--
-- Table structure for table `days`
--

CREATE TABLE `days` (
  `dayid` int(11) NOT NULL,
  `day` varchar(3) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `days`
--

INSERT INTO `days` (`dayid`, `day`) VALUES
(1, 'MON'),
(2, 'TUE'),
(3, 'WED'),
(4, 'THU'),
(5, 'FRI');

-- --------------------------------------------------------

--
-- Table structure for table `grades`
--

CREATE TABLE `grades` (
  `gradeid` int(10) NOT NULL,
  `studentid` int(10) NOT NULL,
  `date` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `mark` int(1) NOT NULL,
  `lessonid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `grades`
--

INSERT INTO `grades` (`gradeid`, `studentid`, `date`, `description`, `mark`, `lessonid`) VALUES
(123, 112, '2020-01-12', 'Algebra Test', 5, 1),
(124, 112, '2020-01-12', 'Activity', 5, 1),
(132, 112, '2020-02-05', 'Activity', 5, 3),
(133, 112, '2020-02-05', 'Geometry test', 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(134);

-- --------------------------------------------------------

--
-- Table structure for table `lessons`
--

CREATE TABLE `lessons` (
  `lessonid` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `classid` int(11) NOT NULL,
  `teacherid` int(11) NOT NULL,
  `dayid` int(11) NOT NULL,
  `subjectid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `lessons`
--

INSERT INTO `lessons` (`lessonid`, `number`, `classid`, `teacherid`, `dayid`, `subjectid`) VALUES
(1, 1, 1, 100, 1, 1),
(2, 2, 1, 104, 1, 2),
(3, 1, 1, 100, 2, 3),
(4, 2, 1, 104, 2, 4),
(5, 2, 2, 100, 3, 1),
(6, 1, 2, 104, 3, 2),
(7, 1, 2, 100, 4, 3),
(8, 2, 2, 104, 4, 4),
(9, 2, 1, 100, 5, 1),
(10, 1, 1, 104, 5, 2);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `messageid` int(11) NOT NULL,
  `caseid` int(10) NOT NULL,
  `senderid` int(10) NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`messageid`, `caseid`, `senderid`, `content`) VALUES
(120, 119, 99, 'Dear Mr. Chapman. I send You my yearly work assessment via email.'),
(122, 121, 99, 'Hello! Please add new student to the class 1A. I provided all required data via email.'),
(129, 119, 94, 'Thank you for information. I received it.');

-- --------------------------------------------------------

--
-- Table structure for table `parents`
--

CREATE TABLE `parents` (
  `parentid` int(10) NOT NULL,
  `userid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `parents`
--

INSERT INTO `parents` (`parentid`, `userid`) VALUES
(108, 107),
(110, 109);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `studentid` int(10) NOT NULL,
  `classid` int(10) NOT NULL,
  `userid` int(11) DEFAULT NULL,
  `parentid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`studentid`, `classid`, `userid`, `parentid`) VALUES
(112, 1, 111, 108),
(114, 2, 113, 108),
(116, 1, 115, 110),
(118, 2, 117, 110);

-- --------------------------------------------------------

--
-- Table structure for table `subjects`
--

CREATE TABLE `subjects` (
  `subjectid` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `subjects`
--

INSERT INTO `subjects` (`subjectid`, `name`) VALUES
(1, 'Mathematic'),
(2, 'Physics'),
(3, 'Biology'),
(4, 'English'),
(5, 'Spanish');

-- --------------------------------------------------------

--
-- Table structure for table `teachers`
--

CREATE TABLE `teachers` (
  `teacherID` int(10) NOT NULL,
  `userid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `teachers`
--

INSERT INTO `teachers` (`teacherID`, `userid`) VALUES
(100, 99),
(104, 103);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userid` int(11) NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `role_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `surname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userid`, `email`, `name`, `password`, `role_type`, `surname`, `user_name`) VALUES
(94, 'toby.chapman@email.com', 'Toby', '$2a$10$xIlZwVhi.Giy9UHNbH95keDYiMcqwY53nuAjLN0XXnwauFnUo2MS6', 'headmaster', 'Chapman', 'tChap'),
(99, 'sami.williamson@email.com', 'Sami', '$2a$10$d/Q9PkTWM17BRCnewnLnvOSZ2aJKY50Ogo88N3PdCA69HhjjoiNJy', 'teacher', 'Williamson', 'sWill'),
(103, 'john.spencer@email.com', 'John', '$2a$10$rwr.0SX4il2NOXh/nL5m2OOsk1UyfVWmkL2.b4jXa5RITt6VMcURe', 'teacher', 'Spencer', 'jSpen'),
(105, 'administrator@email.com', 'System', '$2a$10$.zRAcMh6S9/xmHDlQaV0J.rzC/pu6WDL2hPIZr9Imn/42cC0mfPU.', 'admin', 'Administrator', 'admin123'),
(107, 'brandon.morgan@email.com', 'Brandon', '$2a$10$Uzb8wKfJfsqym0QHEU8bqeQNefWMzYHx4O21pFztDE9lxvqdXJ/AK', 'parent', 'Morgan', 'bMorg'),
(109, 'kaiden.hall@email.com', 'Kaiden', '$2a$10$IyuEueuEp8/jXsiYGOoKHeB.qEfdOKiodBpmFVpBYlcg.p5HtHZCy', 'parent', 'Hall', 'kHall'),
(111, 'aiden.morgan@email.com', 'Aiden', '$2a$10$4PpSAQ7sXnaMLyja0cy9peL5cSYecg8ZnjqNrY904UhD1Hiv7jeVq', 'student', 'Morgan', 'aMorg'),
(113, 'kate.morgan@email.com', 'Kate', '$2a$10$7v8IPw5lo3q/obHZ/2VroeNznXMejc7y5VL/QaO0/qxxkjb/NOkPK', 'student', 'Morgan', 'kMorg'),
(115, 'jasemine.hall@email.com', 'Jasemine', '$2a$10$CTq8RdAk8X2zy1q2//U8GO/gxeT/mLR31zVowp.pKdUf2o3WKh67K', 'student', 'Hall', 'jHall'),
(117, 'george.hall@email.com', 'George', '$2a$10$qn1lsXKilgld/II8NOP0luYoka.K/rS3xdEYGBnmmBzYLX14JBEQO', 'student', 'Hall', 'gHall');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`presenceID`),
  ADD KEY `lessonid` (`lessonid`),
  ADD KEY `attendance_ibfk_1` (`studentID`);

--
-- Indexes for table `cases`
--
ALTER TABLE `cases`
  ADD PRIMARY KEY (`caseid`),
  ADD KEY `cases_ibfk_2` (`senderid`),
  ADD KEY `receiverid` (`receiverid`);

--
-- Indexes for table `classes`
--
ALTER TABLE `classes`
  ADD PRIMARY KEY (`classid`);

--
-- Indexes for table `days`
--
ALTER TABLE `days`
  ADD PRIMARY KEY (`dayid`);

--
-- Indexes for table `grades`
--
ALTER TABLE `grades`
  ADD PRIMARY KEY (`gradeid`),
  ADD KEY `grades_ibfk_1` (`studentid`),
  ADD KEY `grades_ibfk_2` (`lessonid`);

--
-- Indexes for table `lessons`
--
ALTER TABLE `lessons`
  ADD PRIMARY KEY (`lessonid`),
  ADD KEY `dayid` (`dayid`),
  ADD KEY `lessons_ibfk_1` (`classid`),
  ADD KEY `lessons_ibfk_4` (`subjectid`),
  ADD KEY `teacherid` (`teacherid`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`messageid`),
  ADD KEY `case-msg_idx` (`caseid`),
  ADD KEY `case-user_idx` (`senderid`);

--
-- Indexes for table `parents`
--
ALTER TABLE `parents`
  ADD PRIMARY KEY (`parentid`),
  ADD KEY `parents_ibfk_1` (`userid`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`studentid`),
  ADD KEY `students_ibfk_2` (`classid`),
  ADD KEY `FKd750wfvgdjyme40wke9x3ccs6` (`userid`),
  ADD KEY `students_ibfk_1` (`parentid`);

--
-- Indexes for table `subjects`
--
ALTER TABLE `subjects`
  ADD PRIMARY KEY (`subjectid`);

--
-- Indexes for table `teachers`
--
ALTER TABLE `teachers`
  ADD PRIMARY KEY (`teacherID`),
  ADD KEY `teachers_ibfk_2` (`userid`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userid`),
  ADD UNIQUE KEY `user_name` (`user_name`),
  ADD UNIQUE KEY `user_name_2` (`user_name`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `attendance`
--
ALTER TABLE `attendance`
  ADD CONSTRAINT `FKhw2plavgb3byyivrupexbd7uh` FOREIGN KEY (`studentID`) REFERENCES `students` (`studentid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`studentID`) REFERENCES `students` (`studentid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `attendance_ibfk_2` FOREIGN KEY (`lessonid`) REFERENCES `lessons` (`lessonid`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `cases`
--
ALTER TABLE `cases`
  ADD CONSTRAINT `cases_ibfk_2` FOREIGN KEY (`senderid`) REFERENCES `users` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `cases_ibfk_3` FOREIGN KEY (`receiverid`) REFERENCES `users` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `grades`
--
ALTER TABLE `grades`
  ADD CONSTRAINT `grades_ibfk_1` FOREIGN KEY (`studentid`) REFERENCES `students` (`studentid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `grades_ibfk_2` FOREIGN KEY (`lessonid`) REFERENCES `lessons` (`lessonid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `lessons`
--
ALTER TABLE `lessons`
  ADD CONSTRAINT `lessons_ibfk_1` FOREIGN KEY (`classid`) REFERENCES `classes` (`classid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `lessons_ibfk_2` FOREIGN KEY (`dayid`) REFERENCES `days` (`dayid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `lessons_ibfk_4` FOREIGN KEY (`subjectid`) REFERENCES `subjects` (`subjectid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `lessons_ibfk_5` FOREIGN KEY (`teacherid`) REFERENCES `teachers` (`teacherid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `case-msg` FOREIGN KEY (`caseid`) REFERENCES `cases` (`caseid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `user-msg` FOREIGN KEY (`senderid`) REFERENCES `users` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `parents`
--
ALTER TABLE `parents`
  ADD CONSTRAINT `parents_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `FKd750wfvgdjyme40wke9x3ccs6` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `students_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `parents` (`parentid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `students_ibfk_2` FOREIGN KEY (`classid`) REFERENCES `classes` (`classid`);

--
-- Constraints for table `teachers`
--
ALTER TABLE `teachers`
  ADD CONSTRAINT `teachers_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
