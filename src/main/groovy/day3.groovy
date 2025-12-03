/*
--- Day 3: Lobby ---
You descend a short staircase, enter the surprisingly vast lobby, and are quickly cleared by the security checkpoint. When you get to the main elevators, however, you discover that each one has a red light above it: they're all offline.

"Sorry about that," an Elf apologizes as she tinkers with a nearby control panel. "Some kind of electrical surge seems to have fried them. I'll try to get them online soon."

You explain your need to get further underground. "Well, you could at least take the escalator down to the printing department, not that you'd get much further than that without the elevators working. That is, you could if the escalator weren't also offline."

"But, don't worry! It's not fried; it just needs power. Maybe you can get it running while I keep working on the elevators."

There are batteries nearby that can supply emergency power to the escalator for just such an occasion. The batteries are each labeled with their joltage rating, a value from 1 to 9. You make a note of their joltage ratings (your puzzle input). For example:

987654321111111
811111111111119
234234234234278
818181911112111

The batteries are arranged into banks; each line of digits in your input corresponds to a single bank of batteries. Within each bank, you need to turn on exactly two batteries; the joltage that the bank produces is equal to the number formed by the digits on the batteries you've turned on. For example, if you have a bank like 12345 and you turn on batteries 2 and 4, the bank would produce 24 jolts. (You cannot rearrange batteries.)

You'll need to find the largest possible joltage each bank can produce. In the above example:

In 987654321111111, you can make the largest joltage possible, 98, by turning on the first two batteries.
In 811111111111119, you can make the largest joltage possible by turning on the batteries labeled 8 and 9, producing 89 jolts.
In 234234234234278, you can make 78 by turning on the last two batteries (marked 7 and 8).
In 818181911112111, the largest joltage you can produce is 92.

The total output joltage is the sum of the maximum joltage from each bank, so in this example, the total output joltage is 98 + 89 + 78 + 92 = 357.

There are many batteries in front of you. Find the maximum joltage possible from each bank; what is the total output joltage?
 */

class BatteryBank {

    /**
     * Form a joltage value from two battery positions.
     * @param bank The battery bank string
     * @param pos1 First battery position (0-indexed)
     * @param pos2 Second battery position (0-indexed)
     * @return The joltage formed by concatenating the two digits
     */
    static int formJoltage(String bank, int pos1, int pos2) {
        (bank[pos1].toString() + bank[pos2].toString()) as Integer
    }

    /**
     * Find all possible joltages from a battery bank.
     * @param bank The battery bank string
     * @return List of all possible joltages from choosing 2 batteries
     */
    static List<Integer> findAllJoltages(String bank) {
        (0..<bank.length()).collectMany { i ->
            ((i+1)..<bank.length()).collect { j ->
                formJoltage(bank, i, j)
            }
        }
    }

    /**
     * Find the maximum joltage possible from a single battery bank.
     * @param bank The battery bank string
     * @return The maximum joltage
     */
    static int findMaxJoltage(String bank) {
        findAllJoltages(bank).max()
    }

    /**
     * Calculate the total output joltage from all battery banks.
     * @param banks List of battery bank strings
     * @return The sum of maximum joltages from all banks
     */
    static int calculateTotalJoltage(List<String> banks) {
        banks.sum { findMaxJoltage(it) }
    }

    // Part 2 methods - selecting 12 batteries instead of 2

    /**
     * Find the maximum joltage by selecting exactly 12 batteries.
     * Uses a greedy algorithm to find the lexicographically largest subsequence.
     * @param bank The battery bank string
     * @return The maximum 12-digit joltage
     */
    static long findMaxJoltagePart2(String bank) {
      def result = []
      int pos = 0

      while (result.size() < 12) {

          int remaining = 12 - result.size()
          int windowEnd = bank.length() - remaining

          // Find max digit in window [pos..windowEnd]
          def maxDigit = bank[pos..windowEnd].toList().max()
          // Add it to result
          result.add(maxDigit)
          // Update pos to be right after where you found it
          pos = pos + bank[pos..windowEnd].indexOf(maxDigit) + 1
      }

      result.join("") as Long
    }

    /**
     * Calculate the total output joltage from all battery banks for Part 2.
     * @param banks List of battery bank strings
     * @return The sum of maximum 12-digit joltages from all banks
     */
    static long calculateTotalJoltagePart2(List<String> banks) {
        banks.sum { findMaxJoltagePart2(it) }
    }
}

static void main(String[] args) {
    def banks = new File('../../../input/day3.txt').readLines()

    // Part 1: Select 2 batteries
    def totalJoltage = BatteryBank.calculateTotalJoltage(banks)
    println("Part 1 - Total output joltage (2 batteries): ${totalJoltage}")

    // Part 2: Select 12 batteries
    def totalJoltagePart2 = BatteryBank.calculateTotalJoltagePart2(banks)
    println("Part 2 - Total output joltage (12 batteries): ${totalJoltagePart2}")
}
