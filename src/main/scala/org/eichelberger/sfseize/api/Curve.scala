package org.eichelberger.sfseize.api

trait DiscreteSource extends Cardinality with Named

trait Curve extends DiscreteSource {
  import Curve._

  def children: Seq[DiscreteSource]

  def accepts(cardinalities: Seq[Long]): Boolean = cardinalities.forall(acceptNonZero)

  def encode(point: Seq[Long]): Long

  def decode(index: Long): Seq[Long]

  def numChildren: Int = children.size

  def cardinalities: Seq[Long] = children.map(_.cardinality)

  def placeValues: Seq[Long] =
    (for (i <- 1 until numChildren) yield cardinalities.slice(i, numChildren).product) ++ Seq(1L)

  def cardinality: Long = cardinalities.product

  def isSquare: Boolean = cardinalities.size match {
    case 0 | 1 => true
    case _     =>
      cardinalities.tail.forall(_ == cardinalities.head)
  }

  def baseName: String = "Curve"

  def name: String = baseName + children.map(_.name).mkString("(", ", ", ")")
}

object Curve {
  def acceptNonZero(cardinality: Long): Boolean = cardinality > 0

  def acceptMultipleOf(cardinality: Long, factor: Long): Boolean = (cardinality % factor) == 0
}