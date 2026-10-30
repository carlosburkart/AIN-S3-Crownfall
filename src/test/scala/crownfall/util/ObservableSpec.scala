package crownfall.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CountingObserver extends Observer:
  var count = 0
  def update(): Unit = count += 1

class ObservableSpec extends AnyWordSpec with Matchers:

  "An Observable" should {

    "notify an added observer" in {
      val observable = Observable()
      val observer = CountingObserver()
      observable.add(observer)
      observable.notifyObservers()
      observer.count should be(1)
    }

    "notify all added observers" in {
      val observable = Observable()
      val observer1 = CountingObserver()
      val observer2 = CountingObserver()
      observable.add(observer1)
      observable.add(observer2)
      observable.notifyObservers()
      observer1.count should be(1)
      observer2.count should be(1)
    }

    "not notify a removed observer" in {
      val observable = Observable()
      val observer1 = CountingObserver()
      val observer2 = CountingObserver()
      observable.add(observer1)
      observable.add(observer2)
      observable.remove(observer1)
      observable.notifyObservers()
      observer1.count should be(0)
      observer2.count should be(1)
    }
  }