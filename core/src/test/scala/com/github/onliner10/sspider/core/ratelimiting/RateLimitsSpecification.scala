package com.github.onliner10.sspider.core.ratelimiting

import com.github.onliner10.sspider.core.ratelimiting.Arbitraries.*
import com.github.onliner10.sspider.core.ratelimitng.ConstantRateLimits
import io.lemonlabs.uri.Url
import org.scalacheck.Gen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import java.time.temporal.TemporalAmount

class RateLimitsSpecification extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks with Matchers {
  "getDuration" should "return same duration for all subpaths" in {
    forAll { (x: Url, duration: TemporalAmount, subPaths: Seq[String]) =>
      val sut = ConstantRateLimits.empty.withLimit(x, duration)
      val subPathUrl = x.addPathParts(subPaths)
      val actualDelayReturned = sut.getDelay(subPathUrl)

      actualDelayReturned shouldBe Some(duration)
    }
  }
}
