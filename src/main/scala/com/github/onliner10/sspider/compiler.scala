package com.github.onliner10.sspider

import cats.{Id, ~>}

def compiler[T] (sink: T => Unit) : SpiderA[T, _] ~> Id  =
  new (SpiderA[T, _] ~> Id) {
    def apply[A](fa: SpiderA[T, A]): Id[A] =
      fa match {
        case Fetch(url) =>
          println(s"fetch($url)")
          s"FETCHED$url"
        case YieldResult(item) =>
          sink(item)
        case Log(level, reason) =>
          level match {
            case LogLevel.Info => println(s"[INFO] $reason)")
            case LogLevel.Warning => println(s"[WARN] $reason)")
            case LogLevel.Error => System.err.println(s"[ERROR] $reason")
          }
      }
  }