package analysisofairlinedata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;

/**
 * Class used to analyze airline data
 */
public class AnalysisOfAirlineData {
    
    private static final String[] FIELD_NAMES = {"dayOfMonth", "dayOfWeek", "flightDate", "uniqueCarrier", "tailNum",
                                            "originAirportID", "origin", "originStateName", "destAirportID", "dest",
                                            "destStateName", "depTime", "depDelay", "wheelsOff", "wheelsOn", "arrTime",
                                            "arrDelay", "cancelled", "cancellationCode", "diverted", "airTime", "distance"                                        
                                        };
    
    private static Flight createFlight(String[] arrStringsOfCSW) {
        
        Map<String, String> fieldValues = new HashMap<>();
        
        for (int i = 0; i < arrStringsOfCSW.length; i++) {
            fieldValues.put(FIELD_NAMES[i], arrStringsOfCSW[i]);
        }
 
        Flight flight = new Flight(fieldValues);
        
        return flight;
    }
    
    private static ArrayList<Flight> loadDataFile() {
        
        ArrayList<Flight> flights = new ArrayList<>();
        File inputFile = new File("flights_small.csv");        

        try (Scanner in = new Scanner(inputFile)) {

            boolean firstLine = true;
            
            while (in.hasNextLine()) {
                String nextLine = in.nextLine();
                
                if(firstLine) {
                    firstLine = false;
                    continue;
                }
                
                Flight flight = createFlight(nextLine.split(","));                               
                flights.add(flight);                
            }

        } catch (IOException ex) {
            System.out.println(ex);
            System.out.println("Check file flights.csv!");
        }
        
        return flights;
    }
    
    private static String answerOnQuestion1(ArrayList<Flight> flights) {
        
        String answer = "";        
        Entry<String, Double> carrierWithMaxPercentOfCancelledFlights = getCarrierWithMaxPercentOfCancelledFlights(flights);           
        
        if(carrierWithMaxPercentOfCancelledFlights != null) {
           String strNum = String.format("%.2f", carrierWithMaxPercentOfCancelledFlights.getValue());
           answer = carrierWithMaxPercentOfCancelledFlights.getKey() + "," + strNum.replaceAll(",", ".") + "%";
        }
        
        return answer;
    }   
    
    private static String answerOnQuestion2(ArrayList<Flight> flights) {
        
        String answer = "";
        Entry<String, Integer> mostCommonCancellationCode = getMostCommonCancellationCode(flights);        
        
        if(mostCommonCancellationCode != null) {
           answer = mostCommonCancellationCode.getKey();
        }  
        
        return answer;
    }
    
    private static String answerOnQuestion3(List<Flight> flights) {
        
        String answer = "";
        Entry<String, Integer> tailNumWithMaxMiles = getTailNumWithMaxMiles(flights);                
        
        if(tailNumWithMaxMiles != null) {
           answer = tailNumWithMaxMiles.getKey();
        }
        
        return answer;       
    }
    
    private static String answerOnQuestion4(List<Flight> flights) {
        
        String answer = "";
        Entry<String, Integer> busiestAirport = getBusiestAirport(flights, false);
        
        if(busiestAirport != null) {
           answer = busiestAirport.getKey();
        } 
        
        return answer;  
    }
    
    private static String answerOnQuestion5(List<Flight> flights) {
        
        String answer = "";        
        Entry<String, Integer> activeSourceAirport = getBusiestAirport(flights, true);
        
        if(activeSourceAirport != null) {
           answer = activeSourceAirport.getKey();
        }
        
        return answer;  
    }
    
    private static String answerOnQuestion6(List<Flight> flights) {
        
        String answer = "";        
        Entry<String, Integer> activeSinkAirport = getBusiestSinkAirport(flights);
        
        if(activeSinkAirport != null) {
           answer = activeSinkAirport.getKey();
        }     
        
        return answer;  
    }
    
    private static String answerOnQuestion7(List<Flight> flights) {
        
        Long countOfDelayedFlightsofAA = getCountOfDelayedFlightsofAA(flights);
        return Long.toString(countOfDelayedFlightsofAA);  
    }
    
    private static String answerOnQuestion8(List<Flight> flights) {
        
        String answer = "";       
        Flight fixedDelayedFlight = getFixedDelayedFlight(flights);
        
        if(fixedDelayedFlight != null) {
           answer = fixedDelayedFlight.getDayOfMonth() + "," + fixedDelayedFlight.getDepDelay() + "," + fixedDelayedFlight.getTailNum();
        }      
         
        return answer;  
    }
    
    // finds the airline that has the maximum number of delayed flights (DepDelay > 0). Answer: OriginAirportID, CountOfDelayedFlight
    private static String answerOnQuestion9(List<Flight> flights) {
        
        String answer = "";
        Entry<String, Integer> airlineWithMaxDelayedFlights = getAirlineWithMaxDelayedFlights(flights);
        
        if(airlineWithMaxDelayedFlights != null) {
           answer = airlineWithMaxDelayedFlights.getKey() + "," + airlineWithMaxDelayedFlights.getValue();
        }
        
        return answer;  
    }
         
    private static List<Flight> getUncancelledFlights(ArrayList<Flight> flights) {
        return flights
                .stream()
                .filter(f -> !f.getCancelled())
                .collect(Collectors.toList());
    }
    
    private static Entry<String, Double> getCarrierWithMaxPercentOfCancelledFlights(ArrayList<Flight> flights) {
                            
        Entry<String, Double> carrierWithMaxPercentOfCancelledFlights =
                flights
                    .stream()       
                    .parallel()
                    .collect(groupingBy(Flight::getUniqueCarrier))                      
                    .entrySet()
                        .stream()
                        .collect(Collectors.toMap(k -> k.getKey(), 
                                            v -> ((double)(v.getValue()
                                                                .stream()
                                                                .filter(f -> f.getCancelled()).count()) 
                                                            / v.getValue().size())*100))
                        .entrySet()
                            .stream()
                            .max(Comparator.comparingDouble(v -> v.getValue()))
                            .orElse(null);

        return carrierWithMaxPercentOfCancelledFlights;
    }
    
    private static Entry<String, Integer> getMostCommonCancellationCode(ArrayList<Flight> flights) {
        
        Entry<String, Integer> mostCommonCancellationCode =         
                flights        
                    .stream()
                    .filter(f -> !f.getCancellationCode().equals("") && f.getCancelled())
                    .parallel()
                    .collect(groupingBy(Flight::getCancellationCode))                      
                    .entrySet()
                        .stream()
                        .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().size()))
                        .entrySet()
                            .stream()
                            .max(Comparator.comparingInt(v -> v.getValue()))
                            .orElse(null);

        return mostCommonCancellationCode;
    }
      
    private static Entry<String, Integer> getTailNumWithMaxMiles(List<Flight> flights) {
    
           Entry<String, Integer> tailNumWithMaxMiles =         
                    flights   
                        .stream()
                        .parallel()
                        .collect(groupingBy(Flight::getTailNum))                      
                        .entrySet()
                            .stream()
                            .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()
                                                                                .stream()
                                                                                .mapToInt(f -> f.getDistance())
                                                                                .sum()))
                            .entrySet()
                                .stream()
                                .max(Comparator.comparingInt(v -> v.getValue()))
                                .orElse(null);

           return tailNumWithMaxMiles;
    }
    
    private static Entry<String, Integer> getBusiestAirport(List<Flight> flights, boolean onlyLikeSource) {
        
        int i = onlyLikeSource ? -1 : 1;
    
        Entry<String, Integer> busiestAirport =
                flights                 
                    .stream()
                    .map(f -> {
                            Map<String, Integer> airportFlights = new HashMap<>();
                            airportFlights.put(f.getOriginAirportID(), 1);
                            airportFlights.put(f.getDestAirportID(), i);
                            return airportFlights;
                        })
                        .flatMap(m -> m.entrySet().stream())
                        .collect(Collectors.groupingBy(Entry::getKey, Collectors.summingInt(Entry::getValue)))
                        .entrySet()
                                .stream()
                                .max(Comparator.comparingInt(v -> v.getValue()))
                                .orElse(null);

        return busiestAirport;
    }
    
    private static Entry<String, Integer> getBusiestSinkAirport(List<Flight> flights) {
        
        Entry<String, Integer> busiestAirport =
                flights        
                    .stream()
                    .parallel()
                    .map(f -> {
                            Map<String, Integer> airportFlights = new HashMap<>();
                            airportFlights.put(f.getOriginAirportID(), 1);
                            airportFlights.put(f.getDestAirportID(), -1);
                            return airportFlights;
                        })
                        .flatMap(m -> m.entrySet().stream())
                        .collect(Collectors.groupingBy(Entry::getKey, Collectors.summingInt(Entry::getValue)))
                        .entrySet()
                                .stream()
                                .min(Comparator.comparingInt(v -> v.getValue()))
                                .orElse(null);
        
        return busiestAirport;
    }
    
    private static Long getCountOfDelayedFlightsofAA(List<Flight> flights) {
         
        Long countOfDelayedFlightsofAA = 
                flights        
                    .stream()                    
                    .filter(f -> (!f.getDiverted() && f.getUniqueCarrier().equals("AA") && (f.getArrDelay() >= 60 || f.getDepDelay() >= 60)))
                    .count();
 
        return countOfDelayedFlightsofAA;
    }
     
    private static Flight getFixedDelayedFlight(List<Flight> flights) {
       
        Flight flight = 
                flights        
                    .stream()                    
                    .filter(f -> (!f.getDiverted() && f.getArrDelay() <= 0 && f.getDepDelay() > 0))
                    .max(Comparator.comparing(f -> f.getDepDelay()))
                    .orElse(null);    
        
        return flight;
    } 

    private static Entry<String, Integer> getAirlineWithMaxDelayedFlights(List<Flight> flights) {
        
        Entry<String, Integer> airlineWithMaxDelayedFlights = 
                flights    
                    .stream()
                    .filter(f -> f.getDepDelay() > 0)
                    .parallel()
                    .collect(groupingBy(Flight::getOriginAirportID))                      
                    .entrySet()
                        .stream()
                        .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().size()))
                        .entrySet()
                            .stream()
                            .max(Comparator.comparingInt(v -> v.getValue()))
                            .orElse(null);
                                
        return airlineWithMaxDelayedFlights;        
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ArrayList<Flight> flights = loadDataFile();
        List<Flight> uncancelledFlights = getUncancelledFlights(flights);
        FormattedOutput output = new FormattedOutput();
        
        output.addAnswer(1, answerOnQuestion1(flights));
        output.addAnswer(2, answerOnQuestion2(flights));
        output.addAnswer(3, answerOnQuestion3(uncancelledFlights));
        output.addAnswer(4, answerOnQuestion4(uncancelledFlights));
        output.addAnswer(5, answerOnQuestion5(uncancelledFlights));
        output.addAnswer(6, answerOnQuestion6(uncancelledFlights));
        output.addAnswer(7, answerOnQuestion7(uncancelledFlights));
        output.addAnswer(8, answerOnQuestion8(uncancelledFlights));
        output.addAnswer(9, answerOnQuestion9(uncancelledFlights));
        
        output.writeAnswers();
    }
}
