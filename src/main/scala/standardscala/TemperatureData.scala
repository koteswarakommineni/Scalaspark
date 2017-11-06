package standardscala

/**
  * Created by kkommineni on 11/5/17.
  */

case class TemperatureData(day: Int, doy: Int, month: Int, year: Int, precip: Double, snow: Double, tave: Double, tmax: Double, tmin: Double)

object TemperatureData {
  def main(args: Array[String]): Unit = {
    val source = scala.io.Source.fromFile("MN212142_9392.csv")
    val lines = source.getLines().drop(1)
    val data = lines.map(l => {
      val p = l.split(",")
      TemperatureData(p(0).toInt, p(1).toInt, p(2).toInt, p(4).toInt, p(5).toDouble, p(6).toDouble, p(7).toDouble, p(8).toDouble, p(9).toDouble)
    })
    source.close()
  }
}
