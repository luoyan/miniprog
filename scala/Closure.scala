def loopThrough(number: Int)(closure: Int => Unit) {
    for (i <- 1 to number) { closure(i)}
}

var result = 0
val addIt = {value : Int => result += value}

loopThrough(10) {addIt}
println("Total " + result)

var product = 1
loopThrough(5) { product*=_ }
println("Total " + product)
