package com.upload.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.upload.service.RepositoryFile;
import com.upload.entity.MetaData;

/**
 *
 * @author cibarra
 */
@RestController
public class UploadFileController {

    private final Logger log = LoggerFactory.getLogger(UploadFileController.class);

    private static String FOLDER = "C:/files/";

    @Autowired
    private RepositoryFile repositoryFile;

    @RequestMapping(value = "/api/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile upload,
            final HttpServletRequest request) {

        log.debug("*********************");
        log.debug("fileName : " + upload.getOriginalFilename());
        log.debug("contentType : " + upload.getContentType());
        log.debug("contentSize : " + upload.getSize());
        log.debug("*********************");

        if (upload.isEmpty()) {
            return new ResponseEntity<String>("select a file.....", HttpStatus.OK);
        }

        try {
            saveFiles(Arrays.asList(upload));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("Upload OK!! - " + upload.getOriginalFilename(),
                new HttpHeaders(), HttpStatus.OK);

    }

    @RequestMapping(value = "/getMetaData", method = RequestMethod.GET)
    public List<MetaData> getMetaData() {
        List<MetaData> fileMetaData = repositoryFile.findAll();
        return fileMetaData;
    }

    @RequestMapping(value = "/api/upload/multiplefiles", method = RequestMethod.POST)
    public ResponseEntity<?> multiplefilesUpload(@RequestParam("files") MultipartFile[] files) {
        String uploadedFileName = Arrays.stream(files).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity<String>("select files!", HttpStatus.OK);
        }

        if (files.length == 0) {
            return new ResponseEntity<String>("select files!", HttpStatus.OK);
        }

        try {

            saveFiles(Arrays.asList(files));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Upload OK ---> " + uploadedFileName, HttpStatus.OK);

    }
    
    private void saveMetaData(MultipartFile f) throws IOException {
        MetaData md = new MetaData();
        md.setName(f.getOriginalFilename());
        md.setContentType(f.getContentType());
        md.setContentSize(f.getSize());
        repositoryFile.save(md);
    }

    private void saveFiles(List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            saveMetaData(file);

        }

    }

}
