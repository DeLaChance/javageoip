package nl.cloud.location.user

import io.vertx.codegen.annotations.DataObject
import io.vertx.core.json.JsonObject
import java.util.*

@DataObject(generateConverter = true)
class User() {

    lateinit var id: UUID
    lateinit var name: String

    constructor(json: JsonObject?) : this() {
        id = UUID.fromString(json?.getString("id"))
        name = json!!.getString("name")
    }

    constructor(name: String) : this() {
        this.id = UUID.randomUUID()
        this.name = name
    }

    fun toJson():JsonObject{
        val json =  JsonObject()
        json.put("id", id.toString())
        json.put("name", name)
        return json
    }
}