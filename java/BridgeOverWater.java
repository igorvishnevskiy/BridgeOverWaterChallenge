import java.util.*;
import java.util.logging.Logger;


public class BridgeOverWater {

    Map<String, Integer> PEOPLE = new HashMap();
    private static final Logger LOGGER = Logger.getLogger( BridgeOverWater.class.getName() );

    public static void main(String[] args) {

        BridgeOverWater runner = new BridgeOverWater();
        runner.decideProblem();

    }


    /**
     Starting point of the program.
     */
    public void decideProblem() {

        PEOPLE.put("A", 1);
        PEOPLE.put("B", 2);
        PEOPLE.put("C", 5);
        PEOPLE.put("D", 10);

        List<String> sortedPairSequence;
        List<String> pairFormula;
        List<Integer> intFormula;
        int crossTime;
        String intFormulaString;

        sortedPairSequence = getSortedPairSequence();

        pairFormula = getPairFormula(sortedPairSequence);
        LOGGER.info("pairFormula: "+pairFormula);

        intFormula = getIntFormula(pairFormula);
        intFormulaString = stringify(intFormula);
        LOGGER.info("intFormulaString: " + intFormulaString);

        crossTime = sum(intFormula);
        LOGGER.info("crossTime: " + crossTime);

    }


    /**
     Here we calculate a clean sorted pair sequence that will be used as a blueprint in building the
     bridge crossing formulas.
     @return: List<String> fastest_cntnr
     */
    private List<String> getSortedPairSequence() {

        Map<String,Integer> pairsCntnr = new HashMap<String,Integer>(PEOPLE);
        List<String> fastestCntnr = new ArrayList<String>();
        List<String> slowestCntnr = new ArrayList<String>();
        List<String> allPairs;
        List<Map> pairSums;
        String fastestPair;
        String slowestPair;

        while(pairsCntnr.size() > 0) {

            allPairs = getAllPairs(pairsCntnr);
            pairSums = getPairSums(allPairs);
            List<String> fastKeys = new ArrayList<String>(pairSums.get(0).keySet());
            fastestPair = fastKeys.get(0);
            fastestCntnr.add(fastestPair);
            fastKeys = null;

            List<String> slowKeys = new ArrayList<String>(pairSums.get(pairSums.size()-1).keySet());
            slowestPair = slowKeys.get(0);
            slowestCntnr.add(0,slowestPair);

            pairsCntnr = getLeftoverPairs(pairsCntnr, fastestPair, slowestPair);

        }

        fastestCntnr.addAll(slowestCntnr);

        return fastestCntnr;

    }


    /**
     Here we convert strings in pair formula into integers, highest between pair integers
     and built a formula using integers that we then can sum to calculate total time it
     took to cross the bridge.
     @param List<String> pair_formula
     @return: List<Integer> int_formula
     */
    private List<Integer> getIntFormula(List<String> pairFormula) {

        List<Integer> intFormula = new ArrayList<Integer>();
        String[] singles;

        for(String element : pairFormula) {
            singles = element.split("|");
            if (singles.length > 1) {
                List<Integer> strg = new ArrayList<Integer>();
                strg.add(PEOPLE.get(singles[0]));
                strg.add(PEOPLE.get(singles[2]));
                intFormula.add(Collections.max(strg));
                strg = null;

            } else {
                intFormula.add(PEOPLE.get(element));
            }
        }

        return intFormula;
    }


    /**
     Here by analyzing sorted_pair_sequence we derive the formula against,
     which we will use as blueprint to calculate time it takes to cross the bridge.
     @param List<String> sorted_pair_sequence
     @return: List<String> pair_formula
     */
    private List<String> getPairFormula(List<String> pairs) {

        String fastestPair = pairs.get(0);
        pairs.remove(fastestPair);
        String[] fastestPairSplit = fastestPair.split("|");

        List<String> pairFormula = new ArrayList<String>();
        List<String> otherSide = new ArrayList<String>();
        int pairsCounter = 0;

        boolean exitLoop = false;
        while(!exitLoop) {
            if (otherSide.size() == 0) {
                pairFormula.add(fastestPair);
                otherSide.add(fastestPairSplit[0]);
                otherSide.add(fastestPairSplit[2]);
                pairsCounter--;

            } else if (otherSide.size() == 2) {
                pairFormula.add(fastestPairSplit[0]);
                pairFormula.add(pairs.get(pairsCounter));
                otherSide.remove(fastestPairSplit[0]);

            } else {
                pairFormula.add(fastestPairSplit[2]);
                otherSide.remove(0);
                pairsCounter-- ;
            }

            if (pairsCounter >= pairs.size()-1) {
                if (otherSide.size() < 2) {
                    pairFormula.add(otherSide.get(0));
                }
                pairFormula.add(fastestPair);
                exitLoop = true;
            }

            pairsCounter++;

        }

        return pairFormula;

    }


    /**
     Here we filter out the pairs that we have already stored away in previous in current itteration,
     and we return a list of pairs, which we haven't yet analyzed.
     @param Map<String,Integer> pairs
     @param String fastest_pair
     @param String slowest_pair
     @return: Map<String,Integer> pairs
     */
    private Map<String,Integer> getLeftoverPairs(Map<String,Integer> pairs, String fastestPair, String slowestPair) {

        String[] fastestPairSplit = fastestPair.split("|");
        pairs.remove(fastestPairSplit[0]);
        pairs.remove(fastestPairSplit[2]);

        String[] slowestPairSplit = slowestPair.split("|");
        pairs.remove(slowestPairSplit[0]);
        pairs.remove(slowestPairSplit[2]);

        return pairs;

    }


    /**
     In this method we are summarizing combined time for all pairs.
     At the same time we are sorting the results so we could further know what pars are slowest
     and what pairs are fastest and what pairs are slower or faster than other pairs.
     @param List<String> all_pairs:
     @return: List<Map> pair_sums
     */
    private List<Map> getPairSums(List<String> allPairs) {

        List<Integer> sums = new ArrayList<Integer>();
        List pairSums = new ArrayList<Map>();
        Map<String,String> strg = new HashMap<String,String>();
        String[] pairSplit;
        int pairSum;

        for(String pair : allPairs) {
            pairSplit = pair.split("|");
            pairSum = PEOPLE.get(pairSplit[0]) +
                      PEOPLE.get(pairSplit[2]);
            sums.add(pairSum);
            strg.put(String.valueOf(pairSum),pair);
        }

        Collections.sort(sums);
        for(int sum : sums) {
            Map<String,Integer> subMap = new HashMap<String,Integer>();
            subMap.put(strg.get(String.valueOf(sum)), sum);
            pairSums.add(subMap);
            subMap = null;
        }

        return pairSums;

    }


    /**
     In this method we are permutating to get a list of all possible pairs from the
     given list given as an input.
     @param Map<String,Integer> pairsCntnr
     @return: List<String> all_pairs
     */
    private List<String> getAllPairs(Map<String,Integer> pairsCntnr) {

        List<String> allPairs = new ArrayList<String>();
        String pair;

        for(String keyOne : pairsCntnr.keySet()){
            for(String keyTwo : pairsCntnr.keySet()){
                pair = keyOne+"|"+keyTwo;
                if (!pair.equalsIgnoreCase(keyOne+"|"+keyOne) &
                        !allPairs.contains(keyTwo+"|"+keyOne)) {
                    allPairs.add(pair);
                }

            }
        }

        return allPairs;

    }


    /**
     Calculate a sum of integers in the list.
     @param List<Integer> intList
     @return: int sum
     */
    private Integer sum(List<Integer> intList) {
        int sum = 0;

        for(int element : intList) {
            sum += element;
        }
        return sum;
    }


    /**
     Concat all integer values of a list into a String.
     Values are separated by + sign.
     @param List<Integer> intList
     @return: String str
     */
    private String stringify(List<Integer> intList) {
        String str = "";

        for(int element : intList) {
            str += String.valueOf(element)+"+";
        }
        return str.substring(0, str.length() - 1);
    }

}
