package com.dias.services.mnemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SchemasControllerTest extends AbstractModuleTest {

    private static Integer createdSchemaId;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws IOException {
        super.setUp();
    }

    @Test
    public void order010createNewReport() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/schemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Util.readResource("SchemasController/schema.json")))
                .andExpect(status().isCreated()).andReturn();
        Map resultMap = new JacksonJsonParser().parseMap(result.getResponse().getContentAsString());
        createdSchemaId = (Integer) resultMap.get("id");
        Assert.assertNotNull(createdSchemaId);
    }

    @Test
    public void order020updateReport() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/schemas/" + createdSchemaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Util.readResource("SchemasController/updateSchema.json")))
                .andExpect(status().isOk()).andReturn();
        Map resultMap = new JacksonJsonParser().parseMap(result.getResponse().getContentAsString());
        String updatedName = (String) resultMap.get("title");
        Assert.assertEquals("Test Schema Updated", updatedName);
    }


    @Test
    public void order030getAllSchemas() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/schemas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        List<Object> listResports = new JacksonJsonParser().parseList(result.getResponse().getContentAsString());
        Assert.assertEquals(1, listResports.size());
        Assert.assertEquals(createdSchemaId, ((Map)listResports.get(0)).get("id"));
    }

    @Test
    public void order060deleteReport() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/schemas/" + createdSchemaId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/schemas"))
                .andExpect(status().isOk()).andReturn();
        List<Object> resultList = new JacksonJsonParser().parseList(result.getResponse().getContentAsString());
        Assert.assertEquals(0, resultList.size());

    }

}
