import kotlin.js.Date
import lib.react.*

class Hello(val name: String = "Anonymous")
    : Props<HelloComponent>
class HelloComponent : SimpleComponent<Hello>({
    html {
        h2 {
            + "Hello, "
            em { + name }
            + "!"
        }
    }
})

class WithTime(vararg val children: ReactElement)
    : Props<WithTimeComp>
class WithTimeComp : SimpleComponent<WithTime>({
    html {
        div {
            + Hello("Man")
            hr
            p [{ title = "Current time" }] {
                + "Today is ${Date()}"
            }
        }
    }
})

class Root : SimpleComponent<Nothing>({
    + WithTime()
})