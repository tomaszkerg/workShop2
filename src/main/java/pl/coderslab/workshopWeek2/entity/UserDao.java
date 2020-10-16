package pl.coderslab.workshopWeek2.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.workshopWeek2.DBUtil;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Scanner;

public class UserDao {
    private final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES(?, ?, ?);";
    private final String DELETE_USER_BYID =
            "DELETE FROM users WHERE id = ?;";
    private final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?;";
    private final String printUsers = "SELECT * FROM users;";
    private final String printUser = "SELECT * FROM users WHERE id = ?";
    private final String findAll = "SELECT * FROM users";
    private Scanner scan = new Scanner(System.in);

    private void editor(User user) {
        int id = user.getId();
        System.out.println("podaj nową nazwe użytkownika:");
        String par1 = scan.nextLine();
        System.out.println("podaj nowy adres email:");
        String par2 = scan.nextLine();
        System.out.println("podaj nowe hasło:");
        String par3 = scan.nextLine();
        par3 = hashPassword(par3);
        try (PreparedStatement statement =
                     DBUtil.conWorkshop().prepareStatement(UPDATE_USER_QUERY);) {
            statement.setString(1, par1);
            statement.setString(2, par2);
            statement.setString(3, par3);
            statement.setInt(4, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("udało się dokonac edycji");
    }

    public void options() {
        printMenu();
        String get = scan.nextLine();
        while (!get.equals("l") || !get.equals("d") || !get.equals("e") || !get.equals("u") || !get.equals("x")) {
            switch (get) {
                case "l":
                    printList();
                    options();
                case "d":
                    create(addUser());
                    options();
                case "e":
                    User user = read();
                    editor(user);
                    options();
                case "u":
                    deleteLine();
                case "x":
                    System.exit(1);
            }
            System.out.println("podaj poprawną wartość:");
            get = scan.nextLine();
        }
    }

    private User addUser() {
        System.out.println("podaj username użytkownika");
        String userName = scan.nextLine();
        System.out.println("podaj email użytkownika");
        String email = scan.nextLine();
        System.out.println("podaj hasło użytkownika");
        String password = scan.nextLine();
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    private void printList() {
        try {
            DBUtil.printData(DBUtil.conWorkshop(), printUsers, "id", "username", "email", "password");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteLine() {
        System.out.println("podaj wiersz do usuniecia:");
        String input = scan.nextLine();
        int input2 = Integer.parseInt(input);
        System.out.println("czy na pewno chcesz usunac T/N?");
        String sure = scan.nextLine();
        if (sure.equals("T")) {
            DBUtil.remove(DBUtil.conWorkshop(), DELETE_USER_BYID, input2);
            System.out.println("udało się usunać wiersz");
            options();
        } else if (sure.equals("N")) {
            options();
        } else {
            System.out.println("podałeś nieprawidłową wartość: ");
            options();
        }
    }

    public User[] usersAll() {
        User[] users = new User[0];
        try (Connection conn = DBUtil.conWorkshop()) {
            PreparedStatement statement = conn.prepareStatement(findAll);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users = Arrays.copyOf(users, users.length + 1);
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users[users.length - 1] = user;
            }
        } catch (SQLException e) {

        }
        return users;
    }

    public User create(User user) {
        try (Connection conn = DBUtil.conWorkshop()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            System.out.println("udało się dodać użykownika");
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read() {
        Connection conn = DBUtil.conWorkshop();
        System.out.println("podaj id użytkownika");
        String userId = scan.nextLine();
        try (PreparedStatement statement = conn.prepareStatement(printUser)) {
            statement.setInt(1, Integer.parseInt(userId));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String hashPassword(String password) {

        return BCrypt.hashpw(password, BCrypt.gensalt());

    }

    public void printMenu() {
        System.out.println("co byś chciał zrobić");
        System.out.println("l - lista pozycji");
        System.out.println("d - dodać nowego");
        System.out.println("e - edycja");
        System.out.println("u - usunąć wiersz");
        System.out.println("x - wyjść z programu");
    }
}
