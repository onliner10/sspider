package com.github.onliner10.sspider.core

sealed trait SpiderA[T, A]
case class Fetch[T](url: String) extends SpiderA[T, String]
case class YieldResult[T](item: T) extends SpiderA[T, Unit]
case class Log[T](level: LogLevel, reason: String) extends SpiderA[T, Unit]
