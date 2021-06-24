package analysisofairlinedata;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Class for describing an airline flight
 */
public class Flight {

    private final int dayOfMonth;
    private final int dayOfWeek;
    private final LocalDate flightDate;
    
    private final String uniqueCarrier;
    private final String tailNum;
    
    private final String originAirportID;
    private final String origin;
    private final String originStateName;
    
    private final String destAirportID;
    private final String dest;
    private final String destStateName;
    
    private final LocalTime depTime;
    private final int depDelay;
    
    private final LocalTime wheelsOff;
    private final LocalTime wheelsOn;
    
    private final LocalTime arrTime;
    private final int arrDelay;
    
    private final Boolean cancelled;
    private final String cancellationCode;
    
    private final Boolean diverted;
    private final int airTime;
    private final int distance;
    
    /**
    * Creates a flight
    * @param fieldValues - matching field names and their string values 
    */ 
    public Flight(Map<String, String> fieldValues) {
        
        this.dayOfMonth = stringToInt(fieldValues.get("dayOfMonth")); 
        this.dayOfWeek = stringToInt(fieldValues.get("dayOfWeek")); 
                
        this.flightDate = stringToLocalDate(fieldValues.get("flightDate"));

        this.uniqueCarrier = fieldValues.get("uniqueCarrier");
        this.tailNum = fieldValues.get("tailNum");

        this.originAirportID = fieldValues.get("originAirportID");
        this.origin = fieldValues.get("origin");
        this.originStateName = fieldValues.get("originStateName");

        this.destAirportID = fieldValues.get("destAirportID");
        this.dest = fieldValues.get("dest");
        this.destStateName = fieldValues.get("destStateName");
       
        this.depTime = stringToLocalTime(fieldValues.get("depTime")); 
        this.depDelay = stringToInt(fieldValues.get("depDelay"));
        
        this.wheelsOff = stringToLocalTime(fieldValues.get("wheelsOff"));
        this.wheelsOn = stringToLocalTime(fieldValues.get("wheelsOn"));
        this.arrTime = stringToLocalTime(fieldValues.get("arrTime"));
                 
        this.arrDelay = stringToInt(fieldValues.get("arrDelay"));

        this.cancelled = fieldValues.get("cancelled").equals("1");
        this.cancellationCode = fieldValues.get("cancellationCode");

        this.diverted = fieldValues.get("diverted").equals("1");
        this.airTime = stringToInt(fieldValues.get("airTime"));
        this.distance = stringToInt(fieldValues.get("distance"));       
     }
    
    private int stringToInt(String strData) {
        
        int num = 0;
        
        try {
            num = Integer.parseInt(strData);
        }catch(NumberFormatException ex) {                
        }       
        
        return num;
    }
    
    private LocalDate stringToLocalDate(String strDate) {
        
        LocalDate date;
        String[] strPartsOfDate;
        
        if(strDate.indexOf("/") > 0) {
            strPartsOfDate = strDate.split("/");
        }else {
            strPartsOfDate = strDate.split("-");        
        }    
                  
        if(strPartsOfDate.length == 3) {
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
            String strDateFormatted = (strPartsOfDate[0].length() < 2 ? "0" + strPartsOfDate[0] : strPartsOfDate[0]) +
                                      (strPartsOfDate[1].length() < 2 ? "0" + strPartsOfDate[1] : strPartsOfDate[1]) + 
                                       "20" + strPartsOfDate[2];            
            
            try {               
               date = LocalDate.parse(strDateFormatted, formatter);
            }catch(DateTimeParseException ex) { date = null; }
        }else { date = null; }
        
        return date;
    }
    
    private LocalTime stringToLocalTime(String strTime) {
        
        LocalTime time;
        
        if(strTime.isEmpty()) {
            time = null;
        } else {  
            strTime = formatStrTime(strTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");                                   
            String StrHour = strTime.substring(0, strTime.length()-2);
            String strTimeFormatted = StrHour + ":" + 
                                            strTime.substring(strTime.length()-2, strTime.length());

            try {
                 time = LocalTime.parse(strTimeFormatted, formatter);
             }catch(DateTimeException ex) { 
                 time = null;
             }
        }
        
        return time;
    }
    
    private String formatStrTime(String strTime) {
        
        while(strTime.length() < 4) {
            strTime = "0" + strTime;
        } 
        
        return strTime;
    }
    
    /**
    * Returns field cancelled
    * @return boolean value of field "cancelled"
    */
    public boolean getCancelled() {
        return this.cancelled;
    }
    
    /**
    * Returns field cancellationCode
    * @return string value of field "cancellationCode"
    */
    public String getCancellationCode() {
        return this.cancellationCode;
    }
    
    /**
    * Returns field diverted
    * @return boolean value of field "diverted"
    */
    public boolean getDiverted() {
        return this.diverted;
    }
    
    /**
    * Returns field uniqueCarrier
    * @return string value of field "uniqueCarrier"
    */
    public String getUniqueCarrier() {
        return this.uniqueCarrier;
    }
    
    /**
    * Returns field distance
    * @return int value of field "distance"
    */
    public int getDistance() {
        return this.distance;
    }
    
    /**
    * Returns field tailNum
    * @return string value of field "tailNum"
    */
    public String getTailNum() {
        return this.tailNum;
    }
    
    /**
    * Returns field originAirportID
    * @return string value of field "originAirportID"
    */
    public String getOriginAirportID() {
        return this.originAirportID;
    }
    
    /**
    * Returns field destAirportID
    * @return string value of field "destAirportID"
    */
    public String getDestAirportID() {
        return this.destAirportID;
    }
    
    /**
    * Returns field arrDelay
    * @return int value of field "arrDelay"
    */
    public int getArrDelay() {
        return this.arrDelay;
    }
    
    /**
    * Returns field depDelay
    * @return int value of field "depDelay"
    */
    public int getDepDelay() {
        return this.depDelay;
    }
    
    /**
    * Returns field dayOfMonth
    * @return int value of field "dayOfMonth"
    */
    public int getDayOfMonth() {
        return this.dayOfMonth;
    }
}

