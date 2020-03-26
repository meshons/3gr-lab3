import vision.gears.webglmath.Texture

class TexturedMaterial(
        program: Program,
        texture : Texture
) : Material(program) {
    init {
        this["colorTexture"]?.set(texture)
        addComponentsAndGatherUniforms(program)
    }
}