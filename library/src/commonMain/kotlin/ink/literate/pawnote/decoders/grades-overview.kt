package ink.literate.pawnote.decoders

import ink.literate.pawnote.models.GradesOverview
import ink.literate.pawnote.models.SessionInformation
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun decodeGradesOverview(
    overview: JsonObject,
    sessionInformation: SessionInformation
): GradesOverview {
  return GradesOverview(
      grades =
          overview["listeDevoirs"]!!.jsonObject["V"]!!.jsonArray.map {
            decodeGrade(it.jsonObject, sessionInformation)
          },
      subjectsAverages =
          overview["listeServices"]!!.jsonObject["V"]!!.jsonArray.map {
            decodeSubjectAverages(it.jsonObject)
          },
      classAverage =
          if (overview["moyGeneraleClasse"] != null)
              decodeGradeValue(
                  overview["moyGeneraleClasse"]!!.jsonObject["V"]!!.jsonPrimitive.content)
          else null,
      overallAverage =
          if (overview["moyGenerale"] != null)
              decodeGradeValue(overview["moyGenerale"]!!.jsonObject["V"]!!.jsonPrimitive.content)
          else null)
}
