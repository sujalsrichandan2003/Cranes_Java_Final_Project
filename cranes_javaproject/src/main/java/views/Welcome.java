package views;

import dao.UserDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Welcome {
    public void WelcomeScreen(){
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to app");
        System.out.println("Press 1 to Login");
        System.out.println("Press 2 to signup");
        System.out.println("Press 0 to  exit");
        int choice =0;
        try{
            choice= Integer.parseInt(br.readLine());
        }catch (IOException ex){
            ex.printStackTrace();
        }
        switch(choice){
            case 1->login();
            case 2->Signup();
            case 0-> System.exit(0);
        }
    }

    private void Signup() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name:");
        String name = sc.nextLine();
        System.out.println("Enter email:");
        String email = sc.nextLine();
        String genotp = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email,genotp);
        System.out.println("Enter the otp");
        String otp = sc.nextLine();
        if(otp.equals(genotp)) {
            User user = new User(name, email);
            int response = UserService.SaveUser(user);
            switch (response) {
                case 0 -> System.out.println("User registered");
                case 1 -> System.out.println("User already exists");
            }
        } else {
            System.out.println("Wrong OTP");
        }
    }

    private void login() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter email:");
        String email = sc.nextLine();
        try{
            if(UserDAO.isExit(email)){
                String genotp = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email,genotp);
                System.out.println("Enter the otp");
                String otp = sc.nextLine();
                if(otp.equals(genotp)){
                    new UserView(email).home();

                }else{
                    System.out.println("Wrong otp");
                }
            }else{
                System.out.println("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
