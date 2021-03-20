package gasStation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Loading  {
    private  int pumpId;
    private String fuelType;
    private int qty;
    private LocalDateTime dateAndTime;

    public Loading(int pumpId, String fuelType, int qty, LocalDateTime dateAndTime) {
        this.pumpId = pumpId;
        this.fuelType = fuelType;
        this.qty = qty;
        this.dateAndTime = dateAndTime;
    }

    public int getPumpId() {
        return pumpId;
    }

    public String getFuelType() {
        return fuelType;
    }

    public int getQty() {
        return qty;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loading loading = (Loading) o;
        return pumpId == loading.pumpId && qty == loading.qty && Objects.equals(fuelType, loading.fuelType) && Objects.equals(dateAndTime, loading.dateAndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pumpId, fuelType, qty, dateAndTime);
    }

    @Override
    public String toString() {
        return  fuelType + " , " + qty + " liters " + dateAndTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) ;
    }
}
