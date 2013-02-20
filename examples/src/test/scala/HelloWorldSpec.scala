import org.specs2.mutable._

import com.github.mvollebregt.macros.Macros._

class HelloWorldSpec extends Specification {

  "hello()" should {
    "print hello world" in {
      hello()
      // please manually check test output
      true must_== true
    }
  }

  "hi(piet)" should {
    "return hi piet" in {
      hi("piet") must_== "hi piet"
    }
  }

  "debug(variablename)" should {
    "return variablename = variablevalue" in {
      val variablename = "variablevalue"
      debugg(variablename) must_== "variablename = variablevalue"
    }
  }


}