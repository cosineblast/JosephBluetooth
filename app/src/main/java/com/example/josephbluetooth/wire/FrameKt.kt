// Generated by the protocol buffer compiler. DO NOT EDIT!
// source: thing.proto

// Generated files should ignore deprecation warnings
@file:Suppress("DEPRECATION")
package com.example.josephbluetooth.wire;

@kotlin.jvm.JvmName("-initializeframe")
public inline fun frame(block: com.example.josephbluetooth.wire.FrameKt.Dsl.() -> kotlin.Unit): com.example.josephbluetooth.wire.Thing.Frame =
  com.example.josephbluetooth.wire.FrameKt.Dsl._create(com.example.josephbluetooth.wire.Thing.Frame.newBuilder()).apply { block() }._build()
/**
 * Protobuf type `com.example.josephbluetooth.wire.Frame`
 */
public object FrameKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  public class Dsl private constructor(
    private val _builder: com.example.josephbluetooth.wire.Thing.Frame.Builder
  ) {
    public companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.example.josephbluetooth.wire.Thing.Frame.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.example.josephbluetooth.wire.Thing.Frame = _builder.build()

    /**
     * `double x = 1;`
     */
    public var x: kotlin.Double
      @JvmName("getX")
      get() = _builder.getX()
      @JvmName("setX")
      set(value) {
        _builder.setX(value)
      }
    /**
     * `double x = 1;`
     */
    public fun clearX() {
      _builder.clearX()
    }

    /**
     * `double y = 2;`
     */
    public var y: kotlin.Double
      @JvmName("getY")
      get() = _builder.getY()
      @JvmName("setY")
      set(value) {
        _builder.setY(value)
      }
    /**
     * `double y = 2;`
     */
    public fun clearY() {
      _builder.clearY()
    }

    /**
     * `double z = 3;`
     */
    public var z: kotlin.Double
      @JvmName("getZ")
      get() = _builder.getZ()
      @JvmName("setZ")
      set(value) {
        _builder.setZ(value)
      }
    /**
     * `double z = 3;`
     */
    public fun clearZ() {
      _builder.clearZ()
    }
  }
}
@kotlin.jvm.JvmSynthetic
public inline fun com.example.josephbluetooth.wire.Thing.Frame.copy(block: com.example.josephbluetooth.wire.FrameKt.Dsl.() -> kotlin.Unit): com.example.josephbluetooth.wire.Thing.Frame =
  com.example.josephbluetooth.wire.FrameKt.Dsl._create(this.toBuilder()).apply { block() }._build()

