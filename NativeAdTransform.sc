import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Monoid
import cats.instances.all._
import cats.syntax.semigroup._
import cats.syntax.option._

case class TitleObj(str: String)
case class ImgObj(str: String)
case class DataObj(str: String)
case class AssetObj(id: Int,
                      required: Option[Boolean] = None,
                      title: Option[TitleObj] = None,
                      img: Option[ImgObj] = None,
                      data: Option[DataObj] = None,
                      ext: Option[Map[String, String]] = None)

final case class NativeAd(
        headline: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None,
        body: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None,
        callToAction: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None,
        advertiser: _root_.scala.Option[_root_.scala.Predef.String] = _root_.scala.None
)

val list = List(
        AssetObj(1, title = TitleObj("title").some),
        AssetObj(2, img = ImgObj("img").some),
        AssetObj(3, data = DataObj("title").some)
)

implicit val TitleMono = new Monoid[TitleObj] {
        def combine(x: TitleObj, y: TitleObj): TitleObj = x.copy(str = x.str |+| y.str)
        def empty: TitleObj = TitleObj("")
}

implicit val ImgMono = new Monoid[ImgObj] {
        def combine(x: ImgObj, y: ImgObj): ImgObj = x.copy(str = x.str |+| y.str)
        def empty: ImgObj = ImgObj("")
}

implicit val DataMono = new Monoid[DataObj] {
        def combine(x: DataObj, y: DataObj): DataObj = x.copy(str = x.str |+| y.str)
        def empty: DataObj = DataObj("")
}

implicit val AssetMono = new Monoid[AssetObj] {
        def combine(x: AssetObj, y: AssetObj): AssetObj = x.copy(title = x.title |+| y.title,
        img = x.img |+| y.img,
        data = x.data |+| y.data)
        def empty: AssetObj = AssetObj(0)
}

val combine = list.foldLeft(AssetObj(0))(_ |+| _)

print(s"combine: $combine")

//assign combine asset obj to NativeAd