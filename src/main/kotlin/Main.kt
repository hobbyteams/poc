import io.ktor.http.ContentType
import io.ktor.serialization.gson.gson
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import logic.findCourse

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { gson() }
        routing {
            get("/{id}") {
                val course = findCourse(call.parameters["id"]?.toInt() ?: 0)

                // protobuf response - test with https://github.com/spluxx/Protoman
                call.respondBytes(course.toByteArray(), ContentType("application", "x-protobuf"))

                // json response (although it's a jsonified protobuf object)
//                call.respond(course)
            }
        }
    }.start(wait = true)
}
