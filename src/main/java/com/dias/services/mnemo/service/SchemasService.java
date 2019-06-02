package com.dias.services.mnemo.service;

import com.dias.services.mnemo.dto.SchemeDTO;
import com.dias.services.mnemo.model.Schema;
import com.dias.services.mnemo.repository.SchemasRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SchemasService extends AbstractService<Schema> {

    private static final Logger LOG = LoggerFactory.getLogger(SchemasService.class);
    private final SchemasRepository schemasRepository;
    private final ObjectMapper objectMapper;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public SchemasService(SchemasRepository schemasRepository, ObjectMapper objMapper) {
        this.schemasRepository = schemasRepository;
        this.objectMapper = objMapper;

        Converter<String, JsonNode> stringToJsonNodeConverter = mappingContext -> {
            try {
                String source = mappingContext.getSource();
                if (source != null) {
                    return objectMapper.readTree(source);
                }
            } catch (IOException e) {
                LOG.error("Error while model mapper setup", e);
            }
            return null;
        };

        Converter<JsonNode, String> jsonNodeToStringConverter = mappingContext -> {
            try {
                JsonNode source = mappingContext.getSource();
                if (source != null) {
                    return objectMapper.writeValueAsString(source);
                }
            } catch (IOException e) {
                LOG.error("Error while model mapper setup", e);
            }
            return null;
        };


        PropertyMap<Schema, SchemeDTO> stringToJsonNodeMappings = new PropertyMap<Schema, SchemeDTO>() {
            protected void configure() {
                using(stringToJsonNodeConverter).map(source.getContent()).setContent(null);
            }
        };
        PropertyMap<SchemeDTO, Schema> mapToStringMappings = new PropertyMap<SchemeDTO, Schema>() {
            protected void configure() {
                using(jsonNodeToStringConverter).map(source.getContent()).setContent(null);
            }
        };


        modelMapper.addMappings(stringToJsonNodeMappings);
        modelMapper.addMappings(mapToStringMappings);

    }

    public SchemeDTO getSchema(Long id) {
        Schema schema = getById(id);
        return convertToDTO(schema);
    }

    private SchemeDTO convertToDTO(Schema schema) {
        return modelMapper.map(schema, SchemeDTO.class);
    }

    private Schema convertToBO(SchemeDTO originalSchema) {
        return modelMapper.map(originalSchema, Schema.class);
    }

    protected SchemasRepository getRepository() {
        return schemasRepository;
    }

    public void merge(SchemeDTO originalSchemeDTO, SchemeDTO schemeDTO) {
        Optional.ofNullable(schemeDTO).ifPresent ((SchemeDTO updates) -> {
            Optional.ofNullable(updates.getTitle()).ifPresent(originalSchemeDTO::setTitle);
            Optional.ofNullable(updates.getSchemaType()).ifPresent(originalSchemeDTO::setSchemaType);
            Optional.ofNullable(updates.getContent()).ifPresent(originalSchemeDTO::setContent);
        });
        Schema model = convertToBO(originalSchemeDTO);
        getRepository().update(model);
    }

    public List<SchemeDTO> getAllSchemas() {
        List<Schema> schemas = schemasRepository.getAll();
        return modelMapper.map(schemas, new TypeToken<ArrayList<SchemeDTO>>() {}.getType());
    }

    public void createSchema(SchemeDTO schemeDTO) {
        Schema schema = convertToBO(schemeDTO);
        create(schema);
        schemeDTO.setId(schema.getId());
    }

}
