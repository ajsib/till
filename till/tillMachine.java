package till;

import java.util.*;

public class tillMachine {

    class transactionEvent {

        class paymentEvent {

            int paymentMethod;
            double amountPaid;
            double changeGiven;
            boolean succesfullEvent;
            String cardNumber;

            paymentEvent(){
                this.paymentMethod = -1;
                this.amountPaid = 0;
                this.changeGiven = 0;
                this.succesfullEvent = false;
                this.cardNumber = " ";
            }

            public void setPaymentMethodCash(){
                this.paymentMethod = 0;
                System.out.println("Enter the amount paid: ");
                this.amountPaid = scanner.nextDouble();
                this.changeGiven = this.amountPaid - totalCost;

                if (this.changeGiven < 0){
                    System.out.println("Insufficient funds");
                    this.succesfullEvent = false;
                }
                else{
                    this.succesfullEvent = true;
                }
            }

            public void setPaymentMethodCard(){
                this.paymentMethod = 1;
                System.out.println("Enter the card number: ");
                this.cardNumber = scanner.next();
                if (cardNumber.length() != 16 || !cardNumber.matches("^[31-9]{15}$")){
                    System.out.println("Invalid card number");
                    this.succesfullEvent = false;
                }
                else{
                    this.amountPaid = totalCost;
                    this.succesfullEvent = true;
                }
            }

            public void cancelledTransaction(){
                this.succesfullEvent = false;
            }

            @Override
            public String toString(){
                String paymentMethod = "";
                switch(this.paymentMethod){
                    case 0:
                        paymentMethod = "Cash";
                        paymentMethod += "(" + this.amountPaid + "Change given: " + this.changeGiven + ")";
                        break;
                    case 1:
                        paymentMethod = "Card";
                        switch(this.cardNumber.toCharArray()[0]){
                            case '5' :
                                paymentMethod += " (Mastercard)" + "XXXXXXXXXXXXX" + this.cardNumber.toCharArray()[13] 
                                              + this.cardNumber.toCharArray()[14] + this.cardNumber.toCharArray()[15];
                                break;
                            case '4':
                                paymentMethod += " (Visa)"+ "XXXXXXXXXXXXX" + this.cardNumber.toCharArray()[13] 
                                              + this.cardNumber.toCharArray()[14] + this.cardNumber.toCharArray()[15];
                                break;
                            case '3':
                                paymentMethod += " (American Express)"+ "XXXXXXXXXXXX" + this.cardNumber.toCharArray()[12] 
                                              + this.cardNumber.toCharArray()[13] + this.cardNumber.toCharArray()[14];
                                break;
                            case '6':
                                paymentMethod += " (Discover)" + "XXXXXXXXXXXXX" + this.cardNumber.toCharArray()[13] 
                                              + this.cardNumber.toCharArray()[14] + this.cardNumber.toCharArray()[15];
                                break;
                            default:
                                paymentMethod += " (Unknown Card Type)";
                                break;
                        }
                    default:
                        paymentMethod = "---Transaction cancelled---";
                        break;
                }
                return paymentMethod;
            }
        }

        ArrayList<storeItem> cart;
        double totalCost;
        double totalDollars;
        boolean succesfullPayment;
        paymentEvent payment;

        transactionEvent(){
            this.payment = new paymentEvent();
            this.cart = new ArrayList<>();
            this.totalDollars = 0;
            this.succesfullPayment = this.payment.succesfullEvent;
        }

        public int getItemIndex(long barcode){
            for (int i = 0; i < this.cart.size(); i++){
                if (this.cart.get(i).getBarcode() == barcode){
                    return i;
                }
            }
            return -1;
        }

        public void addItemToCart(long barcode) throws ItemNotFoundException{
            storeItem item0 = storeItems.getItem(barcode);
            if (item0 == null){
                throw new ItemNotFoundException("Item not found");
            }
            else{
                storeItem item = new storeItem(item0);
                this.cart.add(item);
                this.totalCost += item.getPrice();
            }
        }

        public void removeItemFromCart(long barcode){
            storeItem item = new storeItem(storeItems.getItem(barcode));
            this.cart.remove(item);
            this.totalCost -= item.getPrice();
        }

        public void applyDiscount(long barcode, double discountRate){
            storeItem item = this.cart.get(getItemIndex(barcode));
            item.applyDiscount(discountRate);
        }

        public void customerPay(){
            System.out.println("Enter payment method: ");
            System.out.println("0: Cash");
            System.out.println("1: Card");
            System.out.println("2: Cancel transaction");
            int paymentMethod = scanner.nextInt();
            switch(paymentMethod){
                case 0:
                    this.payment.setPaymentMethodCash();
                    break;
                case 1:
                    this.payment.setPaymentMethodCard();
                    break;
                default:
                    this.payment.cancelledTransaction();
                    break;
            }
            this.succesfullPayment = this.payment.succesfullEvent;
        }
    }

    private ArrayList<Tuple<ArrayList<storeItem>, String>> prevTransactions;
    private storeData storeItems;
    private int transactionCount;
    private double totalSoldDollars;
    private double registerCashOnHand;
    private String tillID;
    private Scanner scanner;

    public tillMachine(storeData database, final String tillID){
        this.storeItems = database;
        this.transactionCount = 0;
        this.totalSoldDollars = 0;
        this.tillID = tillID;
        this.registerCashOnHand = 0;
        this.prevTransactions = new ArrayList<Tuple<ArrayList<storeItem>, String>>();
        this.scanner = new Scanner(System.in);
    }

    public void newTransaction(){
        transactionEvent transaction = new transactionEvent();
        System.out.println("Enter barcode: " + this.storeItems.getRandomBarcode());
        long barcode = this.scanner.nextLong();
        while (barcode != 0){
            if (barcode == -1){
                transaction.removeItemFromCart(barcode);
            }
            else if (barcode == -2){
                System.out.println("Enter discount rate: ");
                double discountRate = this.scanner.nextDouble();
                transaction.applyDiscount(barcode, discountRate);
            }
            else{
                try {
                    transaction.addItemToCart(barcode);
                } catch (ItemNotFoundException e) {
                    System.err.println(e.getMessage());
                }
            }
            System.out.println("Enter barcode: " + this.storeItems.getRandomBarcode());
            barcode = this.scanner.nextLong();
        }

        transaction.customerPay();
        this.transactionCount++;
        Tuple<ArrayList<storeItem>, String> transactionTuple = 
                new Tuple<ArrayList<storeItem>, String>(transaction.cart, transaction.payment.toString());
        this.prevTransactions.add(transactionTuple);
        if (transaction.succesfullPayment){
            this.totalSoldDollars += transaction.totalCost;
            storeItems.updateStock(transaction.cart);
            if (transaction.payment.paymentMethod == 0){
                this.registerCashOnHand += transaction.payment.amountPaid - transaction.payment.changeGiven;
            }
        }
    }

    public void setCashInRegister(double cash){
        this.registerCashOnHand = cash;
    }

    public double getTotalSoldDollars(){
        return this.totalSoldDollars;
    }

    public int getTransactionCount(){
        return this.transactionCount;
    }

    public double getRegisterCashOnHand(){
        return this.registerCashOnHand;
    }

    @Override
    public String toString(){
        String output = "";
        output += "Till ID: " + this.tillID + "\n";
        output += "Total Sold: $" + this.totalSoldDollars + "\n";
        output += "Total Transactions: " + this.transactionCount + "\n";
        output += "Cash in Register: $" + this.registerCashOnHand + "\n";
        output += "Previous Transactions: \n";
        for (int i = 0; i < this.prevTransactions.size(); i++){
            output += "Transaction " + (i + 1) + ":   ";
            output += this.prevTransactions.get(i).getSecond() + "\n";
        }
        return output;
    }
}
