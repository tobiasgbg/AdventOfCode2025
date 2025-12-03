import spock.lang.Specification

class Day2Specification extends Specification {

    def "should find invalid IDs in range 11-22"() {
        given: "the range 11-22"
        def range = "11-22"

        when: "we find invalid IDs"
        def invalidIds = GiftShop.findInvalidIdsInRange(range)

        then: "should find 11 and 22"
        invalidIds == [11, 22]
    }

    def "should find invalid ID 99 in range 95-115"() {
        given: "the range 95-115"
        def range = "95-115"

        when: "we find invalid IDs"
        def invalidIds = GiftShop.findInvalidIdsInRange(range)

        then: "should find only 99"
        invalidIds == [99]
    }

    def "should find invalid ID 1010 in range 998-1012"() {
        given: "the range 998-1012"
        def range = "998-1012"

        when: "we find invalid IDs"
        def invalidIds = GiftShop.findInvalidIdsInRange(range)

        then: "should find only 1010"
        invalidIds == [1010]
    }

    def "should identify 55 as invalid (5 repeated twice)"() {
        expect:
        GiftShop.isInvalidId(55) == true
    }

    def "should identify 6464 as invalid (64 repeated twice)"() {
        expect:
        GiftShop.isInvalidId(6464) == true
    }

    def "should identify 123123 as invalid (123 repeated twice)"() {
        expect:
        GiftShop.isInvalidId(123123) == true
    }

    def "should identify 1188511885 as invalid"() {
        expect:
        GiftShop.isInvalidId(1188511885) == true
    }

    def "should identify 222222 as invalid"() {
        expect:
        GiftShop.isInvalidId(222222) == true
    }

    def "should identify 101 as valid (not repeated twice)"() {
        expect:
        GiftShop.isInvalidId(101) == false
    }

    def "should identify 1698522 as valid"() {
        expect:
        GiftShop.isInvalidId(1698522) == false
    }

    def "should find no invalid IDs in range 1698522-1698528"() {
        given: "the range 1698522-1698528"
        def range = "1698522-1698528"

        when: "we find invalid IDs"
        def invalidIds = GiftShop.findInvalidIdsInRange(range)

        then: "should find none"
        invalidIds == []
    }

    def "should calculate sum for example input"() {
        given: "the example ranges"
        def input = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
                    "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
                    "824824821-824824827,2121212118-2121212124"

        when: "we calculate the sum of all invalid IDs"
        def sum = GiftShop.sumInvalidIds(input)

        then: "should equal 1227775554"
        sum == 1227775554L
    }

    def "should parse ranges correctly"() {
        given: "a comma-separated list of ranges"
        def input = "11-22,95-115,998-1012"

        when: "we parse the ranges"
        def ranges = GiftShop.parseRanges(input)

        then: "should return list of range objects"
        ranges.size() == 3
        ranges[0] == [11, 22]
        ranges[1] == [95, 115]
        ranges[2] == [998, 1012]
    }

    // ========== Part Two ==========

    def "part2: should identify 12341234 as invalid (1234 repeated 2 times)"() {
        expect:
        GiftShop.isInvalidIdPart2(12341234) == true
    }

    def "part2: should identify 123123123 as invalid (123 repeated 3 times)"() {
        expect:
        GiftShop.isInvalidIdPart2(123123123) == true
    }

    def "part2: should identify 1212121212 as invalid (12 repeated 5 times)"() {
        expect:
        GiftShop.isInvalidIdPart2(1212121212L) == true
    }

    def "part2: should identify 1111111 as invalid (1 repeated 7 times)"() {
        expect:
        GiftShop.isInvalidIdPart2(1111111) == true
    }

    def "part2: should identify 111 as invalid (1 repeated 3 times)"() {
        expect:
        GiftShop.isInvalidIdPart2(111) == true
    }

    def "part2: should identify 999 as invalid (9 repeated 3 times)"() {
        expect:
        GiftShop.isInvalidIdPart2(999) == true
    }

    def "part2: should identify 565656 as invalid (56 repeated 3 times)"() {
        expect:
        GiftShop.isInvalidIdPart2(565656) == true
    }

    def "part2: should identify 824824824 as invalid (824 repeated 3 times)"() {
        expect:
        GiftShop.isInvalidIdPart2(824824824) == true
    }

    def "part2: should identify 2121212121 as invalid (21 repeated 5 times)"() {
        expect:
        GiftShop.isInvalidIdPart2(2121212121L) == true
    }

    def "part2: should identify 101 as valid (not repeated)"() {
        expect:
        GiftShop.isInvalidIdPart2(101) == false
    }

    def "part2: should find invalid IDs in range 95-115"() {
        given: "the range 95-115"
        def range = "95-115"

        when: "we find invalid IDs for part 2"
        def invalidIds = GiftShop.findInvalidIdsInRangePart2(range)

        then: "should find 99 and 111"
        invalidIds == [99, 111]
    }

    def "part2: should find invalid IDs in range 998-1012"() {
        given: "the range 998-1012"
        def range = "998-1012"

        when: "we find invalid IDs for part 2"
        def invalidIds = GiftShop.findInvalidIdsInRangePart2(range)

        then: "should find 999 and 1010"
        invalidIds == [999, 1010]
    }

    def "part2: should calculate sum for example input"() {
        given: "the example ranges"
        def input = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
                    "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
                    "824824821-824824827,2121212118-2121212124"

        when: "we calculate the sum of all invalid IDs for part 2"
        def sum = GiftShop.sumInvalidIdsPart2(input)

        then: "should equal 4174379265"
        sum == 4174379265L
    }

    def "part2: should calculate sum for full input"() {
        given: "the full puzzle input"
        def input = "245284-286195,797927-983972,4949410945-4949555758,115-282,8266093206-8266228431,1-21,483873-655838,419252-466133,6190-13590,3876510-4037577,9946738680-9946889090,99954692-100029290,2398820-2469257,142130432-142157371,9797879567-9798085531,209853-240025,85618-110471,35694994-35766376,4395291-4476150,33658388-33694159,680915-772910,4973452995-4973630970,52-104,984439-1009605,19489345-19604283,22-42,154149-204168,7651663-7807184,287903-402052,2244-5558,587557762-587611332,307-1038,16266-85176,422394377-422468141"

        when: "we calculate the sum of all invalid IDs for part 2"
        def sum = GiftShop.sumInvalidIdsPart2(input)

        then: "should equal 70187097315"
        sum == 70187097315L
    }
}
