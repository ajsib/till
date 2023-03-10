package till;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class storeData {

    double totalInventoryValue;
    ArrayList<storeItem> inventory;
    int numItems;
    ArrayList<tillMachine> tillMachines;

    public storeData(){
        this.totalInventoryValue = 0;
        this.numItems = -1;
        this.inventory = new ArrayList<storeItem>();
        this.tillMachines = new ArrayList<tillMachine>();
    }

    public void addFromFile(String filePath){
        try {
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                if (this.numItems > -1){
                    String[] lineArray = line.split(" ");
                    storeItem item = new storeItem(lineArray[1], lineArray[2], Double.parseDouble(lineArray[3]),
                                            lineArray[4], lineArray[5], Long.parseLong(lineArray[6]), Integer.parseInt(lineArray[7]));
                    this.inventory.add(item);
                    this.totalInventoryValue += item.getPrice() * item.getInStock();
                    this.numItems+=item.getInStock();
                }
                else{
                    this.numItems++;
                };
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            e.printStackTrace();
        }
    }

    public int getItemIndex(long barcode){
        for (int i = 0; i < this.numItems; i++){
            if (this.inventory.get(i).getBarcode() == barcode){
                return i;
            }
        }
        return -1;
    }

    public storeItem getItem(long barcode){
        int index = this.getItemIndex(barcode);
        if (index != -1){
            return this.inventory.get(index);
        }
        return null;
    }

    public void removeItemFromInventory(long barcode){
        int index = this.getItemIndex(barcode);
        if (index != -1){
            this.totalInventoryValue -= this.inventory.get(index).getPrice();
            this.inventory.get(index).inStock --;
            this.numItems--;
        }
    }

    public void updateStock(ArrayList<storeItem> cart){
        for (int i = 0; i < cart.size(); i++){
            this.removeItemFromInventory(cart.get(i).getBarcode());
        }
    }

    public static void addTillMachine(ArrayList<tillMachine> tillMachines, storeData storeItems){
        Integer numTills = tillMachines.size();
        tillMachine till = new tillMachine(storeItems, numTills.toString());
        tillMachines.add(till);
    }

    public long getRandomBarcode(){
        Random rand = new Random();
        int index = rand.nextInt(inventory.size());
        return inventory.get(index).getBarcode();
    }

    @Override
    public String toString(){
        String output = "";
        for (int i = 0; i < this.numItems; i++){
            output += this.inventory.get(i).toString() + "\n";
        }
        return output;
    }

    public static void main(String[] args) {
        storeData data = new storeData();
        data.addFromFile("./generate_grocery_items/storeDB.txt");
        addTillMachine(data.tillMachines, data);
        addTillMachine(data.tillMachines, data);
        tillMachine machine1 = data.tillMachines.get(0);
        tillMachine machine2 = data.tillMachines.get(1);
        machine1.newTransaction();
        machine1.newTransaction();
        machine2.newTransaction();
        System.out.println(machine1.toString());
        System.out.println("\n" + machine2.toString());
    }
}
