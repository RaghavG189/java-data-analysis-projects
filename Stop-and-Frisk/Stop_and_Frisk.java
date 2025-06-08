import java.util.ArrayList;

/**
 * The StopAndFrisk class represents stop-and-frisk data, provided by
 * the New York Police Department (NYPD), that is used to compare
 * during when the policy was put in place and after the policy ended.
 * 
 * @author Tanvi Yamarthy
 * @author Vidushi Jindal
 */
public class StopAndFrisk {

    /*
     * The ArrayList keeps track of years that are loaded from CSV data file.
     * Each SFYear corresponds to 1 year of SFRecords. 
     * Each SFRecord corresponds to one stop and frisk occurrence.
     */ 
    private ArrayList<SFYear> database; 

    /*
     * Constructor creates and initializes the @database array
     * 
     * DO NOT update nor remove this constructor
     */
    public StopAndFrisk () {
        database = new ArrayList<>();
    }

    /*
     * Getter method for the database.
     * *** DO NOT REMOVE nor update this method ****
     */
    public ArrayList<SFYear> getDatabase() {
        return database;
    }

    /**
     * This method reads the records information from an input csv file and populates 
     * the database.
     * 
     * Each stop and frisk record is a line in the input csv file.
     * 
     * 1. Open file utilizing StdIn.setFile(csvFile)
     * 2. While the input still contains lines:
     *    - Read a record line (see assignment description on how to do this)
     *    - Create an object of type SFRecord containing the record information
     *    - If the record's year has already is present in the database:
     *        - Add the SFRecord to the year's records
     *    - If the record's year is not present in the database:
     *        - Create a new SFYear - arraylist
     *        - Add the SFRecord to the new SFYear
     *        - Add the new SFYear to the database ArrayList
     * 
     * @param csvFile
     */
    public void readFile ( String csvFile ) {

        // DO NOT remove these two lines
        StdIn.setFile(csvFile); // Opens the file
        StdIn.readLine();       // Reads and discards the header line

        // WRITE YOUR CODE HERE

        while(StdIn.hasNextLine()){

        String[] recordEntries = StdIn.readLine().split(",");
        int year = Integer.parseInt(recordEntries[0]);
        String description = recordEntries[2];
        String gender = recordEntries[52];
        String race = recordEntries[66];
        String location = recordEntries[71];
        Boolean arrested = recordEntries[13].equals("Y");
        Boolean frisked = recordEntries[16].equals("Y");

        SFRecord rec = new SFRecord(description, arrested, frisked, gender, race, location);

        boolean found = false;

         for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){

                found = true;
                SFYear SFFound = database.get(i);
                SFFound.getRecordsForYear().add(rec);

            }
        }
            if(!found){

                SFYear newSFYear = new SFYear(year);
                newSFYear.getRecordsForYear().add(rec);
                database.add(newSFYear);

            }
            
        }

    }
    

    

    /**
     * This method returns the stop and frisk records of a given year where 
     * the people that was stopped was of the specified race.
     * 
     * @param year we are only interested in the records of year.
     * @param race we are only interested in the records of stops of people of race. 
     * @return an ArrayList containing all stop and frisk records for people of the 
     * parameters race and year.
     */

    public ArrayList<SFRecord> populationStopped ( int year, String race ) {

        // WRITE YOUR CODE HERE
        ArrayList<SFRecord> SFRecordsofYearandRace = new ArrayList<SFRecord>(); // array that ur returnong
        ArrayList<SFRecord> SFRecordsofSpecificYear = null; //temp array 
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){

                SFRecordsofSpecificYear = database.get(i).getRecordsForYear();
                break;

            }
        }

        for(int i = 0; i < SFRecordsofSpecificYear.size(); i++){
            if(SFRecordsofSpecificYear.get(i).getRace().equals(race)){

                SFRecordsofYearandRace.add(SFRecordsofSpecificYear.get(i));

            }

        }

        return SFRecordsofYearandRace;

    }

    /**
     * This method computes the percentage of records where the person was frisked and the
     * percentage of records where the person was arrested.
     * 
     * @param year we are only interested in the records of year.
     * @return the percent of the population that were frisked and the percent that
     *         were arrested.
     */
    public double[] friskedVSArrested ( int year ) {
        
        // WRITE YOUR CODE HERE
        double popFrisked = 0.0;
        double popArrested = 0.0;
        SFYear popSFYear = null;

        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){

                popSFYear = database.get(i);
                break;
            }

        }
        ArrayList<SFRecord> popArrayList = popSFYear.getRecordsForYear(); //All records from desired SFYear object are now in an arraylist of SFRecord
        for(int i = 0; i < popArrayList.size(); i++){
            if(popArrayList.get(i).getArrested() == true && popArrayList.get(i).getFrisked() == true){

                popArrested++;
                popFrisked++;

            }else if (popArrayList.get(i).getArrested() == true){

                popArrested++;

            }else if (popArrayList.get(i).getFrisked() == true){

                popFrisked++;

            }
        }
        double percentageofFrisked = (popFrisked / popArrayList.size()*100);
        double percentageofArrested = (popArrested / popArrayList.size()*100);
        double[] percentArray = {percentageofFrisked, percentageofArrested};

        return percentArray; // update the return value
    }

    /**
     * This method keeps track of the fraction of Black females, Black males,
     * White females and White males that were stopped for any reason.
     * Drawing out the exact table helps visualize the gender bias.
     * 
     * @param year we are only interested in the records of year.
     * @return a 2D array of percent of number of White and Black females
     *         versus the number of White and Black males.
     */
    public double[][] genderBias ( int year ) {

        // WRITE YOUR CODE HERE
        SFYear numSFYear = null;
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){

            numSFYear = database.get(i);
            break;

            }
        }
        double numofBlackMen = 0.0;
        double numofWhiteMen = 0.0;
        double numofBlackWomen = 0.0;
        double numofWhiteWomen = 0.0;
        double numofBlackPeople = 0.0;
        double numofWhitePeople = 0.0;

        ArrayList<SFRecord> RaceArraylist = numSFYear.getRecordsForYear();
        for(int i = 0; i < RaceArraylist.size(); i++){
            if(RaceArraylist.get(i).getRace().equals("B") && RaceArraylist.get(i).getGender().equals("M")){

                numofBlackMen++;

            }else if(RaceArraylist.get(i).getRace().equals("W") && RaceArraylist.get(i).getGender().equals("M")){

                numofWhiteMen++;

            } else if(RaceArraylist.get(i).getRace().equals("B") && RaceArraylist.get(i).getGender().equals("F")){

                numofBlackWomen++;

            }else if(RaceArraylist.get(i).getRace().equals("W") && RaceArraylist.get(i).getGender().equals("F")){

                numofWhiteWomen++;

            }
        }
        for(int i = 0; i < RaceArraylist.size(); i++){
            if(RaceArraylist.get(i).getRace().equals("B")){

                numofBlackPeople++;

            }else if(RaceArraylist.get(i).getRace().equals("W")){

                numofWhitePeople++;

            }

        }

        double blackFemalePercentage = (numofBlackWomen / numofBlackPeople) * 0.5 * 100;
        double whiteFemalePercentage = (numofWhiteWomen / numofWhitePeople) * 0.5 * 100;
        double blackMalePercentage = (numofBlackMen / numofBlackPeople) * 0.5 * 100;
        double whiteMalePercentage = (numofWhiteMen / numofWhitePeople) * 0.5 * 100;
        double totalFemalePercentage = blackFemalePercentage + whiteFemalePercentage;
        double totalMalePercentage = blackMalePercentage + whiteMalePercentage;

        double[][] percentArray = new double[2][3];
        percentArray[0][0] = blackFemalePercentage;
        percentArray[0][1] = whiteFemalePercentage;
        percentArray[0][2] = totalFemalePercentage;
        percentArray[1][0] = blackMalePercentage;
        percentArray[1][1] = whiteMalePercentage;
        percentArray[1][2] = totalMalePercentage;

        return percentArray; // update the return value
    }

    /**
     * This method checks to see if there has been increase or decrease 
     * in a certain crime from year 1 to year 2.
     * 
     * Expect year1 to preceed year2 or be equal.
     * 
     * @param crimeDescription
     * @param year1 first year to compare.
     * @param year2 second year to compare.
     * @return 
     */

    public double crimeIncrease (String crimeDescription, int year1, int year2) {
        
        // WRITE YOUR CODE HERE
        SFYear firstYear = null;
        SFYear secondYear = null;
        boolean GreaterorMatch = false;
        if(year1 >= year2){

            GreaterorMatch = true;

        }
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year1 && !GreaterorMatch){

                firstYear = database.get(i);
                
            }
            if(database.get(i).getcurrentYear() == year2 && !GreaterorMatch){

                secondYear = database.get(i);
                
            }
        }
        ArrayList<SFRecord> firstYearRecord = firstYear.getRecordsForYear();
        ArrayList<SFRecord> secondYearRecord = secondYear.getRecordsForYear();
        double firstYearCrime = 0.0;
        double secondYearCrime = 0.0;
        for(int i = 0; i < firstYearRecord.size(); i++){
            if(firstYearRecord.get(i).getDescription().indexOf(crimeDescription) >= 0){

                firstYearCrime++;

            }
        }
        for(int i = 0; i < secondYearRecord.size(); i++){
            if(secondYearRecord.get(i).getDescription().indexOf(crimeDescription) >= 0){

                secondYearCrime++;

            }
        }
        double year1PercentCrime = firstYearCrime / firstYearRecord.size() * 100;
        double year2PercentCrime = secondYearCrime / secondYearRecord.size() * 100;
        double difference = year2PercentCrime - year1PercentCrime;

	return difference; // update the return value
    }

    /**
     * This method outputs the NYC borough where the most amount of stops 
     * occurred in a given year. This method will mainly analyze the five 
     * following boroughs in New York City: Brooklyn, Manhattan, Bronx, 
     * Queens, and Staten Island.
     * 
     * @param year we are only interested in the records of year.
     * @return the borough with the greatest number of stops
     */
    public String mostCommonBorough ( int year ) {

        // WRITE YOUR CODE HERE
        SFYear SFYearBorough = null;
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getcurrentYear() == year){

            SFYearBorough = database.get(i);

            }
        }
        int numBrooklyn = 0;
        int numManhattan = 0;
        int numBronx = 0;
        int numQueens = 0;
        int numStatenIsland = 0;
        ArrayList<SFRecord> boroughArraylist = SFYearBorough.getRecordsForYear();
        for(int i = 0; i < boroughArraylist.size(); i++){
            if(boroughArraylist.get(i).getLocation().equalsIgnoreCase("BROOKLYN")){

                numBrooklyn++;

            }else if(boroughArraylist.get(i).getLocation().equalsIgnoreCase("MANHATTAN")){

                numManhattan++;

            }else if(boroughArraylist.get(i).getLocation().equalsIgnoreCase("BRONX")){

                numBronx++;

            }else if(boroughArraylist.get(i).getLocation().equalsIgnoreCase("QUEENS")){

                numQueens++;

            }else if(boroughArraylist.get(i).getLocation().equalsIgnoreCase("STATEN IS")){

                numStatenIsland++;

            }
        }
        String[] Boroughs = {"Brooklyn", "Manhattan", "Bronx", "Queens", "Staten Island"};
        int[] numBoroughs = new int[5];
        numBoroughs[0] = numBrooklyn;
        numBoroughs[1] = numManhattan;
        numBoroughs[2] = numBronx;
        numBoroughs[3] = numQueens;
        numBoroughs[4] = numStatenIsland;
        int Max = numBoroughs[0];
        int maxIndex = 0;
        for(int i = 1; i < numBoroughs.length; i++){
            if(numBoroughs[i] > Max){

                maxIndex = i;

            }
            
        }

        return Boroughs[maxIndex];// update the return value
    }

}
