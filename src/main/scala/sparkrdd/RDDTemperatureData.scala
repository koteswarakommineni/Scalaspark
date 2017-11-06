package sparkrdd

import org.apache.spark.{SparkConf, SparkContext}
import standardscala.TemperatureData
import standardscala.TemperatureData.toDoubleOrNegative


object RDDTemperatureData {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Temperature Data").setMaster("local[*]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")
    val lines = sc.textFile("MN212142_9392.csv").filter(l => !l.startsWith("Day"))

    val data = lines.flatMap { l =>
      val p = l.split(",")
      if(p(7) == "." || p(8) == "." || p(9) == ".")
        Seq.empty
      else
        Seq(TemperatureData(p(0).toInt, p(1).toInt, p(2).toInt, p(4).toInt, toDoubleOrNegative(p(5)), toDoubleOrNegative(p(6)), p(7).toDouble, p(8).toDouble, p(9).toDouble))
    }.cache()

    println(data.max()(Ordering.by(_.tmax)))

    println(data.reduce((td1, td2) => if(td1.tmax >= td2.tmax) td1 else td2))

    println(data.reduce((td1, td2) => if(td1.tmax >= td2.tmax) td1 else td2))

    val rainyCount = data.filter(_.precip >= 1.0).count()
    println(s"There are $rainyCount days. There is ${rainyCount * 100.0/data.count()} percent")
  }
}
