package com.dias.services.mnemo;

import com.dias.services.mnemo.repository.SchemasRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@SpringBootTest(classes = {AbstractModuleTest.TestConfig.class})
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
public abstract class AbstractModuleTest {

    private static boolean inited = false;

    @TestConfiguration
    @ComponentScan({"com.dias.services.mnemo"})
    static class TestConfig {

        @Autowired
        NamedParameterJdbcTemplate template;

        @Bean
        public SchemasRepository schemasRepository() {

            return new SchemasRepository(template) {

                @Override
                protected String getInsertSql() {
                    String insertSql = super.getInsertSql();
                    //для работы в h2 базе нужно переделать генерацию id
                    return insertSql.replace("nextval('schemas_id_seq')","schemas_id_seq.NEXTVAL");
                }
            };
        }

    }

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private SchemasRepository schemasRepository;

    @Before
    public void setUp() throws IOException {

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();

        if (!inited) {
            inited = true;
            //создаем тестовые данные
            // schemasRepository.executeSqlFromFile(getClass(), template, "/SchemasController/tablesAndData.sql");

        }
    }
}
