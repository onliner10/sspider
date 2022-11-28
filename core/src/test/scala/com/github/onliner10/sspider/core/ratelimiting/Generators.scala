package com.github.onliner10.sspider.core.ratelimiting

import io.lemonlabs.uri.Url
import org.scalacheck.Gen

import java.time.Duration
import java.time.temporal.TemporalAmount

object Generators:
  val shortAlphaNumStr: Gen[String] =
    for
      len <- Gen.choose(1, 20)
      s <- Gen.stringOfN(len, Gen.alphaNumChar)
    yield s

  val temporalAmount: Gen[TemporalAmount] =
    Gen.long.map(Duration.ofMillis)

  val url: Gen[Url] =
    for
      scheme <- Gen.oneOf(Seq("http://", "https://"))

      domainChars = Gen.oneOf(Gen.alphaNumChar, Gen.const('.'))

      baseElementsCount <- Gen.choose(0, 5)
      basePaths <- Gen.listOfN[String](baseElementsCount, shortAlphaNumStr)
      base = basePaths.mkString(".")

      domain <- Gen.oneOf(Seq(".com", ".pl"))

      pathElementsCount <- Gen.choose(0, 20)
      pathElements <- Gen.listOfN[String](pathElementsCount, shortAlphaNumStr)

      queryParamGen = Gen.zip[String, String](shortAlphaNumStr, shortAlphaNumStr).map(x => s"${x._1}=${x._2}")
      queryParamCount <- Gen.choose(0, 10)
      queryParams <- Gen.listOfN(queryParamCount, queryParamGen)

      path = if pathElements.nonEmpty then "/" + pathElements.mkString("/") else ""
      q = if queryParams.nonEmpty then "?" + queryParams.mkString("&") else ""

      urlStr = scheme + base + domain + path + q
    yield Url.parse(urlStr)

