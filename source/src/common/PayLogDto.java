package common;

import java.util.Date;

public class PayLogDto {
    
    private Date time;
    private String ticketType;
    private int amount;

    public PayLogDto(Date time, String ticketType, int amount) {
        this.time = time;
        this.ticketType = ticketType;
        this.amount = amount;
    }

    // Getter
    public Date getTime () {
        return this.time;
    }
    public String getTicketType () {
        return this.ticketType;
    }
    public int getAmount () {
        return this.amount;
    }

}
