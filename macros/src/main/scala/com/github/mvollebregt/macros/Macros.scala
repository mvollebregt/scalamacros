package com.github.mvollebregt.macros

import language.experimental.macros
import reflect.macros.Context

// To be able to reify from the sbt command
// line, import reflect.runtime.universe._

object Macros {

	def hello(): Unit = macro hello_impl
	def hello_impl(c: Context)(): c.Expr[Unit] = {
		import c.universe._
		reify { println("hello world!") }
	}

  def hi(name: String): String = macro hi_impl
  def hi_impl(c: Context)(name: c.Expr[String]) : c.Expr[String] = {
    import c.universe._
    reify { "hi " + name.splice }
  }

  def debugg(value: Any): String = macro debugg_impl
  def debugg_impl(c:Context)(value: c.Expr[Any]) : c.Expr[String] = {
    import c.universe._
    val name = c.Expr[String](Literal(Constant(show(value.tree))))
    reify ( name.splice + " = " + value.splice )
  }

  def swap(first: Any, second: Any): Unit = macro swap_impl
  def swap_impl(c:Context)(first: c.Expr[Any], second: c.Expr[Any]) : c.Expr[Unit] = {
    import c.universe._
    import c.universe.Flag._
    (first.tree, second.tree) match {
      case (a: Ident, b: Ident) => {
        val temp = newTermName(c.fresh())
        c.Expr[Unit](Block(
          ValDef(Modifiers(MUTABLE), temp, TypeTree(), a),
          Assign(a, b),
          Assign(b, Ident(temp))
        ))
      }
      case _ => c.abort(c.enclosingPosition, "can only swap variables, not values!")
    }
  }

  def defvar() : Unit = macro defvar_impl
  def defvar_impl(c: Context)(): c.Expr[Unit] = {
    import c.universe._
    c.Expr[Unit](Block(ValDef(Modifiers(), newTermName("myvar"), TypeTree(), Literal(Constant(12))), Literal(Constant(()))))

    // (ValDef(Modifiers(), newTermName("myvar"), TypeTree(), Literal(Constant(12))))
  }

  def silent(inpt : Unit) : Unit = macro silent_impl
  def silent_impl(c: Context)(inpt: c.Expr[Unit]): c.Expr[Unit] = {
    import c.universe._
    inpt.tree match {
      case Block(commands : List[Any], lastCommand) => {
        c.Expr[Unit](Block((commands :+ lastCommand).filter {
          case (Apply(Select(Select(This(_scala), _predef), _println), _)) =>
              _scala != newTypeName("scala") ||
              _predef != newTermName("Predef") ||
              _println != newTermName("println")
          case _ => true } : _*))
      }
      case (Apply(Select(Select(This(_scala), _predef), _println), _)) => {
          if (_scala == newTypeName("scala") &&
              _predef == newTermName("Predef") &&
              _println == newTermName("println")) reify {} else inpt
        }
      case x => inpt
    }
  }


}