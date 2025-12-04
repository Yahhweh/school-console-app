package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.entity.Group;

import java.util.Random;

public class GroupsGenerator implements DataGenerator {

    private final static int ALPHABET_SIZE = 26;
    private final static int RANDOM_NUMBER_LIMIT = 10;
    private final GroupDao groupDao;

    public GroupsGenerator(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public void generate(int amount) {
        for (int i = 0; i < amount; i++) {
            Group group = new Group(generateGroupName());
            groupDao.save(group);
        }
    }

    private String generateGroupName() {
        Random random = new Random();

        char ch1 = (char) ('A' + random.nextInt(ALPHABET_SIZE));
        char ch2 = (char) ('A' + random.nextInt(ALPHABET_SIZE));

        return ch1 + "" + ch2 + "-" + random.nextInt(RANDOM_NUMBER_LIMIT) + random.nextInt(RANDOM_NUMBER_LIMIT);
    }
}
