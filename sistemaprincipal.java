import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class sistemaprincipal {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(
            System.in,
            java.nio.charset.StandardCharsets.UTF_8
        );

        ArrayList<User> createdaccounts = new ArrayList<>();

        // Carrega as contas salvas no arquivo
        loadAccounts(createdaccounts);

        System.out.println("==========================");
        System.out.println("          System          ");
        System.out.println("==========================");

        boolean system = true;

        while (system) {

            System.out.println("\n[1]. Register Account");
            System.out.println("[2]. Login Account");
            System.out.println("[3]. Forgot your password?");
            System.out.println("[4]. Delete Account");
            System.out.println("[0]. Exit");
            System.out.print("Choose an option: ");

            String login_noptions = scanner.nextLine();

            switch (login_noptions) {

                case "1":

                    System.out.println("\nYou are creating an account");
                    System.out.print("Enter your Gmail or phone: ");

                    String gmail_phone = scanner.nextLine().trim();

                    String telefoneLimpo =
                        gmail_phone.replaceAll("[()\\s-]", "");

                    boolean accountExists = false;

                    for (User user : createdaccounts) {

                        if (user.getGmailPhone()
                                .equalsIgnoreCase(gmail_phone)) {

                            accountExists = true;
                            break;
                        }
                    }

                    if (accountExists) {
                        System.out.println(
                            "This account is already registered!"
                        );
                        break;
                    }

                    boolean gmail_phone_valid = false;

                    // Validação do Gmail
                    if (gmail_phone.matches(
                        "^[a-zA-Z0-9]+([._%+-]?[a-zA-Z0-9]+)*@gmail\\.com$"
                    )) {

                        System.out.println("Gmail format accepted!");
                        gmail_phone_valid = true;

                    // Validação do telefone brasileiro
                    } else if (
                        telefoneLimpo.matches("[0-9]{10,11}")
                        && !telefoneLimpo.matches("([0-9])\\1{9,10}")
                    ) {

                        System.out.println("Phone format accepted!");
                        gmail_phone_valid = true;

                    } else {

                        System.out.println("Invalid Gmail or phone!");
                    }

                    if (gmail_phone_valid) {

                        System.out.print(
                            "Enter your password (minimum 6 characters): "
                        );

                        String password = scanner.nextLine();

                        if (password.length() < 6) {

                            System.out.println(
                                "Password must contain at least 6 characters."
                            );

                        } else {

                            System.out.print("Confirm your password: ");

                            String confirm_password = scanner.nextLine();

                            if (password.equals(confirm_password)) {

                                String passwordHash =
                                    PasswordUtils.hashPassword(password);

                                User newUser =
                                    new User(gmail_phone, passwordHash);

                                createdaccounts.add(newUser);

                                saveAccounts(createdaccounts);

                                System.out.println(
                                    "Account created successfully!"
                                );

                            } else {

                                System.out.println(
                                    "Passwords do not match!"
                                );
                            }
                        }
                    }

                    break;

                case "2":

                    System.out.println("\n===== LOGIN =====");
                    System.out.print("G-mail or Phone to log in: ");

                    String log_in = scanner.nextLine().trim();

                    boolean accountFound = false;
                    User account = null;

                    for (User user : createdaccounts) {

                        if (user.getGmailPhone()
                                .equalsIgnoreCase(log_in)) {

                            accountFound = true;
                            account = user;
                            break;
                        }
                    }

                    if (accountFound) {

                        System.out.print("Enter your password: ");

                        String loginPassword = scanner.nextLine();

                        boolean passwordCorrect =
                            PasswordUtils.verifyPassword(
                                loginPassword,
                                account.getPasswordHash()
                            );

                        if (passwordCorrect) {

                            System.out.println("Login successful!");

                        } else {

                            System.out.println("Incorrect password!");
                        }

                    } else {

                        System.out.println("Account not found!");
                    }

                    break;

                case "3":

                    System.out.println("\n===== PASSWORD RECOVERY =====");
                    System.out.println("[1]. I remember my old password");
                    System.out.println("[2]. I forgot my old password");
                    System.out.println("[0]. Back to main menu");
                    System.out.print("Choose an option: ");

                    String recoveryOption = scanner.nextLine();

                    switch (recoveryOption) {

                        case "1":

                            System.out.print("Enter your Gmail or phone: ");

                            String recoveryAccount =
                                scanner.nextLine().trim();

                            User recoveryUser = null;

                            for (User user : createdaccounts) {

                                if (user.getGmailPhone()
                                        .equalsIgnoreCase(recoveryAccount)) {

                                    recoveryUser = user;
                                    break;
                                }
                            }

                            if (recoveryUser == null) {
                                System.out.println("Account not found!");
                                break;
                            }

                            System.out.print("Enter your old password: ");

                            String oldPassword = scanner.nextLine();

                            boolean oldPasswordCorrect =
                                PasswordUtils.verifyPassword(
                                    oldPassword,
                                    recoveryUser.getPasswordHash()
                                );

                            if (!oldPasswordCorrect) {
                                System.out.println(
                                    "Incorrect old password!"
                                );
                                break;
                            }

                            System.out.println("Old password confirmed!");

                            System.out.print(
                                "Enter your new password (minimum 6 characters): "
                            );

                            String newPassword = scanner.nextLine();

                            if (newPassword.length() < 6) {
                                System.out.println(
                                    "Password must contain at least 6 characters"
                                );
                                break;
                            }

                            System.out.print("Confirm your new password: ");

                            String confirmNewPassword = scanner.nextLine();

                            if (!newPassword.equals(confirmNewPassword)) {
                                System.out.println(
                                    "Passwords do not match!"
                                );
                                break;
                            }

                            String newPasswordHash =
                                PasswordUtils.hashPassword(newPassword);

                            recoveryUser.setPasswordHash(newPasswordHash);

                            saveAccounts(createdaccounts);

                            System.out.println(
                                "Password changed successfully!"
                            );

                            break;

                        case "2":

                            System.out.println(
                                "Recover account by verification code."
                            );

                            System.out.print(
                                "Enter your registered Gmail or phone: "
                            );

                            String recoveryAccountCode =
                                scanner.nextLine().trim();

                            User codeRecoveryUser = null;

                            for (User user : createdaccounts) {

                                if (user.getGmailPhone()
                                        .equalsIgnoreCase(recoveryAccountCode)) {

                                    codeRecoveryUser = user;
                                    break;
                                }
                            }

                            if (codeRecoveryUser == null) {
                                System.out.println("Account not found!");
                                break;
                            }

                            int verificationCode =
                                new Random().nextInt(900000) + 100000;

                            System.out.println(
                                "Your verification code is: "
                                + verificationCode
                            );

                            System.out.print(
                                "Enter the verification code: "
                            );

                            int enteredCode;

                            try {
                                enteredCode =
                                    Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println(
                                    "Invalid verification code!"
                                );
                                break;
                            }

                            if (enteredCode != verificationCode) {
                                System.out.println(
                                    "Incorrect verification code!"
                                );
                                break;
                            }

                            System.out.println("Verification successful!");

                            System.out.print(
                                "Enter your new password (minimum 6 characters): "
                            );

                            String newPasswordCode = scanner.nextLine();

                            if (newPasswordCode.length() < 6) {
                                System.out.println(
                                    "Password must contain at least 6 characters."
                                );
                                break;
                            }

                            System.out.print(
                                "Confirm your new password: "
                            );

                            String confirmPasswordCode = scanner.nextLine();

                            if (!newPasswordCode.equals(confirmPasswordCode)) {
                                System.out.println(
                                    "Passwords do not match!"
                                );
                                break;
                            }

                            String newPasswordHashCode =
                                PasswordUtils.hashPassword(newPasswordCode);

                            codeRecoveryUser.setPasswordHash(
                                newPasswordHashCode
                            );

                            saveAccounts(createdaccounts);

                            System.out.println(
                                "Password changed successfully!"
                            );

                            break;

                        case "0":

                            System.out.println(
                                "Returning to main menu..."
                            );

                            break;

                        default:

                            System.out.println("Invalid option!");
                    }

                    break;

                case "4":

                    System.out.println("\n===== DELETE ACCOUNT =====");
                    System.out.print("Enter your Gmail or phone: ");

                    String accountToDelete =
                        scanner.nextLine().trim();

                    System.out.print("Enter your password: ");

                    String deletePassword = scanner.nextLine();

                    User userToDelete = null;

                    for (User user : createdaccounts) {

                        if (user.getGmailPhone()
                                .equalsIgnoreCase(accountToDelete)) {

                            userToDelete = user;
                            break;
                        }
                    }

                    if (userToDelete == null) {
                        System.out.println(
                            "Account can't be deleted because it does not exist!"
                        );
                        break;
                    }

                    boolean deletePasswordCorrect =
                        PasswordUtils.verifyPassword(
                            deletePassword,
                            userToDelete.getPasswordHash()
                        );

                    if (deletePasswordCorrect) {

                        System.out.println(
                            "\nDo you really want to delete this account?"
                        );

                        System.out.println("[1]. Yes");
                        System.out.println("[0]. No");
                        System.out.print("Choose an option: ");

                        String confirmDelete = scanner.nextLine();

                        if (confirmDelete.equals("1")) {

                            createdaccounts.remove(userToDelete);

                            saveAccounts(createdaccounts);

                            System.out.println(
                                "Account deleted successfully!"
                            );

                        } else if (confirmDelete.equals("0")) {

                            System.out.println(
                                "Account deletion cancelled."
                            );

                        } else {

                            System.out.println(
                                "Invalid option. Account was not deleted."
                            );
                        }

                    } else {

                        System.out.println(
                            "Incorrect password! Account was not deleted."
                        );
                    }

                    break;

                case "0":

                    System.out.println("System closed.");
                    system = false;
                    break;

                default:

                    System.out.println(
                        "Invalid option. Please choose an option shown on the screen."
                    );
            }
        }

        scanner.close();

    } // Fecha o main


    // Salva todas as contas no arquivo
    public static void saveAccounts(ArrayList<User> createdaccounts)
            throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("contas.txt"))) {

            for (User user : createdaccounts) {
                writer.write(user.toFile());
                writer.newLine();
            }
        }
    }


    // Carrega as contas do arquivo
    public static void loadAccounts(ArrayList<User> createdaccounts)
            throws IOException {

        try (BufferedReader reader = new BufferedReader(
                new FileReader("contas.txt"))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(";", 2);

                if (data.length == 2) {

                    User user = new User(data[0], data[1]);

                    createdaccounts.add(user);
                }
            }

        } catch (FileNotFoundException e) {

            // Se o arquivo ainda não existir, o programa inicia sem contas.
            System.out.println(
                "No saved accounts found. You can register a new account."
            );
        }
    }

}