(ns advent-of-code-2022.day12-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day12 :refer [part1 part2]]))

(def height-map-example
  "Sabqponm
  abcryxxl
  accszExk
  acctuvwj
  abdefghi")

(deftest part1-test
  (testing "Part 1"
    (is (= 31 (part1 height-map-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 29 (part2 height-map-example)))))
