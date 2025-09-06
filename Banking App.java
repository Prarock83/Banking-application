import java.util.Scanner;

/**
 * Banking Application for Account Management
 * 
 * This program allows users to create accounts, deposit money,
 * withdraw money, view account details, and update contact details.
 * It demonstrates Java basics including arrays, control structures,
 * exception handling, and string manipulation.
 * 
 * Instructions:
 * Compile with: javac BankingApp.java
 * Run with: java BankingApp
 */
class Account {
    private final int accountNumber;
    private String accountHolderName;
    private double balance;
    private String email;
    private String phoneNumber;

    public Account(int accountNumber, String accountHolderName, double initialDeposit,
                   String email, String phoneNumber) {
        this.accountNumber = accountNumber;
        this.accountHolderName = normalizeName(accountHolderName);
        this.balance = initialDeposit;
        this.email = email.trim();
        this.phoneNumber = phoneNumber.trim();
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        balance -= amount;
    }

    public void updateContactDetails(String email, String phoneNumber) {
        this.email = email.trim();
        this.phoneNumber = phoneNumber.trim();
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    private static String normalizeName(String name) {
        String trimmed = name.trim();
        String[] parts = trimmed.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            char first = Character.toUpperCase(part.charAt(0));
            sb.append(first).append(part.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return "Account{" +
                "Account Number=" + accountNumber +
                ", Name='" + accountHolderName + '\'' +
                ", Balance=" + String.format("%.2f", balance) +
                ", Email='" + email + '\'' +
                ", Phone='" + phoneNumber + '\'' +
                '}';
    }
}

class UserInterface {
    private static final int MAX_ACCOUNTS = 100;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PHONE_REGEX = "^[0-9]{10}$";

    private final Account[] accounts = new Account[MAX_ACCOUNTS];
    private int accountCount = 0;
    private int nextAccountNumber = 1001;

    private final Scanner sc = new Scanner(System.in);

    public void mainMenu() {
        while (true) {
            System.out.println("\n===============================");
            System.out.println("  Welcome to Banking System  ");
            System.out.println("===============================");
            System.out.println("1. Create a new account");
            System.out.println("2. Deposit money");
            System.out.println("3. Withdraw money");
            System.out.println("4. View account details");
            System.out.println("5. Update contact details");
            System.out.println("6. List all accounts");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> performDeposit();
                case 3 -> performWithdrawal();
                case 4 -> showAccountDetails();
                case 5 -> updateContact();
                case 6 -> listAllAccounts();
                case 7 -> {
                    System.out.println("Thank you for using the Banking System. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createAccount() {
        if (accountCount >= MAX_ACCOUNTS) {
            System.out.println("Cannot create more accounts.");
            return;
        }
        System.out.print("Enter account holder name: ");
        String name = sc.nextLine().trim();
        double initialDeposit = readPositiveDouble("Enter initial deposit amount: ");

        String email = readValidated("Enter email address: ", EMAIL_REGEX, "Invalid email format.");
        String phone = readValidated("Enter phone number (10 digits): ", PHONE_REGEX, "Invalid phone number format.");

        Account account = new Account(nextAccountNumber++, name, initialDeposit, email, phone);
        accounts[accountCount++] = account;
        System.out.println("Account created successfully! Account Number: " + account.getAccountNumber());
    }

    private void performDeposit() {
        int accNo = readInt("Enter account number: ");
        Account account = findAccount(accNo);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        double amount = readPositiveDouble("Enter amount to deposit: ");
        try {
            account.deposit(amount);
            System.out.println("Deposit successful! New balance: " + account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void performWithdrawal() {
        int accNo = readInt("Enter account number: ");
        Account account = findAccount(accNo);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        double amount = readPositiveDouble("Enter amount to withdraw: ");
        try {
            account.withdraw(amount);
            System.out.println("Withdrawal successful! New balance: " + account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showAccountDetails() {
        int accNo = readInt("Enter account number: ");
        Account account = findAccount(accNo);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.println(account);
    }

    private void updateContact() {
        int accNo = readInt("Enter account number: ");
        Account account = findAccount(accNo);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        String newEmail = readValidated("Enter new email address: ", EMAIL_REGEX, "Invalid email format.");
        String newPhone = readValidated("Enter new phone number (10 digits): ", PHONE_REGEX, "Invalid phone number.");
        account.updateContactDetails(newEmail, newPhone);
        System.out.println("Contact details updated successfully.");
    }

    private void listAllAccounts() {
        if (accountCount == 0) {
            System.out.println("No accounts available.");
            return;
        }
        for (int i = 0; i < accountCount; i++) {
            System.out.println(accounts[i]);
        }
    }

    private Account findAccount(int accNo) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber() == accNo) {
                return accounts[i];
            }
        }
        return null;
    }

    private int readInt() {
        while (true) {
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        return readInt();
    }

    private double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    System.out.println("Value must be positive.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String readValidated(String prompt, String regex, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.matches(regex)) {
                return input;
            } else {
                System.out.println(errorMsg);
            }
        }
    }
}

public class BankingApp {
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.mainMenu();
    }
}
