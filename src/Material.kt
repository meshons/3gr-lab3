import vision.gears.webglmath.UniformProvider

open class Material(program : Program) : UniformProvider("material") {
  init {
    addComponentsAndGatherUniforms(program)
  }
}
