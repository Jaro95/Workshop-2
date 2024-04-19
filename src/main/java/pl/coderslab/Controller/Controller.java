package pl.coderslab.Controller;

import com.mysql.cj.exceptions.NumberOutOfRange;
import pl.coderslab.DAO.UserDao;
import pl.coderslab.Model.User;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    public void startDB() {
        List<User> userList = new ArrayList<>();
        UserDao userDao = new UserDao();

        //SECTION CREATE
        userDao.create(new User("Kakarotto", "goku@gmail.com","GOKU"));

        //SECTION READ
        String searchColumn = "";
        String searchValue = "";
        try {
            //searchColumn = "id";
            //searchValue = "1";
            //userList = userDao.readByColumn(searchColumn, searchValue);
            searchColumn = "usernameee";
            searchValue = "Naruto";
            userList = userDao.readByColumn(searchColumn, searchValue);
        } catch (SecurityException e){
            System.out.println("Column: \"" + searchColumn + "\" not exist");
        }
        catch (NumberOutOfRange e) {
            System.out.println("Value: \"" + searchValue + "\" in column: \"" + searchColumn + "\" could not be found");
        }
        showUserList(userList);

        //SECTION UPDATE
        int updateId = 0;
        try {
            User userUpdate = userDao.read(updateId);
            userUpdate.setUserName("Naruto");
            userUpdate.setEmail("goku@gmail.com");
            userUpdate.setPassword(userDao.hashPassword("Itachi"));
            userDao.update(userUpdate);
        } catch (NumberOutOfRange e) {
            System.out.println("Id: " + updateId + " not found");
        }
        //Update by condition
        String columnUpdate = "password";
        String searchEmail = "uchiha@gmail.com";
        String newValue = "TEST";
        try {
            User userUpdate = userDao.readByColumn("email", searchEmail).get(0);
            userDao.updateByColumnAndValue(userUpdate, columnUpdate, newValue);
        } catch (NumberOutOfRange e) {
            System.out.println("Id: " + updateId + " not found");
        }

        //SECTION DELETE
        int deleteId = 0;
        try {
            deleteId = 37;
            userDao.delete(deleteId);
            System.out.println("Id: " + deleteId + " deleted successfully" );
        } catch (NullPointerException e) {
            System.out.println("Id: " + deleteId + " not exist");
        }

        try {
            searchColumn = "username";
            searchValue = "HinataA";
            userDao.deleteByColumnAndValue(searchColumn, searchValue);
            System.out.println(searchColumn + " " +  searchValue + " deleted successfully" );
        } catch (SecurityException e) {
            System.out.println("Column: " +  searchColumn + " not exist");
        } catch (NullPointerException e) {
            System.out.println("Value: \"" + searchValue + "\" not exist");
        }

        //Section Find All
        List<User> allUserList = userDao.findAll();
        showUserList(allUserList);
    }

    public void showUserList(List<User> userList) {
        for (User readUser : userList) {
            System.out.println(readUser);
        }
    }
}
