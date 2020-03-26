import vision.gears.webglmath.Texture

class CubedMaterial(
        program: Program,
        texture : TextureCube
) : Material(program) {
    init {
        this["envTexture"]?.set(texture)
        addComponentsAndGatherUniforms(program)
    }
}