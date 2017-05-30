package common;

import java.text.DateFormat;
import java.util.Date;

public class PayLogParamDto {

    private Date from;
    private Date to;
    private String title;
    private DateFormat date4mat;
    private int types;
    private String groupBy;
    private String timeLabel;
    private String ticketTypeLabel;
    private String amountLabel;

    // Getter
    public Date getFrom () {
        return this.from;
    }
    public Date getTo () {
        return this.to;
    }
    public String getTitle () {
        return this.title;
    }
    public DateFormat getDate4mat () {
        return this.date4mat;
    }
    public int getTypes () {
        return this.types;
    }
    public String getGroupBy () {
        return this.groupBy;
    }
    public String getTimeLabel () {
        return this.timeLabel;
    }
    public String getTicketLabel () {
        return this.ticketTypeLabel;
    }
    public String getAmountLabel () {
        return this.amountLabel;
    }
    
    // Setter
    public void setFrom (Date from) {
        this.from = from;
    }
    public void setTo (Date to) {
        this.to = to;
    }
    public void setTitle (String title) {
        this.title = title;
    }
    public void setDate4mat (DateFormat date4mat) {
        this.date4mat = date4mat;
    }
    public void setTypes (int types) {
        this.types = types;
    }
    public void setGroupBy (String gby) {
        this.groupBy = gby;
    }
    public void setTimeLabel (String timeLabel) {
        this.timeLabel = timeLabel;
    }
    public void setTicketLabel (String ticketLabel) {
        this.ticketTypeLabel = ticketLabel;
    }
    public void setAmountLabel (String amountLabel) {
        this.amountLabel = amountLabel;
    }
}
