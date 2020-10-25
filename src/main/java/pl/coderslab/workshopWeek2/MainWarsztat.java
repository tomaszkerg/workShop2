package pl.coderslab.workshopWeek2;

import pl.coderslab.workshopWeek2.entity.User;
import pl.coderslab.workshopWeek2.entity.UserDao;

public class MainWarsztat {
    public static void main(String[] args) {
        UserDao asd = new UserDao();

        User[] users = asd.usersAll();
        for (int i = 0; i < users.length; i++) {
            System.out.println(users[i].getUserName()+" "+users[i].getEmail()+" "+users[i].getPassword());
        }
          asd.options();
    }

}
