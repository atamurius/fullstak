package app.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController class Test {

    @GetMapping("/test") fun test() = mapOf("random" to Math.random())
}