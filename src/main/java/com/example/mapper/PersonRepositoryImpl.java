package com.example.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PersonRepositoryImpl implements PersonRepository {
    private static final Logger log = LoggerFactory.getLogger(PersonRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    public PersonRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Person person) {
        log.info("adding " + person);
        jdbcTemplate.update("INSERT INTO person(name, dob) VALUES (?,?)",
                person.getName(),
                person.getDob());
    }

    @Override
    public List<Person> find() {
        List<Person> persons = this.jdbcTemplate.query(
                "select * from person",
                new PersonMapper());
        log.info("Found " + persons.size() + " persons");
        return persons;
    }

    private static class PersonMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setId(rs.getInt("id"));
            person.setName(rs.getString("name"));
            person.setDob(rs.getDate("dob").toLocalDate());
            return person;
        }
    }
}
