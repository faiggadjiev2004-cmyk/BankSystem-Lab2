import java.math.BigDecimal;
import java.util.Scanner;

import db.Database;
import model.Account;
import model.Customer;
import model.Transaction;

public class Main {

    public static void main(String[] args) {

        Database.createTables();

        System.out.println("Банковская система запущена");

        Customer customer = new Customer(
            77,
            "Faig",
            "Gajiyev",
            "faig.gajiyev@example.com"
        );

        Database.saveCustomer(customer);

        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите ставку депозита 1 (17 / 17.5 / 19 / 21 / 22.3): ");
BigDecimal rate1 = new BigDecimal(scanner.next().replace(",", "."));

System.out.print("Введите срок депозита 1 (3 / 6 / 9 / 12 месяцев): ");
int term1 = scanner.nextInt();

        Account account1 = new Account(
        1,
        new BigDecimal("667"),
        false,
        rate1,
        term1
);

System.out.print("Введите ставку депозита 2 (17 / 17.5 / 19 / 21 / 22.3): ");
BigDecimal rate2 = new BigDecimal(scanner.next().replace(",", "."));

System.out.print("Введите срок депозита 2 (3 / 6 / 9 / 12 месяцев): ");
int term2 = scanner.nextInt();


        Account account2 = new Account(
        2,
        new BigDecimal("1000"),
        true,
        rate2,
        term2
);

        Database.saveAccount(account1, customer.getId());
        Database.saveAccount(account2, customer.getId());

    
        boolean running = true;

        while (running) {

            System.out.println("\n===== БАНК =====");
            System.out.println("1. Показать баланс");
            System.out.println("2. Пополнить депозит");
            System.out.println("3. Снять деньги");
            System.out.println("4. Перевести деньги");
            System.out.println("5. История транзакций");
            System.out.println("6. Показать данные из базы");
            System.out.println("7. Информация о депозитах");
            System.out.println("8. Симулировать месяц");
            System.out.println("9. Досрочно закрыть депозит");
            System.out.println("10. Закрыть депозит после окончания срока");
            System.out.println("0. Выход");

            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();

            switch (choice) {

                case 1: {
                    System.out.println("Депозит 1: " + account1.getBalance());
                    System.out.println("Депозит 2: " + account2.getBalance());
                    break;
                }

                case 2: {
    System.out.print("Какой депозит пополнить? 1 или 2: ");
    int depositNumber = scanner.nextInt();

    System.out.print("Введите сумму пополнения: ");
    BigDecimal amount = scanner.nextBigDecimal();

    if (depositNumber == 1) {
        account1.deposit(amount);
        System.out.println("Депозит 1 пополнен");
    } else if (depositNumber == 2) {
        account2.deposit(amount);
        System.out.println("Депозит 2 пополнен");
    } else {
        System.out.println("Такого депозита нет");
        break;
    }

    Database.saveAccount(account1, customer.getId());
    Database.saveAccount(account2, customer.getId());

    Database.saveTransactions(account1);
    Database.saveTransactions(account2);

    break;
}

                case 3: {
    System.out.print("С какого депозита снять? 1 или 2: ");
    int depositNumber = scanner.nextInt();

    System.out.print("Введите сумму снятия: ");
    BigDecimal amount = scanner.nextBigDecimal();

    if (depositNumber == 1) {
        account1.withdraw(amount);
    } else if (depositNumber == 2) {
        account2.withdraw(amount);
    } else {
        System.out.println("Такого депозита нет");
        break;
    }

    Database.saveAccount(account1, customer.getId());
    Database.saveAccount(account2, customer.getId());

    Database.saveTransactions(account1);
    Database.saveTransactions(account2);

    break;
}

                case 4: {

                    if (account1.isClosed() || account2.isClosed()) {
    System.out.println("Один из депозитов закрыт. Перевод невозможен.");
    break;
}
    System.out.print("С какого депозита перевести? 1 или 2: ");
    int fromDeposit = scanner.nextInt();

    System.out.print("На какой депозит перевести? 1 или 2: ");
    int toDeposit = scanner.nextInt();

    System.out.print("Введите сумму перевода: ");
    BigDecimal amount = scanner.nextBigDecimal();

    if (fromDeposit == 1 && toDeposit == 2) {

        account1.transferTo(account2, amount);

    } else if (fromDeposit == 2 && toDeposit == 1) {

        account2.transferTo(account1, amount);

    } else {
        System.out.println("Неверно выбраны депозиты");
        break;
    }

    Database.saveAccount(account1, customer.getId());
    Database.saveAccount(account2, customer.getId());

    Database.saveTransactions(account1);
    Database.saveTransactions(account2);

    break;
}

                case 5:
    System.out.print("История какого депозита? 1 или 2: ");
    int historyDeposit = scanner.nextInt();

    Account historyAccount;

    if (historyDeposit == 1) {
        historyAccount = account1;
    } else if (historyDeposit == 2) {
        historyAccount = account2;
    } else {
        System.out.println("Такого депозита нет.");
        break;
    }

    System.out.println("\n===== ИСТОРИЯ ТРАНЗАКЦИЙ =====");

    if (historyAccount.getTransactions().isEmpty()) {
        System.out.println("Транзакций пока нет.");
    } else {
        for (Transaction transaction : historyAccount.getTransactions()) {

            System.out.println(
                    "ID: " + transaction.getId()
                    + " | Сумма: " + transaction.getAmount() + " тг"
                    + " | Дата: " + transaction.getTime()
            );
        }
    }

    break;

                case 6: {
    Database.showAccounts();
    Database.showCustomers();
    Database.showTransactions();
    break;
}

case 7:
    System.out.println("\n===== ДЕПОЗИТ 1 =====");
    System.out.println("Баланс: " + account1.getBalance() + " тг");
    System.out.println("Ставка: " + account1.getRate() + "%");
    System.out.println("Срок: " + account1.getTermMonths() + " месяцев");
    System.out.println("Дата открытия: " + account1.getOpenDate());
    System.out.println("Дата окончания: " + account1.getEndDate());
    System.out.println("Текущая дата симуляции: " + account1.getSimulatedDate());
    System.out.println("Вознаграждение: " + account1.getTotalReward() + " тг");
    System.out.println("Закрыт: " + account1.isClosed());

    System.out.println("\n===== ДЕПОЗИТ 2 =====");
    System.out.println("Баланс: " + account2.getBalance() + " тг");
    System.out.println("Ставка: " + account2.getRate() + "%");
    System.out.println("Срок: " + account2.getTermMonths() + " месяцев");
    System.out.println("Дата открытия: " + account2.getOpenDate());
    System.out.println("Дата окончания: " + account2.getEndDate());
    System.out.println("Текущая дата симуляции: " + account2.getSimulatedDate());
    System.out.println("Вознаграждение: " + account2.getTotalReward() + " тг");
    System.out.println("Закрыт: " + account2.isClosed());

    break;

    case 8:
    System.out.print("Какой депозит симулировать? 1 или 2: ");
    int simulateDeposit = scanner.nextInt();

    if (simulateDeposit == 1) {

        BigDecimal reward = account1.simulateMonth();

        System.out.println("Прошел 1 месяц.");
        System.out.println("Начислено вознаграждение: " + reward + " тг");
        System.out.println("Текущая дата: " + account1.getSimulatedDate());
        System.out.println("Всего вознаграждения: " + account1.getTotalReward() + " тг");

    } else if (simulateDeposit == 2) {

        BigDecimal reward = account2.simulateMonth();

        System.out.println("Прошел 1 месяц.");
        System.out.println("Начислено вознаграждение: " + reward + " тг");
        System.out.println("Текущая дата: " + account2.getSimulatedDate());
        System.out.println("Всего вознаграждения: " + account2.getTotalReward() + " тг");

    } else {
        System.out.println("Такого депозита нет.");
    }

    break;

    case 9:
    System.out.print("Какой депозит закрыть досрочно? 1 или 2: ");
    int closeDepositNumber = scanner.nextInt();

    if (closeDepositNumber == 1) {

        account1.closeDepositEarly();

        System.out.println("Баланс депозита 1: " + account1.getBalance() + " тг");
        System.out.println("Вознаграждение: " + account1.getTotalReward() + " тг");
        System.out.println("Закрыт: " + account1.isClosed());

    } else if (closeDepositNumber == 2) {

        account2.closeDepositEarly();

        System.out.println("Баланс депозита 2: " + account2.getBalance() + " тг");
        System.out.println("Вознаграждение: " + account2.getTotalReward() + " тг");
        System.out.println("Закрыт: " + account2.isClosed());

    } else {
        System.out.println("Такого депозита нет.");
    }

    break;

    case 10:
    System.out.print("Какой депозит закрыть? 1 или 2: ");
    int finishDepositNumber = scanner.nextInt();

    if (finishDepositNumber == 1) {

        account1.closeDeposit();

        System.out.println("Баланс депозита 1: "
                + account1.getBalance() + " тг");

        System.out.println("Закрыт: "
                + account1.isClosed());

    } else if (finishDepositNumber == 2) {

        account2.closeDeposit();

        System.out.println("Баланс депозита 2: "
                + account2.getBalance() + " тг");

        System.out.println("Закрыт: "
                + account2.isClosed());

    } else {
        System.out.println("Такого депозита нет.");
    }

    break;

                case 0: {
                    running = false;
                    System.out.println("Программа завершена");
                    break;
                }

                default: {
                    System.out.println("Такого пункта нет");
                }
            }
        }

        scanner.close();
    }

    
}