import spock.lang.Specification

class Day5Specification extends Specification {

    def "should parse a range string into start and end"() {
        when: "we parse a range string"
        def range = IngredientDatabase.parseRange(rangeStr)

        then: "should return [start, end]"
        range == expected

        where:
        rangeStr | expected
        "3-5"    | [3, 5]
        "10-14"  | [10, 14]
        "16-20"  | [16, 20]
    }

    def "should determine if an ID is fresh"() {
        given: "fresh ingredient ranges"
        def ranges = [[3, 5], [10, 14], [16, 20], [12, 18]]

        expect: "correct freshness determination"
        IngredientDatabase.isFresh(id, ranges) == fresh

        where:
        id | fresh
        1  | false  // Not in any range
        5  | true   // In range 3-5
        8  | false  // Not in any range
        11 | true   // In range 10-14
        17 | true   // In ranges 16-20 and 12-18
        32 | false  // Not in any range
    }

    def "should count fresh ingredients in example"() {
        given: "the example database"
        def input = """3-5
10-14
16-20
12-18

1
5
8
11
17
32"""

        when: "we parse and count fresh ingredients"
        def (ranges, availableIds) = IngredientDatabase.parseDatabase(input)
        def freshCount = IngredientDatabase.countFreshIngredients(ranges, availableIds)

        then: "should have 3 fresh ingredients"
        freshCount == 3
    }

    def "should parse database into ranges and available IDs"() {
        given: "database input"
        def input = """3-5
10-14

1
5
8"""

        when: "we parse the database"
        def (ranges, availableIds) = IngredientDatabase.parseDatabase(input)

        then: "should have correct ranges and IDs"
        ranges == [[3, 5], [10, 14]]
        availableIds == [1, 5, 8]
    }

    def "should handle overlapping ranges correctly"() {
        given: "overlapping ranges"
        def ranges = [[10, 14], [12, 18]]

        expect: "IDs in overlap are fresh"
        IngredientDatabase.isFresh(12, ranges) == true
        IngredientDatabase.isFresh(13, ranges) == true
        IngredientDatabase.isFresh(14, ranges) == true
    }

    // Part 2 tests - counting total fresh IDs in ranges
    def "should merge overlapping ranges"() {
        given: "overlapping ranges"
        def ranges = [[10L, 14L], [12L, 18L], [3L, 5L], [16L, 20L]]

        when: "we merge the ranges"
        def merged = IngredientDatabase.mergeRanges(ranges)

        then: "should have non-overlapping ranges"
        merged.size() <= ranges.size()
        // [3,5], [10,20] after merging
        merged.contains([3L, 5L])
        merged.any { it[0] == 10L && it[1] >= 18L }
    }

    def "should count total fresh IDs in example ranges"() {
        given: "the example ranges"
        def ranges = [[3L, 5L], [10L, 14L], [16L, 20L], [12L, 18L]]

        when: "we count total fresh IDs"
        def total = IngredientDatabase.countTotalFreshIds(ranges)

        then: "should be 14"
        total == 14L
    }

    def "should count IDs in a single range"() {
        expect: "correct count"
        IngredientDatabase.countIdsInRange(3L, 5L) == 3L
        IngredientDatabase.countIdsInRange(10L, 14L) == 5L
        IngredientDatabase.countIdsInRange(1L, 1L) == 1L
    }

    def "should merge adjacent ranges"() {
        given: "adjacent ranges"
        def ranges = [[3L, 5L], [6L, 8L], [10L, 12L]]

        when: "we merge the ranges"
        def merged = IngredientDatabase.mergeRanges(ranges)

        then: "adjacent ranges should merge"
        // [3,8], [10,12]
        merged.size() == 2
    }
}
