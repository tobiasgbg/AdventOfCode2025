import spock.lang.Specification

class Day3Specification extends Specification {

    def "should find maximum joltage 98 from bank 987654321111111"() {
        given: "a battery bank"
        def bank = "987654321111111"

        when: "we find the maximum joltage"
        def maxJoltage = BatteryBank.findMaxJoltage(bank)

        then: "should be 98"
        maxJoltage == 98
    }

    def "should find maximum joltage 89 from bank 811111111111119"() {
        given: "a battery bank"
        def bank = "811111111111119"

        when: "we find the maximum joltage"
        def maxJoltage = BatteryBank.findMaxJoltage(bank)

        then: "should be 89"
        maxJoltage == 89
    }

    def "should find maximum joltage 78 from bank 234234234234278"() {
        given: "a battery bank"
        def bank = "234234234234278"

        when: "we find the maximum joltage"
        def maxJoltage = BatteryBank.findMaxJoltage(bank)

        then: "should be 78"
        maxJoltage == 78
    }

    def "should find maximum joltage 92 from bank 818181911112111"() {
        given: "a battery bank"
        def bank = "818181911112111"

        when: "we find the maximum joltage"
        def maxJoltage = BatteryBank.findMaxJoltage(bank)

        then: "should be 92"
        maxJoltage == 92
    }

    def "should form joltage 24 from batteries at positions 1 and 3 in bank 12345"() {
        given: "a battery bank"
        def bank = "12345"

        when: "we form joltage from positions 1 and 3"
        def joltage = BatteryBank.formJoltage(bank, 1, 3)

        then: "should be 24"
        joltage == 24
    }

    def "should calculate total output joltage for example input"() {
        given: "the example battery banks"
        def banks = [
            "987654321111111",
            "811111111111119",
            "234234234234278",
            "818181911112111"
        ]

        when: "we calculate the total output joltage"
        def totalJoltage = BatteryBank.calculateTotalJoltage(banks)

        then: "should be 357"
        totalJoltage == 357
    }

    def "should find all possible joltages from bank 12345"() {
        given: "a battery bank"
        def bank = "12345"

        when: "we find all possible joltages"
        def joltages = BatteryBank.findAllJoltages(bank)

        then: "should contain all pairs"
        joltages.size() == 10 // C(5,2) = 10 combinations
        joltages.contains(12) // positions 0,1
        joltages.contains(15) // positions 0,4
        joltages.contains(45) // positions 3,4
    }
}
