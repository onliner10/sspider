package com.github.onliner10.sspider.core
import io.lemonlabs.uri.Url

sealed trait SpiderA[T, A]
case class Fetch[T](url: Url) extends SpiderA[T, String]
case class YieldResult[T](item: T) extends SpiderA[T, Unit]
case class Log[T](level: LogLevel, reason: String) extends SpiderA[T, Unit]
