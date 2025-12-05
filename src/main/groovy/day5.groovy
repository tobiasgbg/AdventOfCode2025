/*
--- Day 5: Cafeteria ---
As the forklifts break through the wall, the Elves are delighted to discover that there was a cafeteria on the other side after all.

You can hear a commotion coming from the kitchen. "At this rate, we won't have any time left to put the wreaths up in the dining hall!" Resolute in your quest, you investigate.

"If only we hadn't switched to the new inventory management system right before Christmas!" another Elf exclaims. You ask what's going on.

The Elves in the kitchen explain the situation: because of their complicated new inventory management system, they can't figure out which of their ingredients are fresh and which are spoiled. When you ask how it works, they give you a copy of their database (your puzzle input).

The database operates on ingredient IDs. It consists of a list of fresh ingredient ID ranges, a blank line, and a list of available ingredient IDs. For example:

3-5
10-14
16-20
12-18

1
5
8
11
17
32

The fresh ID ranges are inclusive: the range 3-5 means that ingredient IDs 3, 4, and 5 are all fresh. The ranges can also overlap; an ingredient ID is fresh if it is in any range.

The Elves are trying to determine which of the available ingredient IDs are fresh. In this example, this is done as follows:

Ingredient ID 1 is spoiled because it does not fall into any range.
Ingredient ID 5 is fresh because it falls into range 3-5.
Ingredient ID 8 is spoiled.
Ingredient ID 11 is fresh because it falls into range 10-14.
Ingredient ID 17 is fresh because it falls into range 16-20 as well as range 12-18.
Ingredient ID 32 is spoiled.

So, in this example, 3 of the available ingredient IDs are fresh.

Process the database file from the new inventory management system. How many of the available ingredient IDs are fresh?
 */

class IngredientDatabase {

    /**
     * Parse a range string like "3-5" into [start, end].
     * @param rangeStr The range string
     * @return List with [start, end]
     */
    static List<Long> parseRange(String rangeStr) {
        rangeStr.split('-').collect { it as Long }
    }

    /**
     * Determine if an ingredient ID is fresh (falls in any range).
     * @param id The ingredient ID
     * @param ranges List of [start, end] ranges
     * @return True if fresh, false if spoiled
     */
    static boolean isFresh(Long id, List<List<Long>> ranges) {
        ranges.any { start, end -> id >= start && id <= end }
    }

    /**
     * Parse the database input into ranges and available IDs.
     * @param input The database text
     * @return Tuple of [ranges, availableIds]
     */
    static List parseDatabase(String input) {
      def (rangeSection, idSection) = input.split(/\r?\n\r?\n/)
      [
          rangeSection.split(/\r?\n/)*.trim().findAll { it }.collect { parseRange(it) },        
          idSection.split(/\r?\n/)*.trim().findAll { it }.collect { it as Long }
      ]
    }

    /**
     * Count how many available ingredient IDs are fresh.
     * @param ranges List of fresh ID ranges
     * @param availableIds List of available ingredient IDs
     * @return Count of fresh ingredients
     */
    static Long countFreshIngredients(List<List<Long>> ranges, List<Long> availableIds) {
        availableIds.count { isFresh(it, ranges) }
    }

    // Part 2 methods - counting total fresh IDs

    /**
     * Count the number of IDs in a single range (inclusive).
     * @param start Start of range
     * @param end End of range
     * @return Count of IDs in range
     */
    static long countIdsInRange(long start, long end) {
         end - start + 1
    }

    /**
     * Merge overlapping and adjacent ranges.
     * @param ranges List of [start, end] ranges
     * @return List of merged non-overlapping ranges
     */
    static List<List<Long>> mergeRanges(List<List<Long>> ranges) {
      def sorted = ranges.sort { it[0] }
      sorted.tail().inject([sorted[0]]) { merged, current ->
          def last = merged[-1]
          if (current[0] <= last[1] + 1) {
              merged[-1] = [last[0], Math.max(last[1], current[1])]
          } else {
              merged << current
          }
          merged
      }
    }

    /**
     * Count total number of unique fresh IDs across all ranges.
     * @param ranges List of fresh ID ranges
     * @return Total count of unique fresh IDs
     */
    static long countTotalFreshIds(List<List<Long>> ranges) {
        mergeRanges(ranges).sum { countIdsInRange(it[0], it[1]) }
    }
}

static void main(String[] args) {
    def input = new File('../../../input/day5.txt').text
    def (ranges, availableIds) = IngredientDatabase.parseDatabase(input)

    // Part 1: Count fresh ingredients from available list
    def freshCount = IngredientDatabase.countFreshIngredients(ranges, availableIds)
    println("Part 1 - Fresh ingredients: ${freshCount}")

    // Part 2: Count total unique fresh IDs in all ranges
    def totalFreshIds = IngredientDatabase.countTotalFreshIds(ranges)
    println("Part 2 - Total fresh IDs: ${totalFreshIds}")
}
