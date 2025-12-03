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
        // TODO: Implement joltage formation
        // Hint: Get characters at pos1 and pos2, concatenate them, convert to int
        return 0
    }

    /**
     * Find all possible joltages from a battery bank.
     * @param bank The battery bank string
     * @return List of all possible joltages from choosing 2 batteries
     */
    static List<Integer> findAllJoltages(String bank) {
        // TODO: Implement finding all joltages
        // Hint: Try all pairs (i, j) where i < j
        return []
    }

    /**
     * Find the maximum joltage possible from a single battery bank.
     * @param bank The battery bank string
     * @return The maximum joltage
     */
    static int findMaxJoltage(String bank) {
        // TODO: Implement finding maximum joltage
        // Hint: Find all joltages and return the maximum
        return 0
    }

    /**
     * Calculate the total output joltage from all battery banks.
     * @param banks List of battery bank strings
     * @return The sum of maximum joltages from all banks
     */
    static int calculateTotalJoltage(List<String> banks) {
        // TODO: Implement total joltage calculation
        // Hint: Find max joltage for each bank and sum them
        return 0
    }
}

static void main(String[] args) {
    def banks = new File('../../../input/day3.txt').readLines()

    def totalJoltage = BatteryBank.calculateTotalJoltage(banks)
    println("Total output joltage: ${totalJoltage}")
}
