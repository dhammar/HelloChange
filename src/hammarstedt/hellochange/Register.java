package hammarstedt.hellochange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a cash register that supports adding and removing dollar bills,
 * and using available bills to make change for given dollar amounts.
 */
public class Register {

    /**
     * HashMap representation of bills currently in register. Keys are bill denominations represented by a
     * Bill enum, while values are the number of such bills held.
     */
    private HashMap<Bill, Integer> billsInRegister;

    public Register() {
        billsInRegister = new HashMap<>();
        for(Bill bill : Bill.values()) {
            billsInRegister.put(bill, 0);
        }
    }

    /**
     * Returns a String value that represents monetary contents of Register.
     * @return string representation of Register state.
     */
    public String show() {
        String registerContents = "$" + getTotalBalance();
        for(Bill bill : Bill.values()) {
            registerContents += " " + billsInRegister.get(bill);
        }
        return registerContents;
    }

    /**
     * Adds given bills and their counts to the Register. Does not allow negative values.
     * @param billsToAdd Bill, Integer HashMap of bills to add. Values are counts of each bill.
     * @return String representation of monetary contents of Register after bills are inserted.
     */
    public String put(HashMap<Bill, Integer> billsToAdd) {
        if(billsToAdd == null ||
                billsToAdd.values()
                        .stream()
                        .anyMatch((value) -> value < 0)){
            return "sorry";
        }

        for(Bill bill : billsToAdd.keySet()) {

            int billCount = billsToAdd.get(bill);
            int newBillAmount = billsInRegister.get(bill) + billCount;

            billsInRegister.put(bill, newBillAmount);

        }
        return show();
    }

    /**
     * Removes given bills and their counts from the Register. Does not allow negative values.
     * @param billsToTake Bill, Integer HashMap of bills to add. Values are counts of each bill.
     * @return String representation of monetary contents of Register after bills are inserted.
     */
    public String take(HashMap<Bill, Integer> billsToTake) {
        if(billsToTake == null ||
                billsToTake.values()
                        .stream()
                        .anyMatch((value) -> value < 0)){
            return "sorry";
        }

        for(Bill bill : billsToTake.keySet()) {
            int billCountToTake = billsToTake.get(bill);
            int newBillAmount =  Math.max(billsInRegister.get(bill) - billCountToTake, 0);

            billsInRegister.put(bill, newBillAmount);

        }
        return show();
    }

    /**
     * Makes change for requested amount from bills in register. Returns "sorry" if change cannot be made with
     * given bills in register for requested value.
     * @param changeValue Amount of change needed to be removed from register.
     * @return String representation of bill counts needed to make change.
     */
    public String change(int changeValue) {
        if(changeValue > getTotalBalance()) {
            return "sorry";
        }

        List<Bill> changeBillList = dynamicChangeSolver(changeValue);
        String changeDisplay = "";

        if(changeBillList.isEmpty()){
            return "sorry";
        } else {
            for(Bill billType : Bill.values()) {
                int billCountInChange = changeBillList.stream()
                        .filter((bill) -> bill == billType)
                        .toArray()
                        .length;

                billsInRegister.put(billType, billsInRegister.get(billType) - billCountInChange);

                if(!changeDisplay.isEmpty()) {
                    changeDisplay += " ";
                }
                changeDisplay += billCountInChange;
            }
        }
        return changeDisplay;
    }

    /**
     * Uses a bottom-up dynamic programming algorithm to retrieve the smallest List of Bills that fulfill
     * the requested change amount. Returns an empty List if the requested change value cannot be fulfilled.
     * @param changeValue Amount of change needed to be removed from register.
     * @return List of Bills that fulfil the requested change value.
     */
    private List<Bill> dynamicChangeSolver(int changeValue) {
        ArrayList<Bill> billList = getBillList();
        ArrayList< List<Bill> > solutionTable = new ArrayList<>();
        boolean visitedTable[] = new boolean[changeValue+1];
        visitedTable[0] = true;

        for(int i = 0; i <= changeValue; i++){
            solutionTable.add(new ArrayList<>());
        }

        //for each number less than the changeValue, we want to find a combination of bills to produce change
        for(int i = 1; i <= changeValue; i++) {
            //check if we can use any bill to create the current value
            for(int j = 0; j < billList.size(); j++){
                Bill currentBill = billList.get(j);
                if(currentBill.value > i){
                    continue;
                }

                int subSolutionIndex = i - currentBill.value;
                List<Bill> subSolution = solutionTable.get(subSolutionIndex);

                //if we havent visited this value yet and if using this bill will give us a smaller change combination,
                //or if no solution has been found for this dollar amount yet
                if(visitedTable[subSolutionIndex] &&
                        (subSolution.size() + 1 < solutionTable.get(i).size() ||
                                solutionTable.get(i).isEmpty())){

                    int subBillTypeCount = subSolution.stream()
                            .filter((bill) -> bill == currentBill)
                            .toArray()
                            .length;

                    int totalBillTypeCount = billList.stream()
                            .filter((bill) -> bill == currentBill)
                            .toArray()
                            .length;

                    //if we have enough bills of a given type to use for the combination, add it
                    if(subBillTypeCount < totalBillTypeCount) {
                        List<Bill> subSolutionCopy = new ArrayList<>();
                        subSolutionCopy.addAll(subSolution);
                        subSolutionCopy.add(currentBill);

                        solutionTable.set(i, subSolutionCopy);
                        visitedTable[i] = true;
                    }

                }

            }
        }
        return solutionTable.get(changeValue);
    }

    /**
     * Retrieves an ArrayList of Bills that represent each bill held in the register.
     * @return an ArrayList of all currently held Bills.
     */
    private ArrayList<Bill> getBillList() {
        ArrayList<Bill> billList = new ArrayList<>();
        for(Bill billType : billsInRegister.keySet()){
            for(int i = 0; i < billsInRegister.get(billType); i++){
                billList.add(billType);
            }
        }

        return billList;
    }

    /**
     * Retrieves the total monetary balance of all bills in the register.
     * @return total register balance
     */
    private int getTotalBalance() {
        int balance = 0;
        for(Bill billType : billsInRegister.keySet()){
            balance += billsInRegister.get(billType) * billType.value;
        }
        return balance;
    }

}

