package com.dias.services.mnemo.repository;

import com.dias.services.mnemo.model.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    static class SchemaRowMapper implements RowMapper {

        @Override
        public Schema mapRow(ResultSet rs, int rowNumber) throws SQLException {
            Schema schema = new Schema();
            schema.setId(rs.getLong("id"));
            schema.setTitle(rs.getString("title"));
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
        return "public.mnemo_schemas";
    }

    @Override
    public RowMapper<Schema> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getInsertSql() {
        return "insert into public.mnemo_schemas (" +
                "id," +
                "title," +
                "created_by," +
                "content" +
                ") values (" +
                "nextval('mnemo_schemas_id_seq')," +
                ":title," +
                ":createdBy," +
                ":content)";
    }

    @Override
    protected String getUpdateSql() {
        return "update public.mnemo_schemas set " +
                "title=:title, " +
                "created_by=:createdBy, " +
                "content=:content " +
                "where id=:id";
    }

}
