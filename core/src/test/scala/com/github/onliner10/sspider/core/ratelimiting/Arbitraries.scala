package com.github.onliner10.sspider.core.ratelimiting

import io.lemonlabs.uri.Url
import org.scalacheck.Arbitrary

object Arbitraries:
  implicit lazy val urlArbitrary: Arbitrary[Url] = Arbitrary(Generators.url)