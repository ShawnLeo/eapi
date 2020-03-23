package com.shawn.template.controller;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TemplateController
 * @Description TODO
 * @Author Shawn
 * @Date 2020/3/20 14:41
 * @Version 1.0
 **/

@RestController
@RequestMapping("/template")
public class TemplateController {


    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public ResponseEntity templateFile(@RequestParam String name) throws IOException {
//        String codePath = codeGenerateLocation() + File.separator + name  + ".zip";
        final ClassPathResource classPathResource = new ClassPathResource(name);

        HttpHeaders headers = new HttpHeaders();
        File file = classPathResource.getFile();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());

        byte[] bytes = FileUtils.readFileToByteArray(file);

        return new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
    }
}
