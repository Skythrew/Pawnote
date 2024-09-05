package decoders

import api.private.AES
import api.private.aesKeys
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import models.Attachment
import models.AttachmentKind
import models.SessionInformation
import java.net.URLEncoder

actual fun decodeAttachment (attachment: JsonObject, sessionInfo: SessionInformation, parameters: JsonObject): Attachment {
    val name = attachment["L"]?.jsonPrimitive?.content ?: ""
    val kind = attachment["G"]!!.jsonPrimitive.int
    val id = attachment["N"]!!.jsonPrimitive.content
    val url: String

    if (kind == AttachmentKind.Link.code)
        url = attachment["url"]?.jsonPrimitive?.content ?: name
    else {
        val keys = aesKeys(sessionInfo)

        val data = JsonObject(parameters + buildJsonObject {
            put("N", id)
            put("Actif", true)
        })

        val encrypted = AES.encrypt(Json.encodeToString(data).toByteArray(), keys.key, keys.iv)
        url = "${sessionInfo.url}/FichiersExternes/${encrypted}/${URLEncoder.encode(name)}?Session=${sessionInfo.id}"
    }

    return Attachment(
        kind = AttachmentKind.fromInt(kind),
        name = name,
        url = url,
        id = id
    )
}