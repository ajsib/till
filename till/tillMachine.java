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

            public void setPaymentMethodCash(int paymentMethod){
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

            public void setPaymentMethodCard(int paymentMethod){
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

        public void addItemToCart(long barcode){
            this.cart.add(item);
            this.totalCost += item.getPrice();
        }
    }

    private ArrayList<ArrayList<storeItem>> prevTransactions;
    private storeData storeItems;
    private int transactionCount;
    private double totalSoldDollars;
    private double registerCashOnHand;

    public tillMachine(storeData database){
        this.storeItems = database;
        this.transactionCount = 0;
        this.totalSoldDollars = 0;
        this.registerCashOnHand = 0;
        this.prevTransactions = new ArrayList<ArrayList<storeItem>>();
    }
    
}