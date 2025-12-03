class GiftShop {
    static List<Long> findValidPatternsInRange(long start, long end, int len) {
        def result = []
        for (int patternLen = 1; patternLen <= len.intdiv(2); patternLen++) {
            if (len % patternLen == 0) {
                def reps = len.intdiv(patternLen)
                result.addAll(findPatternsOfLength(start, end, patternLen, reps))
            }
        }
        return result
    }
    
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

    static List<Long> findInvalidIdsInRangePart2(String rangeStr) {
        def (start, end) = rangeStr.split('-')*.toLong()
        def result = [] as Set
        def startLen = start.toString().length()
        def endLen = end.toString().length()
        
        for (int len = startLen; len <= endLen; len++) {
            def rangeStart = Math.max(start, 10L ** (len - 1))
            def rangeEnd = Math.min(end, 10L ** len - 1)
            if (rangeStart > rangeEnd) continue
            result.addAll(findValidPatternsInRange(rangeStart, rangeEnd, len))
        }
        return result.sort()
    }
}

def input = new File('input/day2.txt').text.trim()
def allPart2Ids = input.split(',').collectMany { GiftShop.findInvalidIdsInRangePart2(it) }
def allPart2IdsDedup = allPart2Ids as Set
println("Total Part 2 IDs: ${allPart2Ids.size()}")
println("Unique Part 2 IDs: ${allPart2IdsDedup.size()}")
println("Duplicates: ${allPart2Ids.size() - allPart2IdsDedup.size()}")

// Check if any ID fails the validity check
def invalid = allPart2IdsDedup.findAll { id ->
    def s = id.toString()
    !(1..s.size().intdiv(2)).any { patternLen ->
        s.size() % patternLen == 0 && s == s.take(patternLen) * (s.size().intdiv(patternLen))
    }
}
if (invalid) {
    println "ERROR: Found invalid IDs that don't match pattern: $invalid"
} else {
    println "All IDs match the pattern correctly"
}

println("\nSum: ${allPart2IdsDedup.sum()}")
