package com.dias.services.mnemo.controller;

import com.dias.services.mnemo.dto.SchemeDTO;
import com.dias.services.mnemo.exception.SchemasException;
import com.dias.services.mnemo.service.SchemasService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "REST API модуля мнемосхем")
public class SchemasController implements AbstractController {

    private final SchemasService schemasService;

    @Autowired
    public SchemasController(SchemasService schemasService) {
        this.schemasService = schemasService;
    }

    @ApiOperation(value = "Получение всех схем")
    @GetMapping(value = "/schemas", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SchemeDTO> getAll() {
        return schemasService.getAllSchemas();
    }

    @ApiOperation(value = "Получение схемы по id")
    @GetMapping(value = "/schemas/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SchemeDTO getById(@PathVariable Long id) {
        return schemasService.getSchema(id);
    }

    @ApiOperation(value = "Создание схемы")
    @PostMapping(value = "/schemas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SchemeDTO> create(@RequestBody SchemeDTO schemaDTO) {
        try {
            schemasService.createSchema(schemaDTO);
        } catch (DuplicateKeyException duplicateException) {
            throw new SchemasException("Ошибка создания схемы: %s", HttpStatus.BAD_REQUEST, duplicateException.getMessage());
        }
        return new ResponseEntity<>(schemaDTO, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Обновление схемы")
    @PutMapping(value = "/schemas/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SchemeDTO> update(@PathVariable Long id, @RequestBody SchemeDTO schemeDTO) {
        SchemeDTO originalSchema = schemasService.getSchema(id);
        try {
            schemasService.merge(originalSchema, schemeDTO);
        } catch (DuplicateKeyException duplicateException) {
            throw new SchemasException("Ошибка обновления схемы: %s", HttpStatus.BAD_REQUEST, duplicateException.getMessage());
        }
        return new ResponseEntity<>(originalSchema, HttpStatus.OK);
    }

    @ApiOperation(value = "Удаление схемы")
    @DeleteMapping("/schemas/{id}")
    public void delete(@PathVariable Long id) {
        schemasService.delete(id);
    }

}
