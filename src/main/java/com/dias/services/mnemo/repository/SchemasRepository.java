package com.dias.services.mnemo.repository;

import com.dias.services.mnemo.model.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SchemasRepository extends AbstractRepository<Schema> {

    @Value("${schemas.table:public.mnemo_schemas}")
    private String schemasTable;

    static class SchemaRowMapper implements RowMapper {

        @Override
        public Schema mapRow(ResultSet rs, int rowNumber) throws SQLException {
            Schema schema = new Schema();
            schema.setId(rs.getLong("id"));
            schema.setTitle(rs.getString("title"));
            schema.setSchemaType(rs.getString("schema_type"));
            schema.setContent(rs.getString("content"));
            schema.setCreatedBy(rs.getString("created_by"));
            return schema;
        }
    }

    private static final String INIT_SCRIPT = "/db/schema.sql";
    private static final RowMapper<Schema> ROW_MAPPER = new SchemaRowMapper();
    private static final Logger LOG = LoggerFactory.getLogger(SchemasRepository.class);

    @Autowired
    public SchemasRepository(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @PostConstruct
    public void init() {
        createTablesIfNeeded();
    }

    /**
     * Создание таблиц в БД если они не существуют
     */
    private void createTablesIfNeeded() {
        try {
            executeSqlFromFile(getClass(), template, INIT_SCRIPT);
        } catch (Exception e) {
            LOG.error("Ошибка инициализации модуля", e);
        }
    }

    /**
     * Выполняет SQL скрипт из указанного файла
     *
     * @param path
     * @throws IOException
     */
    @Transactional
    public void executeSqlFromFile(Class clazz, NamedParameterJdbcTemplate template, String path) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(clazz.getResourceAsStream(path), "UTF-8");
        LineNumberReader reader = new LineNumberReader(streamReader);
        String query = ScriptUtils.readScript(reader, "--", ";");
        query = query.replaceAll("\\{schemas.table\\}", schemasTable);
        List<String> queries = new ArrayList<>();
        ScriptUtils.splitSqlScript(query, ";", queries);
        for (String qry: queries) {
            try {
                template.getJdbcOperations().execute(qry);
            } catch (Exception e) {
                LOG.error("Ошибка [{}] выполнения запроса [{}]", e.getMessage(), qry);
            }
        }

    }

    @Override
    public String getTableName() {
        return schemasTable;
    }

    @Override
    public RowMapper<Schema> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getInsertSql() {
        return "insert into " + schemasTable + " (" +
                "id," +
                "title," +
                "schema_type," +
                "created_by," +
                "content" +
                ") values (" +
                "nextval('public.mnemo_schemas_id_seq')," +
                ":title," +
                ":schemaType," +
                ":createdBy," +
                ":content)";
    }

    @Override
    protected String getUpdateSql() {
        return "update " + schemasTable + " set " +
                "title=:title, " +
                "schema_type=:schemaType, " +
                "created_by=:createdBy, " +
                "content=:content " +
                "where id=:id";
    }

}
