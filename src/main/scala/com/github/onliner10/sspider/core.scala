package com.github.onliner10.sspider

import cats.Traverse
import cats.data.{EitherNec, NonEmptyChain}
import cats.free.Free
import cats.free.Free.liftF
import cats.implicits.*
import cats.syntax.all.*
import cats.arrow.FunctionK
import cats.{Id, ~>}
import cats.effect.IO

type HttpResponseMessage = String

enum LogLevel:
  case Info, Warning, Error

sealed trait SpiderA[T, A]
case class Fetch[T](url: String) extends SpiderA[T, HttpResponseMessage]
case class YieldResult[T](item: T) extends SpiderA[T, Unit]
case class Log[T](level: LogLevel, reason: String) extends SpiderA[T, Unit]

type Spider[T, A] = Free[SpiderA[T, _], A]

object Spider {
  def fetch[T](url: String): Spider[T, HttpResponseMessage] =
    liftF(Fetch(url))

  def yieldResult[T](item: T): Spider[T, Unit] =
    liftF(YieldResult(item))

  def log[T](level: LogLevel, reason: String): Spider[T, Unit] =
    liftF(Log(level, reason))
}

sealed trait Sink[A]:
  def init(): IO[Unit]