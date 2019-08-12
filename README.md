
# Spring-Boot-FIle-Upload-API Rest
•	Implement a Rest API in spring-boot application
•	API to upload files with meta data.
•	Persist meta-data H2 database

How to test this service

1. Using CURL
Single file upload
C:\> curl -F file=@"c:\app\file.txt" http://localhost:8080/api/uploadFile

Multiple file upload
C:\> curl -F files=@"c:\app\file2.txt" - F files=@"c:\app\file3.txt" http://localhost:8383/api/upload/multiplefiles

Get meta data files in JSON format
C:\>curl http://localhost:8080/getMetaData
[{
	"name": "file.txt",
	"contentType": "text/plain",
	"contentSize": 15
}, {
	"name": "file2.txt",
	"contentType": "text/plain",
	"contentSize": 15
}, {
	"name": "file3.txt",
	"contentType": "text/plain",
	"contentSize": 15
}]


