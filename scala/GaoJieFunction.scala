def totalResultOverRange(number: Int, codeBlock: Int => Int) : Int = {
    var result = 0
    for (i <- 1 to number) {
        result += codeBlock(i)
    }
    result
}

def inject(arr: Array[Int], initial : Int, operation: (Int, Int) => Int) : Int = {
    var carryOver = initial
    arr.foreach(element => carryOver = operation(carryOver, element))
    carryOver
}

println(totalResultOverRange(11, i => i))
println(totalResultOverRange(11, i => if (i%2 == 0) i else 0))
println(totalResultOverRange(11, i => if (i%2 != 0) i else 0))

val array =  Array(2,4, 5, 8, 6, 1)
//val array =  Array(1,2,3)
val sum = inject(array, 1, (carryOver, elem) => carryOver + elem)
val max = inject(array, 1, (carryOver, elem) => Math.max(carryOver, elem))

println("Sum of elements in array " + array.toString() + " is " + sum)
println("Max of elements in array " + array.toString() + " is " + max)

val sum2 = (1 /: array) { (carryOver, elem) => carryOver + elem }
val max2 = (Integer.MIN_VALUE /: array) { (carryOver, elem) => Math.max(carryOver, elem) }

println("Sum of elements in array " + array.toString() + " is " + sum2)
println("Max of elements in array " + array.toString() + " is " + max2)
