import scala.actors.Actor._
def sumOfFactor(number: Int) = {
    (0 /: (1 to number)) {
        (sum, i) => if (number % i == 0) sum + i else sum
    }
}

def isPerfect(candidate: Int) = 2 * candidate == sumOfFactor(candidate)


def sumOfFactorsInRange(lower: Int, upper: Int, number: Int) = {
    (0 /: (lower to upper)) { (sum, i) => if (number % i == 0) sum +i else sum }
}

def isPerfectConcurrent(candidate: Int) = {
    val RANGE = 100000
    val numberOfPartitions = (candidate.toDouble/ RANGE).ceil.toInt
    val caller = self

    for (i <- 0 until numberOfPartitions) {
        val lower = i * RANGE + 1
        val upper = candidate min (i + 1) * RANGE
        actor {
            caller ! sumOfFactorsInRange(lower, upper, candidate)
        }
    }
    val sum = (0 /: (0 until numberOfPartitions)) {
        (partitionSum, i) => receive {
            case sumInRange : Int => partitionSum + sumInRange
        }
    }

    2 * candidate == sum
}

def usage(args0: String) = {
    println(args0 + "\t0/1")
}
if (args.length == 0) {
    usage(args(0))
}
else if (args.length == 1) {
    println("args(0) " + args(0))
    if (args(0) == '0') {
        println("6 is perfect ? " + isPerfect(6))
        println("33550336 is perfect ? " + isPerfect(33550336))
        println("33550337 is perfect ? " + isPerfect(33550337))
    }
    else {
        println("6 is perfect ? " + isPerfectConcurrent(6))
        println("33550336 is perfect ? " + isPerfectConcurrent(33550336))
        println("33550337 is perfect ? " + isPerfectConcurrent(33550337))
    }
}
