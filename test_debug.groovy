// Test Part 2 logic on the example from the problem
def isInvalidIdPart2(long id) {
    def s = id.toString()
    (1..s.size().intdiv(2)).any { patternLen ->
        s.size() % patternLen == 0 && s == s.take(patternLen) * (s.size().intdiv(patternLen))
    }
}

// Test the example range from Part 2: 95-115 should find 99 and 111
def invalids = (95..115).findAll { isInvalidIdPart2(it) }
println "Range 95-115 invalid IDs: ${invalids}"
println "Expected: [99, 111]"
println "Match: ${invalids == [99, 111]}"

// Test range 998-1012 should find 999 and 1010
invalids = (998..1012).findAll { isInvalidIdPart2(it) }
println "\nRange 998-1012 invalid IDs: ${invalids}"
println "Expected: [999, 1010]"
println "Match: ${invalids == [999, 1010]}"

// Check what we're finding in your first range
invalids = (245284..245300).findAll { isInvalidIdPart2(it) }
println "\nRange 245284-245300 invalid IDs:"
invalids.each { println "  $it" }
