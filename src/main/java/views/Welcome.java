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
import java.util.Scanner;

public class Welcome {
    public void WelcomeScreen () {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome ! ");
        System.out.println("Press 1 to Login : ");
        System.out.println("Press 2 to Signup : ");
        System.out.println("Press 0 to exit : ");
        int choice = 0;
        try {
            choice = Integer.parseInt(br.readLine());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        switch (choice) {
            case 1 -> login();
            case 2 -> signUp();
            case 3 -> System.exit(0);
        }
    }

    private void login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the email - ");
        String email = sc.nextLine();
        try{
            if(UserDAO.isExist(email) ) {
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email,genOTP);
                System.out.print("Enter the OTP - ");
                String otp = sc.nextLine();
                if(otp.equals(genOTP)) {
                    new UserView(email).home();
                } else {
                    System.out.print("Incorrect OTP");
                }

            } else {
                System.out.print("User not Found");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    private void signUp() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name - ");
        String name = sc.nextLine();
        System.out.print("Enter Email - ");
        String email = sc.nextLine();
        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email,genOTP);
        System.out.print("enter the OTP - ");
        String otp = sc.nextLine();
        if(otp.equals(genOTP)) {
            User user = new User(name,email);
            System.out.println("Incorrect OTP");
            int response = UserService.saveUser(user);
            switch (response) {
                case 0 -> System.out.print("User registered");
                case 1 -> System.out.print("User already exists");
            }
        } else {
            System.out.print("Wrong OTP");
        }

    }
}
