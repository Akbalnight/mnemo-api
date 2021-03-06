package com.dias.services.mnemo.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface AbstractController {

    default ResponseEntity<Resource> downloadExcel(String resultFile, byte[] content) {
        MediaType mediaType = new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return downloadContent(resultFile, content, mediaType);
    }

    default ResponseEntity<Resource> downloadPdf(String resultFile, byte[] content) {
        return downloadContent(resultFile, content, MediaType.APPLICATION_PDF);
    }

    default ResponseEntity<Resource> downloadContent(String resultFile, byte[] content, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("charset", "utf-8");
        headers.setContentType(mediaType);
        headers.setContentLength(content.length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resultFile);

        return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(content));
    }

}
