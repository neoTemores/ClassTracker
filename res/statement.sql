	create table term (
		id int primary key auto_increment,
		name varchar(50),
		year int,
		isActive bool
	);

	create table course (
		id int primary key auto_increment,
		termId int,
		code varchar(10),
		name varchar(50),
		foreign key(termId) references term(id) on delete cascade
	);

	create table assignment (
		id int primary key auto_increment,
		courseId int,
		week int,
		name varchar(50),
		status enum('notStarted', 'inProgress', 'complete'),
		notes text,
		foreign key(courseId) references course(id) on delete cascade
	);


	insert into term (name, year, isActive) 
	values ('July-Aug', 2024, false);

	insert into course (termId, code, name)
	values (1, 'cis313', 'AI Driven Business App Coding');

	insert into assignment (courseId, week, name, status, notes)
	values (1, 1, 'discussion posts', 'inProgress', '1/3');
