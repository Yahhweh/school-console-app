package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.entity.Group;

import java.util.Random;

public class InitialGroups implements InitialData {

    private final GroupDao groupDao;

    public InitialGroups(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public void generate() {
        int AMOUNT_OF_GROUPS = 10;
        for (int i = 0; i < AMOUNT_OF_GROUPS; i++) {
            Group group = new Group(generateString());
            groupDao.save(group);
        }
    }

    static String generateString() {
        final int ALPHABET_SIZE = 26;
        final int RANDOM_NUMBER_LIMIT = 10;

        Random random = new Random();

        char ch1 = (char) ('A' + random.nextInt(ALPHABET_SIZE));
        char ch2 = (char) ('A' + random.nextInt(ALPHABET_SIZE));

        return ch1 + "" + ch2 + "-" + random.nextInt(RANDOM_NUMBER_LIMIT) + random.nextInt(RANDOM_NUMBER_LIMIT);
    }
}
