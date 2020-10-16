package pl.coderslab.workshopWeek2;

import pl.coderslab.workshopWeek2.entity.User;

public class MainWarsztat {
    public static void main(String[] args) {
        User[] users = UserDaousersAll();
        for (int i = 0; i < users.length; i++) {
            System.out.println(users[i].getUserName()+" "+users[i].getEmail()+" "+users[i].getPassword());
        }
//          UserDao.options();
    }

}
