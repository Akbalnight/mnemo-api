package com.dias.services.mnemo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер вывода версии модуля
 *
 * @author Eliseev Anton
 */
@RestController
@RequestMapping("/version")
@Api(value = "Api for vesion")
public class VersionController {

    private final BuildProperties bp;

    @Autowired
    public VersionController(BuildProperties bp) {
        this.bp = bp;
    }

    /**
     * Вывод версии модуля
     *
     * @return - ResponseEntity
     */

    @ApiOperation(value = "Вывод версии модуля")
    @GetMapping("")
    public ResponseEntity<String> getDailyReport() {

        try {

            return ResponseEntity.ok().body(bp.getVersion());
        } catch (Exception ex) {
            return new ResponseEntity<>("Ошибка при работе сервера", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}

