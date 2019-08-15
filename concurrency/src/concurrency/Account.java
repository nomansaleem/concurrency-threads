package concurrency;

/*
 * @Author Noman Saleem - 16007532
 * 
 * This is the Account class, which includes the deposit and withdrawal methods
 * 		as well as the printStatement method.
 * This Account class also includes a main which holds and manages the threads.
 * 
 */

import java.util.ArrayList;

public class Account {
	private String name;
	private int balance, id, counter;
	private ArrayList<Transaction> transactions; // this array list is of the type Transaction class.

	public static void main(String[] args) throws InterruptedException {
		// Check to make sure program has been called with correct number of
		// command line arguments
		if (args.length != 3) {
			System.err.println("Error: program should take exactly three command line arguments:");
			System.err.println("\t<No. of card holders> <main acct starting bal.> <backup acct. starting bal.>");
			System.exit(0);
		}

		try {
			int cards = Integer.parseInt(args[0]);
			Account account = new Account("Main", Integer.parseInt(args[1]));
			Account backup = new Account("Backup", Integer.parseInt(args[2]));

			// code to create and manage threads.
			Thread[] bankCards = new Thread[cards];
			for (int i = 0; i < cards; i++) {
				bankCards[i] = new Thread(new CardHolder(i, account));
				bankCards[i].start(); // thread begins execution.
			}
			// join.
			for (int i = 0; i < cards; i++) {
				bankCards[i].join(); // waits for the thread to die.
			}

			account.printStatement();
			
		} catch (NumberFormatException e) {
			System.err.println("All three arguments should be integers");
			System.err.println("\t<No. of card holders> <main acct starting bal.> <backup acct. starting bal.>");
		}
	}

	// Create an account - initialisation goes in here
	public Account(String name, int startingBalance) {
		this.name = name;
		this.balance = startingBalance;
		counter = 1; // ensures the transactions start from number 1.
		this.transactions = new ArrayList<Transaction>();
	}

	// Deposit <amount> into the account
	synchronized public void deposit(int id, int amount) {
		this.id = id;
		balance = balance + amount; // adds the amount of deposit onto the total balance.
		transactions.add(new Transaction(id, counter++, balance, amount, 0));
		notifyAll(); // wakes up all the threads.
	}

	// Withdraw <amount> from the account
	synchronized public void withdraw(int id, int amount) {
		while (amount > balance) {
			try {
				wait(); // current thread will wait until another thread is notified.
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		balance = balance - amount; // takes the amount of withdrawal off the total balance.
		transactions.add(new Transaction(id, counter++, balance, 0, amount));
	}

	// Print out the statement of transactions
	public void printStatement() {
		System.out.println("Account \"" + name + "\":");

		if (transactions != null) {
			/*
			 * System.out.format used to correctly format the headings.
			 * % means the following code is an argument that will be formatted.
			 * - means left alignment.
			 * the number after the '-' means the length of characters of the string.
			 * the 's' means that a string is being formatted.
			 * 
			 */
			System.out.format("%-16s%-15s%-17s%-15s\n", "Transactions", "Deposit", "Withdraw", "Balance");
		} else {
			System.out.println("No Transactions");
		}

		for (int i = 0; i < transactions.size(); i++) {
			Transaction trans = transactions.get(i);
			
			/*
			 * %d means a decimal integer.
			 * \t means a tab.
			 * %f means floating point - a decimal number.
			 * the number before in between the % and f e.g. %3.2f means
			 * 		number of integers.decimal points
			 * 	so %3.2 is 3 characters of integers to 2 decimal points.
			 * 
			 */
			
			if (trans.getDeposit() == 0 && trans.getWithdrawal() == 0) {
				System.out.println(String.format("%4d(%1d)\t\t\t\t\t\t%3.2f", trans.getCounter(), trans.id(), trans.getBal()));
			} else if (trans.getDeposit() == 0) {
				System.out.println(String.format("%4d(%1d)\t\t \t\t %3.2f\t\t%4.2f", trans.getCounter(), trans.id(),
						trans.getWithdrawal(), trans.getBal()));
			} else if (trans.getWithdrawal() == 0) {
				System.out.println(String.format("%4d(%1d)\t\t%3.2f \t\t \t\t%4.2f", trans.getCounter(), trans.id(),
						trans.getDeposit(), trans.getBal()));
			} else {
				System.out.println(String.format("%4d(%1d)\t\t \t\t \t%3.2f\t%4.2f\t\t%5.2f", trans.getCounter(), trans.id(),
						trans.getWithdrawal(), trans.getDeposit(), trans.getBal()));
			}
		}
	}
}
