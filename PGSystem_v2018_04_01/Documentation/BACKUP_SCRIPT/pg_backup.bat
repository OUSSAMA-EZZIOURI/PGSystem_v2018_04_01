@echo off
	SETLOCAL
	set DBNAME=jboost
	set BACKUPDIR=D:/
	set BACKUPFILE_DATE=%DATE:~6,4%_%DATE:~3,2%_%DATE:~0,2%
	set BACKUPFILE_TIME=%TIME:~1,1%_%TIME:~3,2%
	set BACKUPFILE=%BACKUPDIR%%DBNAME%_%BACKUPFILE_DATE%_%BACKUPFILE_TIME%.backup
	set BACKUPTARFILE=%BACKUPDIR%%DBNAME%_%BACKUPFILE_DATE%_%BACKUPFILE_TIME%
		
	SET PGPASSWORD=postgres
	echo on
	pg_dump -i -h localhost -p 5432 -U postgres -F p -b -v -f %BACKUPFILE% -d jboost
	pg_dump -h localhost -p 5432 -U postgres -f tar --blobs --section pre-data --section data --section post-data --encoding UTF8 --verbose --file %BACKUPTARFILE% -d %DBNAME%