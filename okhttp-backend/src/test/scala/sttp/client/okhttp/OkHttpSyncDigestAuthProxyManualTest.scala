package sttp.client.okhttp

import org.scalatest.{AsyncFreeSpec, Ignore, Matchers}
import sttp.client._
import sttp.client.testing.{ConvertToFuture, ToFutureWrapper}

@Ignore
class OkHttpSyncDigestAuthProxyManualTest extends AsyncFreeSpec with Matchers with ToFutureWrapper {
  implicit val backend: SttpBackend[Identity, Nothing, NothingT] =
    new DigestAuthenticationBackend[Identity, Nothing, NothingT](
      OkHttpSyncBackend(options = SttpBackendOptions.httpProxy("localhost", 3128))
    )

  implicit val convertToFuture: ConvertToFuture[Identity] = ConvertToFuture.id

  "complex proxy auth with digest" in {
    val response = basicRequest
      .get(uri"http://httpbin.org/digest-auth/auth/andrzej/test/SHA-512")
      .auth
      .digest("andrzej", "test")
      .proxyAuth
      .digest("kasper", "qweqwe")
      .send()
    response.code.code shouldBe 200
  }
}
