import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import javax.lang.model.util.ElementScanner14;
import javax.swing.plaf.ScrollBarUI;
import java.time.*;
import java.time.format.DateTimeFormatter;
/**
 * Class to manage trasaction history of user
 */
class TransactionHistory{
    private List<LocalDateTime> timeOfTransaction;
    private List<String> transactionType;
    private List<Integer> amount;
    private List<Integer> balance;
    private List<Integer> totalBalance;
    TransactionHistory(){
        timeOfTransaction=new ArrayList<>();
        transactionType=new ArrayList<>();
        amount=new ArrayList<>();
        balance=new ArrayList<>();
        totalBalance=new ArrayList<>();
    }
    public List<LocalDateTime> getTimeOfTransaction() {
        return timeOfTransaction;
    }
    public void setTimeOfTransaction(LocalDateTime timeOfTransaction) {
        this.timeOfTransaction.add(timeOfTransaction);
    }
    public void setAmount(Integer amount) {
        this.amount.add(amount);
    }
    public List<Integer> getAmount() {
        return amount;
    }
    public void setBalance(Integer balance) {
        this.balance.add(balance);
    }
    public List<Integer> getBalance() {
        return balance;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType.add(transactionType);
    }
    public List<String> getTransactionType() {
        return transactionType;
    }
    public List<Integer> getTotalBalance() {
        return totalBalance;
    }
    public void setTotalBalance(Integer totalBalance) {
        this.totalBalance.add(totalBalance);
    }
}
/**
 * Class to create a reciept
 */
class Reciept{
    private static int recieptNumber=1;
    private LocalDateTime currentTime;
    private User user;
    private String protectedAccountNumber;
    Reciept(User user){
        recieptNumber++;
        currentTime=LocalDateTime.now();
        this.user=user;
        protectedAccountNumber=protectedAccountNumber();
    }
    private String protectedAccountNumber(){
        String ac=Integer.toString(user.getAccountNumber());
        String newAc="";
        for (int i = 0; i < ac.length(); i++) {

            if(i<=(ac.length()/2 + 1) && i>=(ac.length()/2-1))
            newAc+='x';
            else
            newAc+=ac.charAt(i);
        }
        return newAc;
    }
    public String getProtectedAccountNumber(){
        return protectedAccountNumber;
    }
    public static int getRecieptNumber() {
        return recieptNumber;
    }
    public LocalDateTime getCurrentTime() {
        return currentTime;
    }
    public TransactionHistory getTransactionHistory()
    {
        return user.getTransactionHistory();
    }
    public String getName()
    {
        return user.getName();
    }

}
interface DisplayMethods{
    void display(String s);
    void display(double d);
    void displayHeading(String string);
    void displayLine(String string);
    void displayChoices(List<String> choices); 

}
class Display implements DisplayMethods{
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public void displayHeading(String string){
        System.out.println("\t"+string+"\t");
    }
    public void displayLine(String string){
        System.out.println(string);
    }
    public void displayChoices(List<String> choices)
    {
        for (int i = 1; i <= choices.size(); i++) {
            System.out.println(i+" "+choices.get(i-1));
        }
    }
    public void display(String string){
        System.out.print(string);
    }
    public void display(double amount){
        System.out.println("₹ " +amount);
    }
    public void displayHistory(TransactionHistory transactionHistory)
    {
        System.out.println("S.No  Transaction Time       Transaction Type   Available Balance   Total Balance ");
        
        int n=transactionHistory.getTimeOfTransaction().size();
        for (int i = n-1; i >= n-11 && i>=0 ; i--) {
            System.out.println((n-i)+"  "+transactionHistory.getTimeOfTransaction().get(i).format(formatter)+"        "+
                                        transactionHistory.getTransactionType().get(i)+"                  "+
                                        transactionHistory.getAmount().get(i)+"                     "+
                                        transactionHistory.getTotalBalance().get(i));
        }
        System.out.println();

    }
}
interface ScreenMenu{
    void welcomeMenu();
    void authenticationMenu();
    void OTPMenu();
    void userAccountMenu(User user);
}
class Screen implements ScreenMenu{
    private Display d;
    private List<String> userAccountMenuChoices;  
    Screen(){
        d=new Display();
        userAccountMenuChoices= new ArrayList<String>(Arrays.asList("Withdraw cash",
                                                                    "Deposit cash", 
                                                                    "View balance",
                                                                    "View reciept (mini statement)",
                                                                    "View transaction history",
                                                                    "Edit profile",
                                                                    "Logout"));
    }
    public void exitMenu(){
        d.displayChoices(Arrays.asList("Exit","Continue"));
    }
    public void welcomeMenu()
    {
        d.displayHeading("=========== Welcome to our ATM ===========");
        d.displayLine("");
        d.displayLine("Please enter your account number");

    }
    public void errorMenu(String message,int attempts){
        d.displayLine(message);
        d.displayLine("You have "+(5-attempts)+" attempts remaining");
    }
    public void errorMenu(String message){
        d.displayLine(message);
    }
    public void authenticationMenu()
    {
        d.displayLine("Please enter your pin");
    }
    public void OTPMenu()
    {
        d.displayLine("Enter the OTP : ");
    }

    public void userAccountMenu(User user)
    {
        d.displayHeading("\n"+"Welcome, "+user.getName());
        d.displayLine("Please choose from the available options : ");
        d.displayChoices(userAccountMenuChoices);
    }
    public void withdrawalMenu()
    {
        d.displayLine("Enter amount to be withdrawn :");
    }
    public void statusMenu(String message)
    {
        d.displayLine(message);
    }
    public void depositMenu()
    {
        d.displayLine("Enter the amount to de deposited and drop the envelope in the deposit slot");
    }
    public void viewBalanceMenu(User user)
    {
        d.displayLine("Your current balance is as follows :");
        d.display("1 Available balance - ");
        d.display(user.getAvailableBalance());
        d.display("2 Total balance (including deposits) - ");
        d.display(user.getTotalBalance());
    }
    public void viewReciept(Reciept reciept)
    {
        d.displayHeading("Reciept");
        d.displayLine("User name : "+reciept.getName());
        d.displayLine("Account number : "+reciept.getProtectedAccountNumber());
        int n=reciept.getTransactionHistory().getTimeOfTransaction().size();
        if(n==0)
        {
            d.displayLine("No transactions done till date.");
        }
        else
        {
            d.displayLine("Latest transaction --> ");
            d.displayLine("Transaction type : "+reciept.getTransactionHistory().getTransactionType().get(n-1)); 
            d.displayLine("Transaction amount : "+reciept.getTransactionHistory().getAmount().get(n-1));
            d.displayLine("Available balance : "+reciept.getTransactionHistory().getBalance().get(n-1));
            d.displayLine("Total balance : "+reciept.getTransactionHistory().getTotalBalance().get(n-1));              
        }

    }
    public void viewTransactionHistoryMenu(User user)
    {
        d.displayHistory(user.getTransactionHistory());
    }
    public void changesMenu(List<String> options)
    {
        d.displayChoices(options);
    }
    public void inactiveMenu(){
        d.displayLine("Sorry, the ATM is inactive");
    }
    public void askMenu(String status,String message){
        d.display("Please tell your "+status+" "+message+" : ");
    }
}
class Keypad{
    private Scanner input;
    Keypad(){
        input =new Scanner(System.in);
    }

    public int getInput(int allowedLength)
    {
        int inputNumber=input.nextInt();
        if(String.valueOf(inputNumber).length()!=allowedLength)
        {
            return -1;
        }
        return inputNumber;
    }
    public int getInput()
    {
        int inputNumber=input.nextInt();
        return inputNumber;
    }
    public long getLargeInput()
    {
        long inputNumber=input.nextLong();
        return inputNumber;
    }
    public int getInputBounded(int lowerIndex,int upperIndex)
    {
        int inputNumber=input.nextInt();
        if(inputNumber<lowerIndex || inputNumber>upperIndex)
        {
            return -1;
        }
        return inputNumber;
    }

}
// Represents the cash dispenser of the ATM

class CashDispenser 
{

    private static List<int []> availableBills = new ArrayList<>();
    private int numberOfTypesOfBills=3; 
    public CashDispenser() 
    {
        for (int i = 0; i < numberOfTypesOfBills; i++) {
            availableBills.add(0,new int [2]);
        }
        availableBills.get(0)[0]=2000;
        availableBills.get(0)[1]=1000;
        availableBills.get(1)[0]=500;
        availableBills.get(1)[1]=2000;
        availableBills.get(2)[0]=100;
        availableBills.get(2)[1]=3000;


    }

    public boolean isShortOfMoney(){
        for (int i = 0; i < numberOfTypesOfBills; i++) {
            if(availableBills.get(i)[1]<5)
                return true;
        }
        return false;
    }
    
    public void dispenseCash(int amountRequired)
    {
        int amount=0;
        for (int i = 0; i < availableBills.size(); i++) {
            while(amount<=amountRequired-availableBills.get(i)[0]){
                amount+=availableBills.get(i)[0];
                availableBills.get(i)[1]--;
            }
        }
         
    }
    public boolean isTransactionPossible(int amount,User user){
        if(user.getAvailableBalance()<amount)
        {
            return false;
        }
        return true;
    }
    public boolean isSufficientCashAvailable(int amountRequired)
    {
        boolean status=true;
        int amount=0;
        for (int i = 0; i < availableBills.size(); i++) {
            int billCount=availableBills.get(i)[1];
            while(amount<=amountRequired-availableBills.get(i)[0]){
                amount+=availableBills.get(i)[0];
                billCount--;
            }
            if(billCount<0)
            {
                status=false;
                break;
            }
        }
        if(amount!=amountRequired)
        status=false;
        return status;

    } 
}
class DepositSlot{
    
    public boolean isEnvelopeRecieved(){
        return true;
    }
}
class User{
    private String name;
    private String phoneNumber;
    private int accountNumber; // account number
    private int pin; // pin for authentication
    private int availableBalance; // funds available for withdrawal
    private int totalBalance; // funds available together with deposits.
    private TransactionHistory transactionHistory; 
    //***************************************************
    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }
    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setPin(int pin) {
        this.pin = pin;
    }
    public int getPin() {
        return pin;
    }
    //***************************************************
    // Account constructor initializes attributes
    public User(String name,int accountNumber, int pin, String phoneNumber, int availableBalance, int totalBalance)
    {
        this.name=name;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.phoneNumber=phoneNumber;
        this.availableBalance = availableBalance;
        this.totalBalance = totalBalance;
        this.transactionHistory=new TransactionHistory();
    }// end Account constructor
   
    // determines whether the user specified pin matches the pin of the account
    public boolean validatePin(int pin)
    {
        if(this.pin == pin)
        {
            return true;
        }
        else
        {
           return false;
        }
    }
   
    // returns available balance
    public int getAvailableBalance()
    {
        return availableBalance;
    }
   
    // returns total balance
    public int getTotalBalance()
    {
        return totalBalance;
    }
   
    // credits an amount to the account
    public void deposit(double amount)
    {
        totalBalance += amount; // add to total balance
    }
   
    // debits an amount from the account
    public void withdraw(double amount)
    {
        availableBalance -= amount; // subtract from available balance
        totalBalance -= amount; // subtract from total balance
    }
   
    // returns account number
    public int getAccountNumber()
    {
        return accountNumber;
    }// end method getAccountNumber

    
}
// Represents the bank account information database
class UserDataBase {
	// ArrayList for all the bank accounts 
	private static ArrayList<User> users = new ArrayList<>();

	
	UserDataBase(){
        try {
            File myFile = new File("bank_data.txt");
            Scanner myReader = new Scanner(myFile);
            int i=0;
            String name=null;
            String accountNumber=null;
            String pin=null;
            String availableBalance=null;
            String totalBalance=null;
            String phoneNumber=null;

            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              //taking data from file.
              switch(i) {
                  case 0:
                      name=data;
                      i++;
                      break;
                  case 1:
                      accountNumber=data;
                      i++;
                      break;
                  case 2:
                      pin=data;
                      i++;
                      break;
                  case 3:
                      phoneNumber=data;
                      i++;
                      break;
                  case 4:
                      availableBalance=data;
                      i++;
                      break;
                  case 5:
                      totalBalance=data;
                      i++;
                      break;
                  case 6:
                      i++;  
                      break;  
                  }
                  if(i==7)
                  {
                      User user=new User(name, Integer.valueOf(accountNumber),Integer.valueOf(pin),phoneNumber, Integer.valueOf(availableBalance),Integer.valueOf(totalBalance));
                      users.add(user);
                      i=0;
                  }

                     
              }

              myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
      
	}
    public List<User> getUserList(){
        return this.users;
    }
    public static void updateFile(){
        try 
        {   FileWriter fileWriter=new FileWriter("updated_bank_data.txt");
            for(User u : users)
            {
                fileWriter.write(u.getName()+"\n");
                fileWriter.write(u.getAccountNumber()+"\n");
                fileWriter.write(u.getPin()+"\n");
                fileWriter.write(u.getPhoneNumber()+"\n");
                fileWriter.write(u.getTotalBalance()+"\n");
                fileWriter.write(u.getAvailableBalance()+"\n");
                fileWriter.write("\n");
            }
            fileWriter.close();
        }
        catch (Exception e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
class BankDatabase 
{
    private List<User> users;
    public BankDatabase(List<User> users)
    {
        this.users=new ArrayList<>();
        this.users=users;
    }
    public void addUser(User user)
    {
        users.add(user);
    }

    public User getAccount(int accountNumber)
    {

        for (User currentAccount : users)
        {

            if (currentAccount.getAccountNumber() == accountNumber)
            {
                return currentAccount;
            }
        }

        return null; // if no matching account is found, return null
    }// end method getAccount

    // determine whether the user specified account number and pin matches
    // those of an account in the database
    public boolean authenticateUser(int userAccountNumber, int userPin)
    {
        
        User userAccount = getAccount(userAccountNumber);

       
        if(userAccount != null)
        {
            return userAccount.validatePin(userPin);
        }
        else
        {
            return false; 
        }
    }

    //return available balance of Account with specified account number
    public int getAvailableBalance(int userAccountNumber)
    {
        return getAccount(userAccountNumber).getAvailableBalance();
    }

    // return total balance of Account with specified account number
    public int getTotalBalance(int userAccountNumber)
    {
        return getAccount(userAccountNumber).getTotalBalance();
    }

}// end class BankDatabase
class ATM extends Screen{
    private String ATMId;
    private Screen screen;
    private Keypad keypad;
    private CashDispenser cashDispenser;
    private DepositSlot depositSlot;
    private boolean isUserAuthenticated;
    private BankDatabase bankDatabase;
    private LocalDateTime currTime;
    ATM(String ATMId,UserDataBase userDataBase){
        // users.add(new User(12345, 12345, 20000, 20000));
        this.ATMId=ATMId;
        this.screen=new Screen();
        this.keypad=new Keypad();
        this.cashDispenser=new CashDispenser();
        this.depositSlot=new DepositSlot();
        this.isUserAuthenticated=false;
        currTime= LocalDateTime.now();
        bankDatabase = new BankDatabase(userDataBase.getUserList());
    }
    private int generateOTP()
    {
        Random rand = new Random();
		int otp = Math.abs(rand.nextInt())%10000+10000;
        return otp;
    }
    private void sendOTP(int otp){
        System.out.println("Your OTP is "+otp);
    }
    int accountNumber;
    int pin;
    public void start(){
        int incorrectCount=0;
        screen.exitMenu();
        int opt =keypad.getInputBounded(1, 2);
        if(opt==1)
        {
            UserDataBase.updateFile();
            TransactionHistoryFileWriter.writeFile();
        }
        screen.welcomeMenu();
        accountNumber=keypad.getInput(5);
        while(accountNumber==-1)
        {   
            screen.errorMenu("Please enter a valid 5-digit account number");
            accountNumber=keypad.getInput(5);
        }
        screen.authenticationMenu();
        pin=keypad.getInput();
        isUserAuthenticated=bankDatabase.authenticateUser(accountNumber, pin);
        if(!isUserAuthenticated)
        {
            screen.errorMenu("Authentication failed! Account number or pin incorrect");
            start();
        }
        incorrectCount=0;
        int Otp=generateOTP();
        sendOTP(Otp);
        screen.OTPMenu();
        int OTP=keypad.getInput(5);
        while(OTP!=Otp)
        {
            incorrectCount++;
            if(incorrectCount==5)
            {
                start();
            }
            screen.errorMenu("OTP is incorrect,please provide a valid OTP.",incorrectCount);
            OTP=keypad.getInput(5);            
        }
        incorrectCount=0;
        mainMenu();
        if(cashDispenser.isShortOfMoney())
        {
            screen.inactiveMenu();
            return;
        }
        start();
        
    }
    void mainMenu()
    {
        User user=bankDatabase.getAccount(accountNumber);
        screen.userAccountMenu(user);
        int mainMenuChoice=keypad.getInputBounded(1, 7);
        while(mainMenuChoice==-1)
        {
            screen.errorMenu("Please enter a valid choice");
        }
        switch(mainMenuChoice)
        {
            case 1: screen.withdrawalMenu();
                    int withdrawAmount=keypad.getInputBounded(0,20000);
                    while(withdrawAmount==-1)
                    {
                        screen.errorMenu("Amount exceeds the limit of ₹ 20,000");
                        withdrawAmount=keypad.getInputBounded(0,20000);
                    }
                    if(!cashDispenser.isSufficientCashAvailable(withdrawAmount))
                    {
                        screen.errorMenu("Sorry, currently we don't have denomination to arrange the given amount");
                    }
                    else if(!cashDispenser.isTransactionPossible(withdrawAmount, user))
                    {
                        screen.statusMenu("Insufficient balance");
                    }
                    else
                    {
                        cashDispenser.dispenseCash(withdrawAmount);
                        user.withdraw(withdrawAmount);
                        user.getTransactionHistory().setTimeOfTransaction(currTime);
                        user.getTransactionHistory().setTransactionType("DEBIT");
                        user.getTransactionHistory().setAmount(withdrawAmount);
                        user.getTransactionHistory().setBalance(user.getAvailableBalance());
                        user.getTransactionHistory().setTotalBalance(user.getTotalBalance());
                        TransactionHistoryFileWriter.addUser(user);
                        screen.statusMenu("Withdrawl successful! Please collect your cash.");                        
                    }
                    mainMenu();
                    break;
            case 2: screen.depositMenu();
                    int depositAmount=keypad.getInputBounded(0,20000);
                    while(depositAmount%100!=0)
                    {
                        screen.errorMenu("Please enter multiples of ₹ 100");
                        depositAmount=keypad.getInputBounded(0,20000);   
                    }
                    while(depositAmount==-1)
                    {
                        screen.errorMenu("Amount exceeds the limit of ₹ 20,000");
                        depositAmount=keypad.getInputBounded(0,20000);
                    }
                    cashDispenser.dispenseCash(depositAmount);
                    user.deposit(depositAmount);
                    user.getTransactionHistory().setTimeOfTransaction(currTime);
                    user.getTransactionHistory().setTransactionType("CREDIT");
                    user.getTransactionHistory().setAmount(depositAmount);
                    user.getTransactionHistory().setBalance(user.getAvailableBalance());
                    user.getTransactionHistory().setTotalBalance(user.getTotalBalance());
                    TransactionHistoryFileWriter.addUser(user);
                    screen.statusMenu("Envelope recieved!");
                    mainMenu();
                    break;
            case 3 :screen.viewBalanceMenu(user);
                    mainMenu();
                    break;
            case 4 :Reciept reciept=new Reciept(user);
                    screen.viewReciept(reciept);
                    mainMenu();
                    break;
            case 5 :screen.viewTransactionHistoryMenu(user);
                    mainMenu();
                    break;
            case 6 :updateData(user);
                    return;
            case 7 :return;
        }
    }
    void updateData(User user){

        screen.changesMenu(Arrays.asList("Pin","Phone number"));
        int choice=keypad.getInputBounded(1, 2);
        switch(choice)
        {
            case 1: screen.askMenu("old", "Pin");
                    int oldPin=keypad.getInput();
                    if(oldPin!=user.getPin())
                    {
                        screen.statusMenu("Incorrect pin, Please try again");
                    }
                    else
                    {
                        screen.askMenu("new", "Pin");
                        int newPin=keypad.getInput();
                        user.setPin(newPin);
                    }
                    mainMenu();
                    break;
            case 2: screen.askMenu("old", "Phone number");
                    long oldPhoneNumber=keypad.getLargeInput();
                    if(!Long.toString(oldPhoneNumber).equals(user.getPhoneNumber()))
                    {
                        screen.statusMenu("Incorrect phone number, Please try again");
                    }
                    else
                    {
                        screen.askMenu("new", "Phone number");
                        long newPhoneNumber=keypad.getLargeInput();
                        user.setPhoneNumber(Long.toString(newPhoneNumber));
                    }
                    mainMenu();
                    break;
        }
        
    }
}
class TransactionHistoryFileWriter {

    static List<User> users=new ArrayList<>();
    public static void addUser(User user){
        users.add(user);
    }
    public static void writeFile(){
        try 
        {
            FileWriter fileWriter=new FileWriter("session_history.txt");
            for(User user : users)
            {
                int size=user.getTransactionHistory().getTimeOfTransaction().size();
                fileWriter.write(user.getName()+"\n");
                fileWriter.write(user.getAccountNumber()+"\n");
                fileWriter.write(user.getTransactionHistory().getTimeOfTransaction().get(size-1)+"\n");
                fileWriter.write(user.getTransactionHistory().getTransactionType().get(size-1)+"\n");
                fileWriter.write(user.getTransactionHistory().getAmount().get(size-1)+"\n");
                fileWriter.write(user.getTransactionHistory().getBalance().get(size-1)+"\n");
                fileWriter.write(user.getTransactionHistory().getTotalBalance().get(size-1)+"\n");
                fileWriter.write("\n");
            }

            fileWriter.close();
        }
        catch (Exception e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
public class ATMSystem {

    public static void main(String[] args) {

        UserDataBase userDataBase=new UserDataBase();
        ATM atm=new ATM("ATMId",userDataBase);
        atm.start();
    }
}