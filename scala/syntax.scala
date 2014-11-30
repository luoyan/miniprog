class Car(val year: Int) {
    private var milesDriven: Int = 0

    def miles() = milesDriven
    def drive(distance: Int) {
        milesDriven += Math.abs(distance)
    }
}

class Persion(val firstName: String, val lastName: String) {
    private var position: String = _

    println("Creating " + toString())

    def this(firstName: String, lastName: String, positionHeld: String) {
        this(firstName, lastName)
        position = positionHeld
    }

    override def toString() : String = {
        firstName + "" + lastName + " holds " + position + " position"
    }
}

def getPersionInfo(primaryKey : Int) = {
    ("luoyan", 30, "beijing")
}
val (name, age, city) = getPersionInfo(1)
println("name " + name)
println("age " + age)
println("city " + city)

var car = new Car(2014)
println("Car made in year " + car.year)
println("Car drive miles " + car.miles)
car.drive(1000)
println("Car drive miles " + car.miles)

val john = new Persion("John", "Smith", "Analyst")
println(john)
