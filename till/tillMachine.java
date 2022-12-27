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
                Scanner s = new Scanner(System.in);
                this.paymentMethod = 0;
                System.out.println("Enter the amount paid: ");
                this.amountPaid = s.nextDouble();
                s.close();
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
                Scanner s = new Scanner(System.in);
                this.paymentMethod = 1;
                System.out.println("Enter the card number: ");
                this.cardNumber = s.nextLine();
                s.close();
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
                                paymentMethod += " (Mastercard)" + "XXXXXXXXXXXXX" + this.cardNumber.toCharArray()[14] 
                                              + this.cardNumber.toCharArray()[15] + this.cardNumber.toCharArray()[16];
                                break;
                            case '4':
                                paymentMethod += " (Visa)"+ "XXXXXXXXXXXXX" + this.cardNumber.toCharArray()[14] 
                                              + this.cardNumber.toCharArray()[15] + this.cardNumber.toCharArray()[16];
                                break;
                            case '3':
                                paymentMethod += " (American Express)"+ "XXXXXXXXXXXX" + this.cardNumber.toCharArray()[13] 
                                              + this.cardNumber.toCharArray()[14] + this.cardNumber.toCharArray()[15];;
                                break;
                            case '6':
                                paymentMethod += " (Discover)" + "XXXXXXXXXXXXX" + this.cardNumber.toCharArray()[14] 
                                              + this.cardNumber.toCharArray()[15] + this.cardNumber.toCharArray()[16];
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

        public void addItemToCart(long barcode){
            storeItem item = new storeItem(storeItems.getItem(barcode));
            this.cart.add(item);
            this.totalCost += item.getPrice();
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
            Scanner s = new Scanner(System.in);
            System.out.println("Enter payment method: ");
            System.out.println("0: Cash");
            System.out.println("1: Card");
            System.out.println("2: Cancel transaction");
            int paymentMethod = s.nextInt();
            s.close();
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

    public tillMachine(storeData database){
        this.storeItems = database;
        this.transactionCount = 0;
        this.totalSoldDollars = 0;
        this.registerCashOnHand = 0;
        this.prevTransactions = new ArrayList<Tuple<ArrayList<storeItem>, String>>();
    }

    public void newTransaction(){
        transactionEvent transaction = new transactionEvent();
        Scanner s = new Scanner(System.in);
        System.out.println("Enter barcode: ");
        long barcode = s.nextLong();
        s.close();
        while (barcode != 0){
            Scanner p = new Scanner(System.in);
            transaction.addItemToCart(barcode);
            System.out.println("Enter barcode: ");
            barcode = p.nextLong();
            p.close();
        }
        transaction.customerPay();
        this.transactionCount++;
        Tuple<ArrayList<storeItem>, String> transactionTuple = 
                new Tuple<ArrayList<storeItem>, String>(transaction.cart, transaction.payment.toString());
        this.prevTransactions.add(transactionTuple);
        if (transaction.succesfullPayment){
            this.totalSoldDollars += transaction.totalCost;
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
    public String toString() {
        String output = "";
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
