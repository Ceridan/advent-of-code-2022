(ns advent-of-code-2022.day15-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day15 :refer [part1 part2]]))

(def beacon-data-example ["Sensor at x=2, y=18: closest beacon is at x=-2, y=15"
                          "Sensor at x=9, y=16: closest beacon is at x=10, y=16"
                          "Sensor at x=13, y=2: closest beacon is at x=15, y=3"
                          "Sensor at x=12, y=14: closest beacon is at x=10, y=16"
                          "Sensor at x=10, y=20: closest beacon is at x=10, y=16"
                          "Sensor at x=14, y=17: closest beacon is at x=10, y=16"
                          "Sensor at x=8, y=7: closest beacon is at x=2, y=10"
                          "Sensor at x=2, y=0: closest beacon is at x=2, y=10"
                          "Sensor at x=0, y=11: closest beacon is at x=2, y=10"
                          "Sensor at x=20, y=14: closest beacon is at x=25, y=17"
                          "Sensor at x=17, y=20: closest beacon is at x=21, y=22"
                          "Sensor at x=16, y=7: closest beacon is at x=15, y=3"
                          "Sensor at x=14, y=3: closest beacon is at x=15, y=3"
                          "Sensor at x=20, y=1: closest beacon is at x=15, y=3"])

(deftest part1-test
  (testing "Part 1"
    (is (= 26 (part1 beacon-data-example 10)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 56000011 (part2 beacon-data-example 0 20)))))
