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

  "swap(a,b)" should {
    "swap the values of variables a and b" in {
      var a = 2
      var b = 3
      swap(a,b) // replace a or b with a value instead of a variable and you'll get a compilation error!
      (a,b) must_== (3, 2)
    }
  }

  "defvar" should {
    "define a variable" in {
      defvar()
      // myvar must_== 12 does not work
    }
  }


}