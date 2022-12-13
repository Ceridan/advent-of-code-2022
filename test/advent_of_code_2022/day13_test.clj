(ns advent-of-code-2022.day13-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day13 :refer [part1 part2]]))

(def packet-pairs-example
  "[1,1,3,1,1]
  [1,1,5,1,1]

  [[1],[2,3,4]]
  [[1],4]

  [9]
  [[8,7,6]]

  [[4,4],4,4]
  [[4,4],4,4,4]

  [7,7,7,7]
  [7,7,7]

  []
  [3]

  [[[]]]
  [[]]

  [1,[2,[3,[4,[5,6,7]]]],8,9]
  [1,[2,[3,[4,[5,6,0]]]],8,9]")

(deftest part1-test
  (testing "Part 1"
    (is (= 13 (part1 packet-pairs-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 140 (part2 packet-pairs-example)))))
