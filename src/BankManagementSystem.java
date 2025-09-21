import java.util.Scanner;

public class BankManagementSystem {
    private final AccountDAO dao = new AccountDAO();
    private final Scanner sc = new Scanner(System.in);

    public void start() {
    int choice;
    do {
        System.out.println("\n=== Bank Management System ===");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Check Balance");
        System.out.println("6. View All Accounts (Admin)");
        System.out.println("7. Delete Account");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
        choice = sc.nextInt();

        switch (choice) {
            case 1 -> createAccount();
            case 2 -> deposit();
            case 3 -> withdraw();
            case 4 -> transfer();
            case 5 -> checkBalance();
            case 6 -> dao.viewAllAccounts();
            case 7 -> deleteAccount();   // 👈 after this, loop continues and menu shows again
            case 8 -> System.out.println("🚪 Exiting...");
            default -> System.out.println("⚠️ Invalid choice!");
        }
    } while (choice != 8);
}

    private void createAccount() {
    System.out.print("Enter Name: ");
    sc.nextLine(); // clear buffer
    String name = sc.nextLine();

    System.out.print("Enter Initial Balance: ");
    while (!sc.hasNextDouble()) {
        System.out.println("⚠️ Please enter a valid number for balance!");
        sc.next(); // discard wrong input
    }
    double balance = sc.nextDouble();

    dao.createAccount(name, balance);
}


    private void deposit() {
        System.out.print("Enter Account No: ");
        int accNo = sc.nextInt();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        dao.deposit(accNo, amount);
    }

    private void withdraw() {
        System.out.print("Enter Account No: ");
        int accNo = sc.nextInt();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        dao.withdraw(accNo, amount);
    }

    private void transfer() {
        System.out.print("Enter From Account No: ");
        int fromAcc = sc.nextInt();
        System.out.print("Enter To Account No: ");
        int toAcc = sc.nextInt();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        dao.transfer(fromAcc, toAcc, amount);
    }

    private void checkBalance() {
        System.out.print("Enter Account No: ");
        int accNo = sc.nextInt();
        dao.checkBalance(accNo);
    }

   private void deleteAccount() {
    System.out.print("Enter Account No to delete: ");
    int accNo = sc.nextInt();
    dao.deleteAccount(accNo);
}
}
