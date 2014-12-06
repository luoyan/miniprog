import scala.actors.Actor._
var startTime : Long = 0
val caller = self

val engrossedActor = actor {
    println("so far received messages " + mailboxSize)
    caller ! "send"
    Thread.sleep(3000)
    println("received messages after sleep " + mailboxSize)
    receive {
        case msg =>
            val receivedTime = System.currentTimeMillis() - startTime
            println("Received message " + msg + " after " + receivedTime + " ms")
    }
    caller ! "received"
}

receive { case _ => "aa" }

println("Sending Message ")
startTime = System.currentTimeMillis()
engrossedActor ! "hello Buddy"
val endTime = System.currentTimeMillis() - startTime

printf("took less than %dms to send message \n ", endTime)

receive {
    case _=> "bb"
}
