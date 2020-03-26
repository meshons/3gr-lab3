import org.w3c.xhr.XMLHttpRequest
import vision.gears.webglmath.UniformProvider
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class JsonMesh(
        val vertices : Array<Float>,
        val normals : Array<Float>,
        val texturecoords : Array<Array<Float>>,
        val faces : Array<Array<Short>>)

@Serializable
data class JsonModel(
        val meshes : Array<JsonMesh>)

class MultiMesh(
        gl : WebGL2RenderingContext,
        jsonModelFileUrl : String,
        vararg materials : Material) : UniformProvider("multiMesh") {

    init{
        val request = XMLHttpRequest()
        request.overrideMimeType("application/json")
        request.open("GET", jsonModelFileUrl, false)
        request.onloadend = {
            val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys=true))
            val jsonModel = json.parse(JsonModel.serializer(), request.responseText)

            if(jsonModel.meshes.size != materials.size) {
                throw Error("MultiMesh from ${jsonModelFileUrl} has ${jsonModel.meshes.size} submeshes, but only ${materials.size} materials were provided.")
            }
            val submeshes = Array<Mesh>(jsonModel.meshes.size) {
                i ->
                Mesh(materials[i], SubmeshGeometry(gl, jsonModel.meshes[i]))}

            addComponentsAndGatherUniforms(*submeshes)
        }

        request.send()
    }
}
