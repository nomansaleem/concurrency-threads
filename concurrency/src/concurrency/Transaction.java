package concurrency;

/*
 * @Author Noman Saleem - 16007532
 * 
 * This is the Transaction class which holds all the details for the transactions
 * 		and passes it on to the Account class.
 * 
 */

public class Transaction {

	private int id;
	private double withdrawal, deposit, bal;
	private int counter = 0;

	public Transaction(int id, int counter, double bal, double deposit, double withdrawal) {
		this.id = id;
		this.counter = counter;
		this.bal = bal;
		this.deposit = deposit;
		this.withdrawal = withdrawal;
	}
	
	/*
	 * These set and get methods are called in the Account printStatement method 
	 * 		to print the transactions in the correct format.
	 */

	public int id() {
		return id;
	}

	public double getWithdrawal() {
		return withdrawal;
	}

	public double getDeposit() {
		return deposit;
	}
	
	public double getBal() {
		return bal;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
}
