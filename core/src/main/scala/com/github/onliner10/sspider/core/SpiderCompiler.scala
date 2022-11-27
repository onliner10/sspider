package com.github.onliner10.sspider.core

import cats.Id
import cats.effect.IO
import cats.~>
import io.lemonlabs.uri.Url

object SpiderCompiler {
  def compile[T](sink: T => IO[Unit]): SpiderA[T, _] ~> IO =
    new (SpiderA[T, _] ~> IO) {
      def apply[A](fa: SpiderA[T, A]): IO[A] =
        fa match {
          case Fetch(url: Url) =>
            IO(println(s"fetch($url)")) >> IO(s"FETCHED$url")
          case YieldResult(item) =>
            sink(item)
          case Log(level, reason) =>
            level match {
              case LogLevel.Info    => IO(println(s"[INFO] $reason)"))
              case LogLevel.Warning => IO(println(s"[WARN] $reason)"))
              case LogLevel.Error => IO(System.err.println(s"[ERROR] $reason"))
            }
        }
    }

}
