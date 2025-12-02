package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroup;
import kegly.organisation.schoolconsoleapp.entity.Group;

import java.util.Random;

public class GroupsGenerator implements Seeder {

    private final static int ALPHABET_SIZE = 26;
    private final static int RANDOM_NUMBER_LIMIT = 10;
    private final JdbcGroup jdbcGroup;

    public GroupsGenerator(JdbcGroup jdbcGroup) {
        this.jdbcGroup = jdbcGroup;
    }

    @Override
    public void generate(int amount) {
        for (int i = 0; i < amount; i++) {
            Group group = new Group(generateGroupName());
            jdbcGroup.save(group);
        }
    }

    private String generateGroupName() {

        Random random = new Random();

        char ch1 = (char) ('A' + random.nextInt(ALPHABET_SIZE));
        char ch2 = (char) ('A' + random.nextInt(ALPHABET_SIZE));

        return ch1 + "" + ch2 + "-" + random.nextInt(RANDOM_NUMBER_LIMIT) + random.nextInt(RANDOM_NUMBER_LIMIT);
    }
}
