(ns awesome.core-test
  (:require [clojure.test :refer :all]
            [awesome.core :refer :all]
            [midje.sweet :refer :all]))

(defn strike [previous] 10)
(defn spare [previous] 5)

(facts "roll"
  (facts "when called with previous pin count"
    (fact "it returns a pin count between 0 and 10 - previous pin count"
      (< (roll 5) 6) => true)))

(facts "roll-frame"
  (facts "given we aren't in the 10th frame"
    (fact "it returns a 0 for the second roll"
      (binding [roll strike]
        (last (roll-frame))) => 0)
    (fact "it rolls 2 balls if the first is not a strike"
      (binding [roll spare]
        (count (roll-frame))) => 2)))

(facts "roll-tenth-frame"
  (fact "will roll 3 balls if each are strikes"
    (binding [roll strike]
      (roll-tenth-frame)) => [10 10 10])

  (fact "will roll 3 balls if spare picked up"
    (binding [roll (fn [pins] 
                     (cond (= pins 0) 4 
                           (= pins 4) 6
                           :otherwise 4))]
      (roll-tenth-frame)) => [4 6 4])

  (fact "will only roll 2 balls if spare missed"
    (binding [roll (fn [pins] 
                     (cond (= pins 0) 4 
                           (= pins 4) 3
                           :otherwise 4))]
      (roll-tenth-frame)) => [4 3 0]))


(facts "roll-game"
  (fact "it rolls 10 frames"
    (count (roll-game)) =>  10)
  (fact "all frames except tenth should have 2 scores"
    (every? #(= (count %) 2) (rest (reverse (roll-game)))) => true)
  (fact "last frame has 3 scores"
    (-> (roll-game)
        reverse
        first
        count) => 3))


(facts "score-game"
  (facts "given a spare is rolled in a frame"
    (fact "it adds the score of the next ball to the frame"
      (score-game [[6 4] [5 1]]) => [15 6])))
