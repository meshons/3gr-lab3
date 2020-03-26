import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Mat4
import kotlin.js.Date

class Scene (
  val gl : WebGL2RenderingContext) : UniformProvider("scene") {

  val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "shaders/trafo-vs.glsl")
  val vsBackground = Shader(gl, GL.VERTEX_SHADER, "shaders/background-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "shaders/solid-fs.glsl")
  val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "shaders/textured-fs.glsl")
  val fsBackground = Shader(gl, GL.FRAGMENT_SHADER, "shaders/background-fs.glsl")
  val solidProgram = Program(gl, vsTrafo, fsSolid)
  val texturedProgram = Program(gl, vsTrafo, fsTextured)
  val quadGeometry = TexturedQuadGeometry(gl)
  val backgroundProgram = Program(gl, vsBackground, fsBackground)

  val slowpoke = GameObject(
          MultiMesh(
                  gl,
                  "media/slowpoke/Slowpoke.json",
                  TexturedMaterial(texturedProgram, Texture2D(gl, "media/slowpoke/YadonDh.png")),
                  TexturedMaterial(texturedProgram, Texture2D(gl, "media/slowpoke/YadonEyeDh.png"))
          )
  )

  val background = GameObject(
          Mesh(
                  CubedMaterial(
                          backgroundProgram,
                          TextureCube(gl,
                                  "media/posx512.jpg",
                                  "media/negx512.jpg",
                                  "media/posy512.jpg",
                                  "media/negy512.jpg",
                                  "media/posz512.jpg",
                                  "media/negz512.jpg"
                          )
                  ),
                  quadGeometry
          )
  )

  val gameObjects = ArrayList<GameObject>()
  init{
    gameObjects.add(background)
    gameObjects.add(slowpoke)
  }

  val camera = PerspectiveCamera(*Program.all)

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame

  init{
    gl.enable(GL.DEPTH_TEST)

    addComponentsAndGatherUniforms(*Program.all)
  }

  fun resize(gl : WebGL2RenderingContext, canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)
    camera.setAspectRatio(canvas.width.toFloat() / canvas.height.toFloat())
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(gl : WebGL2RenderingContext, keysPressed : Set<String>) {

    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t  = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f    
    timeAtLastFrame = timeAtThisFrame

    // clear the screen
    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)
    gl.clearDepth(1.0f)
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)

    camera.move(dt, keysPressed)
    gameObjects.forEach { it.move(dt, t, keysPressed, gameObjects) }
    gameObjects.forEach { it.update() }
    gameObjects.forEach { it.draw( camera ) }
  }
}
