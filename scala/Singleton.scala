class Marker(val color: String) {
    println("Creating " + this)

    override def toString() : String = "marker color " + color
}

object MarkerFactory {
    private val markers = Map(
        "red" -> new Marker("red"),
        "blue" -> new Marker("blue"),
        "green" -> new Marker("green")
    )
    /*
    private val markers = Map("red" -> "haha red")
    */
    def getMarker(color : String) = if (markers.contains(color)) markers(color) else null
    /*
    def getMarker(color : String) = "hello " + color
    */
    
}

println(MarkerFactory.getMarker("red"))
println(MarkerFactory.getMarker("blue"))
println(MarkerFactory.getMarker("green"))
