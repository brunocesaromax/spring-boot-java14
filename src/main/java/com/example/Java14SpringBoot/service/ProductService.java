package com.example.Java14SpringBoot.service;

import com.example.Java14SpringBoot.enumerate.ProductStatus;
import com.example.Java14SpringBoot.records.ProductRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Types;
import java.util.List;

import static com.example.Java14SpringBoot.enumerate.ProductStatus.ACTIVE;
import static com.example.Java14SpringBoot.enumerate.ProductStatus.INACTIVE;

@Service
public class ProductService {

    //Utilizado para projetos simples
    private final JdbcTemplate template;

    //Text-blocks
    private final String findByIdSql = """
            select * from product \
            where id = ?
            """;

    private final String insertSql = """
                insert into product (name, status)
                values (?,?)
            """;

    private final RowMapper<ProductRecord> productRecordRowMapper = (rs, rowNum) -> new ProductRecord(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("status"));

    public ProductService(JdbcTemplate template) {
        this.template = template;
    }

    public ProductRecord findById(Long id) {
        return template.queryForObject(findByIdSql, new Object[]{id}, productRecordRowMapper);
    }

    public ProductRecord create(String name, ProductStatus status) {
        //Switch com lambda
        var statusCode = switch (status) {
            case ACTIVE -> 1;
            case INACTIVE -> 0;
        };

        var params = List.of(
                new SqlParameter(Types.VARCHAR, "name"),
                new SqlParameter(Types.INTEGER, "status")
        );

        var pscf = new PreparedStatementCreatorFactory(insertSql, params) {
            {
                setReturnGeneratedKeys(true);
                setGeneratedKeysColumnNames("id");
            }
        };

        var psc = pscf.newPreparedStatementCreator(List.of(name, statusCode));
        var generatedKey = new GeneratedKeyHolder();

        this.template.update(psc, generatedKey);

        //Pattern matching for instanceof
        if (generatedKey.getKey() instanceof BigInteger id) {
            return findById(id.longValue());
        }

        throw new IllegalArgumentException("Não foi possível criar produto " + name + ".");
    }
}
