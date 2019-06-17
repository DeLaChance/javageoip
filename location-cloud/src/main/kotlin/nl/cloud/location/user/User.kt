package nl.cloud.location.user

import io.vertx.codegen.annotations.DataObject
import io.vertx.core.json.JsonObject
import java.util.*

@DataObject(generateConverter = true)
class User() {

    lateinit var id: UUID

    constructor(json: JsonObject?) : this() {
        id = UUID.fromString(json?.getString("id"))
    }

    fun toJson():JsonObject{
        val json =  JsonObject()
        json.put("id", id.toString())
        return json
    }
}