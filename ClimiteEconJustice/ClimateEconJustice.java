package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] communityData = inputLine.split(",");
        String stateName = communityData[2];

        StateNode Temp = new StateNode(stateName, null, null);

        if(firstState == null){

            firstState = Temp;

        }else {
            StateNode Pointer = firstState;
            while(Pointer.getNext() != null && !Pointer.getName().equals(stateName)){
                
                Pointer = Pointer.getNext();

            }
            if(Pointer.getNext() == null && !Pointer.getName().equals(stateName)){

                Pointer.setNext(Temp);
    
            }

        }
        
        

    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] communityData = inputLine.split(",");
        String stateName = communityData[2];
        String countyName = communityData[1];

        StateNode statePointer = firstState;
        CountyNode countyTemp = new CountyNode(countyName, null, null); 

        while(statePointer.getNext() != null && !statePointer.getName().equals(stateName)){

            statePointer = statePointer.getNext();

        }
        if(statePointer.getName().equals(stateName)){

            if(statePointer.getDown() == null){
            
                statePointer.setDown(countyTemp);

            }else{
                CountyNode countyPointer = statePointer.getDown();
                while(countyPointer.getNext() != null && !countyPointer.getName().equals(countyName)){
                    
                    countyPointer = countyPointer.getNext();

                }
                if(countyPointer.getNext() == null && !countyPointer.getName().equals(countyName)){

                    countyPointer.setNext(countyTemp);

                }
            }

        }

    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] communityData = inputLine.split(",");
        String stateName = communityData[2];
        String countyName = communityData[1];
        String communityName = communityData[0];

        //Data from Community
        double percentAfricanAmerican = Double.parseDouble(communityData[3]);
        double percentNative = Double.parseDouble(communityData[4]);
        double percentAsian = Double.parseDouble(communityData[5]);
        double percentWhite = Double.parseDouble(communityData[8]);
        double percentHispanic = Double.parseDouble(communityData[9]);
        String disadvantaged = communityData[19];
        double PMLEVEL = Double.parseDouble(communityData[49]);
        double chanceofFlood = Double.parseDouble(communityData[37]);
        double percentPovertyLine = Double.parseDouble(communityData[121]);
        Data dataTemp = new Data(percentAfricanAmerican, percentNative, percentAsian, percentWhite, percentHispanic, disadvantaged, PMLEVEL, chanceofFlood, percentPovertyLine);

        StateNode statePointer = firstState;
        CountyNode countyPointer = statePointer.getDown();
        CommunityNode communityTemp = new CommunityNode(communityName, null, null);

        while(statePointer.getNext() != null && !statePointer.getName().equals(stateName)){

            statePointer = statePointer.getNext();
            countyPointer = statePointer.getDown();

        }
        while(countyPointer.getNext() != null && !countyPointer.getName().equals(countyName)){

            countyPointer = countyPointer.getNext();

        }
        if(countyPointer.getName().equals(countyName)){

            if(countyPointer.getDown() == null){

                countyPointer.setDown(communityTemp);
                communityTemp.setInfo(dataTemp);

            }else {
                CommunityNode communityPointer = countyPointer.getDown();
                while(communityPointer.getNext() != null && !communityPointer.getName().equals(communityName)){
                    
                    communityPointer = communityPointer.getNext();

                }
                if(communityPointer.getNext() == null && !communityPointer.getName().equals(communityName)){

                    communityPointer.setNext(communityTemp);
                    communityTemp.setInfo(dataTemp);

                }

            }
        }
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {

        // WRITE YOUR CODE HERE
        int numofCommunities = 0;

        StateNode statePointer = firstState;
        CountyNode countyPointer = statePointer.getDown();
        CommunityNode communityPointer = countyPointer.getDown();

            while(communityPointer != null){
                if(race.equals("African American")){
                    if(communityPointer.getInfo().getAdvantageStatus().equals("True") && communityPointer.getInfo().prcntAfricanAmerican*100 >= userPrcntage){
                            
                        numofCommunities++;
                            
                    }
                }else if(race.equals("Native American")){
                    if(communityPointer.getInfo().getAdvantageStatus().equals("True") && communityPointer.getInfo().prcntNative*100 >= userPrcntage){

                        numofCommunities++;

                    }

                }else if(race.equals("Asian American")){
                    if(communityPointer.getInfo().getAdvantageStatus().equals("True") && communityPointer.getInfo().prcntAsian*100 >= userPrcntage){

                        numofCommunities++;

                    }

                }else if(race.equals("White American")){
                    if(communityPointer.getInfo().getAdvantageStatus().equals("True") && communityPointer.getInfo().prcntWhite*100 >= userPrcntage){

                        numofCommunities++;

                    }

                }else if(race.equals("Hispanic American")){
                    if(communityPointer.getInfo().getAdvantageStatus().equals("True") && communityPointer.getInfo().prcntHispanic*100 >= userPrcntage){

                        numofCommunities++;

                    }

                }

                communityPointer = communityPointer.getNext();

                if(communityPointer == null && countyPointer.getNext() != null){

                    countyPointer = countyPointer.getNext();
                    communityPointer = countyPointer.getDown();

                }else if(communityPointer == null && countyPointer.getNext() == null && statePointer.getNext() != null){

                    statePointer = statePointer.getNext();
                    countyPointer = statePointer.getDown();
                    communityPointer = countyPointer.getDown();

                }
                
            }

        return numofCommunities; // replace this line

    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {

        //WRITE YOUR CODE HERE
        int numofDCommunities = 0;

        StateNode statePointer = firstState;
        CountyNode countyPointer = statePointer.getDown();
        CommunityNode communityPointer = countyPointer.getDown();

            while(communityPointer != null){
                if(race.equals("African American")){
                    if(communityPointer.getInfo().getAdvantageStatus().equals("False") && communityPointer.getInfo().prcntAfricanAmerican*100 >= userPrcntage){
                            
                        numofDCommunities++;
                            
                    }
                }else if(race.equals("Native American")){
                    if(communityPointer.getInfo().getAdvantageStatus().equals("False") && communityPointer.getInfo().prcntNative*100 >= userPrcntage){

                        numofDCommunities++;

                    }

                }else if(race.equals("Asian American")){
                    if(communityPointer.getInfo().getAdvantageStatus().equals("False") && communityPointer.getInfo().prcntAsian*100 >= userPrcntage){

                        numofDCommunities++;

                    }

                }else if(race.equals("White American")){
                    if(communityPointer.getInfo().getAdvantageStatus().equals("False") && communityPointer.getInfo().prcntWhite*100 >= userPrcntage){

                        numofDCommunities++;

                    }

                }else if(race.equals("Hispanic American")){
                    if(communityPointer.getInfo().getAdvantageStatus().equals("False") && communityPointer.getInfo().prcntHispanic*100 >= userPrcntage){

                        numofDCommunities++;

                    }

                }

                communityPointer = communityPointer.getNext();

                if(communityPointer == null && countyPointer.getNext() != null){

                    countyPointer = countyPointer.getNext();
                    communityPointer = countyPointer.getDown();

                }else if(communityPointer == null && countyPointer.getNext() == null && statePointer.getNext() != null){

                    statePointer = statePointer.getNext();
                    countyPointer = statePointer.getDown();
                    communityPointer = countyPointer.getDown();

                }
                
            }

        return numofDCommunities; // replace this line

        
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {

        // WRITE YOUR METHOD HERE
        ArrayList<StateNode> States = new ArrayList<>();

        StateNode statePointer = firstState;
        CountyNode countyPointer = statePointer.getDown();
        CommunityNode communityPointer = countyPointer.getDown();

            while(communityPointer != null){
                if(communityPointer.getInfo().PMlevel >= PMlevel && !States.contains(statePointer)){
                    
                    States.add(statePointer);

                }

                communityPointer = communityPointer.getNext();

                if(communityPointer == null && countyPointer.getNext() != null){

                    countyPointer = countyPointer.getNext();
                    communityPointer = countyPointer.getDown();

                }else if(communityPointer == null && countyPointer.getNext() == null && statePointer.getNext() != null){

                    statePointer = statePointer.getNext();
                    countyPointer = statePointer.getDown();
                    communityPointer = countyPointer.getDown();

                }
                
            }


        return States; // replace this line
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {

        // WRITE YOUR METHOD HERE
        int numfCommunities = 0;

        StateNode statePointer = firstState;
        CountyNode countyPointer = statePointer.getDown();
        CommunityNode communityPointer = countyPointer.getDown();

            while(communityPointer != null){
                if(communityPointer.getInfo().chanceOfFlood >= userPercntage){
                            
                    numfCommunities++;
                    
                }

                communityPointer = communityPointer.getNext();

                if(communityPointer == null && countyPointer.getNext() != null){

                    countyPointer = countyPointer.getNext();
                    communityPointer = countyPointer.getDown();

                }else if(communityPointer == null && countyPointer.getNext() == null && statePointer.getNext() != null){

                    statePointer = statePointer.getNext();
                    countyPointer = statePointer.getDown();
                    communityPointer = countyPointer.getDown();

                }
                
            }


        return numfCommunities; // replace this line

    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {

        //WRITE YOUR METHOD HERE
        StateNode statePointer = firstState;
        CountyNode countyPointer = firstState.getDown();
        CommunityNode communityPointer = countyPointer.getDown();
        ArrayList<CommunityNode> topCommunities = new ArrayList<CommunityNode>();
        
        while(!statePointer.getName().equals(stateName)){

            statePointer = statePointer.getNext();
            countyPointer = statePointer.getDown();
            communityPointer = countyPointer.getDown();

        }
        while(communityPointer != null){
            if(topCommunities.size() < 10){

                topCommunities.add(communityPointer);

                if(topCommunities.size() == 10){

                  communityPointer = communityPointer.getNext();

                }

            }
            if(topCommunities.size() == 10){
                CommunityNode lowestCommunity = topCommunities.get(0);
                int i = 0;
                while(i < topCommunities.size()){
                    //Find lowest community in the array
                    if(topCommunities.get(i).getInfo().getPercentPovertyLine() < lowestCommunity.getInfo().getPercentPovertyLine()){

                        lowestCommunity = topCommunities.get(i);
        
                    }
                    i++;
                }
                
                if(communityPointer.getInfo().getPercentPovertyLine() > lowestCommunity.getInfo().getPercentPovertyLine()){

                    topCommunities.set(topCommunities.indexOf(lowestCommunity), communityPointer);

                }
            }
            
            communityPointer = communityPointer.getNext();
            
            if(communityPointer == null && countyPointer.getNext() != null){

                countyPointer = countyPointer.getNext();
                communityPointer = countyPointer.getDown();

            }
            
        }


        return topCommunities; // replace this line

    }
}
