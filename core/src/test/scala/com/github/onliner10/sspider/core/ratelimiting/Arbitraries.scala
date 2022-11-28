package com.github.onliner10.sspider.core.ratelimiting

import io.lemonlabs.uri.Url
import org.scalacheck.Arbitrary

import java.time.Duration
import java.time.temporal.TemporalAmount

object Arbitraries:
  implicit lazy val urlArbitrary: Arbitrary[Url] = Arbitrary(Generators.url)
  implicit lazy val durationArb: Arbitrary[TemporalAmount] = Arbitrary(Generators.temporalAmount)