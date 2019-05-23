package com.dias.services.mnemo.repository;

import com.dias.services.mnemo.model.Schema;
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
import java.util.logging.Logger;

@Repository
public class SchemasRepository extends AbstractRepository<Schema> {

    private static final String INIT_SCRIPT = "/db/schema.sql";

    private static Logger LOG = Logger.getLogger(SchemasRepository.class.getName());

    @Autowired
    public SchemasRepository(NamedParameterJdbcTemplate template) {
        super(template);
    }

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

    private static final RowMapper<Schema> ROW_MAPPER = new SchemaRowMapper();


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
            LOG.severe("Ошибка инициализации модуля");
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
            } catch (Exception ignore) {
            }
        }

    }

    @Override
    public String getTableName() {
        return "schemas";
    }

    @Override
    public RowMapper<Schema> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getInsertSql() {
        return "insert into schemas (" +
                "id," +
                "title," +
                "created_by," +
                "content" +
                ") values (" +
                "nextval('schemas_id_seq')," +
                ":title," +
                ":createdBy," +
                ":content)";
    }

    @Override
    protected String getUpdateSql() {
        return "update schemas set " +
                "title=:title, " +
                "created_by=:createdBy, " +
                "content=:content " +
                "where id=:id";
    }

}
