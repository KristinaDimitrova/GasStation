package gasStation;

public class Cashier extends Thread {
    CashDesk cashDesk;

    public Cashier(CashDesk cashDesk){
        this.cashDesk = cashDesk;
    }

    @Override
    public void run() {
        while (true){
            cashDesk.serveCar();
        }

    }
}
