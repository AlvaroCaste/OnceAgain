package onceagain.modules.scheduling

package object model {
  type EasinessFactor = Double

  object EasinessFactor {
    val Min = 1.3
    val Max = 2.5
    val Default = Max
  }
}
