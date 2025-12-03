/*
--- Day 2: Gift Shop ---
You get inside and take the elevator to its only other stop: the gift shop. "Thank you for visiting the North Pole!" gleefully exclaims a nearby sign. You aren't sure who is even allowed to visit the North Pole, but you know you can access the lobby through here, and from there you can access the rest of the North Pole base.

As you make your way through the surprisingly extensive selection, one of the clerks recognizes you and asks for your help.

As it turns out, one of the younger Elves was playing on a gift shop computer and managed to add a whole bunch of invalid product IDs to their gift shop database! Surely, it would be no trouble for you to identify the invalid product IDs for them, right?

They've even checked most of the product ID ranges already; they only have a few product ID ranges (your puzzle input) that you'll need to check. For example:

11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
1698522-1698528,446443-446449,38593856-38593862,565653-565659,
824824821-824824827,2121212118-2121212124
(The ID ranges are wrapped here for legibility; in your input, they appear on a single long line.)

The ranges are separated by commas (,); each range gives its first ID and last ID separated by a dash (-).

Since the young Elf was just doing silly patterns, you can find the invalid IDs by looking for any ID which is made only of some sequence of digits repeated twice. So, 55 (5 twice), 6464 (64 twice), and 123123 (123 twice) would all be invalid IDs.

None of the numbers have leading zeroes; 0101 isn't an ID at all. (101 is a valid ID that you would ignore.)

Your job is to find all of the invalid IDs that appear in the given ranges. In the above example:

11-22 has two invalid IDs, 11 and 22.
95-115 has one invalid ID, 99.
998-1012 has one invalid ID, 1010.
1188511880-1188511890 has one invalid ID, 1188511885.
222220-222224 has one invalid ID, 222222.
1698522-1698528 contains no invalid IDs.
446443-446449 has one invalid ID, 446446.
38593856-38593862 has one invalid ID, 38593859.
The rest of the ranges contain no invalid IDs.
Adding up all the invalid IDs in this example produces 1227775554.

What do you get if you add up all of the invalid IDs?
 */

class GiftShop {

    /**
     * Checks if an ID is invalid (repeated pattern).
     * Invalid IDs are numbers where the digits form a pattern repeated exactly twice.
     * Examples: 55 (5 twice), 6464 (64 twice), 123123 (123 twice)
     */
    static boolean isInvalidId(long id) {
      def s = id.toString()
      def mid = s.size().intdiv(2)
      s.size() % 2 == 0 && s.take(mid) == s.drop(mid)
    }

    /**
     * Generate the next potential invalid ID from current position.
     * Skips ahead to avoid checking every single number.
     */
    static long nextPotentialInvalidId(long current) {
        def s = current.toString()
        def len = s.length()
        
        // If odd length, jump to next even length
        if (len % 2 == 1) {
            return 10L ** len
        }
        
        current
    }

    /**
     * Parse a comma-separated list of ranges into a list of [start, end] pairs.
     * Example: "11-22,95-115" -> [[11, 22], [95, 115]]
     */
    static List<List<Long>> parseRanges(String input) {
        input.split(',').collect { it.split('-')*.toLong() }
    }

    /**
     * Checks if an ID is invalid for Part 2 (repeated pattern at least twice).
     * Invalid IDs are numbers where the digits form a pattern repeated at least twice.
     * Examples: 12341234 (1234 twice), 123123123 (123 three times), 1111111 (1 seven times)
     */
    static boolean isInvalidIdPart2(long id) {
        def s = id.toString()
        (1..s.size().intdiv(2)).any { patternLen ->
            s.size() % patternLen == 0 && s == s.take(patternLen) * (s.size().intdiv(patternLen))
        }
    }

    /**
     * Find all invalid IDs within a single range.
     * Optimized: generates patterns mathematically instead of iterating.
     */
    static List<Long> findInvalidIdsInRange(String rangeStr) {
        def (start, end) = rangeStr.split('-')*.toLong()
        def result = [] as Set  // Use Set to avoid duplicates
        
        // For Part 1, only pattern length of exactly half the string
        def startLen = start.toString().length()
        def endLen = end.toString().length()
        
        for (int len = startLen; len <= endLen; len++) {
            if (len % 2 == 1) continue  // Only even-length numbers can have exact half repetition
            
            def rangeStart = Math.max(start, 10L ** (len - 1))
            def rangeEnd = Math.min(end, 10L ** len - 1)
            
            if (rangeStart > rangeEnd) continue
            
            // For even-length numbers, check if first half == second half
            int patternLen = len.intdiv(2)
            long patternStart = 10L ** (patternLen - 1)
            long patternEnd = 10L ** patternLen - 1
            
            for (long pattern = patternStart; pattern <= patternEnd; pattern++) {
                def patternStr = pattern.toString()
                def fullStr = patternStr + patternStr
                long fullNum = fullStr as Long
                
                if (fullNum >= rangeStart && fullNum <= rangeEnd) {
                    result << fullNum
                }
            }
        }
        
        return result.sort()
    }

    /**
     * Calculate the sum of all invalid IDs in the given ranges.
     */
    static long sumInvalidIds(String input) {
        input.split(',').collectMany { findInvalidIdsInRange(it) }.sum() ?: 0L
    }

    // ========== Part Two ==========

    /**
     * Get all invalid IDs in a range without iterating every number.
     * Groups IDs by digit length and uses math to generate patterns.
     */
    static List<Long> findInvalidIdsInRangePart2(String rangeStr) {
      def (start, end) = rangeStr.split('-')*.toLong()

      (start.toString().length()..end.toString().length())
          .collectMany { len ->
              def s = [start, 10L ** (len - 1)].max()
              def e = [end, 10L ** len - 1].min()
              s <= e ? findValidPatternsInRange(s, e, len) : []
          }
          .toSet()
          .sort()
    }
    
    /**
     * Find valid (invalid ID) patterns of a specific digit length in a range.
     */
    static List<Long> findValidPatternsInRange(long start, long end, int len) {
        def result = []
        
        // Check all possible pattern lengths that divide evenly into len
        for (int patternLen = 1; patternLen <= len.intdiv(2); patternLen++) {
            if (len % patternLen == 0) {
                def reps = len.intdiv(patternLen)
                result.addAll(findPatternsOfLength(start, end, patternLen, reps))
            }
        }
        
        return result
    }
    
    /**
     * Find IDs formed by repeating a pattern of given length.
     */
    static List<Long> findPatternsOfLength(long start, long end, int patternLen, int reps) {
        def result = []
        
        long patternStart = 10L ** (patternLen - 1)
        long patternEnd = 10L ** patternLen - 1
        
        for (long pattern = patternStart; pattern <= patternEnd; pattern++) {
            def patternStr = pattern.toString()
            def fullStr = patternStr * reps
            long fullNum = fullStr as Long
            
            if (fullNum >= start && fullNum <= end) {
                result << fullNum
            }
        }
        
        return result
    }

    /**
     * Calculate the sum of all invalid IDs in the given ranges for Part 2.
     */
    static long sumInvalidIdsPart2(String input) {
        input.split(',').collectMany { findInvalidIdsInRangePart2(it) }.sum() ?: 0L
    }
}

static void main(String[] args) {
    def input = new File('../../../input/day2.txt').text.trim()

    def sumPart1 = GiftShop.sumInvalidIds(input)
    def allInvalidIdsPart2 = input.split(',').collectMany { GiftShop.findInvalidIdsInRangePart2(it) }
    def sumPart2 = allInvalidIdsPart2.sum() ?: 0L
    
    println(sumPart1)
    println(sumPart2)
}
