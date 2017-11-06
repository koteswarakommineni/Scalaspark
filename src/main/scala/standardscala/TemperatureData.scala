package standardscala

import sun.nio.cs.ext.DoubleByteEncoder

/**
  * Created by kkommineni on 11/5/17.
  */

case class TemperatureData(day: Int, doy: Int, month: Int, year: Int, precip: Double, snow: Double, tave: Double, tmax: Double, tmin: Double)

object TemperatureData {

  def toDoubleOrNegative(s: String) : Double = {
    try {
      s.toDouble
    }
      catch {
        case e: NumberFormatException => -1
      }
  }

  def main(args: Array[String]): Unit = {
    val source = scala.io.Source.fromFile("MN212142_9392.csv")
    val lines = source.getLines().drop(1)
    val data = lines.flatMap { l =>
      val p = l.split(",")
      if(p(7) == "." || p(8) == "." || p(9) == ".")
        Seq.empty
      else
      Seq(TemperatureData(p(0).toInt, p(1).toInt, p(2).toInt, p(4).toInt, toDoubleOrNegative(p(5)), toDoubleOrNegative(p(6)), p(7).toDouble, p(8).toDouble, p(9).toDouble))
    }.toArray
    source.close()

    val maxtemp = data.map(_.tmax).max

    val maxTempDays = data.filter(m => m.tmax == maxtemp)

    println(s"Hot days are ${maxTempDays.mkString(", ")}")

    val hotday = data.maxBy(t => t.tmax)
    println(s"Hot day is $hotday")

    val hotDay2 = data.reduceLeft((d1, d2) => if(d1.tmax >= d2.tmax) d1 else d2)
    println(s"Hot day is $hotDay2")

    val rainyCount = data.count(d => d.precip >= 1.0)
    println(s"There are $rainyCount Rainy Days. There is ${rainyCount *100.0/data.length} percent")

    val (rainySum, rainyCount2) = data.foldLeft(0.0 -> 0) {
      case ((sum, cnt), td) => {
        if(td.precip < 1.0)
          (sum, cnt)
        else
          (sum + td.tmax, cnt + 1)
      }
    }
    println(s"Average RainyTemp is ${rainySum/rainyCount2}")

    val rainyTemps = data.flatMap(f => if(f.precip < 1.0) Seq.empty else Seq(f.tmax))
    println(s"Average RainyTemp is ${rainyTemps.sum/rainyTemps.length}")

    val monthGroups = data.groupBy(f => f.month)
    val monthlyAverageTemp = monthGroups.map {
      case (m, days) =>
        m -> days.foldLeft(0.0)((sum, day) => sum + day.tmax)/days.length
    }.toSeq.sortBy(_._1)
    println(s"Monthly Temp is ${monthlyAverageTemp}")
  }
}
