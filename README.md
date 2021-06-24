# Airline-data-analysis
US Airlines Flight Data Analysis from the Bureau of Transportation Statistics for October 2016 (csv file).
Only Java Stream API is used to search and process information.

Here is a list of questions to be answered by the Java Stream API:
1) Which carrier has the highest percentage of cancelled flights? Output the 2-letter Carrier ID and the chance of a cancelled flight, as a percentage (Example: AA,1.22%).
2) What’s the most common cause of cancellations? Output the one-letter code.
3) Which plane (tail number) flew the furthest (most miles)? Output the complete tailnumber (Example: N775AJ).
4) Which airport is the busiest by total number of flights in and out? Use the number OriginAirportID (Example: 12478).
5) Which airport is the biggest “source” of airplanes? Use the difference between arrivals and departures to compute this value. Output the OriginAirportID (Example: 12478).
6) Which airport is the biggest “sink” of airplanes? Again, use the difference between arrivals and departures, outputting the OriginAirportID (Example: 12478).
7) How many American Airlines (Unique Carrier ID ‘AA’) flights were delayed by 60 minutes or more? If a flight was delayed departing and arriving, only count that as 1. Output an integer.
8) What was the largest delay that was made up (arrived early/on time)? Output the Day of Month (the number), departure delay (as a number), and the tail-number. (Example: 10,30,N947JB).
9) Find the airline that has the maximum number of delayed flights. Output OriginAirportID and CountOfDelayedFlight (Answer: 12478, 145).
