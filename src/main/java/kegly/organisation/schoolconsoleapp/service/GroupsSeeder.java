package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Group;

import java.util.Random;

public class GroupsSeeder implements Seeder {

    private final GroupDaoImpl groupDaoImpl;

    public GroupsSeeder(GroupDaoImpl groupDaoImpl) {
        this.groupDaoImpl = groupDaoImpl;
    }

    @Override
    public void generate(int amount) {
        for (int i = 0; i < amount; i++) {
            Group group = new Group(generateString());
            groupDaoImpl.save(group);
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
