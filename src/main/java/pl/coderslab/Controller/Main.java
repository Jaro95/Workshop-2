package pl.coderslab.Controller;

import com.mysql.cj.exceptions.NumberOutOfRange;
import pl.coderslab.DAO.UserDao;
import pl.coderslab.Model.User;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        UserDao userDao = new UserDao();
        //SECTION ADD

        userDao.create(new User("Itachi", "sasuke@gmail.com","Sasuke"));

        //SECTION READ
        String searchColumn = "";
        String searchValue = "";
        try {
            searchColumn = "id";
            searchValue = "1";
            userList.add(userDao.readByColumn(searchColumn, searchValue));
            searchColumn = "username";
            searchValue = "Naruto";
            userList.add(userDao.readByColumn(searchColumn, searchValue));
        } catch (SecurityException e){
            System.out.println("Column " + searchColumn + " not exist");
        }
        catch (NumberOutOfRange e) {
            System.out.println("Value " + searchValue + " in column " + searchColumn + " could not be found");
        }
        //showUserList(userList);
        //SECTION UPDATE
        /*
        int updateId = 1;
        if(checkInListById(userList ,updateId)) {
            User userUpdate = userList.get(updateId - 1);
            System.out.println(userUpdate);
            userUpdate.setUserName("Sasuke");
            userUpdate.setEmail("uchiha@gmail.com");
            userUpdate.setPassword("Itachi");
            userDao.update(userUpdate);
        }
       */
        //SECTION DELETE
        userDao.delete(3);

        //Section Find All
        List<User> allUserList = userDao.findAll();
        showUserList(allUserList);


    }

    public static void showUserList(List<User> userList) {
        for (User readUser : userList) {
            System.out.println(readUser);
        }
    }

    public static boolean checkInListById(List<User> userList, int id) {
        boolean check = false;
        for (User readUser : userList) {
            if (readUser.getId() == id) {
                check = true;
            }
        }
        return check;
    }
}