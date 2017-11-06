package standardscala

object ParallelCollections extends  App {
  val a = Array(4,2,7,4,9,1,8).par
  println(a.aggregate(0)(_ + _, _ + _))


}
