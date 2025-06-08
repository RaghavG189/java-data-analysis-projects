package forensic;

/**
 * This class represents a forensic analysis system that manages DNA data using
 * BSTs.
 * Contains methods to create, read, update, delete, and flag profiles.
 * 
 * @author Kal Pandit
 */
public class ForensicAnalysis {

    private TreeNode treeRoot;            // BST's root
    private String firstUnknownSequence;
    private String secondUnknownSequence;

    public ForensicAnalysis () {
        treeRoot = null;
        firstUnknownSequence = null;
        secondUnknownSequence = null;
    }

    /**
     * Builds a simplified forensic analysis database as a BST and populates unknown sequences.
     * The input file is formatted as follows:
     * 1. one line containing the number of people in the database, say p
     * 2. one line containing first unknown sequence
     * 3. one line containing second unknown sequence
     * 2. for each person (p), this method:
     * - reads the person's name
     * - calls buildSingleProfile to return a single profile.
     * - calls insertPerson on the profile built to insert into BST.
     *      Use the BST insertion algorithm from class to insert.
     * 
     * DO NOT EDIT this method, IMPLEMENT buildSingleProfile and insertPerson.
     * 
     * @param filename the name of the file to read from
     */
    public void buildTree(String filename) {
        // DO NOT EDIT THIS CODE
        StdIn.setFile(filename); // DO NOT remove this line

        // Reads unknown sequences
        String sequence1 = StdIn.readLine();
        firstUnknownSequence = sequence1;
        String sequence2 = StdIn.readLine();
        secondUnknownSequence = sequence2;
        
        int numberOfPeople = Integer.parseInt(StdIn.readLine()); 

        for (int i = 0; i < numberOfPeople; i++) {
            // Reads name, count of STRs
            String fname = StdIn.readString();
            String lname = StdIn.readString();
            String fullName = lname + ", " + fname;
            // Calls buildSingleProfile to create
            Profile profileToAdd = createSingleProfile();
            // Calls insertPerson on that profile: inserts a key-value pair (name, profile)
            insertPerson(fullName, profileToAdd);
        }
    }

    /** 
     * Reads ONE profile from input file and returns a new Profile.
     * Do not add a StdIn.setFile statement, that is done for you in buildTree.
    */
    public Profile createSingleProfile() {

        // WRITE YOUR CODE HERE
        int s = StdIn.readInt();
        STR[] strObjects = new STR[s];

        for(int i = 0; i < s; i++){

        String strName = StdIn.readString();
        int strOccurences = StdIn.readInt();

        STR strObject = new STR(strName, strOccurences);
        strObjects[i] = strObject;

        }

        Profile profile = new Profile(strObjects);

        return profile; // update this line
    }

    /**
     * Inserts a node with a new (key, value) pair into
     * the binary search tree rooted at treeRoot.
     * 
     * Names are the keys, Profiles are the values.
     * USE the compareTo method on keys.
     * 
     * @param newProfile the profile to be inserted
     */
    public void insertPerson(String name, Profile newProfile) {

        // WRITE YOUR CODE HERE
        TreeNode treeNodeTemp = new TreeNode(name, newProfile, null, null);
    
        if(treeRoot == null){

            treeRoot = treeNodeTemp;

        }else {
            TreeNode treePointer = treeRoot;

            int compare = name.compareTo(treePointer.getName());
           
            while(treePointer != null){
                compare = name.compareTo(treePointer.getName());
            if(compare < 0){
                if(treePointer.getLeft() == null){

                    treePointer.setLeft(treeNodeTemp);
                    treePointer = null;
                    
                }else {

                    treePointer = treePointer.getLeft();
                    
                }

            }
            
            if(compare > 0){
                if(treePointer.getRight() == null){

                    treePointer.setRight(treeNodeTemp);
                    treePointer = null;

                }else {

                    treePointer = treePointer.getRight();
                    
                }

            }
        }

        }


    }

    /**
     * Finds the number of profiles in the BST whose interest status matches
     * isOfInterest.
     *
     * @param isOfInterest the search mode: whether we are searching for unmarked or
     *                     marked profiles. true if yes, false otherwise
     * @return the number of profiles according to the search mode marked
     */
    private void getMatchingProfileCountHelper(boolean interest, TreeNode x, int[] profileCount){
        if(x == null) return;

        if(x.getProfile().getMarkedStatus() == interest){

            profileCount[0]++;

        }

        getMatchingProfileCountHelper(interest, x.getLeft(), profileCount);
        
        getMatchingProfileCountHelper(interest, x.getRight(), profileCount);
    
    }
    public int getMatchingProfileCount(boolean isOfInterest) {
        
        // WRITE YOUR CODE HERE
        int[] profileCount = {0};
        TreeNode treePointer = treeRoot;


        getMatchingProfileCountHelper(isOfInterest, treePointer, profileCount);
        return profileCount[0];
        
    }

    /**
     * Helper method that counts the # of STR occurrences in a sequence.
     * Provided method - DO NOT UPDATE.
     * 
     * @param sequence the sequence to search
     * @param STR      the STR to count occurrences of
     * @return the number of times STR appears in sequence
     */
    private int numberOfOccurrences(String sequence, String STR) {
        
        // DO NOT EDIT THIS CODE
        
        int repeats = 0;
        // STRs can't be greater than a sequence
        if (STR.length() > sequence.length())
            return 0;
        
            // indexOf returns the first index of STR in sequence, -1 if not found
        int lastOccurrence = sequence.indexOf(STR);
        
        while (lastOccurrence != -1) {
            repeats++;
            // Move start index beyond the last found occurrence
            lastOccurrence = sequence.indexOf(STR, lastOccurrence + STR.length());
        }
        return repeats;
    }

    /**
     * Traverses the BST at treeRoot to mark profiles if:
     * - For each STR in profile STRs: at least half of STR occurrences match (round
     * UP)
     * - If occurrences THROUGHOUT DNA (first + second sequence combined) matches
     * occurrences, add a match
     */
    private void Traversal(TreeNode x){
        if(x == null) return;

        Traversal(x.getLeft());
        flagProfilesOfInterest(x);
        Traversal(x.getRight());

    }
    private void flagProfilesOfInterest(TreeNode x) {

        // WRITE YOUR CODE HERE
        TreeNode treePointer = x;
        STR[] strsofTreeNode = x.getProfile().getStrs();
        int countofSTRs = strsofTreeNode.length;
        int strOccurences = 0;
        int strMatch = 0;
        
    
        if(countofSTRs % 2 != 0 ){
            countofSTRs = countofSTRs/2 + 1;
        }else {
           countofSTRs = countofSTRs/2;
        }
        for(int i = 0; i < strsofTreeNode.length; i++){
            STR tempObject = strsofTreeNode[i];
            strOccurences = tempObject.getOccurrences();
            
            if(strOccurences == numberOfOccurrences(firstUnknownSequence, tempObject.getStrString()) + numberOfOccurrences(secondUnknownSequence, tempObject.getStrString())){
                strMatch++;
            }

        }
        if(strMatch >= countofSTRs){

            treePointer.getProfile().setInterestStatus(true);
                
        }
        
        
    }


    public void flagProfilesOfInterest() {

        // WRITE YOUR CODE HERE
        TreeNode treePointer = treeRoot;
        STR[] strsofTreeNode = treePointer.getProfile().getStrs();
        int countofSTRs = strsofTreeNode.length;
        int strOccurences = 0;
        int strMatch = 0;
    
        if(countofSTRs % 2 != 0 ){
            countofSTRs = countofSTRs/2 + 1;
        }else {
            countofSTRs = countofSTRs/2;
        }
        for(int i = 0; i < strsofTreeNode.length; i++){
            STR tempObject = strsofTreeNode[i];
            strOccurences = tempObject.getOccurrences();
            

            if(strOccurences == numberOfOccurrences(firstUnknownSequence, tempObject.getStrString()) + numberOfOccurrences(secondUnknownSequence, tempObject.getStrString())){
                strMatch++;
            }

        }
        if(strMatch >= countofSTRs){

            treePointer.getProfile().setInterestStatus(true);
                
        }
        
        Traversal(treePointer);

    }
    

    /**
     * Uses a level-order traversal to populate an array of unmarked Strings representing unmarked people's names.
     * - USE the getMatchingProfileCount method to get the resulting array length.
     * - USE the provided Queue class to investigate a node and enqueue its
     * neighbors.
     * 
     * @return the array of unmarked people
     */
    private void leveltraversalHelper(TreeNode x, int[] count, String[] unmark){

        Queue<TreeNode> unmarked = new Queue<>();

        unmarked.enqueue(x);

        while(!unmarked.isEmpty()){

        x = unmarked.dequeue();

            getUnmarkedPeople(x, count, unmark);

            if(x.getLeft() != null){

                unmarked.enqueue(x.getLeft());

            }
            if(x.getRight() != null){

                unmarked.enqueue(x.getRight());

            }

        }


    }
    private String[] getUnmarkedPeople(TreeNode x, int[] count, String[] unmarked) {

        // WRITE YOUR CODE HERE
        String[] unmarkedNames = unmarked;
        TreeNode treePointer = x;
        int[] c = count;
        
        if(treePointer.getProfile().getMarkedStatus() == false && count[0] < getMatchingProfileCount(false)){
            String name = treePointer.getName();

            unmarkedNames[c[0]] = name;
            c[0]++;

        }

        return unmarkedNames; // update this line

    }
    public String[] getUnmarkedPeople() {

        // WRITE YOUR CODE HERE
        String[] unmarkedNames = new String[getMatchingProfileCount(false)];
        TreeNode treePointer = treeRoot;
        int[] count = {0};
        


        leveltraversalHelper(treePointer, count, unmarkedNames);
        return unmarkedNames; // update this line
    }

    /**
     * Removes a SINGLE node from the BST rooted at treeRoot, given a full name (Last, First)
     * This is similar to the BST delete we have seen in class.
     * 
     * If a profile containing fullName doesn't exist, do nothing.
     * You may assume that all names are distinct.
     * 
     * @param fullName the full name of the person to delete
     */
    private TreeNode deleteMin(TreeNode x){
        if(x.getLeft() == null) return x.getRight();
        x.setLeft(deleteMin(x.getLeft()));
        return x;

    }
    private TreeNode min(TreeNode x){
        if(x.getLeft() == null) return x;
        else return min(x.getLeft());

    }
    private TreeNode Delete(TreeNode x, String fullname){
        if(x == null) return null;
        int compare = fullname.compareTo(x.getName());
        if(compare < 0) x.setLeft(Delete(x.getLeft(), fullname));
        else if (compare > 0) x.setRight(Delete(x.getRight(), fullname));
        else{

            if(x.getRight() == null) return x.getLeft();
            if(x.getLeft() == null) return x.getRight();

            TreeNode t = x;
            x = min(t.getRight());
            x.setRight(deleteMin(t.getRight()));
            x.setLeft(t.getLeft());

        }

        return x;

    }
    public void removePerson(String fullName) {
        // WRITE YOUR CODE HERE

        treeRoot = Delete(treeRoot, fullName);

    }

    /**
     * Clean up the tree by using previously written methods to remove unmarked
     * profiles.
     * Requires the use of getUnmarkedPeople and removePerson.
     */
    public void cleanupTree() {
        // WRITE YOUR CODE HERE
        String[] unmarkedNameList = getUnmarkedPeople();
        String Name = "";

        for(int i = 0; i < unmarkedNameList.length; i++){

            Name = unmarkedNameList[i];
            removePerson(Name);

        }


    }

    /**
     * Gets the root of the binary search tree.
     *
     * @return The root of the binary search tree.
     */
    public TreeNode getTreeRoot() {
        return treeRoot;
    }

    /**
     * Sets the root of the binary search tree.
     *
     * @param newRoot The new root of the binary search tree.
     */
    public void setTreeRoot(TreeNode newRoot) {
        treeRoot = newRoot;
    }

    /**
     * Gets the first unknown sequence.
     * 
     * @return the first unknown sequence.
     */
    public String getFirstUnknownSequence() {
        return firstUnknownSequence;
    }

    /**
     * Sets the first unknown sequence.
     * 
     * @param newFirst the value to set.
     */
    public void setFirstUnknownSequence(String newFirst) {
        firstUnknownSequence = newFirst;
    }

    /**
     * Gets the second unknown sequence.
     * 
     * @return the second unknown sequence.
     */
    public String getSecondUnknownSequence() {
        return secondUnknownSequence;
    }

    /**
     * Sets the second unknown sequence.
     * 
     * @param newSecond the value to set.
     */
    public void setSecondUnknownSequence(String newSecond) {
        secondUnknownSequence = newSecond;
    }

}
