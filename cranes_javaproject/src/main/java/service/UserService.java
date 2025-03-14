package service;

import dao.UserDAO;
import model.User;

import java.sql.SQLException;

public class UserService {
    public static Integer SaveUser(User user){
        try{
            if(UserDAO.isExit(user.getEmail())){
                return 0;
            }else{
                return UserDAO.saveUser(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
