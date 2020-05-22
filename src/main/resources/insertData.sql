INSERT INTO EMPLOYEE VALUES (1, 'Кузнецова Валентина Михайловна', TO_DATE('1989/05/03', 'yyyy/mm/dd'), '+380665012290', 'Cумы', NULL , 'ADMIN_TINA', 'ADMIN') /
INSERT INTO EMPLOYEE VALUES (2, 'Цветкова Юлия Александровна', TO_DATE('1992/07/20', 'yyyy/mm/dd'), '+380501299941', 'Cумы', 1 , 'ADMIN_YULIA', 'ADMIN') /
INSERT INTO EMPLOYEE VALUES (3, 'Котова Ксения Олеговна', TO_DATE('1995/03/15', 'yyyy/mm/dd'), '+380505118009', 'Cумы', 2 , 'ADMIN_KSENIA', 'ADMIN') /
INSERT INTO EMPLOYEE VALUES (4, 'Смирнов Андрей Кириллович', TO_DATE('2001/06/10', 'yyyy/mm/dd'), '+380981250033','Cумы', 3, 'ANDREY', 'USER_ANDREY') /
INSERT INTO EMPLOYEE VALUES (5, 'Сидорова Ирина Олексеевна', TO_DATE('1999/01/13', 'yyyy/mm/dd'), '+380981388103', 'Cумы', 3 , 'IRINA', 'USER_IRINA') /

INSERT INTO STATUS VALUES (1, 'PRO', 200) /
INSERT INTO STATUS VALUES (2, 'ASSISTANT', 150) /
INSERT INTO STATUS VALUES (3, 'Директор', 250) /
INSERT INTO STATUS VALUES (4, 'Менеджер', 100) /

INSERT INTO COURSE VALUES (1, 'ScratchJR', 'Coding is the new literacy! With ScratchJr, young children (ages 5-6) can program their own interactive stories and games. In the process, they learn to solve problems, design projects, and express themselves creatively on the computer.', 32, 175) /
INSERT INTO COURSE VALUES (2, 'TynkerJR', 'Even pre-readers can learn to code with Tynker Junior! Tynker Junior is the fun, interactive way to spark your child’s interest in coding. Young children (ages 6-7) learn the fundamentals of coding by snapping together graphical blocks to move their characters.', 30, 190) /
INSERT INTO COURSE VALUES (3, 'Scratch', 'Scratch is a free programming language and online community where children (ages 8-9) can create your own interactive stories, games, and animations.', 35, 185) /
INSERT INTO COURSE VALUES (4, 'Tynker', 'The fun way to learn programming and develop problem solving & critical thinking skills! Ages 5-17', 40, 200) /

INSERT INTO CLASS VALUES (1, 1, 'SCR JR 1.1', TO_DATE('2020/05/18', 'yyyy/mm/dd')) /
INSERT INTO CLASS VALUES (2, 2, 'TYN JR 1.1', TO_DATE('2020/05/19', 'yyyy/mm/dd')) /
INSERT INTO CLASS VALUES (3, 3, 'SCR 1.1',TO_DATE('2020/05/18', 'yyyy/mm/dd')) /

INSERT INTO EMPSCHEDULE VALUES (1, 4, 1, 1) /
INSERT INTO EMPSCHEDULE VALUES (2, 5, 1, 2) /
INSERT INTO EMPSCHEDULE VALUES (3, 1, 3, NULL) /
INSERT INTO EMPSCHEDULE VALUES (4, 3, 4, NULL) /
INSERT INTO EMPSCHEDULE VALUES (5, 5, 1, 3) /
INSERT INTO EMPSCHEDULE VALUES (6, 2, 3, NULL) /

INSERT INTO CHILD VALUES (1, 'Никитин Богдан', TO_DATE('2012/12/12', 'yyyy/mm/dd'), 'Cумы', '+380506205877') /
INSERT INTO CHILD VALUES (2, 'Сидак Алексей', TO_DATE('2014/02/20', 'yyyy/mm/dd'), 'Cумы', '+380661215888') /
INSERT INTO CHILD VALUES (3, 'Васильев Марк', TO_DATE('2012/10/02', 'yyyy/mm/dd'), 'Cумы', '+380663291122') /
INSERT INTO CHILD VALUES (4, 'Литовцев Никита', TO_DATE('2012/02/24', 'yyyy/mm/dd'), 'Cумы', '+380993597421') /
INSERT INTO CHILD VALUES (5, 'Сизоненко Богдан', TO_DATE('2013/04/18', 'yyyy/mm/dd'), 'Cумы', '+380991299934') /
INSERT INTO CHILD VALUES (6, 'Грабина Анастасия', TO_DATE('2014/05/15', 'yyyy/mm/dd'), 'Cумы', '+380665141294') /
INSERT INTO CHILD VALUES (7, 'Лебедев Артем', TO_DATE('2011/05/03', 'yyyy/mm/dd'), 'Cумы', '+380997722212') /
INSERT INTO CHILD VALUES (8, 'Чайка Евгений', TO_DATE('2010/05/08', 'yyyy/mm/dd'), 'Cумы', '+380506008850') /

INSERT INTO CHSCHEDULE VALUES (1, 1, 3) /
INSERT INTO CHSCHEDULE VALUES (2, 2, 1) /
INSERT INTO CHSCHEDULE VALUES (3, 3, 3) /
INSERT INTO CHSCHEDULE VALUES (4, 4, 2) /
INSERT INTO CHSCHEDULE VALUES (5, 5, 1) /
INSERT INTO CHSCHEDULE VALUES (6, 6, 2) /
INSERT INTO CHSCHEDULE VALUES (7, 7, 1) /
INSERT INTO CHSCHEDULE VALUES (8, 8, 3) /
INSERT INTO CHSCHEDULE VALUES (9, 1, 2) /
INSERT INTO CHSCHEDULE VALUES (10, 3, 2) /

INSERT INTO LESSONTIME VALUES (1, 1, TO_DATE('2020/05/20 17:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (2, 1, TO_DATE('2020/05/27 17:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (3, 1, TO_DATE('2020/06/03 17:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (4, 1, TO_DATE('2020/06/10 17:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (5, 1, TO_DATE('2020/06/17 17:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (6, 1, TO_DATE('2020/06/24 17:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (7, 1, TO_DATE('2020/07/01 17:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (8, 1, TO_DATE('2020/07/08 17:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (9, 2, TO_DATE('2020/05/22 16:30', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (10, 2, TO_DATE('2020/05/29 16:30', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (11, 2, TO_DATE('2020/06/05 16:30', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (12, 2, TO_DATE('2020/06/12 16:30', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (13, 2, TO_DATE('2020/06/19 16:30', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (14, 2, TO_DATE('2020/06/26 16:30', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (15, 2, TO_DATE('2020/07/03 16:30', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (16, 2, TO_DATE('2020/07/10 16:30', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (17, 3, TO_DATE('2020/05/23 12:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (18, 3, TO_DATE('2020/05/30 12:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (19, 3, TO_DATE('2020/06/06 12:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (20, 3, TO_DATE('2020/06/13 12:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (21, 3, TO_DATE('2020/06/20 12:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (22, 3, TO_DATE('2020/06/27 12:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (23, 3, TO_DATE('2020/07/04 12:00', 'yyyy/mm/dd HH24:MI')) /
INSERT INTO LESSONTIME VALUES (24, 3, TO_DATE('2020/07/11 12:00', 'yyyy/mm/dd HH24:MI')) /